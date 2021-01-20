package com.adc.da.smallprogram.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.smallprogram.entity.ScheduleSupportUserEO;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_SUPPORT_USER ScheduleSupportUserEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ScheduleSupportUserEODao extends BaseDao<ScheduleSupportUserEO> {
    int insertList(List<ScheduleSupportUserEO> list);
    List<ScheduleSupportUserEO> selectBySupportId(String value) ;

}
