package com.example.demo.src.reviews;

import com.example.demo.config.BaseException;
import com.example.demo.src.reviews.model.PatchReviewsRes;
import com.example.demo.src.reviews.model.PostReviewsReq;
import com.example.demo.src.reviews.model.PostReviewsRes;
import com.example.demo.src.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ReviewsService {

    private final ReviewsProvider reviewsProvider;
    private final ReviewsDao reviewsDao;
    private final UserProvider userProvider;

    @Autowired
    public ReviewsService(ReviewsProvider reviewsProvider, ReviewsDao reviewsDao, UserProvider userProvider) {
        this.reviewsProvider = reviewsProvider;
        this.reviewsDao = reviewsDao;
        this.userProvider = userProvider;
    }

    public PostReviewsRes postReviews(PostReviewsReq postReviewsReq, int userId) throws BaseException {
        boolean isSeller = false; // 로그인한 유저가 해당 거래 이력의 판매자인지 거래자인지 구별하는 구별자

        if (reviewsProvider.checkThId(postReviewsReq.getThId()) == 0) {
            // 거래 이력 idx에 따른 조회되는 거래 이력이 없을 시
            throw new BaseException(GET_TRANSACTION_HISTORY_NULL);
        }

        if (reviewsProvider.checkParticipantId(postReviewsReq.getThId(), userId) == 0) {
            // 로그인한 유저가 거래 이력 idx에 따른 거래 내 참여자가 아닐 시
            throw new BaseException(GET_TRANSACTION_PARTICIPANT_ERROR);
        }

        if (reviewsProvider.checkWroteReviews(postReviewsReq.getThId(), userId) != 0) {
            // 이미 로그인한 유저가 입력한 리뷰 내용이 있을 시 작성하지 못하게 반환
            throw new BaseException(GET_ALREADY_REVIEWS_ON_USERID);
        }

        if (reviewsProvider.checkIsSellerByThId(postReviewsReq.getThId(), userId)) {
            isSeller = true;
            if (reviewsProvider.checkBuyerReview(postReviewsReq.getThId()) != 1) {
                // 거래에 대한 구매자의 후기가 없을 때
                throw new BaseException(GET_NON_REVIEWS_BUYER);
            }
        }

        try {
            if (isSeller == false) {
                return reviewsDao.postReviewsOnBuyer(postReviewsReq, userId);
            } else {
                return reviewsDao.postReviewsOnSeller(postReviewsReq, userId);
            }
        } catch (Exception exception) {
            throw new BaseException(POST_ERROR_REVIEWS);
        }
    }

    public PatchReviewsRes deleteReviews(int trId, int userId) throws BaseException {
        if (reviewsProvider.checkReview(trId, userId) != 1) {
            throw new BaseException(PATCH_NON_REVIEWS_ON_USER_ID);
        }

        try {
                return reviewsDao.deleteReviews(trId);
        } catch (Exception exception) {
            throw new BaseException(PATCH_ERROR_REVIEWS);
        }

    }
}
