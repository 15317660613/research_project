package com.adc.da.finance.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.finance.entity.CashflowManagementEO;
import org.apache.ibatis.annotations.Param;

/**
 *
 * <br>
 * <b>功能：</b>F_CASHFLOW_MANAGEMENT CashflowManagementEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface CashflowManagementEODao extends BaseDao<CashflowManagementEO> {

    /**
     * 根据年月查询数据
     * @param businessGonfigId
     * @param cmYear
     * @param cmMonth
     * @return
     */
    CashflowManagementEO queryByBusinessGonfigIdAndYearAndMonth(@Param("businessGonfigId") String businessGonfigId, @Param("cmYear") String cmYear, @Param("cmMonth") String cmMonth);

    /**
     * 根据业务id
     */
    int updateByBusinessGonfigIdAndYearAndMonth(CashflowManagementEO cashflowManagementEO);

}
