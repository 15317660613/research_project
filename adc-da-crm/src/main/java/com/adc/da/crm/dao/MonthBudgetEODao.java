package com.adc.da.crm.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.crm.entity.MonthBudgetEO;
import org.apache.ibatis.annotations.Param;

/**
 *
 * <br>
 * <b>功能：</b>MONTH_BUDGET MonthBudgetEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface MonthBudgetEODao extends BaseDao<MonthBudgetEO> {
    int updateByProTargetBotomIdAndDelFlag(@Param("proTargetBotomId") String proTargetBotomId, @Param("delFlag") int delFlag);
}
