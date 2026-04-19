package com.example.service.dto.response;

import com.example.service.entity.Song;

public class SongResponseDto {

    private Long id;
    private String title;
    private String hlsUrl;

    private Long userId;

    private String username;
    private String avatarUrl;

    //thêm thumbnail
    private String thumbnailUrl;

    public SongResponseDto() {
    }

    //constructor chính
    public SongResponseDto(Song song) {
        this.id = song.getId();
        this.title = song.getTitle();
        this.hlsUrl = song.getHlsUrl();

        if (song.getUser() != null) {
            this.userId = song.getUser().getId();
            this.username = song.getUser().getUsername();
            this.avatarUrl = song.getUser().getAvatarUrl();
        }

        this.thumbnailUrl = song.getThumbnailUrl();
    }

    public SongResponseDto(Long id, String title, String hlsUrl,
                           Long userId,
                           String username, String avatarUrl, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.hlsUrl = hlsUrl;
        this.userId = userId;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    // ===== getter setter =====


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHlsUrl() {
        return hlsUrl;
    }

    public void setHlsUrl(String hlsUrl) {
        this.hlsUrl = hlsUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}