package com.example.SentinelBE.utils.dto;

import java.util.Map;

public record AnalyzeResponseDTO(
        String file,
        double score,
        String label,
        String message,
        Map<String, Object> rawFeatures
) {
}
