package com.example.service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class WatchRequest {
    private Long videoId;
    private int seconds;
}