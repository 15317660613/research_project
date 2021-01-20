package com.adc.da.industymeeting.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.industymeeting.entity.ReceivableIncomeEO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RECEIVABLE_INCOME ReceivableIncomeEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ReceivableIncomeEODao extends BaseDao<ReceivableIncomeEO> {

    /**
     * 逻辑清空
     */
    void empty();

    /**
     * 批量逻辑删除
     */
    void deleteLogicInBatch(List<String> ids);

    //所有应收
    Double sumReceivableAmount();

    Double sumIncomeAmount();


    //<!--小于某个年份的应收集合-->
    BigDecimal sumReceivableAmountByYearLt(@Param("year") int year);

    BigDecimal sumReceivableAmountByYearLte(@Param("year") int year);

    //<!--小于某个年份的到账集合-->
    BigDecimal sumIncomeAmountByYearLt(@Param("year") int year);


    List<ReceivableIncomeEO> selectByYear(@Param("year") int year);

    List<ReceivableIncomeEO> selectByYearLte(@Param("year") int year);

    List<ReceivableIncomeEO> selectByYearLt(@Param("year") int year);

    List<ReceivableIncomeEO> selectByYearAndMonth(@Param("year") int year,@Param("month") int month);

    /***
     * 统计 zyh
     */

    //① 企业应收账款top 10

    List<ReceivableIncomeEO> accountReceivableByEnterprise(@Param("topNum") Integer topNum);

    //② 部门应收账款占比TOP10

    List<ReceivableIncomeEO> accountReceivableByDepart(@Param("topNum") Integer topNum);

    //③ 历史年份到账额
    ReceivableIncomeEO queryHistoryYearWeeklyArrivalByCorpname(
            @Param("year") String year,@Param("corpname") String corpname);

    //④ 本年应该收账款余额
    ReceivableIncomeEO queryCurrentYearAmountReceivableByCorpname(
            @Param("year") String year,@Param("corpname") String corpname);

}
