package com.adc.da.statistics.service;

import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.statistics.entity.BusinessWorktimeEO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.adc.da.budget.repository.ProjectRepository;

import com.adc.da.base.service.BaseService;
import com.adc.da.statistics.dao.ProjectWorktimeEODao;
import com.adc.da.statistics.entity.ProjectWorktimeEO;

import java.util.*;


/**
 *
 * <br>
 * <b>功能：</b>ST_PROJECT_WORKTIME ProjectWorktimeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectWorktimeEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectWorktimeEOService extends BaseService<ProjectWorktimeEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectWorktimeEOService.class);

    @Autowired
    private ProjectWorktimeEODao dao;


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    public ProjectWorktimeEODao getDao() {
        return dao;
    }

    public BusinessStatistics getPBWorkTime(String businessId ,String beginTime, String endTime, String userids){
        Date startTime = null ;
        Date finishTime = null ;

        if (StringUtils.equals("year",beginTime)){
            startTime = DateUtils.getCurrentYearStartTime();
            finishTime=DateUtils.getCurrentYearEndTime();

        }else if (StringUtils.equals("season",beginTime)){
            startTime = DateUtils.getCurrentSeasonStartTime();
            finishTime =DateUtils.getCurrentSeasonEndTime();

        }else if (StringUtils.equals("month",beginTime)){
            startTime = DateUtils.getTimesMonthStartTime();
            finishTime =DateUtils.getTimesMonthEndTime();

        }else if (StringUtils.equals("week",beginTime)){
            startTime = DateUtils.getTimesWeekStartTime();
            finishTime =DateUtils.getTimesWeekEndTime();

        }else {
            startTime = DateUtils.stringToDate(beginTime, DateUtils.YYYY_MM_DD_EN);
            finishTime = DateUtils.stringToDate(endTime, DateUtils.YYYY_MM_DD_EN);
        }

        BusinessStatistics allBusinessStatistics = new BusinessStatistics();
        double ProallWorkTime = 0.0;
        double TaskallWorkTime = 0.0;
        List<Statistics> ProstatisticsEntityList = new ArrayList<>();
        List<Statistics> TaskstatisticsEntityList = new ArrayList<>();
        ProallWorkTime = setProStatisticsEntityList(ProstatisticsEntityList, businessId, startTime, finishTime);
        TaskallWorkTime = setTaskStatisticsEntityList(TaskstatisticsEntityList,businessId,startTime,finishTime);

        allBusinessStatistics.setProjectAllWorkTime(ProallWorkTime);
        allBusinessStatistics.setTaskAllWorkTime(TaskallWorkTime);
        Collections.sort(TaskstatisticsEntityList);
        Collections.sort(ProstatisticsEntityList);
        allBusinessStatistics.setProjectStatistics(ProstatisticsEntityList);
        allBusinessStatistics.setTaskStatistics(TaskstatisticsEntityList);
        allBusinessStatistics.setBusinessId(businessId);
        return allBusinessStatistics;
    }
    public Double setProStatisticsEntityList(List<Statistics> statisticsEntityList,
                                          String businessId,Date startTime,Date finishTime){
        Double allWorkTime = 0.0;
        if(businessId.length() != 0 ){
            List<Statistics> statisticsList = dao.getProTaskWorkTimeByBusinessId(businessId,startTime,finishTime);
            List<String> projectIds = new ArrayList<String>();
            List<Project> projects = new ArrayList<>();
            for (Statistics s : statisticsList){
                projectIds.add(s.getId());
            }
            if(projectIds.size()>0) {
                projects = projectRepository.findByIdIn(projectIds);
            }
            for (Statistics s: statisticsList) {
                allWorkTime += s.getWorkTime();
                for(Project project : projects){
                    if(s.getId().equals(project.getId())){
                        s.setName(project.getName());
                    }
                }
                if(CollectionUtils.isNotEmpty(statisticsList)&&null!=statisticsList.get(0)) {
                    statisticsEntityList.add(s);
                }
            }
        }
        return allWorkTime;
    }

    public Double setTaskStatisticsEntityList(List<Statistics> statisticsEntityList,
                                          String business_id,Date startTime,Date finishTime){
        Double allWorkTime = 0.0;
        if(business_id.length() != 0 ){
            //List<Statistics> statisticsList = dao.getBusTaskWorkTimeByBusinessId(business_id,startTime,finishTime);
            List<Statistics> statisticsList = new ArrayList<>();
            List<Task> taskList = taskRepository.findByBudgetId(business_id);
            List<String> taskIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(taskList)){
                for(Task task:taskList){
                    taskIdList.add(task.getId());
                }
                statisticsList = dao.getBusTaskWorkTimeByTaskId(taskIdList,startTime,finishTime);
            }

            List<String> taskIds = new ArrayList<String>();
            List<Task> tasks = new ArrayList<>();
            for (Statistics s : statisticsList){
                taskIds.add(s.getId());
            }
            if(taskIds.size()>0){
                tasks = taskRepository.findByIdIn(taskIds);
            }
            for (Statistics s: statisticsList) {
                allWorkTime += s.getWorkTime();
                for(Task task : tasks){
                    if(s.getId().equals(task.getId())){
                        s.setName(task.getName());
                    }
                }
                if(CollectionUtils.isNotEmpty(statisticsList)&&null!=statisticsList.get(0)) {
                    statisticsEntityList.add(s);
                }
            }
        }
        return allWorkTime;
    }

    public List<ProjectWorktimeEO> getManDayByMonth(String id, String year) {
        return dao.getManDayByMonth(id, year);
    }

    public Double getProjectTotalWorkTime(String id) {
        return dao.getProjectTotalWorkTime(id);
    }

}
