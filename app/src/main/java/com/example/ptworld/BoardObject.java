package com.example.ptworld;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

class BoardObject implements Serializable {
    String isLike;
    int no;
    int 좋아요;
    String nickname;
    String contentsText;
    Bitmap profile_image;
    ArrayList<Bitmap> boardImage;
    int replycount;
    String device_token;

    public BoardObject(){}
    public BoardObject(String nickname, Bitmap profile_image, String contentsText)
    {
        this.profile_image = profile_image;
        this.nickname = nickname;
        this.contentsText = contentsText;
    }
    public BoardObject(int replycount, int no, int 좋아요, String nickname, String contentsText, Bitmap profile_image, ArrayList<Bitmap> boardImage, String isLike, String device_token) {
        this.nickname = nickname;
        this.contentsText = contentsText;
        this.profile_image = profile_image;
        this.boardImage = boardImage;
        this.no = no;
        this.좋아요 = 좋아요;
        this.isLike = isLike;
        this.replycount = replycount;
        this.device_token = device_token;
    }
}
