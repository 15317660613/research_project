package com.adc.da.smallprogram.dao;


import com.adc.da.base.dao.BaseDao;
import com.adc.da.smallprogram.entity.UserOpenidEO;
import com.adc.da.sys.entity.UserEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_USER_OPENID UserOpenidEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-03-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface UserOpenidEODao extends BaseDao<UserOpenidEO> {
    UserEO getUserByOpenId(String openId);

    int checkUserByOpenId(String openId);
    String getUserIdByOpenId(String openId);
    int deleteByUserId(@Param("userId") String userId);
    int deleteByUserIdList(List<String> list);
}
