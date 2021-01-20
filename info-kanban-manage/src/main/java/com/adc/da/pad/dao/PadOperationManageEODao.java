package com.adc.da.pad.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.pad.entity.PadOperationManageEO;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>PAD_OPERATION_MANAGE PadOperationManageEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface PadOperationManageEODao extends BaseDao<PadOperationManageEO> {
   void deleteLogicInBatch(@Param("list") List<String> list);

   int insertList(@Param("list")List<PadOperationManageEO> list);
   PadOperationManageEO sumByMonthAndYear(@Param("month") Integer month,@Param("year")Integer year);
   PadOperationManageEO sumByYear(Integer year);
   List<PadOperationManageEO> sumGroupByMonthInYear(Integer year);
   List<PadOperationManageEO> sumGroupByOrgInYear(Integer year);
   List<PadOperationManageEO> sumGroupByOrgInYearAndMonthLte(@Param("year")Integer year, @Param("month")Integer month);
   List<PadOperationManageEO> sumGroupByOrgInYearAndMonthEq(@Param("year")Integer year, @Param("month")Integer month);
   List<String> selectOrgNames();
   List<String> selectBigOrgNames();
}
