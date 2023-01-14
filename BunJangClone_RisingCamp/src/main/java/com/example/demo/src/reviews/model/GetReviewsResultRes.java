package com.example.demo.src.reviews.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewsResultRes {
    public String countOfReviews;
    public List<GetReviewsRes> reviewsList;
}
