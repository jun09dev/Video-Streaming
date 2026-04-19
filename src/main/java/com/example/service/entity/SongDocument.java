package com.example.service.entity;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "songs")
public class SongDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    private String artist;

    private String thumbnailUrl;

    public SongDocument() {
    }

    public SongDocument(Long id, String title, String artist, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
