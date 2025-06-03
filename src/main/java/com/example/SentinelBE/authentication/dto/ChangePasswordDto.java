package com.example.SentinelBE.authentication.dto;

import com.example.SentinelBE.authentication.validation.ValidPassword;

public record ChangePasswordDto(
        String oldPassword,
        @ValidPassword
        String newPassword,
        String username
) {
}
