package com.adc.da.statistics.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.budget.entity.Statistics;
import com.adc.da.budget.entity.StatisticsEntity;
import com.adc.da.statistics.entity.BusinessWorktimeEO;
import com.adc.da.statistics.entity.ProjectWorktimeEO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>ST_BUSINESS_WORKTIME BusinessWorktimeEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface BusinessWorktimeEODao extends BaseDao<BusinessWorktimeEO> {
    int insertSelectiveAll(@Param("list") List<BusinessWorktimeEO> taskWorktimeEOList);

    List<BusinessWorktimeEO> getManDayByMonth(@Param("id") String id, @Param("year") String year);
    int deleteAll();
    List<StatisticsEntity> getWorkTimeByOrg(@Param("orgIds") List<String> orgIds,@Param("startTime") Date startTime,@Param("finishTime") Date finishTime);

    List<StatisticsEntity> getWorkTimeByOrgIds(@Param("orgIds") List<String> orgIds, @Param("startTime") Date startTime, @Param("finishTime") Date finishTime);

    List<Statistics> getBusWorkTimeByOrgIds(@Param("orgIds") List<String> orgIds, @Param("startTime") Date startTime, @Param("finishTime") Date finishTime);
}
