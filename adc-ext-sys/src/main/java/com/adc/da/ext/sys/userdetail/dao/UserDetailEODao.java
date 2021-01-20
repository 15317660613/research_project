package com.adc.da.ext.sys.userdetail.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.ext.sys.userdetail.entity.UserDetailEO;
import org.apache.ibatis.annotations.Param;


import java.util.Date;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_USER_DETAIL UserDetailEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-09-27 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface UserDetailEODao extends BaseDao<UserDetailEO> {
    int updateUserPasswordChangingTime(@Param("userId")String userId,@Param("updatedDate")Date updatedDate);
    int saveUserPasswordChangingTime(UserDetailEO userDetailEO);
    int insertUserPasswordChangingTime(@Param("id")String id, @Param("userId")String userId, @Param("updatePwdDate")String updatePwdDate);
    String getUserPasswordChangingTime(@Param("userId")String userId);
    List<UserDetailEO> selectByUserId(@Param("userId")String userId);

    void deleteByUserId(@Param("ids")List<String> ids);
    int updateByUserId(UserDetailEO userDetailEO);
}
