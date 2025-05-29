package com.example.SentinelBE.utils.dto;

import java.util.Map;

public record AnalyzeResponseDto(
        String file,
        double score,
        String label,
        String message,
        Map<String, Object> rawFeatures
) {
}
