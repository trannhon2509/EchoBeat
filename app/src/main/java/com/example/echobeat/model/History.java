package com.example.echobeat.model;

import java.util.Date;

public class History {
    private String id;
    private String type; // Loại (song, album, playlist)
    private String itemId; // ID của bài hát, album hoặc playlist
    private String title; // Tiêu đề
    private String coverImage; // Resource ID của ảnh bìa
    private Date timestamp; // Thời gian

    public History() {

    }

    public History(String id, String type, String itemId, String title, String coverImage, Date timestamp) {
        this.id = id;
        this.type = type;
        this.itemId = itemId;
        this.title = title;
        this.coverImage = coverImage;
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}
