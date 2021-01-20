package com.adc.da.finance.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.finance.entity.ResearchIssueEO;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>F_RESEARCH_ISSUE ResearchIssueEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ResearchIssueEODao extends BaseDao<ResearchIssueEO> {

    /**
     * 批量逻辑删除
     */
    void deleteLogicInBatch(List<String> ids);

}
