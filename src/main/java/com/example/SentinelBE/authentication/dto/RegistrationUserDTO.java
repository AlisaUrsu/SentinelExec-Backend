package com.example.SentinelBE.authentication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class RegistrationUserDTO {
    private String username;
    private String password;
    private String email;
}
