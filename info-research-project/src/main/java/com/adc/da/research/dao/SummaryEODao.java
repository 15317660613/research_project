package com.adc.da.research.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.entity.SummaryEO;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_SUMMARY SummaryEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface SummaryEODao extends BaseDao<SummaryEO> {
    SummaryEO selectByProjectId(Object var1);

}
