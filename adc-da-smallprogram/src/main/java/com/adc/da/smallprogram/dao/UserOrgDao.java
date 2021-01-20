package com.adc.da.smallprogram.dao;

import com.adc.da.smallprogram.entity.UserOrgEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserOrgDao {

    List<UserOrgEO> queryUserLeaders(String orgId);

    List<UserOrgEO> queryAllUserByOrgId(String orgId);

    List<UserOrgEO> queryAllAdcUserByOrgId(String orgId);

    List<UserOrgEO> queryChildOrg(String orgId);

    int queryUserNum(String orgId);
    int queryUserNumNew(@Param("orgId")String orgId);

    int queryAdcNum(String orgId);

    List<UserOrgEO> findUserByName(String usname);


    List<UserOrgEO> findUserByKeyword(@Param("keyword") String keyword,@Param("orgId") String orgId);

    List<UserOrgEO> findAllUser();

    List<UserOrgEO>  queryDepLeader(String orgId);

    List<String> queryRoleByUser(String userId);

    String queryOrgNameByOrgId(@Param("orgId") String orgId);

    List<UserOrgEO>  findUserByKeywordBigDeptLeader(@Param("orgId") String orgId) ;

    List<UserOrgEO>  findUserByKeywordSmallDeptLeader(@Param("orgId") String orgId) ;
    List<UserOrgEO>  findUserByKeywordADCLeader(@Param("orgId") String orgId) ;

    String queryOrgParentIdsByOrgId(@Param("orgId") String orgId);
}
