package com.example.demo.src.reviews;

import com.example.demo.src.reviews.model.*;
import com.example.demo.src.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewsDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int checkThId(int thId) {
        String checkThIdQuery = "select exists (select thId from TransactionHistory where status <> 'deleted' and thId = ?);";
        return this.jdbcTemplate.queryForObject(checkThIdQuery,
                int.class,
                thId);
    }

    public boolean checkIsSellerByThId(int thId, int userId) {
        String isBuyerByThIdQuery = "select exists(select * from TransactionHistory where sellerId = ? and thId = ?)";
        Object[] isBuyerByThIdParams = new Object[] { userId, thId };
        int result = this.jdbcTemplate.queryForObject(isBuyerByThIdQuery,
                int.class, isBuyerByThIdParams);
        if (result == 1) return true;
        else return false;
    }

    public int checkParticipantId(int thId, int userId) {
        String checkParticipantIdQuery = "select exists(select * from TransactionHistory where (sellerId = ? or buyerId = ?) and thId = ?)";
        Object[] isBuyerByThIdParams = new Object[] { userId, userId, thId };
        return this.jdbcTemplate.queryForObject(checkParticipantIdQuery,
                int.class,
                isBuyerByThIdParams);
    }

    public int checkWroteReviews(int thId, int userId) {
        String checkWroteReviewsQuery = "select exists(select * from TransactionReview where writerId = ? and thId = ?)";
        Object[] checkWroteReviewsParams = new Object[]{ userId, thId };
        return this.jdbcTemplate.queryForObject(checkWroteReviewsQuery,
                int.class,
                checkWroteReviewsParams);
    }

    public int checkBuyerReview(int thId) {
        String checkBuyerReviewQuery = "select exists(select * from TransactionReview where thId = ? and isNestedReply = 'N')";
        return this.jdbcTemplate.queryForObject(checkBuyerReviewQuery,
                int.class,
                thId);
    }

    public int checkReview(int trId, int userId) {
        String checkReviewQuery = "select exists(select trId from TransactionReview where trId = ? and writerId = ? and status <> 'deleted')";
        Object[] checkReviewParams = new Object[] { trId , userId };
        return this.jdbcTemplate.queryForObject(checkReviewQuery,
                int.class,
                checkReviewParams);
    }

    @Transactional
    public PostReviewsRes postReviewsOnBuyer(PostReviewsReq postReviewsReq, int userId) {
//        String getTransactionHistoryInfo = "select buyerId, sellerId from TransactionHistory where thId = ?";
//        GetUserId getUserId = this.jdbcTemplate.queryForObject(getTransactionHistoryInfo,
//                GetUserId.class,
//                postReviewsReq.getThId());
        String postReviewsQuery = "insert into TransactionReview(content, writerId, starRating, thId, isNestedReply) values (?,?,?,?,?)";
        Object[] postReviewsParams = new Object[] { postReviewsReq.getContent(), userId, postReviewsReq.getStarRating()
        ,postReviewsReq.getThId(), "N" };
        this.jdbcTemplate.update(postReviewsQuery, postReviewsParams);

        String lastInsertIdQuery = "select last_insert_id()";
        int trId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        String getReviewsQuery = "select trId, content, writerId, starRating, numberOfReports, thId, isNestedReply from TransactionReview\n" +
                "where trId = ?";
        return this.jdbcTemplate.queryForObject(getReviewsQuery,
                (rs,rowNum)-> new PostReviewsRes(
                        rs.getInt("trId"),
                        rs.getString("content"),
                        rs.getInt("writerId"),
                        rs.getDouble("starRating"),
                        rs.getInt("numberOfReports"),
                        rs.getInt("thId"),
                        rs.getString("isNestedReply")),
                trId
                );
    }

    @Transactional
    public PostReviewsRes postReviewsOnSeller(PostReviewsReq postReviewsReq, int userId) {
//        String getTransactionHistoryInfo = "select buyerId, sellerId from TransactionHistory where thId = ?";
//        GetUserId getUserId = this.jdbcTemplate.queryForObject(getTransactionHistoryInfo,
//                GetUserId.class,
//                postReviewsReq.getThId());
        String postReviewsQuery = "insert into TransactionReview(content, writerId, starRating, thId, isNestedReply) values (?,?,?,?,?)";
        Object[] postReviewsParams = new Object[] { postReviewsReq.getContent(), userId, postReviewsReq.getStarRating()
                ,postReviewsReq.getThId(), "Y" };
        this.jdbcTemplate.update(postReviewsQuery, postReviewsParams);

        String lastInsertIdQuery = "select last_insert_id()";
        int trId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        String getReviewsQuery = "select trId, content, writerId, starRating, numberOfReports, thId, isNestedReply from TransactionReview\n" +
                "where trId = ?";
        return this.jdbcTemplate.queryForObject(getReviewsQuery,
                (rs,rowNum)-> new PostReviewsRes(
                        rs.getInt("trId"),
                        rs.getString("content"),
                        rs.getInt("writerId"),
                        rs.getDouble("starRating"),
                        rs.getInt("numberOfReports"),
                        rs.getInt("thId"),
                        rs.getString("isNestedReply")),
                trId
        );
    }

    public PatchReviewsRes deleteReviews(int trId) {
        String patchReviewsQuery = "update TransactionReview\n" +
                "set status = 'deleted', content = '삭제되었습니다.'\n" +
                "where trId = ?";
        this.jdbcTemplate.update(patchReviewsQuery, trId);
        return new PatchReviewsRes(trId, "삭제되었습니다.");
    }

    public List<GetReviewsRes> getReviewsOnUser(int userId) {
        String getReviewsOnUserQuery = "select trId, case when TIMESTAMPDIFF(YEAR , TR.createdAt, CURRENT_DATE)>0 then CONCAT(TIMESTAMPDIFF(YEAR , TR.createdAt, CURRENT_DATE), '년 전')\n" +
                "    when TIMESTAMPDIFF(MONTH, TR.createdAt, CURRENT_DATE)>0 then CONCAT(TIMESTAMPDIFF(MONTH, TR.createdAt, CURRENT_DATE), '개월 전')\n" +
                "    when TIMESTAMPDIFF(DAY, TR.createdAt, CURRENT_DATE)>0 then CONCAT(TIMESTAMPDIFF(DAY, TR.createdAt, CURRENT_DATE), '일 전')\n" +
                "    else '오늘' end as time,\n" +
                "    TR.content, U.nickName as writerName, starRating, TR.thId, isNestedReply\n" +
                "from TransactionReview TR\n" +
                "inner join TransactionHistory TH on TR.thId = TH.thId and TH.sellerId = ?\n" +
                "left join User U on TR.writerId = U.userId";
        return this.jdbcTemplate.query(getReviewsOnUserQuery,
                (rs,rowNum)-> new GetReviewsRes(
                        rs.getInt("trId"),
                        rs.getString("time"),
                        rs.getString("content"),
                        rs.getString("writerName"),
                        rs.getDouble("starRating"),
                        rs.getInt("thId"),
                        rs.getString("isNestedReply")),
                userId);
    }

    public String getReviewsCount(int userId) {
        String getReviewsCountQuery = "select case when count(distinct trId) = 0 then '등록된 후기가 없습니다.'\n" +
                "    else CONCAT('이 상점의 거래후기 ' ,count(distinct trId)) end as reviewList\n" +
                "from TransactionReview TR\n" +
                "inner join TransactionHistory TH on TR.thId = TH.thId and TH.sellerId = ?\n" +
                "left join User U on TR.writerId = U.userId";
        return this.jdbcTemplate.queryForObject(getReviewsCountQuery,
                String.class,
                userId);
    }
}
