package com.adc.da.capital.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.capital.entity.HiCapitalExpenditureEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_HI_CAPITAL_EXPENDITURE HiCapitalExpenditureEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface HiCapitalExpenditureEODao extends BaseDao<HiCapitalExpenditureEO> {
    /**
     * 批量新增方法
     *
     * @param list
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-10-31
     **/
    int insertSelectiveAll(@Param("list") List<HiCapitalExpenditureEO> list);

}
