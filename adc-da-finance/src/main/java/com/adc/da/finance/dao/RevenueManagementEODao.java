package com.adc.da.finance.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.finance.entity.RevenueManagementEO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>F__REVENUE_MANAGEMENT RevenueManagementEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface RevenueManagementEODao extends BaseDao<RevenueManagementEO> {

    //根据业务id逻辑删除收入数据
    int logicDeleteByBusinessGonfigId(@Param("businessGonfigIds") List<String> businessGonfigIds);
    //根据id逻辑删除收入数据
    int logicDeleteIds(@Param("ids")List<String> ids);

    //根据Id、年、月查询收入金额
    RevenueManagementEO statisticsMoney(@Param("businessGonfigId") String businessGonfigId,@Param("rmYear") String rmYear,@Param("rmMonth") String rmMonth);

    //分组查询业务名称的金额
    List<RevenueManagementEO> queryByGroup(@Param("ids") List<String> ids);

}
