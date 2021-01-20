package com.adc.da.industymeeting.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.industymeeting.entity.ReceivableIncomeFiledEO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RECEIVABLE_INCOME_FILED ReceivableIncomeFiledEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-03 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ReceivableIncomeFiledEODao extends BaseDao<ReceivableIncomeFiledEO> {

    /**
     * 批量逻辑删除
     */
    void deleteLogicInBatch(List<String> ids);

    List<ReceivableIncomeFiledEO> listByYearEqual(@Param("year") int year);

    BigDecimal sumReceivableAmountByYearAndMonthLte(@Param("year") int year, @Param("month") int month);

    BigDecimal sumReceivableAmountByYearAndMonthEqual(@Param("year") int year, @Param("month") int month);

    BigDecimal sumReceivableAmountByYearAndMonthLt(@Param("year") int year, @Param("month") int month);

    BigDecimal sumReceivableAmountByYearLte(@Param("year") int year);

    BigDecimal sumIncomeAmountByYearEqual(@Param("year") int year);
    List<ReceivableIncomeFiledEO> listByYearAndMonthEqualGroupArea(@Param("year") int year, @Param("month") int month);
    List<ReceivableIncomeFiledEO> listByYearAndMonthEqualGroupCompanyFullName(@Param("year") int year, @Param("month") int month);
    BigDecimal sumIncomeAmountByYearAndMonthLte(@Param("year") int year, @Param("month") int month);

    BigDecimal sumIncomeAmountByYearAndMonthEqual(@Param("year") int year, @Param("month") int month);

    BigDecimal sumIncomeAmountByYearAndMonthLt(@Param("year") int year, @Param("month") int month);

    BigDecimal sumIncomeAmountByYearLte(@Param("year") int year);

    BigDecimal sumIncomeAmountByYearLt(@Param("year") int year);

    BigDecimal sumIncomeAmountByYearAndMonthLtAndArea(@Param("year") int year, @Param("month") int month, @Param("area") String area);

    BigDecimal sumIncomeAmountByYearAndMonthEqualAndArea(@Param("year") int year, @Param("month") int month, @Param("area") String area);
    void insertList(@Param("list")List<ReceivableIncomeFiledEO> list);
    void deleteAll();
}
