package com.example.demo.src.account;

import com.example.demo.config.BaseException;
import com.example.demo.src.account.model.PostUserAccountReq;
import com.example.demo.src.account.model.PostUserAccountRes;
import com.example.demo.src.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AccountService {
    private final AccountDao accountDao;
    private final AccountProvider accountProvider;
    private final UserProvider userProvider;

    @Autowired
    public AccountService(AccountDao accountDao, AccountProvider accountProvider, UserProvider userProvider) {
        this.accountDao = accountDao;
        this.accountProvider = accountProvider;
        this.userProvider = userProvider;
    }

    @Transactional
    public List<PostUserAccountRes> postUserAccount(PostUserAccountReq postUserAccountReq, int userId) throws BaseException {
        if (userProvider.checkUserId(userId) != 1) {
            // 회원정보에 userId가 없을 때
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (accountProvider.checkAccountCount(userId) >= 2) {
            throw new BaseException(GET_LOST_OF_ACCOUNT_ON_USER);
        }

        if (postUserAccountReq.getIsDefault().equals("Y")) {
            try {
                // 기존에 있는 계좌들 전부 대표 계좌 off 처리
                patchAllUserAccounts(userId);
            } catch (BaseException exception) {
                System.out.println("데이터가 없을 때는 pass");
            }
        }

        try {
            accountDao.postUserAccount(postUserAccountReq, userId);
            return accountProvider.getUserAccount(userId);
        } catch (BaseException exception) {
            throw new BaseException(POST_ERROR_ACCOUNT_ID);
        }
    }

    @Transactional
    public List<PostUserAccountRes> deleteUserAccount(int accountId, int userId) throws BaseException {
        if (userProvider.checkUserId(userId) != 1) {
            // 회원정보에 userId가 없을 때
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (accountProvider.checkAccountId(accountId) != 1) {
            // 계좌 테이블에 해당 계좌 idx가 없을 때
            throw new BaseException(GET_NON_ACCOUNT_ID);
        }

        try {
            // 기존에 있는 계좌들 전부 대표 계좌 off 처리
            patchAllUserAccounts(userId);
        } catch (BaseException exception) {
            System.out.println("데이터가 없을 때는 pass");
        }

        try {
            accountDao.deleteUserAccount(accountId);
            return accountProvider.getUserAccount(userId);
        } catch (BaseException exception) {
            throw new BaseException(PATCH_ERROR_ACCOUNT_ID);
        }
    }

    @Transactional
    public List<PostUserAccountRes> patchUserAccount(int accountId, int userId) throws BaseException {
        if (userProvider.checkUserId(userId) != 1) {
            // 회원정보에 userId가 없을 때
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (accountProvider.checkAccountId(accountId) != 1) {
            // 계좌 테이블에 해당 계좌 idx가 없을 때
            throw new BaseException(GET_NON_ACCOUNT_ID);
        }

        try {
            // 기존에 있는 계좌들 전부 대표 계좌 off 처리
            patchAllUserAccounts(userId);
        } catch (BaseException exception) {
            System.out.println("데이터가 없을 때는 pass");
        }

        try {
            accountDao.patchUserAccount(accountId);
            return accountProvider.getUserAccount(userId);
        } catch (BaseException exception) {
            throw new BaseException(PATCH_ERROR_ACCOUNT_ID);
        }
    }

    public void patchAllUserAccounts(int userId) throws BaseException {
        try {
            accountDao.patchAllUserAccounts(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
