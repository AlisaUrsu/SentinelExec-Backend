package com.example.SentinelBE.utils.converter;

import com.example.SentinelBE.model.Executable;
import com.example.SentinelBE.utils.dto.ExecutableSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ExecutableSummaryDtoConverter implements Converter<Executable, ExecutableSummaryDto> {
    @Override
    public ExecutableSummaryDto createFromEntity(Executable entity) {
        Map<String, Object> rawFeatures = entity.getRawFeatures();
        String sha256 = rawFeatures != null ? (String) rawFeatures.get("sha256") : null;

        Long size = null;
        if (rawFeatures != null && rawFeatures.get("general") instanceof Map<?, ?> generalMap) {
            Object sizeObj = ((Map<?, ?>) generalMap).get("size");
            if (sizeObj instanceof Number) {
                size = ((Number) sizeObj).longValue();
            }
        }
        return new ExecutableSummaryDto(
                entity.getId(),
                entity.getName(),
                entity.getLabel(),
                sha256,
                size,
                entity.getScore(),
                entity.getFirstDetection(),
                entity.getFirstReport(),
                entity.getReporters() != null ? (long) entity.getReporters().size() : 0L
        );
    }

    @Override
    public Executable createFromDto(ExecutableSummaryDto dto) {
        Executable executable = new Executable();
        executable.setName(dto.name());
        executable.setLabel(dto.label());
        executable.setScore(dto.score());
        executable.setFirstDetection(dto.firstDetection());
        executable.setFirstReport(dto.firstReport());

        // Since reporters is just a count in the DTO, we can't fully reconstruct the Set<User>
        executable.setReporters(new HashSet<>()); // or leave null

        return executable;
    }
}
