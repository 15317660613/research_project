package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.BudgetFundHistoryEO;
import com.adc.da.research.project.page.BudgetFundHistoryEOPage;
import com.adc.da.research.project.vo.BudgetFundVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_BUDGET_FUND_HISTORY BudgetFundHistoryEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface BudgetFundHistoryEODao extends BaseDao<BudgetFundHistoryEO> {

    void deleteByProjectId(@Param("projectId")String projectId);


    List<BudgetFundVO> queryByCompareList(BudgetFundHistoryEOPage var1);

    /**
     * 批量新增
     *
     * @param budgetFundHistoryEOS
     */
    void batchInsertSelective(@Param("budgetFundHistoryEOS") List<BudgetFundHistoryEO> budgetFundHistoryEOS);

}
