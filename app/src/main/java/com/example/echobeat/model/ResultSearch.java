package com.example.echobeat.model;

public class ResultSearch {
    private String title;
    private String description;

    public ResultSearch(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}