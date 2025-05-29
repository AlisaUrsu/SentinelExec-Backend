package com.example.SentinelBE.authentication.dto;


import com.example.SentinelBE.model.Role;

public record UserAdminDto(
        long id,
        String username,
        String email,
        Role role,
        byte[] profilePicture,
        boolean isEnabled
) {
}
