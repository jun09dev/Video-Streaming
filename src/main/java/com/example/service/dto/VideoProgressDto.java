package com.example.service.dto;

import lombok.Data;

@Data
public class VideoProgressDto {
    private Long userId;
    private Long videoId;
    private int currentTime;
    private int duration;
}
