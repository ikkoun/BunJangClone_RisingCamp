package com.example.demo.src.delievery;

import com.example.demo.src.delievery.model.PostUserAddressReq;
import com.example.demo.src.delievery.model.PostUserAddressRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class DeliveryAddressDao {

    private JdbcTemplate jdbcTemplate;


    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<PostUserAddressRes> getUserDeliveryAddress(int userId) {
        String getUserDeliveryAddressQuery = "select addressId, name, phone, address, addressDetail, isDefault from Address where userId = ? and status <> 'deleted' order by isDefault desc";
        return this.jdbcTemplate.query(getUserDeliveryAddressQuery,
                (rs, rowNum) -> new PostUserAddressRes(
                        rs.getInt("addressId"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getString("addressDetail"),
                        rs.getString("isDefault")),
                userId);
    }

    public void postUserDeliveryAddress(PostUserAddressReq postUserAddressReq, int userId) {
        String postUserDeliveryAddressQuery = "insert into Address(name, phone, address, addressDetail, isDefault, userId) values (?,?,?,?,?,?)";
        Object[] postUserDeliveryAddressParams = new Object[]{ postUserAddressReq.getName(), postUserAddressReq.getPhone(),
        postUserAddressReq.getAddress(), postUserAddressReq.getAddressDetail(), postUserAddressReq.getIsDefault(), userId};
        this.jdbcTemplate.update(postUserDeliveryAddressQuery, postUserDeliveryAddressParams);
    }

    public void deleteUserDeliveryAddress(int addressId) {
        String deleteUserDeliveryAddressQuery = "update Address\n" +
                "set status = 'deleted'\n" +
                "where addressId = ?";
        this.jdbcTemplate.update(deleteUserDeliveryAddressQuery, addressId);
    }

    public int checkAddressId(int addressId) {
        String checkAddressIdQuery = "select exists(select addressId from Address where addressId = ?)";
        return this.jdbcTemplate.queryForObject(checkAddressIdQuery,
                int.class,
                addressId);
    }

    public void patchUserDeliveryAddress(int addressId) {
        String patchUserDeliveryAddressQuery = "update Address\n" +
                "set isDefault = 'Y'\n" +
                "where addressId = ?";
        this.jdbcTemplate.update(patchUserDeliveryAddressQuery, addressId);
    }

    public void patchAllUserDeliveryAddress(int userId) {
        String patchAllUserDeliveryAddressQuery = "update Address\n" +
                "set isDefault = 'N'\n" +
                "where userId = ?";
        this.jdbcTemplate.update(patchAllUserDeliveryAddressQuery, userId);
    }

    public int checkDeliveryAddressCount(int userId) {
        String checkAccountCountQuery = "select count(distinct addressId) from Address where userId = ? and status <> 'deleted'";
        return this.jdbcTemplate.queryForObject(checkAccountCountQuery,
                int.class,
                userId);
    }
}
