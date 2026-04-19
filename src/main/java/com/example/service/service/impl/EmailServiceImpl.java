package com.example.service.service.impl;

import com.example.service.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendResetPasswordEmail(String to, String resetLink) {

        String subject = "Reset Password";
        String html = """
<html>
<body style="margin:0;padding:0;background-color:#f4f6f8;font-family:Arial,sans-serif;">

    <table width="100%%" cellpadding="0" cellspacing="0" style="padding:20px;">
        <tr>
            <td align="center">

                <table width="500" cellpadding="0" cellspacing="0"
                       style="background:#ffffff;border-radius:10px;padding:30px;
                              box-shadow:0 4px 10px rgba(0,0,0,0.1);">

                    <tr>
                        <td align="center">
                            <h2 style="color:#333;">Reset Password</h2>
                        </td>
                    </tr>

                    <tr>
                        <td align="center">
                            <p style="color:#555;font-size:14px;">
                                Bạn đã yêu cầu đặt lại mật khẩu.
                            </p>
                        </td>
                    </tr>

                    <tr>
                        <td align="center" style="padding:15px 0;">
                            <img src="https://hoanghamobile.com/tin-tuc/wp-content/uploads/2024/10/tai-anh-phong-canh-dep-thump.jpg"
                                 width="100%%" style="border-radius:8px;" />
                        </td>
                    </tr>

                    <tr>
                        <td align="center" style="padding:20px 0;">
                            <a href="%s"
                               style="background:#4CAF50;color:#fff;
                                      padding:12px 25px;
                                      text-decoration:none;
                                      border-radius:6px;
                                      font-weight:bold;
                                      display:inline-block;">
                                Reset Password
                            </a>
                        </td>
                    </tr>

                    <tr>
                        <td align="center">
                            <p style="color:#999;font-size:12px;">
                                Link sẽ hết hạn sau 15 phút.
                            </p>
                        </td>
                    </tr>

                </table>

            </td>
        </tr>
    </table>

</body>
</html>
""".formatted(resetLink);

        sendHtml(to, subject, html);
    }

    @Override
    public void sendWelcomeEmail(String to, String name) {

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

        } catch (MessagingException e) {
            throw new RuntimeException("Send mail failed", e);
        }
    }
}
