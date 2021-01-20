package com.adc.da.crm.dao;

import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author qichunxu
 */
public interface UserDao {


    /**
     * 查询指定部门列表的用户信息
     * @param orgIds
     * @param usname
     * @return
     */
    List<UserEO> selectByOrgIdAndUsName(@Param("orgIds") List<String> orgIds, @Param("usname")String usname);


    List<OrgEO> listAllOrg();

    //查询用户对应的机构
    List<String> queryOrgIdByUid(String userId);


//    List<String> queryBigDeptUserIds();

//    List<String> queryCenterUserIds();

    List<UserEO> queryUserEOByUserIds();

    UserEO queryUserByUserId(@Param("usid") String usid);

}
