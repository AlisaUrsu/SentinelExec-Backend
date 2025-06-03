package com.example.SentinelBE.utils.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ScanDto(
        Long id,
        Long userId,
        ScanExecutableDto executableDTO,
        BigDecimal score,
        String label,
        boolean reported,
        String content,
        LocalDateTime createdAt
) {
}
