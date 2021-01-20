package com.adc.da.finance.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.finance.entity.CostManagementEO;
import com.adc.da.finance.page.MyCostManagementEOPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>F_COST_MANAGEMENT CostManagementEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface CostManagementEODao extends BaseDao<CostManagementEO> {

     void logicDeleteByPrimaryKeys(@Param("list") List<String> list);

     void goBackByPrimaryKey(@Param("id") String id);

     int insertList(@Param("list") List<CostManagementEO> list);

     List<CostManagementEO> selectByPrimaryKeys(@Param("list") List<String> list);

     List<CostManagementEO> pageByLoginUser(MyCostManagementEOPage myCostManagementEOPage);

     Integer pageByLoginUserCount(MyCostManagementEOPage myCostManagementEOPage);
     /**
      * 根据业务id年月统计余额
      * @param businessId
      * @param year
      * @param month
      * @return
      */
     CostManagementEO statisticsAmountByBusinessId(@Param("businessId") String businessId,@Param("year") String year,@Param("month") String month);

     List<CostManagementEO> selectSumCostByYearAndMonthGroupByBusinessId(@Param("year") String year, @Param("month") String month);

     List<CostManagementEO> selectSumCostByYearAndMonthAndBusinessIdGroupByBusinessId(@Param("year") String year, @Param("month") String month,
             @Param("businessId") String businessId);

     List<CostManagementEO> queryListByOrgId(@Param("orgId") String orgId);
}
