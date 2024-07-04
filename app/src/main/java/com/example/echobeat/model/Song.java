package com.example.echobeat.model;

public class Song {
    private String songId;
    private String userId;
    private String songUrl;
    private String title;
    private int duration;
    private int releaseYear;
    private String pictureSong;
    private String categoryId;

    // Constructors, getters, and setters
    public Song() {}

    public Song(String songId, String userId, String songUrl, String title, int duration, int releaseYear, String pictureSong, String categoryId) {
        this.songId = songId;
        this.userId = userId;
        this.songUrl = songUrl;
        this.title = title;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.pictureSong = pictureSong;
        this.categoryId = categoryId;
    }

    public String getSongId() {
        return songId;
    }

    public void setSongId(String songId) {
        this.songId = songId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getPictureSong() {
        return pictureSong;
    }

    public void setPictureSong(String pictureSong) {
        this.pictureSong = pictureSong;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
