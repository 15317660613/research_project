package com.adc.da.finance.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.finance.entity.ReceivablesManagementEO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>F__RECEIVABLES_MANAGEMENT ReceivablesManagementEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ReceivablesManagementEODao extends BaseDao<ReceivablesManagementEO> {

    //根据业务id逻辑删除应收账款数据
    int logicDeleteByBusinessGonfigId(@Param("businessGonfigIds")List<String> businessGonfigIds);
    //根据id逻辑删除应收账款数据
    int logicDeleteIds(@Param("ids")List<String> ids);

    int insertList(@Param("list") List<ReceivablesManagementEO> list);
    //根据业务id年月统计本月收回金额（元）
    ReceivablesManagementEO statisticsMoney (@Param("businessGonfigId") String businessGonfigId,@Param("remYear") String remYear,@Param("remMonth") String remMonth);
    //分组查询业务名称的金额
    List<ReceivablesManagementEO> queryByGroup(@Param("ids") List<String> ids);

    List<ReceivablesManagementEO> selectSumReceiveByYearAndMonthGroupByBusinessId(@Param("remYear") String remYear,
            @Param("remMonth") String remMonth);
}
