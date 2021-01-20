package com.adc.da.statistics.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.budget.entity.Statistics;
import com.adc.da.statistics.entity.BusinessWorktimeEO;
import com.adc.da.statistics.entity.TaskWorktimeEO;
import com.adc.da.statistics.vo.SqlResultMap;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>ST_TASK_WORKTIME TaskWorktimeEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface TaskWorktimeEODao extends BaseDao<TaskWorktimeEO> {

    int insertSelectiveAll(@Param("list") List<TaskWorktimeEO> taskWorktimeEOList);

    int deleteAll();

    List<Statistics> getTaskWorkTimeByProId(@Param("taskIds") List<String> taskIdss, @Param("startTime") Date startTime,
        @Param("finishTime") Date finishTime);

    List<String> queryOrgIdsByNameLike(@Param("args") String args);

    List<String> queryOrgIdsByUserId(@Param("userId") String userId);

    /**
     * 根据部门id查询工时投入
     *
     * @param deptId
     * @param startTime
     * @param finishTime
     * @return
     */
    List<TaskWorktimeEO> getTaskWorkTimeByTopDeptId(@Param("deptId") String deptId, @Param("startTime") Date startTime,
        @Param("finishTime") Date finishTime);

    /**
     * 查询业务id查询工时投入
     *
     * @param budgetId
     * @param startTime
     * @param finishTime
     * @return
     */
    List<TaskWorktimeEO> getBusinessWorkTimeByBudgetId(@Param("budgetId") String budgetId,
        @Param("startTime") Date startTime,
        @Param("finishTime") Date finishTime);

    Float getTaskWorkTimeByDeptIds(@Param("deptIds") List<String> deptIds, @Param("startTime") Date startTime,
        @Param("finishTime") Date finishTime);

    Double getTaskWorkTimeByTaskId(@Param("taskId") String taskId);

    List<SqlResultMap> getTaskWorkTimeByTaskIdAndYearGroupByMonth(@Param("year") String year , @Param("taskId") String taskId);
}
