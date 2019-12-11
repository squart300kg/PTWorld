package com.example.ptworld;

import android.graphics.Bitmap;

public class LikeListDTO {
    String nickname;
    String email;
    Bitmap profile_image;

    public LikeListDTO(String nickname, String email, Bitmap profile_image) {
        this.nickname = nickname;
        this.email = email;
        this.profile_image = profile_image;
    }
}
