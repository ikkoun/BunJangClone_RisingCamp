package com.example.demo.src.reviews.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewsRes {
    private int trId;
    private String time;
    private String content;
    private String writerName;
    private double starRating;
    private int thId;
    private String isNestedReply;
}
