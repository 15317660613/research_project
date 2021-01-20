package com.adc.da.budget.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.business.entity.BudgetEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MyBudgetEODao  extends BaseDao<BudgetEO> {
    /**
     * 根据Budget名称查询机构Budget ID
     */
    String getBudgetEOIdByBudgetEOName(@Param("budgetName") String budgetName);

    List<String> queryBudgetEOIdsByBudgetEONames(@Param("budgetNames") List<String> budgetName) ;
}
