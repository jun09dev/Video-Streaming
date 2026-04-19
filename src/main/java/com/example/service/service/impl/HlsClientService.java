package com.example.service.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HlsClientService {

    public void callHlsAsync(String objectName , Long songId) {
        new Thread(() -> {
            try {
                RestTemplate restTemplate = new RestTemplate();

                String encoded = java.net.URLEncoder.encode(
                        objectName,
                        java.nio.charset.StandardCharsets.UTF_8
                );

//                String url = "http://61.106.148.251:8080/hls/process?file=" + encoded + "&songId=" + songId;
                String url = "http://61.106.157.117:8080/hls/process?file=" + encoded + "&songId=" + songId;

                restTemplate.postForObject(url, null, String.class); // body = null

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
