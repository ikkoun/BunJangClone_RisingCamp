package com.example.demo.src.reviews.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostReviewsReq {
    private int thId;
    private String content;
    private Double starRating;
}
