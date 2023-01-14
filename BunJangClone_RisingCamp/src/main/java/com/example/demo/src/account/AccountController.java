package com.example.demo.src.account;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.account.model.PostUserAccountReq;
import com.example.demo.src.account.model.PostUserAccountRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexName;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountProvider accountProvider;
    private final AccountService accountService;
    private final JwtService jwtService;

    @Autowired
    public AccountController(AccountProvider accountProvider, AccountService accountService, JwtService jwtService) {
        this.accountProvider = accountProvider;
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    /**
     * 계좌 생성 API
     * [POST] /accounts
     * @return BaseResponse<List<PostUserAccountRes>>
     */
    @ResponseBody
    @PostMapping
    public BaseResponse<List<PostUserAccountRes>> postUserAccount(@RequestBody PostUserAccountReq postUserAccountReq) {
        try {
        int userId = jwtService.getUserId();

            if(postUserAccountReq == null) {
                return new BaseResponse<>(EMPTY_BODY);
            }

            if (postUserAccountReq.getOwnerName().trim().isEmpty() || postUserAccountReq.getOwnerName() == null) {
                return new BaseResponse<>(POST_CANNOT_EMPTY_VALUE);
            }

            if (!isRegexName(postUserAccountReq.getOwnerName())) {
                return new BaseResponse<>(POST_USERS_INVALID_NAME);
            }

            if (postUserAccountReq.getBankId() < 0 || postUserAccountReq.getBankId() > 10) {
                return new BaseResponse<>(POST_USERS_NON_BANK_ID);
            }

            if (postUserAccountReq.getAccount().isEmpty() || postUserAccountReq.getAccount() == null) {
                return new BaseResponse<>(POST_CANNOT_EMPTY_VALUE);
            }

            if (postUserAccountReq.getIsDefault().isEmpty() || postUserAccountReq == null) {
                return new BaseResponse<>(POST_CANNOT_EMPTY_VALUE);
            }

            if (!(postUserAccountReq.getIsDefault().equals("Y"))) {
                postUserAccountReq.setIsDefault("N");
            }

            List<PostUserAccountRes> postUserAccountRes = accountService.postUserAccount(postUserAccountReq, userId);
            return new BaseResponse<>(postUserAccountRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 계좌 전체 조회 API
     * [GET] /accounts
     * @return BaseResponse<List<PostUserAccountRes>>
     */
    @ResponseBody
    @GetMapping
    public BaseResponse<List<PostUserAccountRes>> getUserAccount() {
        try {
            int userId = jwtService.getUserId();

            List<PostUserAccountRes> getUserAccount = accountProvider.getUserAccount(userId);
            return new BaseResponse<>(getUserAccount);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 계좌 삭제 API
     * [PATCH] /accounts/removal/:accountId
     * @return BaseResponse<List<PostUserAccountRes>>
     */
    @ResponseBody
    @PatchMapping("/removal/{accountId}")
    public BaseResponse<List<PostUserAccountRes>> deleteUserAccount(@PathVariable("accountId") int accountId) {
        try {
            int userId = jwtService.getUserId();

            List<PostUserAccountRes> getUserAccount = accountService.deleteUserAccount(accountId, userId);
            return new BaseResponse<>(getUserAccount);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 대표 계좌 변경 API
     * [PATCH] /accounts/:accountId
     * @return BaseResponse<List<PostUserAccountRes>>
     */
    @ResponseBody
    @PatchMapping("/{accountId}")
    public BaseResponse<List<PostUserAccountRes>> patchUserAccount(@PathVariable("accountId") int accountId) {
        try {
            int userId = jwtService.getUserId();

            List<PostUserAccountRes> getUserAccount = accountService.patchUserAccount(accountId, userId);
            return new BaseResponse<>(getUserAccount);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
