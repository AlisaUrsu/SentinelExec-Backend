package com.example.SentinelBE.utils.dto;

public record CreatePostDto(
        Long discussionId,
        String content,
        Long parentId,
        String image
) {
}
