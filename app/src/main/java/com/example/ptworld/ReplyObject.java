package com.example.ptworld;

import android.graphics.Bitmap;

public class ReplyObject {
    int replyno;
    String nickname;
    String email;
    String contents;
    Bitmap profile_image;
    String device_token;
    String boardno;

    public ReplyObject(String nickname, String email, String contents, Bitmap profile_image, int replyno, String device_token, String boardno) {
        this.nickname = nickname;
        this.email = email;
        this.profile_image = profile_image;
        this.contents = contents;
        this.replyno = replyno;
        this.device_token = device_token;
        this.boardno = boardno;
    }
}
