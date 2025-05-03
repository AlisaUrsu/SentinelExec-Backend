package com.example.SentinelBE.authentication.dto;

public record EnableUserDTO(
        String username,
        boolean isEnabled
) {
}
