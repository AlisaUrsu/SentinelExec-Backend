package com.example.SentinelBE.utils.dto;

public record ScanExecutableDto(
        Long id,
        String name,
        Long fileSize,
        String sha256
) {
}
