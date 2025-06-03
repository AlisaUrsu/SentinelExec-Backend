package com.example.SentinelBE.utils.converter;

import com.example.SentinelBE.model.Executable;
import com.example.SentinelBE.utils.dto.ScanExecutableDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ScanExcutableDtoConverter implements Converter<Executable, ScanExecutableDto> {
    @Override
    public Executable createFromDto(ScanExecutableDto dto) {
        return null;
    }

    @Override
    public ScanExecutableDto createFromEntity(Executable entity) {
        Map<String, Object> rawFeatures = entity.getRawFeatures();
        String sha256 = rawFeatures != null ? (String) rawFeatures.get("sha256") : null;

        Long size = null;
        if (rawFeatures != null && rawFeatures.get("general") instanceof Map<?, ?> generalMap) {
            Object sizeObj = ((Map<?, ?>) generalMap).get("size");
            if (sizeObj instanceof Number) {
                size = ((Number) sizeObj).longValue();
            }
        }
        return new ScanExecutableDto(
                entity.getId(),
                entity.getName(),
                size,
                sha256
        );
    }
}
