package com.example.echobeat.model;

public class Album {
    private int albumId;
    private int artistId;
    private String title;
    private int releaseYear;
    private String coverImage;
    private int genreId;

    // Constructors, getters, and setters
    public Album() {}

    public Album(int albumId, int artistId, String title, int releaseYear, String coverImage, int genreId) {
        this.albumId = albumId;
        this.artistId = artistId;
        this.title = title;
        this.releaseYear = releaseYear;
        this.coverImage = coverImage;
        this.genreId = genreId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
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

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
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