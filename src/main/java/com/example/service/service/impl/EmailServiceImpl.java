package com.example.service.service.impl;

import com.example.service.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;


    @Override
    public void sendResetPasswordEmail(String to, String resetLink) {

        String subject = "Reset Password";

        Context context = new Context();
        context.setVariable("resetLink", resetLink);

        String html = templateEngine.process("email/reset-password", context);

        sendHtml(to, subject, html);
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Chào mừng bạn đến với hệ thống!");

            Context context = new Context();
            context.setVariable("username", username);
            context.setVariable("loginUrl", "http://localhost:5173/");

            String htmlContent = templateEngine.process("email/welcome", context);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendPasswordResetSuccessEmail(String toEmail, String username) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Password changed successfully");

            Context context = new Context();
            context.setVariable("username", username);

            String htmlContent = templateEngine.process("email/password-success", context);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendLoginAlertEmail(String toEmail, String username, String ip, String location, String mapUrl, String isp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Đăng nhập mới được phát hiện");

            String htmlContent = buildLoginAlertHtml(username, ip, location, mapUrl, isp);

            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String buildLoginAlertHtml(
            String username,
            String ip,
            String location,
            String mapUrl,
            String isp
    ) {
        Context context = new Context();

        context.setVariable("username", username);
        context.setVariable("ip", ip);
        context.setVariable("location", location);
        context.setVariable("isp", isp);
        context.setVariable("mapUrl", mapUrl);
        context.setVariable("time", java.time.LocalDateTime.now());

        return templateEngine.process("email/login-alert", context);
    }


    private void sendHtml(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
