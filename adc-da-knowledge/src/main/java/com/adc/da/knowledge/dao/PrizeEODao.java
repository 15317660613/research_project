package com.adc.da.knowledge.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.knowledge.entity.PrizeEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>K_PRIZE PrizeEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface PrizeEODao extends BaseDao<PrizeEO> {
    List<PrizeEO> queryByMyPage(BasePage page);
    int  queryByMyCount(BasePage page);
    List<PrizeEO> queryPrizeByPrizeIdsIn(@Param("prizeIdList") List<String> prizeIdList);

}
