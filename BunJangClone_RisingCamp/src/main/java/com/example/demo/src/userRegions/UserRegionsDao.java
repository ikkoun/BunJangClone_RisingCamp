package com.example.demo.src.userRegions;

import com.example.demo.src.regions.model.GetNearRegionsListRes;
import com.example.demo.src.regions.model.GetRegionsNameRes;
import com.example.demo.src.userRegions.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserRegionsDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){ this.jdbcTemplate = new JdbcTemplate(dataSource);}

    @Transactional
    public PostUserRegionsRes postUserRegions(int regionId, int userId) {
        String postUserRegionsQuery = "insert into UserRegions(regionId, userId, isRepresentative) values(?, ?, ?)";
        Object[] postUserRegionsParams = new Object[]{ regionId, userId, "Y"};
        this.jdbcTemplate.update(postUserRegionsQuery, postUserRegionsParams);

        String lastInsertIdQuery = "select last_insert_id()";
        int urId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        String getUserRegionsQuery = "select regionId, urId, userId, isRepresentative, rangeStatus from UserRegions\n" +
                "where urId = ?";
        GetUserRegionsInfo getUserRegionsInfo = this.jdbcTemplate.queryForObject(getUserRegionsQuery,
                (rs,rowNum)-> new GetUserRegionsInfo(
                        rs.getInt("regionId"),
                        rs.getInt("urId"),
                        rs.getInt("userId"),
                        rs.getString("isRepresentative"),
                        rs.getInt("rangeStatus")),
                urId
        );

        String getUserRegionsNameQuery = "select regionId, provinceName, districtName, regionName from RegionList where regionId = ?";
        GetRegionsNameRes getRegionsNameRes = this.jdbcTemplate.queryForObject(getUserRegionsNameQuery,
                (rs,rowNum)-> new GetRegionsNameRes(
                        rs.getInt("regionId"),
                        rs.getString("provinceName"),
                        rs.getString("districtName"),
                        rs.getString("regionName")),
                regionId
        );

        return new PostUserRegionsRes(getUserRegionsInfo.getRegionId(), getUserRegionsInfo.getUrId(),
                getUserRegionsInfo.getUserId(),
                getRegionsNameRes.getProvinceName() + " " + getRegionsNameRes.getDistrictName() + " " + getRegionsNameRes.getRegionName(),
                getUserRegionsInfo.getIsRepresentative(), getUserRegionsInfo.getRangeStatus());
    }

    @Transactional
    public PostUserRegionsRes patchUserRegions(int urId) {
        String patchUserRegionsQuery = "update UserRegions\n" +
                "set isRepresentative = 'Y'\n" +
                "where urId = ?";
        this.jdbcTemplate.update(patchUserRegionsQuery, urId);

        String getUserRegionsQuery = "select regionId, urId, userId, isRepresentative, rangeStatus from UserRegions\n" +
                "where urId = ?";
        GetUserRegionsInfo getUserRegionsInfo = this.jdbcTemplate.queryForObject(getUserRegionsQuery,
                (rs,rowNum)-> new GetUserRegionsInfo(
                        rs.getInt("regionId"),
                        rs.getInt("urId"),
                        rs.getInt("userId"),
                        rs.getString("isRepresentative"),
                        rs.getInt("rangeStatus")),
                urId
        );

        String getUserRegionsNameQuery = "select regionId, provinceName, districtName, regionName from RegionList where regionId = ?";
        GetRegionsNameRes getRegionsNameRes = this.jdbcTemplate.queryForObject(getUserRegionsNameQuery,
                (rs,rowNum)-> new GetRegionsNameRes(
                        rs.getInt("regionId"),
                        rs.getString("provinceName"),
                        rs.getString("districtName"),
                        rs.getString("regionName")),
                getUserRegionsInfo.getRegionId()
        );

        return new PostUserRegionsRes(getUserRegionsInfo.getRegionId(), getUserRegionsInfo.getUrId(),
                getUserRegionsInfo.getUserId(),
                getRegionsNameRes.getProvinceName() + " " + getRegionsNameRes.getDistrictName() + " " + getRegionsNameRes.getRegionName(),
                getUserRegionsInfo.getIsRepresentative(), getUserRegionsInfo.getRangeStatus());
    }

    public void patchUserRegionsNoRepresentative(int userId) {
        String patchUserRegionsNoRepresentative = "update UserRegions\n" +
                "set isRepresentative = 'N'\n" +
                "where isRepresentative = 'Y' and userId = ?";
        this.jdbcTemplate.update(patchUserRegionsNoRepresentative, userId);
    }

    public PatchUserRegionsRangeReq patchUserRegionsRange(PatchUserRegionsRangeReq patchUserRegionsRangeReq) {
        String patchUserRegionsRangeQuery = "update UserRegions\n" +
                "set rangeStatus = ?\n" +
                "where urId = ?";
        Object[] patchUserRegionsRangeParams = new Object[]{patchUserRegionsRangeReq.getRangeStatus(), patchUserRegionsRangeReq.getUrId()};
        this.jdbcTemplate.update(patchUserRegionsRangeQuery, patchUserRegionsRangeParams);

        return patchUserRegionsRangeReq;
    }

    public int checkUserRegionsByRegionId(int regionId, int userId) {
        String checkUserRegionsQuery = "select exists(select urId from UserRegions where regionId = ? and userId = ?)";
        Object[] checkUserRegionsParams = new Object[]{ regionId, userId };
        return this.jdbcTemplate.queryForObject(checkUserRegionsQuery,
                int.class,
                checkUserRegionsParams);
    }

    public int checkUserRegionsByUrId(int urId, int userId) {
        String checkUserRegionsQuery = "select exists(select urId from UserRegions where urId = ? and userId = ?)";
        Object[] checkUserRegionsParams = new Object[]{ urId, userId };
        return this.jdbcTemplate.queryForObject(checkUserRegionsQuery,
                int.class,
                checkUserRegionsParams);
    }

    public int getUserRegionsByRegionId(int regionId, int userId) {
        String getUserRegionsByRegionIdQuery = "select urId from UserRegions where regionId = ? and userId = ?";
        Object[] getUserRegionsByRegionIdParams = new Object[]{ regionId, userId };
        return this.jdbcTemplate.queryForObject(getUserRegionsByRegionIdQuery,
                int.class,
                getUserRegionsByRegionIdParams);
    }

    public List<GetUserRegionsRes> getUserRegionsList(int userId) {
        String getUserRegionsListQuery = "select urId, provinceName, districtName, regionName from UserRegions\n" +
                "inner join RegionList RL on UserRegions.regionId = RL.regionId\n" +
                "where userId = ? order by urId limit 20";
         return this.jdbcTemplate.query(getUserRegionsListQuery,
                (rs,rowNum)-> new GetUserRegionsRes(
                        rs.getInt("urId"),
                        rs.getString("provinceName") + " " +
                        rs.getString("districtName") + " " +
                        rs.getString("regionName")),
                userId);
    }

    public GetUserRegionsInfoRes getUserRegionsInfo(int urId) {
        String getUserRegionsInfoQuery = "select urId, provinceName, districtName, regionName, rangeStatus from UserRegions\n" +
                "inner join RegionList RL on UserRegions.regionId = RL.regionId\n" +
                "where urId = ?";
        return this.jdbcTemplate.queryForObject(getUserRegionsInfoQuery,
                (rs,rowNum)-> new GetUserRegionsInfoRes(
                        rs.getInt("urId"),
                        rs.getString("provinceName") + " " +
                                rs.getString("districtName") + " " +
                                rs.getString("regionName"),
                                rs.getInt("rangeStatus")),
                urId);
    }
}
