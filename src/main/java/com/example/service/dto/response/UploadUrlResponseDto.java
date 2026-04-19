package com.example.service.dto.response;

public class UploadUrlResponseDto {

    private String uploadUrl;

    private String objectName;

    public UploadUrlResponseDto() {
    }

    public UploadUrlResponseDto(String uploadUrl, String objectName) {
        this.uploadUrl = uploadUrl;
        this.objectName = objectName;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
