package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.BudgetFundEO;
import com.adc.da.research.project.page.BudgetFundEOPage;
import com.adc.da.research.project.vo.BudgetFundVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 * <br>
 * <b>功能：</b>RS_BUDGET_FUND BudgetFundEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface BudgetFundEODao extends BaseDao<BudgetFundEO> {

    public List<Map<Object, Object>> queryByYear(BudgetFundEOPage page);

    void deleteByProjectId(@Param("projectId")String projectId);

    /**
     * 批量新增
     *
     * @param budgetFundEOS
     */
    void batchInsertSelective(@Param("budgetFundEOS") List<BudgetFundEO> budgetFundEOS);

    List<BudgetFundVO> queryByCompareList(BudgetFundEOPage var1);

}
