package com.adc.da.smallprogram.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.smallprogram.entity.ScheduleResearchMarkEO;
import com.adc.da.smallprogram.entity.ScheduleResearchUserEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_RESEARCH_MARK ScheduleResearchMarkEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-05-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ScheduleResearchMarkEODao extends BaseDao<ScheduleResearchMarkEO> {
    int insertList(@Param("list") List<ScheduleResearchMarkEO> list);
    int updateTopOrCollect(ScheduleResearchMarkEO researchMarkEO);
    int resetTop(ScheduleResearchMarkEO researchMarkEO);

}
