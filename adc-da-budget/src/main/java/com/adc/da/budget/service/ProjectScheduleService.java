package com.adc.da.budget.service;

import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.dto.ProjectScheduleResponseDTO;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.DailyRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.utils.DateUtil;
import com.google.common.collect.Lists;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 项目日程业务层
 * created by chenhaidong 2018/11/22
 */
@Service
public class ProjectScheduleService {

    private Logger logger = LoggerFactory.getLogger(ProjectIOService.class);

    @Autowired
    DailyRepository dailyRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    private UserEPDao userEPDao;
    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    ProjectRepository projectRepository;

    /**
     * 查询项目日程信息
     *
     * @param projectId         项目id
     * @param scheduleBeginDate 日程开始日期(包含)
     * @param scheduleEndDate   日程结束日期(不包含)
     * @return
     */
    public List<ProjectScheduleResponseDTO> queryDailySchedule
                (String projectId, Date scheduleBeginDate, Date scheduleEndDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<ProjectScheduleResponseDTO> projectInfoList = new LinkedList<>();
        if (projectId == null) { return projectInfoList;}
        try {
            //如果开始时间为空，则默认为当前月的第一天
            if (scheduleBeginDate == null) {
                scheduleBeginDate = DateUtil.getFirstDayOfMonth
                        (simpleDateFormat.parse(simpleDateFormat.format(new Date())));
            }
            //如果结束时间为空，则默认为下个月的第一天
            if (scheduleEndDate == null) {
                scheduleEndDate = DateUtil.addMonths(DateUtil.getFirstDayOfMonth
                        (simpleDateFormat.parse(simpleDateFormat.format(new Date()))),1);
            }
        } catch (ParseException e) {
            logger.error(e.getMessage());
            return projectInfoList;
        }

        //查询时间范围内的项目日报记录
        Iterable<Daily> dailyIterable = dailyRepository.search
                (QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("projectId", projectId)).must(
                QueryBuilders.rangeQuery("dailyCreateTime")
                        .from(scheduleBeginDate.getTime())
                        .to(scheduleEndDate.getTime())
                        .includeLower(true)     // 包含下界
                        .includeUpper(false)      // 不包含上界
        ));
        Iterator<Daily> iterator = null;
        //如果没有日报记录则直接返回空列表
        if (dailyIterable == null || !(iterator = dailyIterable.iterator()).hasNext()) { return projectInfoList;}
        List<Daily> dailyList = new ArrayList<>();
        while (iterator.hasNext()) {
            dailyList.add(iterator.next());
        }

        //查询项目信息，获取项目名称
        Project project = projectRepository.findOne(projectId);
        String projectName = project != null ? project.getName() : null;

