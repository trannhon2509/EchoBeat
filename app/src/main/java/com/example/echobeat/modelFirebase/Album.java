package com.example.echobeat.modelFirebase;

import java.util.Date;

public class Album {
    private String albumId;
    private int artistId;
    private String title;
    private Date releaseYear;
    private String coverImage;
    private int genreId;



    // Constructors, getters, and setters
    public Album() {}

    public Album(String albumId, int artistId, String title, Date releaseYear, String coverImage, int genreId) {
        this.albumId = albumId;
        this.artistId = artistId;
        this.title = title;
        this.releaseYear = releaseYear;
        this.coverImage = coverImage;
        this.genreId = genreId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Date releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }
}