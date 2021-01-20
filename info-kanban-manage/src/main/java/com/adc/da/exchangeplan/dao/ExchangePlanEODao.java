package com.adc.da.exchangeplan.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.exchangeplan.entity.ExchangePlanEO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>DB_EXCHANGE_PLAN ExchangePlanEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ExchangePlanEODao extends BaseDao<ExchangePlanEO> {

    int logicDeleteByPrimaryKey(@Param("id") String id);
    List<ExchangePlanEO> getCurrentExchangePlanList(@Param("date") Date date);
    int logicDeleteByPrimaryKeys(@Param("ids") List<String> ids);

}
