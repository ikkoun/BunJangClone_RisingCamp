package com.example.demo.src.delievery;

import com.example.demo.config.BaseException;
import com.example.demo.src.chatting.model.GetMakeDealRes;
import com.example.demo.src.delievery.model.PostUserAddressRes;
import com.example.demo.src.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class DeliveryAddressProvider {

    private final DeliveryAddressDao deliveryAddressDao;
    private final UserProvider userProvider;

    @Autowired
    public DeliveryAddressProvider(DeliveryAddressDao deliveryAddressDao, UserProvider userProvider) {
        this.deliveryAddressDao = deliveryAddressDao;
        this.userProvider = userProvider;
    }

    public List<PostUserAddressRes> getUserDeliveryAddress(int userId) throws BaseException {

        if (userProvider.checkUserId(userId) != 1) {
            throw new BaseException(USERS_EMPTY_USER_ID);
        }

        try {
            return deliveryAddressDao.getUserDeliveryAddress(userId);
        }
        catch (Exception exception) {
            throw new BaseException(GET_ERROR_ADDRESS_ID);
        }
    }

    public int checkAddressId(int addressId) throws BaseException {
        try {
            return deliveryAddressDao.checkAddressId(addressId);
        } catch (Exception exception) {
            throw new BaseException(GET_NON_ADDRESS_ID);
        }
    }

    public int checkDeliveryAddressCount(int userId) throws BaseException {
        try {
            return deliveryAddressDao.checkDeliveryAddressCount(userId);
        } catch (Exception exception) {
            throw new BaseException(RESPONSE_ERROR);
        }
    }
}
