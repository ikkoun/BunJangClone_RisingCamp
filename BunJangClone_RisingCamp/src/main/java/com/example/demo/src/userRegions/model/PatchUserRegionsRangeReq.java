package com.example.demo.src.userRegions.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PatchUserRegionsRangeReq {
    private int urId;
    private int rangeStatus;
}
