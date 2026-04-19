package com.example.service.service.impl;

import com.example.service.dto.request.ForgotPasswordRequestDto;
import com.example.service.dto.request.ResetPasswordRequestDto;
import com.example.service.dto.request.UserLoginRequestDto;
import com.example.service.dto.response.ForgotPasswordResponseDto;
import com.example.service.dto.response.ResetPasswordResponseDto;
import com.example.service.entity.PasswordResetToken;
import com.example.service.entity.User;
import com.example.service.repository.PasswordResetTokenRepository;
import com.example.service.service.AuthService;
import com.example.service.repository.UserRepository;
import com.example.service.service.EmailService;
import com.example.service.service.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private EmailService emailService;

    @Override
    public String login(UserLoginRequestDto req) {

        // 2. Tìm user theo email
//        User user = userRepository.findByEmail(req.getEmail().trim());
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // 3. Check password
        boolean passwordMatches = passwordEncoder.matches(
                req.getPassword().trim(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // 4. Tạo claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());
        claims.put("userId", user.getId());

        // 5. Generate JWT
        return jwtService.generateToken(claims);
    }

    @Transactional
    @Override
    public ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto request) {

        // 1. tìm token
        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        // 2. check expire
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        // 3. check used (thêm cái này)
        if (Boolean.TRUE.equals(resetToken.getUsed())) {
            throw new RuntimeException("Token already used");
        }

        // 4. update password
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // 5. mark used (QUAN TRỌNG)
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return new ResetPasswordResponseDto("Đổi mật khẩu thành công");
    }

    @Override
    public ForgotPasswordResponseDto forgotPassword(ForgotPasswordRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        resetToken.setUsed(false);

        tokenRepository.save(resetToken);

        String resetLink =
                "http://localhost:5173/reset_password?token=" + token;

        emailService.sendResetPasswordEmail(user.getEmail(), resetLink);

        return new ForgotPasswordResponseDto(
                "Email sent successfully",
                user.getEmail()
        );
    }
}
