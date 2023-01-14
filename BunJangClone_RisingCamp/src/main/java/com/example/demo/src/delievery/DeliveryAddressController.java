package com.example.demo.src.delievery;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.chatting.model.PostChatRes;
import com.example.demo.src.delievery.model.PostUserAddressReq;
import com.example.demo.src.delievery.model.PostUserAddressRes;
import com.example.demo.utils.JwtService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexName;
import static com.example.demo.utils.ValidationRegex.isRegexPhone;

@RestController
@RequestMapping("/delivery-address")
public class DeliveryAddressController {

    private final DeliveryAddressProvider deliveryAddressProvider;
    private final DeliveryAddressService deliveryAddressService;
    private final JwtService jwtService;

    @Autowired
    public DeliveryAddressController(DeliveryAddressProvider deliveryAddressProvider, DeliveryAddressService deliveryAddressService, JwtService jwtService) {
        this.deliveryAddressProvider = deliveryAddressProvider;
        this.deliveryAddressService = deliveryAddressService;
        this.jwtService = jwtService;
    }

    /**
     * 배달 주소지 생성 API
     * [POST] /delivery-address
     * @return BaseResponse<PostUserAddressRes>
     */
    @ResponseBody
    @PostMapping
    public BaseResponse<List<PostUserAddressRes>> postUserDeliveryAddress(@RequestBody PostUserAddressReq postUserAddressReq) {
        try {
        int userId = jwtService.getUserId();

            if(postUserAddressReq == null) {
                return new BaseResponse<>(EMPTY_BODY);
            }

            if (postUserAddressReq.getName().trim().isEmpty() || postUserAddressReq.getName() == null) {
                return new BaseResponse<>(POST_CANNOT_EMPTY_VALUE);
            }

            if (!isRegexName(postUserAddressReq.getName())) {
                return new BaseResponse<>(POST_USERS_INVALID_NAME);
            }

            if (!isRegexPhone(postUserAddressReq.getPhone()) || postUserAddressReq.getPhone().trim().isEmpty() || postUserAddressReq.getPhone() == null) {
                return new BaseResponse<>(POST_USERS_INVALID_PHONE);
            }

            if (postUserAddressReq.getAddress().isEmpty() || postUserAddressReq.getAddressDetail().isEmpty() || postUserAddressReq.getAddressDetail() == null || postUserAddressReq.getAddress() == null) {
                return new BaseResponse<>(POST_CANNOT_EMPTY_VALUE);
            }

            if (postUserAddressReq.getIsDefault().isEmpty() || postUserAddressReq == null) {
                return new BaseResponse<>(POST_CANNOT_EMPTY_VALUE);
            }

            if (!(postUserAddressReq.getIsDefault().equals("Y"))) {
                postUserAddressReq.setIsDefault("N");
            }

            List<PostUserAddressRes> postUserAddressRes = deliveryAddressService.postUserDeliveryAddress(postUserAddressReq, userId);
            return new BaseResponse<>(postUserAddressRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 배달 주소지 전체 조회 API
     * [GET] /delivery-address
     * @return BaseResponse<List<PostUserAddressRes>>
     */
    @ResponseBody
    @GetMapping
    public BaseResponse<List<PostUserAddressRes>> getUserDeliveryAddress() {
        try {
            int userId = jwtService.getUserId();

            List<PostUserAddressRes> getUserDeliveryAddress = deliveryAddressProvider.getUserDeliveryAddress(userId);
            return new BaseResponse<>(getUserDeliveryAddress);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 배달 주소지 삭제 API
     * [PATCH] /delivery-address/removal/:addressId
     * @return BaseResponse<List<PostUserAddressRes>>
     */
    @ResponseBody
    @PatchMapping("/removal/{addressId}")
    public BaseResponse<List<PostUserAddressRes>> deleteUserDeliveryAddress(@PathVariable("addressId") int addressId) {
        try {
            int userId = jwtService.getUserId();

            List<PostUserAddressRes> getUserDeliveryAddress = deliveryAddressService.deleteUserDeliveryAddress(addressId, userId);
            return new BaseResponse<>(getUserDeliveryAddress);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 배달 주소지 대표 주소로 변경 API
     * [PATCH] /delivery-address/:addressId
     * @return BaseResponse<List<PostUserAddressRes>>
     */
    @ResponseBody
    @PatchMapping("/{addressId}")
    public BaseResponse<List<PostUserAddressRes>> patchUserDeliveryAddress(@PathVariable("addressId") int addressId) {
        try {
            int userId = jwtService.getUserId();

            List<PostUserAddressRes> getUserDeliveryAddress = deliveryAddressService.patchUserDeliveryAddress(addressId, userId);
            return new BaseResponse<>(getUserDeliveryAddress);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
