package com.example.ptworld.DTO;

import android.graphics.Bitmap;

public class TrainnerInfo {
    public static String email;
    public static String password;
    public static String nickname;
    public static String prize;
    public static String place;
    public static Bitmap profile_image;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPrize() {
        return prize;
    }

    public String getPlace() {
        return place;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
