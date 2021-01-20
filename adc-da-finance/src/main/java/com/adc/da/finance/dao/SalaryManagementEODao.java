package com.adc.da.finance.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.finance.entity.SalaryManagementEO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>F_SALARY_MANAGEMENT SalaryManagementEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface SalaryManagementEODao extends BaseDao<SalaryManagementEO> {

    /**
     * 根据业务名称年月统计余额
     * @param businessId
     * @param year
     * @param month
     * @return
     */
    SalaryManagementEO statisticsAmountByBusinessId(@Param("businessId") String businessId, @Param("year") String year, @Param("month") String month);

    int insertList(@Param("list") List<SalaryManagementEO> list);

    void logicDeleteByPrimaryKeys(@Param("list") List<String> list);

    List<SalaryManagementEO> selectByPrimaryKeys(@Param("list") List<String> list);

    List<SalaryManagementEO> selectSumSalaryByYearAndMonthGroupByBusinessId(@Param("year") String year, @Param("month") String month);

    List<SalaryManagementEO> selectSumSalaryByYearAndMonthAndBusinessIdGroupByBusinessId(@Param("year") String year,
            @Param("month") String month, @Param("businessId") String businessId);
}
