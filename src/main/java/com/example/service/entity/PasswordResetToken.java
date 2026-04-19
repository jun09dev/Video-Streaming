package com.example.service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token")
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    @Column(name = "created_at")
    private LocalDateTime createdDate;

    @Column(name = "used")
    private Boolean used;


    public PasswordResetToken() {
    }

    public PasswordResetToken(Long id, String token, User user, LocalDateTime expiryDate, LocalDateTime createdDate, Boolean used) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
        this.createdDate = createdDate;
        this.used = used;
    }
}
