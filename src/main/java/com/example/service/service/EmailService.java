package com.example.service.service;

public interface EmailService {
    void sendResetPasswordEmail(String to, String resetLink);

    void sendWelcomeEmail(String toEmail, String username);

    void sendPasswordResetSuccessEmail(String toEmail, String username);

    void sendLoginAlertEmail(String toEmail, String username, String ip, String location, String mapUrl, String isp
    );
}
