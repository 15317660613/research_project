package com.adc.da.statistics.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.entity.ProjectStatistics;
import com.adc.da.budget.entity.Statistics;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.statistics.dao.TaskWorktimeEODao;
import com.adc.da.statistics.entity.TaskWorktimeEO;
import com.adc.da.statistics.vo.SqlResultMap;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <br>
 * <b>功能：</b>ST_TASK_WORKTIME TaskWorktimeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("taskWorktimeEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class TaskWorktimeEOService extends BaseService<TaskWorktimeEO, String> {

    @Autowired
    private TaskWorktimeEODao dao;

    public TaskWorktimeEODao getDao() {
        return dao;
    }

    @Autowired
    private TaskRepository taskRepository;

    public ProjectStatistics getTaskWorkTime(String projectId, String beginTime, String endTime, String userids) {
        Date startTime = null;
        Date finishTime = null;

        if (StringUtils.equals("year", beginTime)) {
            startTime = DateUtils.getCurrentYearStartTime();
            finishTime = DateUtils.getCurrentYearEndTime();

        } else if (StringUtils.equals("season", beginTime)) {
            startTime = DateUtils.getCurrentSeasonStartTime();
            finishTime = DateUtils.getCurrentSeasonEndTime();

        } else if (StringUtils.equals("month", beginTime)) {
            startTime = DateUtils.getTimesMonthStartTime();
            finishTime = DateUtils.getTimesMonthEndTime();

        } else if (StringUtils.equals("week", beginTime)) {
            startTime = DateUtils.getTimesWeekStartTime();
            finishTime = DateUtils.getTimesWeekEndTime();

        } else {
            startTime = DateUtils.stringToDate(beginTime, DateUtils.YYYY_MM_DD_EN);
            finishTime = DateUtils.stringToDate(endTime, DateUtils.YYYY_MM_DD_EN);
        }
        ProjectStatistics allProjectStatistics = new ProjectStatistics();

        double allWorkTime = 0.0;
        List<Statistics> statisticsEntityList = new ArrayList<>();

        List<Task> tasks = taskRepository.findByProjectId(projectId);
        List<String> taskIds = new ArrayList<>();
        for (Task s : tasks) {
            taskIds.add(s.getId());
        }
        allWorkTime = setStatisticsEntityList(statisticsEntityList, taskIds, startTime, finishTime);
        allProjectStatistics.setProjectId(projectId);
        allProjectStatistics.setTaskAllWorkTime(allWorkTime);
        Collections.sort(statisticsEntityList);
        allProjectStatistics.setTaskStatistics(statisticsEntityList);
        return allProjectStatistics;
    }

    public Double setStatisticsEntityList(List<Statistics> statisticsEntityList,
        List<String> taskIdss, Date startTime, Date finishTime) {
        Double allWorkTime = 0.0;
        if (CollectionUtils.isNotEmpty(taskIdss)) {
            List<Statistics> statisticsList = dao.getTaskWorkTimeByProId(taskIdss, startTime, finishTime);
            List<String> taskIds = new ArrayList<>();
            List<Task> tasks = new ArrayList<>();
            for (Statistics s : statisticsList) {
                taskIds.add(s.getId());
            }
            if (!taskIds.isEmpty()) {
                tasks = taskRepository.findByIdIn(taskIds);
            }
            for (Statistics s : statisticsList) {
                allWorkTime += s.getWorkTime();
                for (Task task : tasks) {
                    if (s.getId().equals(task.getId())) {
                        s.setName(task.getName());
                    }
                }
                if (CollectionUtils.isNotEmpty(statisticsList) && null != statisticsList.get(0)) {
                    statisticsEntityList.add(s);
                }
            }
        }
        return allWorkTime;
    }

    public List<String> queryOrgIdsByNameLike(String args) {
        return dao.queryOrgIdsByNameLike(args);
    }

    public List<String> queryOrgIdsByUserId(String userId) {
        return dao.queryOrgIdsByUserId(userId);
    }

    public Float getTaskWorkTimeByDeptIds(List<String> deptIds, Date startTime, Date finishTime) {
        return dao.getTaskWorkTimeByDeptIds(deptIds, startTime, finishTime);
    }

    public List<TaskWorktimeEO> getTaskWorkTimeByTaskIdAndYearGroupByMonth( String year ,String taskId){
        List<SqlResultMap> sqlResultMapList = dao.getTaskWorkTimeByTaskIdAndYearGroupByMonth(year,taskId);
        Map<Integer,Float> map = getWorkTimeByMouthMap(sqlResultMapList);

        List<TaskWorktimeEO> taskWorktimeEOList = new ArrayList<>();
        for (int month = 1 ; month < 13 ; month ++){
            TaskWorktimeEO taskWorktimeEO = new TaskWorktimeEO();
            taskWorktimeEO.setMonth(String.valueOf(month));
            taskWorktimeEO.setYear(year);
            Float workTime = map.get(month);
            if (null != workTime){
                taskWorktimeEO.setWorktime(workTime);
            }else {
                taskWorktimeEO.setWorktime(0.0f);
            }
            taskWorktimeEOList.add(taskWorktimeEO);
        }
        return taskWorktimeEOList ;
    }
//   taskWorktimeEO.setYear(year);
    private Map<Integer,Float> getWorkTimeByMouthMap( List<SqlResultMap> sqlResultMapList){
        Map<Integer,Float> map = new HashMap<>();
        for(SqlResultMap sqlResultMap : sqlResultMapList){
            map.put(sqlResultMap.getMonth(),sqlResultMap.getWorktime());
        }
        return map;
    }
}
