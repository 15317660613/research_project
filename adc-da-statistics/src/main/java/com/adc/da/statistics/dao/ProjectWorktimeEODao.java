package com.adc.da.statistics.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.budget.entity.Statistics;
import com.adc.da.statistics.entity.ProjectWorktimeEO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * <br>
 * <b>功能：</b>ST_PROJECT_WORKTIME ProjectWorktimeEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ProjectWorktimeEODao extends BaseDao<ProjectWorktimeEO> {
    int insertSelectiveAll(@Param("list") List<ProjectWorktimeEO> taskWorktimeEOList);
    int deleteAll();

    List<Statistics> getProTaskWorkTimeByBusinessId(@Param("businessId") String businessId, @Param("startTime") Date startTime, @Param("finishTime") Date finishTime);

    List<Statistics> getBusTaskWorkTimeByBusinessId(@Param("businessId") String businessId, @Param("startTime") Date startTime, @Param("finishTime") Date finishTime);

    List<Statistics> getBusTaskWorkTimeByTaskId(@Param("taskIds") List<String> taskIds, @Param("startTime") Date startTime, @Param("finishTime") Date finishTime);

    List<ProjectWorktimeEO> getManDayByMonth(@Param("id") String id, @Param("year") String year);

    Double getProjectTotalWorkTime(@Param("projectId") String projectId);

    List<ProjectWorktimeEO> querySumWorkTime(BasePage var1);

    List<ProjectWorktimeEO>  queryByDeptIdsAndProjectIds(@Param("deptIds") List<String> deptIds,@Param("projectIds") List<String> projectIds);
    List<ProjectWorktimeEO>  queryByProjectIds(@Param("projectIds") List<String> projectIds);
}
