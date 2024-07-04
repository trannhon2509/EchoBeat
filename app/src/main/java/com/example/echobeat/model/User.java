package com.example.echobeat.model;

public class User {
    private int userId;
    private String username;
    private String email;
    private String profilePicture;
    private String password;

    // Constructors, getters, and setters
    public User() {}

    public User(int userId, String username, String email, String profilePicture, String password) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.profilePicture = profilePicture;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
