package com.adc.da.research.config.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.config.entity.FundDetailsEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_FUND_DETAILS FundDetailsEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface FundDetailsEODao extends BaseDao<FundDetailsEO> {
    /**
     * 批量新增经费科目详情
     *
     * @param fundDetailsEOS
     */
    void batchInsertSelective(@Param("fundDetailsEOS") List<FundDetailsEO> fundDetailsEOS);

    /**
     * 根据模板id删除经费科目详情
     *
     * @param fundRulesIds
     */
    void deleteByFundRuleIds(@Param("fundRulesIds") List<String> fundRulesIds);

}
