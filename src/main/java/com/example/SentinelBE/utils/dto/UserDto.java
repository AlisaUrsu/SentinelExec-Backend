package com.example.SentinelBE.utils.dto;


import com.example.SentinelBE.model.Role;


import java.time.LocalDateTime;

public record UserDto(
        long id,
        String username,
        String email,
        Role role,
        String profilePicture,
        int totalScans,
        int uniqueExecutablesScanned,
        int totalReports,
        //int totalPosts,
        //ScanDTO lastScan,
        LocalDateTime createdAt
        //LocalDateTime lastLogin


) {
}
