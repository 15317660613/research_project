package com.adc.da.finance.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.finance.entity.ProfitManagementEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>F_PROFIT_MANAGEMENT ProfitManagementEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ProfitManagementEODao extends BaseDao<ProfitManagementEO> {


    /**
     * 根据年月查询数据
     * @param businessGonfigId
     * @param pmYear
     * @param pmMonth
     * @return
     */
    ProfitManagementEO queryByBusinessGonfigIdAndYearAndMonth(@Param("businessGonfigId") String businessGonfigId,@Param("pmYear") String pmYear,@Param("pmMonth") String pmMonth);

    int deleteByBusinessIdAndYearAndMonth(@Param("businessId") String businessId, @Param("pmYear") String pmYear, @Param("pmMonth") String pmMonth);



    /**
     * 根据业务id
     */
    int updateByBusinessGonfigIdAndYearAndMonth(ProfitManagementEO profitManagementEO);

    int insertList(@Param("list") List<ProfitManagementEO> list);


}
