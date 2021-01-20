package com.adc.da.capital.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.capital.entity.CapitalExpenditureEO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_CAPITAL_EXPENDITURE CapitalExpenditureEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface CapitalExpenditureEODao extends BaseDao<CapitalExpenditureEO> {
    int insertSelectiveAll(@Param("list") List<CapitalExpenditureEO> list);

    /**
     * 删除
     * @param value
     * @return
     */
    @Delete("DELETE FROM RS_CAPITAL_EXPENDITURE WHERE research_project_id_ = #{value}")
    int deleteByProjectId(String value);
}
