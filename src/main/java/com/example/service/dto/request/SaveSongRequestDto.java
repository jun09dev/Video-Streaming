package com.example.service.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveSongRequestDto {

    private String objectName;

    private String contentType;

    private Long size;

    private String thumbnailObjectName; // file ảnh
    private String thumbnailContentType;
    private Long thumbnailSize;

    public SaveSongRequestDto() {
    }

    public SaveSongRequestDto(String objectName, String contentType, Long size, String thumbnailObjectName, String thumbnailContentType, Long thumbnailSize) {
        this.objectName = objectName;
        this.contentType = contentType;
        this.size = size;
        this.thumbnailObjectName = thumbnailObjectName;
        this.thumbnailContentType = thumbnailContentType;
        this.thumbnailSize = thumbnailSize;
    }

}
