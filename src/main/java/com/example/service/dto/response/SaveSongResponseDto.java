package com.example.service.dto.response;

public class SaveSongResponseDto {

    private Long id;

    private String title;

    private String fileName;

    private String filePath;

    private String contentType;

    private Long size;

    private Long userId;
    // Thêm thumbnail
    private String thumbnailUrl;

    public SaveSongResponseDto() {
    }

    public SaveSongResponseDto(Long id, String title, String fileName, String filePath, String contentType, Long size, Long userId, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.fileName = fileName;
        this.filePath = filePath;
        this.contentType = contentType;
        this.size = size;
        this.userId = userId;
        this.thumbnailUrl = thumbnailUrl;
    }

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
