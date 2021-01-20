package com.adc.da.smallprogram.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.smallprogram.entity.ScheduleResearchUserEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_RESEARCH_USER ScheduleResearchUserEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-05-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ScheduleResearchUserEODao extends BaseDao<ScheduleResearchUserEO> {
   List<ScheduleResearchUserEO> selectByResearchIdList(@Param("list") List<String> list);

   List<ScheduleResearchUserEO> selectByResearchIdAndStatus(@Param("researchId") String researchId,@Param("status") Integer status );

   int insertList(@Param("list") List<ScheduleResearchUserEO> list);
   List<ScheduleResearchUserEO> queryFinishList(BasePage page);
}
