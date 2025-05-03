package com.example.SentinelBE.utils.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

public record ExecutableSummaryDTO(
        Long id,
        String name,
        BigDecimal score,
        LocalDateTime firstDetection,
        LocalDateTime firstReport,
        Long reporters
) {
}
