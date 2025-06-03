package com.example.SentinelBE.authentication.dto;

import com.example.SentinelBE.authentication.validation.ValidPassword;
import lombok.*;


@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class RegistrationUserDto {
    private String username;
    @ValidPassword
    private String password;
    private String email;
}
