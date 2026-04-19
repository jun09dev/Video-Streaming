package com.example.service.service;

public interface EmailService {
    void sendResetPasswordEmail(String to, String resetLink);

    void sendWelcomeEmail(String to, String name);
}
