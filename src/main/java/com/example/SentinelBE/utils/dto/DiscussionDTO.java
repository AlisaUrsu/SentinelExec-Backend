package com.example.SentinelBE.utils.dto;

import java.time.LocalDateTime;

public record DiscussionDTO(
        Long id,
        Long executableId,
        String title,
        LocalDateTime createdAt
) {
}
