package com.example.service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "size")
    private Long size;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "creat_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // 🔥 THÊM DÒNG NÀY
    private int views = 0;

//    @Column(name = "user_id")
//    private Long userId;

    @Column(name = "status")
    private String status;

    @Column(name = "hls_url")
    private String hlsUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // vẫn dùng cột cũ
    private User user;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    public Song() {
    }

    public Song(Long id, String title, String fileName, String filePath, Long size, String contentType, LocalDateTime createdAt, String status, String hlsUrl, User user, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.fileName = fileName;
        this.filePath = filePath;
        this.size = size;
        this.contentType = contentType;
        this.createdAt = createdAt;
        this.status = status;
        this.hlsUrl = hlsUrl;
        this.user = user;
        this.thumbnailUrl = thumbnailUrl;
    }

}
