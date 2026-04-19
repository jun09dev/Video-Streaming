package com.example.service.dto.request;

public class UploadUrlRequestDto {

    private String fileName;


    public UploadUrlRequestDto() {
    }

    public UploadUrlRequestDto(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
