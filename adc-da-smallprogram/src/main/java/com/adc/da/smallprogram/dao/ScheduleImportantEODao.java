package com.adc.da.smallprogram.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.smallprogram.entity.ScheduleImportantEO;
/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_IMPORTANT ScheduleImportantEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-12-11 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ScheduleImportantEODao extends BaseDao<ScheduleImportantEO> {
   void softDeleteByPrimaryKey(String id);

}
