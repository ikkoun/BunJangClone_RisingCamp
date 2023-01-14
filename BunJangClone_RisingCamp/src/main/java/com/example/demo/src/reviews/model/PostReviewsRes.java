package com.example.demo.src.reviews.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostReviewsRes {
    private int trId;
    private String content;
    private int writerId;
    private Double starRating;
    private int numberOfReports;
    private int thId;
    private String isNestedReply;
}
