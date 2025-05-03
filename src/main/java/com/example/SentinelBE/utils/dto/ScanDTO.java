package com.example.SentinelBE.utils.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ScanDTO(
        Long id,
        Long userId,
        ScanExecutableDTO executableDTO,
        BigDecimal score,
        String label,
        boolean reported,
        String content,
        LocalDateTime createdAt
) {
}
