package com.example.service.cronJob;

import com.example.service.repository.PasswordResetTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TokenCleanupJob {

    private final PasswordResetTokenRepository tokenRepository;

    public TokenCleanupJob(PasswordResetTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Scheduled(fixedRate = 60 * 1000)
    @Transactional
    public void cleanExpiredOrUsedTokens() {

        tokenRepository.deleteExpiredOrUsed(LocalDateTime.now());

        System.out.println("Cleanup run at: " + LocalDateTime.now());
    }
}
