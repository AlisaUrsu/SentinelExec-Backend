package com.example.SentinelBE.utils.converter;

import com.example.SentinelBE.model.Executable;
import com.example.SentinelBE.model.User;
import com.example.SentinelBE.utils.dto.AnalyzeResponseDto;
import com.example.SentinelBE.utils.dto.ExecutableDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExecutableDtoConverter implements Converter<Executable, ExecutableDto> {
    @Override
    public Executable createFromDto(ExecutableDto dto) {
        return null;
    }

    @Override
    public ExecutableDto createFromEntity(Executable entity) {
        return new ExecutableDto(
                entity.getId(),
                entity.getName(),
                entity.getLabel(),
                entity.getRawFeatures(),
                entity.getScore(),
                entity.getFirstDetection(),
                entity.getFirstReport(),
                entity.getUpdatedAt(),
                entity.getReporters().stream().map(User::getUsername).collect(Collectors.toSet())
        );
    }

    public Executable createFromAnalyzeResponse(AnalyzeResponseDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("AnalyzeResponseDTO cannot be null");
        }

        return Executable.builder()
                .name(dto.file())
                .label(dto.label())
                .rawFeatures(dto.rawFeatures())
                .score(BigDecimal.valueOf(dto.score()))
                .firstDetection(LocalDateTime.now())
                .reporters(new HashSet<>())
                .build();
    }
}
