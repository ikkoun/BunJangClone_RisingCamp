package com.example.demo.src.userRegions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRegionsInfo {
    private int regionId;
    private int urId;
    private int userId;
    private String isRepresentative;
    private int rangeStatus;
}
