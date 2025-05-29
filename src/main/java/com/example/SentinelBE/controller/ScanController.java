package com.example.SentinelBE.controller;

import com.example.SentinelBE.controller.utils.HelperMethods;
import com.example.SentinelBE.model.Executable;
import com.example.SentinelBE.model.Scan;
import com.example.SentinelBE.model.User;
import com.example.SentinelBE.service.ExecutableService;
import com.example.SentinelBE.service.ScanService;
import com.example.SentinelBE.service.UserService;
import com.example.SentinelBE.utils.Result;
import com.example.SentinelBE.utils.converter.ScanDTOConverter;
import com.example.SentinelBE.utils.converter.ScanExcutableDTOConverter;
import com.example.SentinelBE.utils.converter.UserDtoConverter;
import com.example.SentinelBE.utils.dto.AnalyzeResponseDTO;
import com.example.SentinelBE.utils.dto.ScanDTO;
import com.example.SentinelBE.utils.dto.ScanExecutableDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.base-url}/scans")
//@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Scan Management")
public class ScanController {
    private final ExecutableService executableService;
    private final UserService userService;
    private final ScanService scanService;
    private final ScanExcutableDTOConverter scanExcutableDTOConverter;
    private final ScanDTOConverter scanDTOConverter;

    @PostMapping("/analyze")
    public Result<ScanDTO> scanFile(@RequestParam("file") MultipartFile file) throws IOException {
        var user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Prepare file part
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Call Flask API
        RestTemplate restTemplate = new RestTemplate();
        String flaskUrl = "http://127.0.0.1:5000/analyze";
        ResponseEntity<AnalyzeResponseDTO> response = restTemplate.postForEntity(flaskUrl, requestEntity, AnalyzeResponseDTO.class);

        Executable executable = Executable.builder()
                .name(response.getBody().file())
                .label(response.getBody().label())
                .rawFeatures(response.getBody().rawFeatures())
                .score(BigDecimal.valueOf(response.getBody().score()))
                .firstDetection(LocalDateTime.now())
                .reporters(new HashSet<>())
                .build();
        executableService.addExecutable(executable);

        Scan scan = Scan.builder()
                .user(user)
                .executable(executable)
                .score(BigDecimal.valueOf(response.getBody().score()))
                .label(response.getBody().label())
                .content(response.getBody().message())
                .reported(false)
                .build();

        var scanToShow = scanDTOConverter.createFromEntity(scanService.addScan(scan));
        // Return raw JSON or parse as needed
        return new Result<>(true, HttpStatus.OK.value(), "File analyzed.", scanToShow);
    }

    @PutMapping ("/report/{id}")
    public Result<ScanDTO> reportScan(@PathVariable Long id) {
        Scan scan = scanService.getScan(id);
        var user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        scan.setReported(true);
        Executable executable = scan.getExecutable();
        //Hibernate.initialize(executable.getReporters());
        Set<User> reporters = executable.getReporters();

        if (reporters == null) {
            reporters = new HashSet<>();
            executable.setReporters(reporters);
        }

        reporters.add(user);
        executable.setFirstReport(LocalDateTime.now());
        executableService.addExecutable(executable);
        //Hibernate.initialize(executable.getReporters());
        ScanDTO scanDTO = scanDTOConverter.createFromEntity(scanService.addScan(scan));

        return new Result<>(true, HttpStatus.OK.value(), "File reported.", scanDTO);
    }

    @GetMapping("/{id}")
    public Result<ScanDTO> getScan(@PathVariable Long id) {
        ScanDTO scanDTO = scanDTOConverter.createFromEntity(scanService.getScan(id));

        return new Result<>(true, HttpStatus.OK.value(), "Scan retrieved.", scanDTO);
    }

    @GetMapping
    public Result<Map<String, Object>> getPosts(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "100") int pageSize,
            @RequestParam(required = false) String label,
            @RequestParam(required = false) String filename,
            @RequestParam(required = false) Boolean isReported,
            @RequestParam(required = false) String user

    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Scan> posts = scanService.findAllByCriteria( label, filename, isReported, user, pageable);
        Map<String, Object> response = HelperMethods.makeResponse(posts, scanDTOConverter);
        return new Result<>(true, HttpStatus.OK.value(), "Retrieved all scans based on given params", response);
    }
}
