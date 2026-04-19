//package com.example.service.service.impl;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendHtml(String to, String subject, String resetLink) {
//
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//            helper.setTo(to);
//            helper.setSubject(subject);
//
//            String html = """
//                <html>
//                    <body>
//                        <h2>Xin chào 👋</h2>
//                        <p>Bạn đã yêu cầu đặt lại mật khẩu.</p>
//                        <img src="https://hoichimtroi.com/wp-content/uploads/2025/07/hinh-anh-hello-kitty-cute-vo-tri.jpg" width="200"/>
//                        <br/>
//
//                        <a href="%s">Reset Password</a>
//
//                        <p>Link sẽ hết hạn sau 15 phút.</p>
//                    </body>
//                </html>
//                """.formatted(resetLink);
//
//            helper.setText(html, true);
//
//            mailSender.send(message);
//
//        } catch (MessagingException e) {
//            throw new RuntimeException("Send mail failed", e);
//        }
//    }
//}
