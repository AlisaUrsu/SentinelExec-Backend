package com.example.SentinelBE.controller;

import com.example.SentinelBE.controller.utils.HelperMethods;
import com.example.SentinelBE.model.Executable;
import com.example.SentinelBE.model.Scan;
import com.example.SentinelBE.model.User;
import com.example.SentinelBE.service.ExecutableService;
import com.example.SentinelBE.service.ScanService;
import com.example.SentinelBE.service.UserService;
import com.example.SentinelBE.utils.Result;
import com.example.SentinelBE.utils.converter.ExecutableDtoConverter;
import com.example.SentinelBE.utils.converter.ScanDtoConverter;
import com.example.SentinelBE.utils.converter.ScanExcutableDtoConverter;
import com.example.SentinelBE.utils.dto.AnalyzeResponseDto;
import com.example.SentinelBE.utils.dto.ScanDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    private final ExecutableDtoConverter executableDtoConverter;
    private final ScanDtoConverter scanDtoConverter;

    @PostMapping("/analyze")
    public Result<ScanDto> scanFile(@RequestParam("file") MultipartFile file) throws IOException {
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
        ResponseEntity<AnalyzeResponseDto> response = restTemplate.postForEntity(flaskUrl, requestEntity, AnalyzeResponseDto.class);

        AnalyzeResponseDto responseDto = response.getBody();
        Executable executable = executableDtoConverter.createFromAnalyzeResponse(responseDto);
        executableService.addExecutable(executable);


        Scan scan = scanDtoConverter.createFromAnalyzeResponse(responseDto);
        scan.setUser(user);
        scan.setExecutable(executable);

        var scanDto = scanDtoConverter.createFromEntity(scanService.addScan(scan));

        return new Result<>(true, HttpStatus.OK.value(), "File analyzed.", scanDto);
    }

    @PutMapping ("/report/{id}")
    public Result<ScanDto> reportScan(@PathVariable Long id) {
        User user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Scan scan = scanService.reportScan(id, user);
        ScanDto scanDto = scanDtoConverter.createFromEntity(scanService.addScan(scan));

        return new Result<>(true, HttpStatus.OK.value(), "File reported.", scanDto);
    }

    @GetMapping("/{id}")
    public Result<ScanDto> getScan(@PathVariable Long id) {
        ScanDto scanDTO = scanDtoConverter.createFromEntity(scanService.getScan(id));

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
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc("scannedAt")));
        Page<Scan> posts = scanService.findAllByCriteria( label, filename, isReported, user, pageable);
        Map<String, Object> response = HelperMethods.makeResponse(posts, scanDtoConverter);
        return new Result<>(true, HttpStatus.OK.value(), "Retrieved all scans based on given params", response);
    }
}
