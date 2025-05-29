package com.example.SentinelBE.utils.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExecutableSummaryDto(
        Long id,
        String name,
        String label,
        String sha256,
        Long fileSize,
        BigDecimal score,
        LocalDateTime firstDetection,
        LocalDateTime firstReport,
        Long reporters
) {
}
