package com.example.SentinelBE.utils.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public record ExecutableDTO(
        Long id,
        String name,
        String label,
        Map<String, Object> rawFeatures,
        BigDecimal score,
        LocalDateTime firstDetection,
        LocalDateTime firstReport,
        LocalDateTime updatedAt,
        Set<String> reporters
) {
}
