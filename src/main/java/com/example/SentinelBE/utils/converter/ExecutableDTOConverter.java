package com.example.SentinelBE.utils.converter;

import com.example.SentinelBE.model.Executable;
import com.example.SentinelBE.model.User;
import com.example.SentinelBE.utils.dto.ExecutableDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ExecutableDTOConverter implements Converter<Executable, ExecutableDTO> {
    @Override
    public Executable createFromDto(ExecutableDTO dto) {
        return null;
    }

    @Override
    public ExecutableDTO createFromEntity(Executable entity) {
        return new ExecutableDTO(
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
}
