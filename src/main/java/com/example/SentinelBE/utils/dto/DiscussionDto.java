package com.example.SentinelBE.utils.dto;

import java.time.LocalDateTime;

public record DiscussionDto(
        Long id,
        Long executableId,
        String title,
        LocalDateTime createdAt
) {
}
