package com.example.demo.src.account;

import com.example.demo.config.BaseException;
import com.example.demo.src.account.model.PostUserAccountRes;
import com.example.demo.src.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class AccountProvider {

    private final AccountDao accountDao;
    private final UserProvider userProvider;

    @Autowired
    public AccountProvider(AccountDao accountDao, UserProvider userProvider) {
        this.accountDao = accountDao;
        this.userProvider = userProvider;
    }

    public List<PostUserAccountRes> getUserAccount(int userId) throws BaseException {
        if (userProvider.checkUserId(userId) != 1) {
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        try {
            return accountDao.getUserAccount(userId);
        }
        catch (Exception exception) {
            throw new BaseException(GET_ERROR_ACCOUNT_ID);
        }
    }

    public int checkAccountId(int accountId) throws BaseException {
        try {
            return accountDao.checkAccountId(accountId);
        } catch (Exception exception) {
            throw new BaseException(GET_NON_ACCOUNT_ID);
        }
    }

    public int checkAccountCount(int userId) throws BaseException {
        try {
            int result = accountDao.checkAccountCount(userId);
            return result;
        } catch (Exception exception) {
            throw new BaseException(RESPONSE_ERROR);
        }
    }
}
