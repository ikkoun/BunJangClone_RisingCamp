package com.example.demo.src.delievery;

import com.example.demo.config.BaseException;
import com.example.demo.src.delievery.model.PostUserAddressReq;
import com.example.demo.src.delievery.model.PostUserAddressRes;
import com.example.demo.src.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class DeliveryAddressService {
    private final DeliveryAddressDao deliveryAddressDao;
    private final DeliveryAddressProvider deliveryAddressProvider;
    private final UserProvider userProvider;

    @Autowired
    public DeliveryAddressService(DeliveryAddressDao deliveryAddressDao, DeliveryAddressProvider deliveryAddressProvider, UserProvider userProvider) {
        this.deliveryAddressDao = deliveryAddressDao;
        this.deliveryAddressProvider = deliveryAddressProvider;
        this.userProvider = userProvider;
    }

    @Transactional
    public List<PostUserAddressRes> postUserDeliveryAddress(PostUserAddressReq postUserAddressReq, int userId) throws BaseException {
        if (userProvider.checkUserId(userId) != 1) {
            // 회원정보에 userId가 없을 때
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (deliveryAddressProvider.checkDeliveryAddressCount(userId) >= 5) {
            throw new BaseException(GET_LOST_OF_DELIVERY_ON_USER);
        }

        if (postUserAddressReq.getIsDefault().equals("Y")) {
            try {
                // 기존에 있는 계좌들 전부 대표 계좌 off 처리
                patchAllUserDeliveryAddress(userId);
            } catch (BaseException exception) {
                System.out.println("데이터가 없을 때는 pass");
            }
        }

        try {
            deliveryAddressDao.postUserDeliveryAddress(postUserAddressReq, userId);
            return deliveryAddressProvider.getUserDeliveryAddress(userId);
        } catch (BaseException exception) {
            throw new BaseException(POST_ERROR_ADDRESS_ID);
        }
    }

    @Transactional
    public List<PostUserAddressRes> deleteUserDeliveryAddress(int addressId, int userId) throws BaseException {
        if (userProvider.checkUserId(userId) != 1) {
            // 회원정보에 userId가 없을 때
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (deliveryAddressProvider.checkAddressId(addressId) != 1) {
            // 배송지 테이블에 해당 배송지 idx가 없을 때
            throw new BaseException(GET_NON_ADDRESS_ID);
        }

        try {
            // 기존에 있는 계좌들 전부 대표 계좌 off 처리
            patchAllUserDeliveryAddress(userId);
        } catch (BaseException exception) {
            System.out.println("데이터가 없을 때는 pass");
        }

        try {
            deliveryAddressDao.deleteUserDeliveryAddress(addressId);
            return deliveryAddressProvider.getUserDeliveryAddress(userId);
        } catch (BaseException exception) {
            throw new BaseException(PATCH_ERROR_ADDRESS_ID);
        }
    }

    @Transactional
    public List<PostUserAddressRes> patchUserDeliveryAddress(int addressId, int userId) throws BaseException {
        if (userProvider.checkUserId(userId) != 1) {
            // 회원정보에 userId가 없을 때
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        if (deliveryAddressProvider.checkAddressId(addressId) != 1) {
            // 배송지 테이블에 해당 배송지 idx가 없을 때
            throw new BaseException(GET_NON_ADDRESS_ID);
        }

        try {
            // 기존에 있는 계좌들 전부 대표 계좌 off 처리
            patchAllUserDeliveryAddress(userId);
        } catch (BaseException exception) {
            System.out.println("데이터가 없을 때는 pass");
        }

        try {
            deliveryAddressDao.patchUserDeliveryAddress(addressId);
            return deliveryAddressProvider.getUserDeliveryAddress(userId);
        } catch (BaseException exception) {
            throw new BaseException(PATCH_ERROR_ADDRESS_ID);
        }
    }

    public void patchAllUserDeliveryAddress(int userId) throws BaseException {
        try {
            deliveryAddressDao.patchAllUserDeliveryAddress(userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
