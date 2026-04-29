package com.example.service.redisModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoProgress {
    private int lastTime;
    private int viewLevel; // 0 → 1 → 2 → 3
}
