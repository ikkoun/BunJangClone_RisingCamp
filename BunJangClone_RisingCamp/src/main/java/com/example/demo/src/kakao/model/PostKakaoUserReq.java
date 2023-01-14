package com.example.demo.src.kakao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostKakaoUserReq {
    private String imgUrl;
    private String UserName;
    private String userNick;
    private String email;
    private String phone;
}