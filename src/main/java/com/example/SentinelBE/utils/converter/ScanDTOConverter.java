package com.example.SentinelBE.utils.converter;

import com.example.SentinelBE.model.Scan;
import com.example.SentinelBE.utils.dto.ScanDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScanDTOConverter implements Converter<Scan, ScanDTO> {
    @Override
    public ScanDTO createFromEntity(Scan entity) {
        return new ScanDTO(
                entity.getId(),
                entity.getUser().getId(),
                new ScanExcutableDTOConverter().createFromEntity(entity.getExecutable()),
                entity.getScore(),
                entity.getLabel(),
                entity.isReported(),
                entity.getContent(),
                entity.getScannedAt()
        );
    }

    @Override
    public Scan createFromDto(ScanDTO dto) {
        return null;
    }
}
