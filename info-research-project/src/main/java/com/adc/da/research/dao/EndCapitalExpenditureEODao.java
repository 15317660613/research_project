package com.adc.da.research.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.entity.EndCapitalExpenditureEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_END_CAPITAL_EXPENDITURE EndCapitalExpenditureEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface EndCapitalExpenditureEODao extends BaseDao<EndCapitalExpenditureEO> {
    /**
     * 批量新增方法
     *
     * @param list
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-10-31
     **/
    int insertSelectiveAll(@Param("list") List<EndCapitalExpenditureEO> list);

}
