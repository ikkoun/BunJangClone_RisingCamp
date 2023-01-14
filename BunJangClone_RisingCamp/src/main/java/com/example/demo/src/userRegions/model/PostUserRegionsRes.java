package com.example.demo.src.userRegions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostUserRegionsRes {
    private int regionId;
    private int urId;
    private int userId;
    private String fullRegionName;
    private String isRepresentative;
    private int rangeStatus;
}
