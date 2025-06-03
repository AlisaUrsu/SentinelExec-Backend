package com.example.SentinelBE.security.recovery;


import com.example.SentinelBE.authentication.dto.PasswordResetDto;

public interface RecoveryService {
    void recoveryRequest(String email);
    void resetPassword(String token);
    void resetPassword(PasswordResetDto passwordResetDto);

}
