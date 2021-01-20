package com.adc.da.business.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.business.entity.BudgetIncomeCalculateEO;
import com.adc.da.business.entity.OperatingBudgetEO;
import com.adc.da.business.entity.PairEO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>TS_OPERATING_BUDGET OperatingBudgetEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-05-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface OperatingBudgetEODao extends BaseDao<OperatingBudgetEO> {

    List<OperatingBudgetEO> queryYearData(String year, String startMonth, String endMonth);

    //查询已存在记录年份
    List<String> queryAllYear();

    //查询往年应收款
    Float queryForwardYearAR(String year);

    //查询当年应收
    Float queryThisYearAR(String year);

    //查询合同金额
    Float queryContractAmountByYearAndMonths(@Param("deptIds") List<String> deptIds, @Param("year") String year,
        @Param("startMonth") String startMonth, @Param("endMonth") String endMonth);

    //查询进账金额
    Float queryInvoiceAmountByYearAndMonths(@Param("deptIds") List<String> deptIds, @Param("year") String year,
        @Param("startMonth") String startMonth, @Param("endMonth") String endMonth);

    List<String> queryBusinessNamesByYearAndMonths(@Param("year") String year, @Param("startMonth") String startMonth,
        @Param("endMonth") String endMonth);

    List<BudgetIncomeCalculateEO> queryInvoiceAndBudgetByNameYearAndMonths(@Param("businessName") String businessName,
        @Param("year") String year, @Param("startMonth") String startMonth, @Param("endMonth") String endMonth,
        @Param("pager") BasePage pager);

    Integer countInvoiceAndBudgetByNameYearAndMonths(@Param("businessName") String businessName,
        @Param("year") String year, @Param("startMonth") String startMonth, @Param("endMonth") String endMonth);

    List<OperatingBudgetEO> selectEveryMonthInvoiceDataByBudgetName(@Param("bizName") String bizName,
        @Param("year") String year);

    List<PairEO> queryAllInvoiceByYearGroupByDeptId(@Param("year") Integer year);

    List<PairEO> queryAllInvoiceByYearGroupByDeptIdNew(@Param("beginTime") Date beginTime,
        @Param("endTime") Date endTime);

}
