package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.BudgetDetailHistoryEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_BUDGET_DETAIL_HISTORY BudgetDetailHistoryEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface BudgetDetailHistoryEODao extends BaseDao<BudgetDetailHistoryEO> {


    void deleteByProjectId(@Param("projectId")String projectId);

    /**
     * 批量新增
     *
     * @param budgetDetailHistoryEOS
     */
    void batchInsertSelective(@Param("budgetDetailHistoryEOS") List<BudgetDetailHistoryEO> budgetDetailHistoryEOS);

}
