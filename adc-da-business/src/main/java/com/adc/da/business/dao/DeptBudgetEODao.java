package com.adc.da.business.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.business.entity.DeptBudgetEO;
import com.adc.da.business.entity.PairEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * <br>
 * <b>功能：</b>TS_DEPT_BUDGET DeptBudgetEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-05-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface DeptBudgetEODao extends BaseDao<DeptBudgetEO> {

//    List<DeptBudgetEO> querySameTerm(String year ,String startMonth , String endMonth);

    //查询本部的利润
    Float queryBigDeptProfit(@Param("deptIds") List<String> deptIds ,@Param("thisYear") String thisYear);
    // 查询整个数据中心的利润
    Float queryCenterProfit(@Param("thisYear") String thisYear) ;

    Float queryCenterContractAmountByYear( String thisYear );
    Float queryCenterInvoiceAmountByYear( String thisYear ) ;

    Float queryBigDeptContractAmountByYear( @Param("deptIds") List<String> deptIds ,@Param("thisYear") String thisYear);

    Float queryBigDeptInvoiceAmountByYear(@Param("deptIds") List<String> deptIds , @Param("thisYear") String thisYear);


    //根据部门id查询指定年份某个月份区间的实际进账
    Map<String,Object> queryIncomeProfitAndCostByYearAndMonths(@Param("deptId") String deptId , @Param("year")String year ,
            @Param("startMonth")String startMonth , @Param("endMonth")String endMonth) ;
    // 根据年份列举所有部门办公用品花费
    List<Map<String,Object>> queryOfficeCostByYear(@Param("thisYear") String thisYear) ;
    // 根据年份列举所有部门办公耗材花费
    List<Map<String,Object>> queryConsumableCostByYear(@Param("thisYear") String thisYear) ;
    // 根据年份雷剧所有部门差旅支出
    List<Map<String,Object>> queryTravelCostByYear(@Param("thisYear") String thisYear) ;

    List<DeptBudgetEO> selectIncomeProfitAndCostByYearAndMonths(@Param("deptIds") List<String> deptIds , @Param("year")String year ,
                                                                   @Param("startMonth")String startMonth , @Param("endMonth")String endMonth) ;

    List<PairEO> queryAllCostByYearGroupByDeptId(@Param("year") Integer year) ;

}
