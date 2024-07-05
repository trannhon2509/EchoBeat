package com.example.echobeat.model;

import java.util.List;

public class Artist extends User {
    private int artistId;
    private String bio;
    private List<String> songIds;
    private String musicGenre;

    // Constructors, getters, and setters
    public Artist() {
        super();
    }

    public Artist(int userId, String username, String email, String profilePicture, int roleId, String googleId, String password, int artistId, String bio, List<String> songIds, String musicGenre) {
        super(userId, username, email, profilePicture, roleId, googleId);
        this.artistId = artistId;
        this.bio = bio;
        this.songIds = songIds;
        this.musicGenre = musicGenre;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getSongIds() {
        return songIds;
    }

    public void setSongIds(List<String> songIds) {
        this.songIds = songIds;
    }

    public String getMusicGenre() {
        return musicGenre;
    }

    public void setMusicGenre(String musicGenre) {
        this.musicGenre = musicGenre;
    }
}
