package com.example.demo.src.account;

import com.example.demo.src.account.model.PostUserAccountReq;
import com.example.demo.src.account.model.PostUserAccountRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AccountDao {

    private JdbcTemplate jdbcTemplate;


    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<PostUserAccountRes> getUserAccount(int userId) {
        String getUserAccountQuery = "select accountId, ownerName, bankId, account, isDefault from Account where userId = ? and status <> 'deleted' order by isDefault desc";
        return this.jdbcTemplate.query(getUserAccountQuery,
                (rs, rowNum) -> new PostUserAccountRes(
                        rs.getInt("accountId"),
                        rs.getString("ownerName"),
                        rs.getInt("bankId"),
                        rs.getString("account"),
                        rs.getString("isDefault")),
                userId);
    }

    public void postUserAccount(PostUserAccountReq postUserAccountReq, int userId) {
        String postUserAccountQuery = "insert into Account(isDefault, ownerName, bankId, account, userId) values (?,?,?,?,?)";
        Object[] postUserAccountParams = new Object[]{ postUserAccountReq.getIsDefault(), postUserAccountReq.getOwnerName(),
                postUserAccountReq.getBankId(), postUserAccountReq.getAccount(), userId};
        this.jdbcTemplate.update(postUserAccountQuery, postUserAccountParams);
    }

    public void deleteUserAccount(int accountId) {
        String deleteUserAccountQuery = "update Account\n" +
                "set status = 'deleted'\n" +
                "where accountId = ?";
        this.jdbcTemplate.update(deleteUserAccountQuery, accountId);
    }

    public int checkAccountId(int accountId) {
        String checkAccountIdQuery = "select exists(select accountId from Account where accountId = ?)";
        return this.jdbcTemplate.queryForObject(checkAccountIdQuery,
                int.class,
                accountId);
    }

    public int checkAccountCount(int userId) {
        String checkAccountCountQuery = "select count(distinct accountId) from Account where userId = ? and status <> 'deleted'";
        return this.jdbcTemplate.queryForObject(checkAccountCountQuery,
                int.class,
                userId);
    }

    public void patchUserAccount(int accountId) {
        String patchUserDeliveryAccount = "update Account\n" +
                "set isDefault = 'Y'\n" +
                "where accountId = ?";
        this.jdbcTemplate.update(patchUserDeliveryAccount, accountId);
    }

    public void patchAllUserAccounts(int userId) {
        String patchAllUserAccountQuery = "update Account\n" +
                "set isDefault = 'N'\n" +
                "where userId = ?";
        this.jdbcTemplate.update(patchAllUserAccountQuery, userId);
    }
}
