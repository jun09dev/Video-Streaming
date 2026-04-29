package com.example.service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_id")
    private String providerId;

    private String lastLoginIp;

    private String lastUserAgent;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private PasswordResetToken passwordResetToken;

    //1 user có nhiều song
    @OneToMany(mappedBy = "user")
    private List<Song> songs;

    public User() {
    }

    public User(Long id, String username, String password, String email, String avatarUrl, String provider, String providerId, PasswordResetToken passwordResetToken, List<Song> songs) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.avatarUrl = avatarUrl;
        this.provider = provider;
        this.providerId = providerId;
        this.passwordResetToken = passwordResetToken;
        this.songs = songs;
    }

}
