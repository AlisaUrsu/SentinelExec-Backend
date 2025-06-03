package com.example.SentinelBE.security.auth;

import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
public class AuthResult {
    private String accessToken;
    private String refreshToken;


}
