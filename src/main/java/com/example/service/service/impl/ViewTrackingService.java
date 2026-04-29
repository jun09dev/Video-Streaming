//package com.example.service.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class ViewTrackingService {
//
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final VideoRepository videoRepository;
//
//    private String key(Long userId, Long videoId) {
//        return "video:view:" + userId + ":" + videoId;
//    }
//
//    public void track(VideoProgressDto dto) {
//
//        String key = key(dto.getUserId(), dto.getVideoId());
//
//        VideoProgress progress =
//                (VideoProgress) redisTemplate.opsForValue().get(key);
//
//        if (progress == null) {
//            progress = new VideoProgress(0, 0);
//        }
//
//        int percent = (dto.getCurrentTime() * 100) / dto.getDuration();
//
//        int level = progress.getViewLevel();
//
//        // chống tua ngược
//        if (dto.getCurrentTime() < progress.getLastTime()) {
//            return;
//        }
//
//        // LEVEL 1 - 30%
//        if (level == 0 && percent >= 30) {
//            addView(dto.getVideoId());
//            level = 1;
//        }
//
//        // LEVEL 2 - 60%
//        else if (level == 1 && percent >= 60) {
//            addView(dto.getVideoId());
//            level = 2;
//        }
//
//        // LEVEL 3 - 80%
//        else if (level == 2 && percent >= 80) {
//            addView(dto.getVideoId());
//            level = 3;
//        }
//
//        // reset cycle
//        if (level >= 3) {
//            level = 0;
//        }
//
//        progress.setViewLevel(level);
//        progress.setLastTime(dto.getCurrentTime());
//
//        redisTemplate.opsForValue().set(key, progress);
//    }
//
//    private void addView(Long videoId) {
//        videoRepository.increaseView(videoId);
//    }
//}
