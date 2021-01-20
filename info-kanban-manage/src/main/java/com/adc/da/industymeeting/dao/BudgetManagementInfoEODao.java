package com.adc.da.industymeeting.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.industymeeting.entity.BudgetManagementInfoEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>BUDGET_MANAGEMENT_INFO BudgetManagementInfoEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface BudgetManagementInfoEODao extends BaseDao<BudgetManagementInfoEO> {

    /**
     * 批量逻辑删除
     */
    void deleteLogicInBatch(List<String> ids);

    /**
     * 逻辑清空
     */
    void empty();

    List<BudgetManagementInfoEO> selectByYear(@Param("year") String year);
}
