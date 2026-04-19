package com.example.service.service;

import com.example.service.dto.request.SaveSongRequestDto;
import com.example.service.dto.response.SaveSongResponseDto;
import com.example.service.dto.response.SongResponseDto;
import com.example.service.dto.response.UploadUrlResponseDto;
import com.example.service.dto.response.UserProfileResponseDto;
import com.example.service.entity.Song;

import java.util.List;

public interface SongService {
    SaveSongResponseDto saveSong(String authHeader, SaveSongRequestDto request);

//    UploadUrlResponseDto getUploadUrl(String authHeader, String fileName) throws Exception;

    UploadUrlResponseDto getUploadUrl(String authHeader, String fileName, boolean isThumbnail) throws Exception;


    List<SongResponseDto> listSongs();

    UploadUrlResponseDto getUploadAvatarUrl(String authHeader, String fileName) throws Exception;

    List<SongResponseDto> getSongsByUserId(Long userId);

    void deleteSong(String authHeader, Long songId);

    UserProfileResponseDto getUserProfile(Long userId);
}
