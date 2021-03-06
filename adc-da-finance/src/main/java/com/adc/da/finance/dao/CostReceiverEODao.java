package com.adc.da.finance.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.finance.entity.CostReceiverEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>F_COST_RECEIVER CostReceiverEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface CostReceiverEODao extends BaseDao<CostReceiverEO> {

    List<CostReceiverEO> queryAll();

    CostReceiverEO selectByOrgName(@Param("orgName") String orgName);

}
