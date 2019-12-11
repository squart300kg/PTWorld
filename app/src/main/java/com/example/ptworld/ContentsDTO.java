package com.example.ptworld;

public class ContentsDTO {
    String thumbnail_url;
    String contents_url;
    String subject;
    int views;

    public ContentsDTO(String thumbnail_url, String contents_url, String subject, int views) {
        this.thumbnail_url = thumbnail_url;
        this.contents_url = contents_url;
        this.subject = subject;
        this.views = views;
    }
}
