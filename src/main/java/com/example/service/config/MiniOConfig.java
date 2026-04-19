package com.example.service.config;

import com.google.api.client.util.Value;
import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MiniOConfig {

    private final MinioProperties properties;

    public MiniOConfig(MinioProperties properties) {
        this.properties = properties;
    }

    @Bean
    public MinioClient minioClient() {
        if (properties.getUrl() == null) {
            throw new RuntimeException("MinIO URL is null! Check application.properties");
        }
        System.out.println("Connecting to MinIO: " + properties.getUrl());
        return MinioClient.builder()
                .endpoint(properties.getUrl())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }
}
