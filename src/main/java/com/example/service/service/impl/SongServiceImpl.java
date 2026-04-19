package com.example.service.service.impl;

import com.example.service.dto.request.SaveSongRequestDto;
import com.example.service.dto.response.SaveSongResponseDto;
import com.example.service.dto.response.SongResponseDto;
import com.example.service.dto.response.UploadUrlResponseDto;
import com.example.service.dto.response.UserProfileResponseDto;
import com.example.service.entity.Song;
import com.example.service.entity.SongDocument;
import com.example.service.entity.User;
import com.example.service.repository.SongRepository;
import com.example.service.repository.SongSearchRepository;
import com.example.service.repository.UserRepository;
import com.example.service.service.JwtService;
import com.example.service.service.SongService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SongServiceImpl implements SongService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private MinioClient minioClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HlsClientService hlsClientService;

    @Autowired
    private SongSearchRepository songSearchRepository;


    @Transactional
    @Override
    public SaveSongResponseDto saveSong(String authHeader, SaveSongRequestDto req) {

        String token = authHeader.replace("Bearer ", "");
        Long userId = jwtService.getUserIdFromToken(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ===== 1. Lưu nhạc =====
        String fileName = req.getObjectName().substring(req.getObjectName().lastIndexOf("/") + 1);
        String title = fileName.contains("_") ?
                fileName.substring(fileName.lastIndexOf("_") + 1).replaceFirst("\\.[^.]+$", "") :
                fileName.replaceFirst("\\.[^.]+$", "");

        Song song = new Song();
        song.setTitle(title);
        song.setFileName(fileName);
        song.setFilePath(req.getObjectName());
        song.setContentType(req.getContentType());
        song.setSize(req.getSize());
        song.setUser(user);

        // ===== 2. Lưu thumbnail nếu có =====
        if (req.getThumbnailObjectName() != null && !req.getThumbnailObjectName().isEmpty()) {
            String thumbnailFileName = req.getThumbnailObjectName().substring(req.getThumbnailObjectName().lastIndexOf("/") + 1);
            String thumbnailUrl = "https://minio01.beatmusic.click/bucket-jun09dev/" + req.getThumbnailObjectName();
            song.setThumbnailUrl(thumbnailUrl);
        }

        Song saved = songRepository.save(song);


// ===== 2.5. Lưu Elasticsearch =====
        SongDocument doc = new SongDocument();
        doc.setId(saved.getId());
        doc.setTitle(saved.getTitle());

        if (saved.getUser() != null) {
            doc.setArtist(saved.getUser().getUsername());
        }

        doc.setThumbnailUrl(saved.getThumbnailUrl());

        songSearchRepository.save(doc);

        // ===== 3. Gọi HLS cho nhạc =====
        hlsClientService.callHlsAsync(req.getObjectName(), saved.getId());

        // ===== 4. map DTO trả về =====
        SaveSongResponseDto res = new SaveSongResponseDto();
        res.setId(saved.getId());
        res.setTitle(saved.getTitle());
        res.setFileName(saved.getFileName());
        res.setFilePath(saved.getFilePath());
        res.setContentType(saved.getContentType());
        res.setSize(saved.getSize());
        res.setUserId(saved.getUser().getId());
        res.setThumbnailUrl(saved.getThumbnailUrl());

        return res;
    }


    @Override
    public UploadUrlResponseDto getUploadUrl(String authHeader, String fileName, boolean isThumbnail) throws Exception {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token không hợp lệ");
        }

        Long userId = jwtService.getUserIdFromToken(authHeader);

        LocalDate now = LocalDate.now();
        String year = String.valueOf(now.getYear());
        String month = String.format("%02d", now.getMonthValue());
        String day = String.format("%02d", now.getDayOfMonth());

        String timestamp = String.valueOf(System.currentTimeMillis());
        String objectName;

        if (isThumbnail) {
            // file ảnh upload trực tiếp
            objectName = String.format("users/%d/thumbnailmusic/%s_%s", userId, timestamp, fileName);
        } else {
            // file nhạc
            String videoId = "vid_" + System.currentTimeMillis();
            objectName = String.format("users/%d/%s/%s/%s/%s/%s_%s", userId, year, month, day, videoId, timestamp, fileName);
        }

        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket("bucket-jun09dev")
                        .object(objectName)
                        .expiry(60 * 10)
                        .build()
        );

        return new UploadUrlResponseDto(url, objectName);
    }

    @Override
    public UploadUrlResponseDto getUploadAvatarUrl(String authHeader, String fileName) throws Exception {

        // ===== 1. validate token =====
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token không hợp lệ");
        }

        // ===== 2. lấy token =====
        String token = authHeader.substring(7);

        // ===== 3. lấy userId =====
        Long userId = jwtService.getUserIdFromToken(token);

        // ===== 4. validate file =====
        String lower = fileName.toLowerCase();

        if (!lower.endsWith(".png") && !lower.endsWith(".jpg") && !lower.endsWith(".jpeg")) {
            throw new RuntimeException("Chỉ cho upload ảnh");
        }

        // ===== 5. objectName (đơn giản + chuẩn) =====
        String timestamp = String.valueOf(System.currentTimeMillis());

        String objectName = String.format(
                "users/%d/photoprofile/%s_%s",
                userId,
                timestamp,
                fileName
        );

        // ===== 6. presigned URL =====
        String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket("bucket-jun09dev")
                        .object(objectName)
                        .expiry(60 * 10)
                        .build()
        );

        return new UploadUrlResponseDto(url, objectName);
    }

    @Override
    public List<SongResponseDto> getSongsByUserId(Long userId) {

        List<Song> songs = songRepository.findByUserIdWithUser(userId);

        return songs.stream()
                .map(SongResponseDto::new)
                .toList();
    }

    @Override
    public void deleteSong(String authHeader, Long songId) {

        // ===== 1. lấy userId từ JWT =====
        String token = authHeader.substring(7);
        Long userId = jwtService.getUserIdFromToken(token);

        // ===== 2. tìm song =====
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        // ===== 3. check quyền =====
        if (!song.getUser().getId().equals(userId)) {
            throw new RuntimeException("Không có quyền xoá");
        }

        try {

            // ===== 4. xoá RAW file =====
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket("bucket-jun09dev")
                            .object(song.getFilePath()) // users/.../file.mov
                            .build()
            );

            // ===== 5. xoá HLS folder =====
            deleteHlsFolder(song.getHlsUrl());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi xoá file MinIO");
        }

        // ===== 6. xoá DB =====
        songRepository.delete(song);
    }
    private void deleteHlsFolder(String hlsUrl) throws Exception {

        if (hlsUrl == null) return;

        // ví dụ:
        // https://minio/.../hls-video/users/32/.../master.m3u8

        String objectPath = hlsUrl.split(".click/")[1];
        // → hls-video/users/32/.../master.m3u8

        String folderPath = objectPath.substring(0, objectPath.lastIndexOf("/") + 1);

        Iterable<Result<Item>> objects = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket("hls-video")
                        .prefix(folderPath)
                        .recursive(true)
                        .build()
        );

        for (Result<Item> result : objects) {
            Item item = result.get();

            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket("hls-video")
                            .object(item.objectName())
                            .build()
            );
        }
    }

    private String extractPrefixFromHls(String hlsUrl) {
        int index = hlsUrl.indexOf("hls-video/");
        return hlsUrl.substring(index + "hls-video/".length(), hlsUrl.lastIndexOf("/"));
    }

    @Override
    public List<SongResponseDto> listSongs() {

        List<Song> songs = songRepository.findAllWithUser();

        return songs.stream()
                .map(SongResponseDto::new)
                .toList();
    }

    @Override
    public UserProfileResponseDto getUserProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Song> songs = songRepository.findByUserId(userId);

        return new UserProfileResponseDto(user, songs);
    }
}
