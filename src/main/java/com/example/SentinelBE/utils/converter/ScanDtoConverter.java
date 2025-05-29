package com.example.SentinelBE.utils.converter;

import com.example.SentinelBE.model.Scan;
import com.example.SentinelBE.utils.dto.AnalyzeResponseDto;
import com.example.SentinelBE.utils.dto.ScanDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ScanDtoConverter implements Converter<Scan, ScanDto> {
    @Override
    public ScanDto createFromEntity(Scan entity) {
        return new ScanDto(
                entity.getId(),
                entity.getUser().getId(),
                new ScanExcutableDtoConverter().createFromEntity(entity.getExecutable()),
                entity.getScore(),
                entity.getLabel(),
                entity.isReported(),
                entity.getContent(),
                entity.getScannedAt()
        );
    }

    @Override
    public Scan createFromDto(ScanDto dto) {
        return null;
    }

    public Scan createFromAnalyzeResponse(AnalyzeResponseDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("AnalyzeResponseDTO cannot be null");
        }

        return Scan.builder()
                .score(BigDecimal.valueOf(dto.score()))
                .label(dto.label())
                .content(dto.message())
                .reported(false)
                .build();
    }
}
