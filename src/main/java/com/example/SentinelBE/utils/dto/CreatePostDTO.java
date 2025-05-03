package com.example.SentinelBE.utils.dto;

public record CreatePostDTO(
        Long discussionId,
        String content,
        Long parentId,
        String image
) {
}
