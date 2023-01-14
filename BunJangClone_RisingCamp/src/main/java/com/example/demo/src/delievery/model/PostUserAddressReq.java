package com.example.demo.src.delievery.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserAddressReq {
    private String name;
    private String phone;
    private String address;
    private String addressDetail;
    private String isDefault;
}
