package com.adc.da.research.project.dao;


import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.SuspendAppEO;

/**
 *
 * <br>
 * <b>功能：</b>RS_SUSPEND_APP SuspendAppEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface SuspendAppEODao extends BaseDao<SuspendAppEO> {

    Integer insertSelectiveSuspend(SuspendAppEO suspendAppEO);

    Integer deleteById(String id);



}
