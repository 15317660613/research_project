package com.adc.da.research.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.pad.entity.PadOperationManageEO;
import com.adc.da.research.entity.ResProArriveFeeEO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>DB_RES_PRO_ARRIVE_FEE ResProArriveFeeEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ResProArriveFeeEODao extends BaseDao<ResProArriveFeeEO> {

    int insertList(@Param("list") List<ResProArriveFeeEO> list);
    List<ResProArriveFeeEO> queryByYearEqAndMonthGteAndResearchProjectIdList(@Param("year") int year,
                                                                                    @Param("month") int month,
                                                                                    @Param("list") List<String> list);

    List<ResProArriveFeeEO> queryByYearGteAndResearchProjectIdList(@Param("year") int year,
                                                                   @Param("list") List<String> list);
    void deleteLogicAll();
    BigDecimal sumArriveFeeByResearchProjectIdList( @Param("list") List<String> list);
    BigDecimal sumArriveFeeByYearAndResearchProjectIdList(@Param("year") Integer year, @Param("list") List<String> list);

    void deleteLogicInBatch(@Param("list") List<String> list);
    List<ResProArriveFeeEO> selectByResearchProjectIdList(@Param("list") List<String> list);

}
