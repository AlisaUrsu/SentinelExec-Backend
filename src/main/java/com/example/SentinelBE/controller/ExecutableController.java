package com.example.SentinelBE.controller;

import com.example.SentinelBE.controller.utils.HelperMethods;
import com.example.SentinelBE.model.Executable;
import com.example.SentinelBE.service.ExecutableService;
import com.example.SentinelBE.utils.Result;
import com.example.SentinelBE.utils.converter.ExecutableDtoConverter;
import com.example.SentinelBE.utils.converter.ExecutableSummaryDtoConverter;
import com.example.SentinelBE.utils.dto.ExecutableDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.base-url}/executables")
//@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Executable Management")
public class ExecutableController {
    private final ExecutableService executableService;
    private final ExecutableSummaryDtoConverter executableSummaryDtoConverter;
    private final ExecutableDtoConverter executableDTOConverter;

    @GetMapping
    public Result<Map<String, Object>> getExecutables(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "100") int pageSize,
            //@RequestParam(required = false) Long executableId,
            @RequestParam(required = false) String executableName
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc("updatedAt")));
        Page<Executable> executables = executableService.findAllByCriteria(executableName, pageable);
        Map<String, Object> response = HelperMethods.makeResponse(executables, executableSummaryDtoConverter);
        return new Result<>(true, HttpStatus.OK.value(), "Retrieved all executables based on given params", response);
    }




    @GetMapping("/{sha256}")
    public Result<ExecutableDto> getExecutableBySha256(@PathVariable String sha256) {
        Executable executable = executableService.getExecutableByHash(sha256);
        ExecutableDto executableDto = executableDTOConverter.createFromEntity(executable);
        return new Result<>(true, HttpStatus.OK.value(), "Retrieved executable", executableDto);
    }
}
