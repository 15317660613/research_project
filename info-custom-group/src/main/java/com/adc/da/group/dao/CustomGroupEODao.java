package com.adc.da.group.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.group.entity.CustomGroupEO;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>TS_CUSTOM_GROUP CustomGroupEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface CustomGroupEODao extends BaseDao<CustomGroupEO> {

    List<CustomGroupEO> queryByCreateUserId(String userId);

    List<CustomGroupEO> queryByCreateUserIdAndGroupNameLike(String groupName, String userId);
}
