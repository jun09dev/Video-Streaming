package com.example.service.dto.response;

import com.example.service.entity.Song;
import com.example.service.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileResponseDto {

    private Long id;
    private String username;
    private String avatarUrl;
    private List<SongResponseDto> songs;

    public UserProfileResponseDto(User user, List<Song> songList) {

        this.id = user.getId();
        this.username = user.getUsername();
        this.avatarUrl = user.getAvatarUrl();

        this.songs = songList.stream().map(song -> {
            SongResponseDto dto = new SongResponseDto();
            dto.setId(song.getId());
            dto.setTitle(song.getTitle());
            dto.setThumbnailUrl(song.getThumbnailUrl());
            dto.setHlsUrl(song.getHlsUrl());
            return dto;
        }).toList();
    }
}
