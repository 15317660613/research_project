package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.BudgetDetailEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_BUDGET_DETAIL BudgetDetailEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface BudgetDetailEODao extends BaseDao<BudgetDetailEO> {


   List<BudgetDetailEO> queryAmount ();

   void deleteByProjectId(@Param("projectId")String projectId);

   void deleteByProjectIdAndBudgetType(@Param("projectId")String projectId,@Param("budgetType")String budgetType);

   /**
    * 批量新增
    *
    * @param budgetDetailEOS
    */
   void batchInsertSelective(@Param("budgetDetailEOS") List<BudgetDetailEO>budgetDetailEOS);

}
