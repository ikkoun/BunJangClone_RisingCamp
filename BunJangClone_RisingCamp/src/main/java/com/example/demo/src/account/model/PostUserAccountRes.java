package com.example.demo.src.account.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserAccountRes {
    private int accountId;
    private String ownerName;
    private int bankId;
    private String account;
    private String isDefault;
}
