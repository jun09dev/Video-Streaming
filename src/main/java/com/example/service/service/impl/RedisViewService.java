//package com.example.service.service.impl;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.time.Duration;
//
//@Service
//public class RedisViewService {
//
//    @Autowired
//    private StringRedisTemplate redis;
//
//    public boolean increaseView(Long songId, Long userId, String ip) {
//
//        // 🔥 chống spam user
//        String userKey = "view:user:" + userId + ":song:" + songId;
//
//        if (Boolean.TRUE.equals(redis.hasKey(userKey))) {
//            return false;
//        }
//
//        redis.opsForValue().set(userKey, "1", Duration.ofMinutes(10));
//
//        // 🔥 chống spam IP (optional)
//        String ipKey = "view:ip:" + ip + ":song:" + songId;
//
//        if (Boolean.TRUE.equals(redis.hasKey(ipKey))) {
//            return false;
//        }
//
//        redis.opsForValue().set(ipKey, "1", Duration.ofMinutes(5));
//
//        // 🔥 tăng view
//        redis.opsForValue().increment("view:song:" + songId);
//
//        return true;
//    }
//}
