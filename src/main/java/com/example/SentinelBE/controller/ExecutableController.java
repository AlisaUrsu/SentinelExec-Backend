package com.example.SentinelBE.controller;

import com.example.SentinelBE.controller.utils.HelperMethods;
import com.example.SentinelBE.model.Executable;
import com.example.SentinelBE.service.ExecutableService;
import com.example.SentinelBE.utils.Result;
import com.example.SentinelBE.utils.converter.ExecutableDTOConverter;
import com.example.SentinelBE.utils.converter.ExecutableSummaryConverter;
import com.example.SentinelBE.utils.dto.ExecutableDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.base-url}/executables")
//@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Executable Management")
public class ExecutableController {
    private final ExecutableService executableService;
    private final ExecutableSummaryConverter executableSummaryConverter;
    private final ExecutableDTOConverter executableDTOConverter;

    @GetMapping
    public Result<Map<String, Object>> getExecutables(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "100") int pageSize,
            //@RequestParam(required = false) Long executableId,
            @RequestParam(required = false) String executableName
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Executable> executables = executableService.findAllByCriteria(executableName, pageable);
        Map<String, Object> response = HelperMethods.makeResponse(executables, executableSummaryConverter);
        return new Result<>(true, HttpStatus.OK.value(), "Retrieved all executables based on given params", response);
    }


    @GetMapping("/{id}")
    public Result<ExecutableDTO> getExec(@PathVariable Long id) {
        var executable = executableService.getExecutable(id);
        var executableDTO = executableDTOConverter.createFromEntity(executable);
        return new Result<>(true, HttpStatus.OK.value(), "Retrived executable.", executableDTO);
    }
}
