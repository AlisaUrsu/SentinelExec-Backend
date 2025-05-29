package com.example.SentinelBE.utils.converter;

import com.example.SentinelBE.model.Executable;
import com.example.SentinelBE.utils.dto.ScanExecutableDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScanExcutableDTOConverter implements Converter<Executable, ScanExecutableDTO> {
    @Override
    public Executable createFromDto(ScanExecutableDTO dto) {
        return null;
    }

    @Override
    public ScanExecutableDTO createFromEntity(Executable entity) {
        return new ScanExecutableDTO(
                entity.getId(),
                entity.getName()
        );
    }
}
