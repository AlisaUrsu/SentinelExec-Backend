package com.example.SentinelBE.authentication.dto;

public record EnableUserDto(
        String username,
        boolean isEnabled
) {
}
