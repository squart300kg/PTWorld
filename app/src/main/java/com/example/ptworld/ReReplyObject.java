package com.example.ptworld;

import android.graphics.Bitmap;

public class ReReplyObject {
    int rereplyno;
    String nickname;
    String email;
    String contents;
    Bitmap profile_image;

    public ReReplyObject(String nickname, String email, String contents, Bitmap profile_image, int rereplyno) {
        this.nickname = nickname;
        this.email = email;
        this.profile_image = profile_image;
        this.contents = contents;
        this.rereplyno = rereplyno;
    }
}
