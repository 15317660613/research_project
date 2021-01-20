package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.ResearchBudgetDetailEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_RESEARCH_BUDGET_DETAIL ResearchBudgetDetailEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-10 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ResearchBudgetDetailEODao extends BaseDao<ResearchBudgetDetailEO> {
    /**
     * 批量新增
     *
     * @param researchBudgetDetailEOS
     */
    void batchInsertSelective(@Param("researchBudgetDetailEOS") List<ResearchBudgetDetailEO> researchBudgetDetailEOS);


    void deleteByProjectId(@Param("projectId")String projectId,@Param("detailType")String  detailType);


    void deleteByDetailType(@Param("projectId")String projectId,@Param("extInfo3")String  extInfo3);


    void deleteAllByProjectId(@Param("projectId")String projectId);
}
