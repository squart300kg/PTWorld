package com.example.ptworld.DTO;

import android.graphics.Bitmap;

public class ReplyObject {
    public int replyno;
    public String nickname;
    String email;
    public String contents;
    public Bitmap profile_image;
    String device_token;
    public String boardno;

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
