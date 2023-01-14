package com.example.demo.src.reviews;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.reviews.model.*;
import com.example.demo.utils.JwtService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexStarRating;

@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;
    private final ReviewsProvider reviewsProvider;
    private final JwtService jwtService;

    @Autowired
    public ReviewsController(ReviewsService reviewsService, ReviewsProvider reviewsProvider, JwtService jwtService) {
        this.reviewsService = reviewsService;
        this.reviewsProvider = reviewsProvider;
        this.jwtService = jwtService;
    }

    /**
     * 리뷰 생성 API
     * [POST] /reviews
     * @return BaseResponse<PostReviewsRes>
     */
    @ResponseBody
    @PostMapping
    public BaseResponse<PostReviewsRes> postReviews(@RequestBody PostReviewsReq postReviewsReq) {
        try {
            Integer userId = jwtService.getUserId();

            BaseResponse<PostReviewsRes> INVALID_USER_JWT1 = getReviewsResBaseResponse(postReviewsReq);

            if (INVALID_USER_JWT1 != null) return INVALID_USER_JWT1;

            PostReviewsRes postReviewsRes = reviewsService.postReviews(postReviewsReq, userId);

            return new BaseResponse<>(postReviewsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 리뷰 생성 API 관련 형식적 validation
     * [POST] /reviews
     * @return BaseResponse<PostReviewsRes>
     */
    @Nullable
    private BaseResponse<PostReviewsRes> getReviewsResBaseResponse(PostReviewsReq postReviewsReq) {
        if (postReviewsReq.getStarRating() == null) {
            postReviewsReq.setStarRating(5.0);
        }

        if (!isRegexStarRating(postReviewsReq.getStarRating())) {
            return new BaseResponse<>(GET_STAR_RATING_ERROR);
        }

        if (postReviewsReq.getStarRating() > 5.0) {
            postReviewsReq.setStarRating(5.0);
        }

        if (postReviewsReq.getContent().isEmpty() | postReviewsReq.getContent().trim().equals("")) {
            return new BaseResponse<>(POST_CHATS_EMPTY_CONTENT);
        }

        if (postReviewsReq.getContent().trim().toCharArray().length < 10) {
            return new BaseResponse<>(GET_CONTENT_LESS_THAN_10);
        }
        return null;
    }

    /**
     * 리뷰 삭제 API
     * [PATCH] /reviews
     * @return BaseResponse<PatchReviewsRes>
     */
    @ResponseBody
    @PatchMapping("/{trId}")
    public BaseResponse<PatchReviewsRes> deleteReviews(@PathVariable("trId") Integer trId) {
        try {
            Integer userId = jwtService.getUserId();

            if (trId == null) {
                throw new BaseException(EMPTY_PATH_VARIABLE);
            }

            PatchReviewsRes patchReviewsRes = reviewsService.deleteReviews(trId, userId);

            return new BaseResponse<>(patchReviewsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 특정 유저에게 달린 상점후기 리스트 조회 API
     * [GET] /reviews/:userId
     * @return BaseResponse<GetReviewsResultRes>
     */
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<GetReviewsResultRes> getReviewsOnUser(@PathVariable("userId") Integer userId) {
        try {
            Integer logInUserIdByJwt = jwtService.getUserId();

            if (userId == null) {
                throw new BaseException(EMPTY_PATH_VARIABLE);
            }
            GetReviewsResultRes getReviewsResList = reviewsProvider.getReviewsOnUser(userId);

            return new BaseResponse<>(getReviewsResList);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
