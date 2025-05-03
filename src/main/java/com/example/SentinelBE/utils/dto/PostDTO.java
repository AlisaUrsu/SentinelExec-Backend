package com.example.SentinelBE.utils.dto;

import java.time.LocalDateTime;

public record PostDTO(
        Long id,
        String content,
        Long parentId,
        Long discussionId,
        String authorUsername,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String image
) {
}
