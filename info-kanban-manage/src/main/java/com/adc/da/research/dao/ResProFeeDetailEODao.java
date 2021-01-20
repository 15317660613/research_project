package com.adc.da.research.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.pad.entity.PadOperationManageEO;
import com.adc.da.research.entity.ResProFeeDetailEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>DB_RES_PRO_FEE_DETAIL ResProFeeDetailEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ResProFeeDetailEODao extends BaseDao<ResProFeeDetailEO> {

    int insertList(@Param("list") List<ResProFeeDetailEO> list);

    List<ResProFeeDetailEO> queryByResearchProjectIdList(@Param("list") List<String> list);
    List<ResProFeeDetailEO> sumByResearchProjectIdListGroupByFundsType(@Param("list") List<String> list);
    List<ResProFeeDetailEO> sumByResearchProjectIdList(@Param("list") List<String> list);
    void deleteLogicAll();
    void deleteLogicInBatch(@Param("list") List<String> list);
    List<ResProFeeDetailEO> selectByResearchProjectIdList(@Param("list") List<String> list);
}
