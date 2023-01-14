package com.example.demo.src.userRegions;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.userRegions.model.PatchUserRegionsRangeReq;
import com.example.demo.src.userRegions.model.PostUserRegionsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class UserRegionsService {

    private final UserRegionsProvider userRegionsProvider;
    private final UserProvider userProvider;
    private final UserRegionsDao userRegionsDao;

    @Autowired
    public UserRegionsService(UserRegionsProvider userRegionsProvider, UserProvider userProvider, UserRegionsDao userRegionsDao) {
        this.userRegionsProvider = userRegionsProvider;
        this.userProvider = userProvider;
        this.userRegionsDao = userRegionsDao;
    }

    @Transactional
    public PostUserRegionsRes postUserRegions(int regionId, int userId) throws BaseException {
        if (userRegionsProvider.checkUserRegionsByRegionId(regionId, userId) != 0) {
            try {
                int urId = userRegionsProvider.getUserRegionsByRegionId(regionId, userId);
                return patchUserRegions(urId, userId);
            } catch (BaseException exception) {
                throw new BaseException(PATCH_ERROR_USER_REGIONS);
            }
        }

        else try {
            try {
                patchUserRegionsNoRepresentative(userId); // 대표 동네 미설정
            } catch (BaseException exception) {
                System.out.println("처리할 데이터가 없으므로 pass");
            }

            return userRegionsDao.postUserRegions(regionId, userId);
        } catch (Exception exception) {
            throw new BaseException(POST_ERROR_USER_REGIONS);
        }
    }

    @Transactional
    public PostUserRegionsRes patchUserRegions(int urId, int userId) throws BaseException {
        if (userRegionsProvider.checkUserRegionsByUrId(urId, userId) == 0) {
            throw new BaseException(PATCH_NON_USER_REGIONS);
        }
        try {
            try {
                patchUserRegionsNoRepresentative(userId); // 대표 동네 미설정
            } catch (BaseException exception) {
                System.out.println("처리할 데이터가 없으므로 pass");
            }
            return userRegionsDao.patchUserRegions(urId);
        } catch (Exception exception) {
            throw new BaseException(PATCH_ERROR_USER_REGIONS);
        }
    }

    public PatchUserRegionsRangeReq patchUserRegionsRange(PatchUserRegionsRangeReq patchUserRegionsRangeReq, int userId) throws BaseException {
        if (userRegionsProvider.checkUserRegionsByUrId(patchUserRegionsRangeReq.getUrId(), userId) == 0) {
            throw new BaseException(PATCH_NON_USER_REGIONS);
        }
        try {
            return userRegionsDao.patchUserRegionsRange(patchUserRegionsRangeReq);
        } catch (Exception exception) {
            throw new BaseException(PATCH_ERROR_USER_REGIONS_RANGE);
        }
    }

    public void patchUserRegionsNoRepresentative(int userId) throws BaseException {
        try {
            userRegionsDao.patchUserRegionsNoRepresentative(userId);
        } catch (Exception exception) {
            throw new BaseException(PATCH_ERROR_USER_REGIONS);
        }
    }
}
