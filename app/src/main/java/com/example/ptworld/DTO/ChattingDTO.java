package com.example.ptworld.DTO;

public class ChattingDTO {
    public String nickname;
    public String contents;
    public String direction;
    public String time;

    public ChattingDTO(String nickname, String contents)
    {
        this.nickname = nickname;
        this.contents = contents;
    }
    public ChattingDTO(){}
}
