package com.example.ptworld.DTO;

public class ContentsDTO {
    public String thumbnail_url;
    public String contents_url;
    public String subject;
    public int views;

    public ContentsDTO(String thumbnail_url, String contents_url, String subject, int views) {
        this.thumbnail_url = thumbnail_url;
        this.contents_url = contents_url;
        this.subject = subject;
        this.views = views;
    }
}
