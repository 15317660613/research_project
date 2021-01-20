package com.adc.da.finicialProcess.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.finicialProcess.entity.FinancialProcessTableEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>FINANCIAL_PROCESS_TABLE FinancialProcessTableEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-09-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface FinancialProcessTableEODao extends BaseDao<FinancialProcessTableEO> {
    void deleteLogicInBatch(List<String> ids);
    void deleteByParentId(List<String> ids);

    int insertList(@Param("list") List<FinancialProcessTableEO> list);
}
