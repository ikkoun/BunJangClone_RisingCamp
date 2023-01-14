package com.example.demo.src.reviews;

import com.example.demo.config.BaseException;
import com.example.demo.src.reviews.model.GetReviewsRes;
import com.example.demo.src.reviews.model.GetReviewsResultRes;
import com.example.demo.src.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ReviewsProvider {

    private final ReviewsDao reviewsDao;
    private final UserProvider userProvider;

    @Autowired
    public ReviewsProvider(ReviewsDao reviewsDao, UserProvider userProvider) {
        this.reviewsDao = reviewsDao;
        this.userProvider = userProvider;
    }

    public int checkThId(int thId) throws BaseException {
        try {
            return reviewsDao.checkThId(thId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkParticipantId(int thId, int userId) throws BaseException {
        try {
            return reviewsDao.checkParticipantId(thId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkWroteReviews(int thId, int userId) throws BaseException {
        try {
            return reviewsDao.checkWroteReviews(thId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkReview(int trId, int userId) throws BaseException {
        try {
            return reviewsDao.checkReview(trId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean checkIsSellerByThId(int thId, int userId) throws BaseException {
        try {
            return reviewsDao.checkIsSellerByThId(thId, userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkBuyerReview(int thId) throws BaseException {
        try {
            return reviewsDao.checkBuyerReview(thId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public GetReviewsResultRes getReviewsOnUser(int userId) throws BaseException {
        if (userProvider.checkUserId(userId) != 1) {
            // 해당 유저가 가입한 경우가 아닐 경우 (탈퇴 등)
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        try {
            List<GetReviewsRes> getReviewsResList = reviewsDao.getReviewsOnUser(userId);
            String getReviewsCount = reviewsDao.getReviewsCount(userId);
            return new GetReviewsResultRes(getReviewsCount, getReviewsResList);
        } catch (Exception exception) {
            throw new BaseException(GET_ERROR_REVIEWS);
        }
    }
}
