package com.example.demo.src.userRegions;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.userRegions.model.GetUserRegionsInfoRes;
import com.example.demo.src.userRegions.model.GetUserRegionsRes;
import com.example.demo.src.userRegions.model.PatchUserRegionsRangeReq;
import com.example.demo.src.userRegions.model.PostUserRegionsRes;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexRangeStatus;

@RestController
@RequestMapping("/user-regions")
public class UserRegionsController {

    private final UserRegionsProvider userRegionsProvider;

    private final UserRegionsService userRegionsService;

    private final JwtService jwtService;

    @Autowired
    public UserRegionsController(UserRegionsProvider userRegionsProvider, UserRegionsService userRegionsService, JwtService jwtService) {
        this.userRegionsProvider = userRegionsProvider;
        this.userRegionsService = userRegionsService;
        this.jwtService = jwtService;
    }

    /**
     * 유저 동네 생성 (생성과 동시에 대표 동네로 설정) API
     * [POST] /user-regions/:regionId
     * @return BaseResponse<PostUserRegionsRes>
     */
    @ResponseBody
    @PostMapping("/{regionId}")
    public BaseResponse<PostUserRegionsRes> postUserRegions(@PathVariable("regionId") int regionId) {
        try {
            int userId = jwtService.getUserId();

            if (regionId < 1 || regionId > 22080) {
                return new BaseResponse<>(GET_REGION_EXIST_ERROR);
            }

            PostUserRegionsRes postUserRegionsRes = userRegionsService.postUserRegions(regionId, userId);
            return new BaseResponse<>(postUserRegionsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 동네 대표 동네 수정 API
     * [PATCH] /user-regions/:urId
     * @return BaseResponse<PostUserRegionsRes>
     */
    @ResponseBody
    @PatchMapping("/{urId}")
    public BaseResponse<PostUserRegionsRes> patchUserRegions(@PathVariable("urId") int urId) {
        try {
            int userId = jwtService.getUserId();

            PostUserRegionsRes postUserRegionsRes = userRegionsService.patchUserRegions(urId, userId);
            return new BaseResponse<>(postUserRegionsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저 동네 동네 범위 수정 API
     * [PATCH] /user-regions
     * @return BaseResponse<PostUserRegionsRes>
     */
    @ResponseBody
    @PatchMapping
    public BaseResponse<PatchUserRegionsRangeReq> patchUserRegionsRange(@RequestBody PatchUserRegionsRangeReq patchUserRegionsRangeReq) {
        try {
            int userId = jwtService.getUserId();

            if (!isRegexRangeStatus(patchUserRegionsRangeReq.getRangeStatus())) {
                return new BaseResponse<>(PATCH_USER_REGIONS_RANGE_ERROR);
            }

            PatchUserRegionsRangeReq patchUserRegionsRangeRes = userRegionsService.patchUserRegionsRange(patchUserRegionsRangeReq, userId);
            return new BaseResponse<>(patchUserRegionsRangeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저지역 리스트 최근 20개 불러오기 API
     * [GET] /user-regions
     * @return BaseResponse<List<GetUserRegionsRes>>
     */
    @ResponseBody
    @GetMapping
    public BaseResponse<List<GetUserRegionsRes>> getUserRegionsList() {
        try {
            int userId = jwtService.getUserId();

            List<GetUserRegionsRes> getUserRegionsRes = userRegionsProvider.getUserRegionsList(userId);
            return new BaseResponse<>(getUserRegionsRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 유저지역 id로 동네 이름, 동네 범위 가져오기 API
     * [GET] /user-regions/:urId
     * @return BaseResponse<List<GetUserRegionsRes>>
     */
    @ResponseBody
    @GetMapping("/{urId}")
    public BaseResponse<GetUserRegionsInfoRes> getUserRegionsInfo(@PathVariable("urId") int urId) {
        try {
            int userId = jwtService.getUserId();

            GetUserRegionsInfoRes getUserRegionsInfoRes = userRegionsProvider.getUserRegionsInfo(urId, userId);
            return new BaseResponse<>(getUserRegionsInfoRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
