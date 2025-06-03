package com.example.SentinelBE.authentication.email;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendHtmlEmail(String to, String subject, String body);
}
