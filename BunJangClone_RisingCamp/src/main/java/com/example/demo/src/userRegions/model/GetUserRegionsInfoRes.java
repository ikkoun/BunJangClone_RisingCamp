package com.example.demo.src.userRegions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRegionsInfoRes {
    private int urId;
    private String fullRegionName;
    private int rangeStatus;
}
