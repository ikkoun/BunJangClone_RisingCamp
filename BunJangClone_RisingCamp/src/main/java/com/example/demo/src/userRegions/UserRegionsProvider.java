package com.example.demo.src.userRegions;

import com.example.demo.config.BaseException;
import com.example.demo.src.userRegions.model.GetUserRegionsInfoRes;
import com.example.demo.src.userRegions.model.GetUserRegionsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.GET_REGIONS_FAIL;
import static com.example.demo.config.BaseResponseStatus.PATCH_NON_USER_REGIONS;

@Service
public class UserRegionsProvider {
    private final UserRegionsDao userRegionsDao;

    @Autowired
    public UserRegionsProvider(UserRegionsDao userRegionsDao) {
        this.userRegionsDao = userRegionsDao;
    }

    public int checkUserRegionsByRegionId(int regionId, int userId) throws BaseException {
        try {
            return userRegionsDao.checkUserRegionsByRegionId(regionId, userId);
        } catch (Exception exception) {
            throw new BaseException(GET_REGIONS_FAIL);
        }
    }

    public int checkUserRegionsByUrId(int urId, int userId) throws BaseException {
        try {
            return userRegionsDao.checkUserRegionsByUrId(urId, userId);
        } catch (Exception exception) {
            throw new BaseException(GET_REGIONS_FAIL);
        }
    }

    public int getUserRegionsByRegionId(int regionId, int userId) throws BaseException {
        try {
            return userRegionsDao.getUserRegionsByRegionId(regionId, userId);
        } catch (Exception exception) {
            throw new BaseException(GET_REGIONS_FAIL);
        }
    }

    public List<GetUserRegionsRes> getUserRegionsList(int userId) throws BaseException {
        try {
            return userRegionsDao.getUserRegionsList(userId);
        } catch (Exception exception) {
            throw new BaseException(GET_REGIONS_FAIL);
        }
    }

    public GetUserRegionsInfoRes getUserRegionsInfo(int urId, int userId) throws BaseException {
        if (checkUserRegionsByUrId(urId, userId) == 0) {
            throw new BaseException(PATCH_NON_USER_REGIONS);
        }
        try {
            return userRegionsDao.getUserRegionsInfo(urId);
        } catch (Exception exception) {
            throw new BaseException(GET_REGIONS_FAIL);
        }
    }
}