        //存储所有日报所属子任务id的Set
        Set<String> childredTaskIdSet = new HashSet<>();
        Map<String, List<TaskIndex>> taskIdMap = new HashMap<>();
        //存储日报创建人id的Set
        Set<String> staffIdSet = new HashSet<>();
        //存储日报创建人id对应的项目日报情况
        Map<String, List<ProjectScheduleResponseDTO>> projectScheduleResponseDTOMap = new HashMap<>();
        String[] childredTaskIds = null;
        int taskIdL = 0;
        String[] taskNameList = null;
        List<TaskIndex> taskIndexList = null;
        //遍历日报列表,添加要查询的项目id
        for (Daily daily : dailyList) {
            //当前日报创建人id
            String userId = daily.getCreateUserId();
            staffIdSet.add(userId);
            //复制日报信息
            ProjectScheduleResponseDTO projectScheduleResponseDTO = new ProjectScheduleResponseDTO();
            //日报id
            projectScheduleResponseDTO.setId(daily.getId());
            //项目id
            projectScheduleResponseDTO.setProjectId(projectId);
            //项目名称
            projectScheduleResponseDTO.setProjectName(projectName);
            //工作描述
            projectScheduleResponseDTO.setDescription(daily.getWorkDescription());
            //创建时间
            projectScheduleResponseDTO.setDailyCreateTime(simpleDateFormat.format(daily.getDailyCreateTime()));
            //是否完成
            projectScheduleResponseDTO.setComplete((daily.getFinishedStatus() != null
                    && ProjectStatusEnums.COMPLETE.getStatus().equals(daily.getFinishedStatus()) ?
                        Boolean.TRUE : Boolean.FALSE));
            //工时
            projectScheduleResponseDTO.setTaskWorktime(daily.getWorkTime());
            projectScheduleResponseDTO.setDailyName(daily.getEventName());

            //任务id列表
            projectScheduleResponseDTO.setTaskIdArray(daily.getTaskIdArray());
            //任务状态列表
            projectScheduleResponseDTO.setTaskStatusArray(daily.getTaskStatusArray());
            //任务工时列表
            projectScheduleResponseDTO.setWorktimeArray(daily.getWorktimeArray());

            //如果任务id不为空
            if ((childredTaskIds = daily.getTaskIdArray()) != null && (taskIdL = childredTaskIds.length) > 0) {
                for (int i = 0; i < taskIdL; i++) {
                    String id = childredTaskIds[i];
                    //创建一个新任务名称数组，长度与任务id数组一致
                    taskNameList = new String[taskIdL];
                    //创建一个任务id在数组中位置的辅助类
                    TaskIndex taskIndex = new TaskIndex(i, taskNameList);
                    childredTaskIdSet.add(id);
                    //将辅助类与对应的任务id关联在一起
                    if ((taskIndexList = taskIdMap.get(id)) == null) {
                        taskIdMap.put(id, (taskIndexList = new ArrayList<>()));
                    }
                    taskIndexList.add(taskIndex);
                }
            } else {
                //如果任务id为空
                taskNameList = new String[0];
            }
            projectScheduleResponseDTO.setTaskNameArray(taskNameList);

            //如果存在日程列表则增加,不存在则新增后添加
            if (!projectScheduleResponseDTOMap.containsKey(userId)) {
                projectScheduleResponseDTOMap.put(userId, new ArrayList<ProjectScheduleResponseDTO>());}
            projectScheduleResponseDTOMap.get(userId).add(projectScheduleResponseDTO);
            //将项目信息添加到列表中
            projectInfoList.add(projectScheduleResponseDTO);
        }

        //根据任务id列表查询任务信息
        List<ChildrenTask> taskList = Lists.newArrayList(childTaskRepository.findAll(childredTaskIdSet));
        //遍历任务列表
        for (ChildrenTask childrenTask : taskList) {
            String taskName = childrenTask.getChildTaskName();
            //根据任务id获取相关辅助类列表
            for (TaskIndex taskIndex : taskIdMap.get(childrenTask.getId())) {
                //在任务id对应的下标处加上任务id
                taskIndex.getTaskNames()[taskIndex.getIndex()] = taskName;
            }

        }


        //根据人员id列表查询人员信息
        String[] staffArrary = new String[staffIdSet.size()];
        int i = 0;
        for (String staff : staffIdSet) {
            staffArrary[i] = staff;
            i++;
        }

        List<UserEPEntity> userEPEntities = userEPDao.checkUserExistById(staffArrary);
        for (UserEPEntity staff : userEPEntities) {
            //添加日报创建人名称
            for (ProjectScheduleResponseDTO projectScheduleResponseDTO :
                    projectScheduleResponseDTOMap.get(staff.getUsid())) {
                projectScheduleResponseDTO.setPersonName(staff.getUsname());
                projectScheduleResponseDTO.setPersonId(staff.getUsid());
            }
        }
        return projectInfoList;
    }


    /**
     * 根据当前用户id查询所在的项目
     *
     * @param userId
     * @return
     */
    public List<Project> queryProjectByUserId(String userId) {
        List<Project> projectList = new ArrayList<>();
        if(userId == null) {
            return projectList;
        }
        Iterable<Project> projectIterable =
                projectRepository.search
                        (QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("projectMemberIds", userId)));
        Iterator<Project> iterator = null;
        //如果没有日报记录则直接返回空列表
        if (projectIterable == null || !(iterator = projectIterable.iterator()).hasNext()) {
            return projectList;
        }
        while (iterator.hasNext()) {
            projectList.add(iterator.next());
        }
        return projectList;
    }


}
