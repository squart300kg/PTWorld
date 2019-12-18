package com.example.ptworld;

public class ChattingDTO {
    String nickname;
    String contents;
    String direction;
    String time;

    public ChattingDTO(String nickname, String contents)
    {
        this.nickname = nickname;
        this.contents = contents;
    }
    public ChattingDTO(){}
}
