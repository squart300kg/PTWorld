package com.example.ptworld.DTO;

import java.io.Serializable;

public class ItemObject implements Serializable {
    private String subject;
    private String contents_url;
    private String image_url;


    public ItemObject(String subject, String contents_url, String image_url) {
        this.subject = subject;
        this.contents_url = contents_url;
        this.image_url = image_url;
    }
    public ItemObject(String subject ) {
        this.subject = subject;
    }


    public String getSubject() {
        return subject;
    }

    public String getContents_url() {
        return contents_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContents_url(String contents_url) {
        this.contents_url = contents_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
