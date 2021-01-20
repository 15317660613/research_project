package com.adc.da.group.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.group.entity.UserCustomEO;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>TR_USER_CUSTOM UserCustomEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface UserCustomEODao extends BaseDao<UserCustomEO> {

    List<UserCustomEO> getUserByGroupId(String groupId);

}
