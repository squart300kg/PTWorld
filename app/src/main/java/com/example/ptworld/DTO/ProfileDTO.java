package com.example.ptworld.DTO;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class ProfileDTO {
    String email;
    public String nickname;
    public Bitmap profile_image;
    Bitmap thumbnail_image;
    public ArrayList<Bitmap> thumbnail_Image;
    String device_token;
    public int boardCount;
    public int followingCount;
    public int followerCount;


    public ProfileDTO(String email, String nickname, Bitmap profile_image, ArrayList<Bitmap> thumbnail_image, String device_token, int boardCount, int followingCount, int followerCount) {
        this.email = email;
        this.nickname = nickname;
        this.profile_image = profile_image;
        this.thumbnail_Image = thumbnail_image;
        this.device_token = device_token;
        this.boardCount = boardCount;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }
}
