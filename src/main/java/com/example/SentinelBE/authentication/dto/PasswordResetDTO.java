package com.example.SentinelBE.authentication.dto;

import com.example.SentinelBE.authentication.validation.ValidPassword;

public record PasswordResetDTO(String token, @ValidPassword String password) {
}
