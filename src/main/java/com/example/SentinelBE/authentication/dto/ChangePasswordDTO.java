package com.example.SentinelBE.authentication.dto;

public record ChangePasswordDTO(
        String oldPassword,
        String newPassword,
        String username
) {
}
