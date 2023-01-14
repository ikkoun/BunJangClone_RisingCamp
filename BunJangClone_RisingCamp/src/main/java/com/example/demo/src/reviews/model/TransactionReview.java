package com.example.demo.src.reviews.model;

import java.time.LocalDateTime;

public class TransactionReview {
    private int trId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private String content;
    private int writerId;
    private float starRating;
    private int numberOfReport;
    private int thId;
    private String isNestedReply;
}
