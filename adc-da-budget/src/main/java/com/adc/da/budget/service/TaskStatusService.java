package com.adc.da.budget.service;

import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.utils.DateUtil;
import com.adc.da.budget.vo.TaskDetailVO;
import com.adc.da.budget.vo.TaskStatusVO;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskStatusService {

    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private BudgetEOService budgetEOService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserEODao userEODao;

    public TaskDetailVO queryShowTaskStatus(String taskId) throws Exception {
        TaskDetailVO.TaskDetailVOBuilder result = TaskDetailVO.builder();
        Task task = taskRepository.findByIdAndDelFlagNot(taskId, Boolean.TRUE);
        if (null == task) {
            return result.build();
        }
        result.taskName(task.getName())
              .type(task.getBudgetId() == null ? "项目任务" : "业务任务")
              .memberNames(task.getMemberNames())
              .status(task.getTaskStatus())
              .priority(task.getPriority())
              .planStartTime(task.getPlanStartTime())
              .planEndTime(task.getPlanEndTime())
              .taskStatusFinished(getTaskStatus(taskId))
                .approveUserName(task.getApproveUserName())
                .memberIds(task.getMemberIds());
        Project project = null;
        BudgetEO budgetEO = null;
        if (StringUtils.isNotEmpty(task.getProjectId())) {
            //获取项目任务负责人id
            project = projectRepository.findById(task.getProjectId());
            budgetEO = budgetEOService.selectByPrimaryKey(project.getBudgetId());
            if (null != budgetEO) {
                result.businessAdminId(budgetEO.getBusinessAdminId());
                result.businessAdminName(budgetEO.getBusinessAdminName());
                result.pm(budgetEO.getPm());
            }
            if (null != project) {
                result.projectAdminId(project.getProjectAdminId());
                result.projectAdminName(project.getProjectAdminName());
                result.projectLeaderId(project.getProjectLeaderId());
                result.projectLeader(project.getProjectLeader());
            }

        } else {
            //获取业务任务负责人id

            budgetEO = budgetEOService.selectByPrimaryKey(task.getBudgetId());
            if (null != budgetEO) {
                result.businessAdminId(budgetEO.getBusinessAdminId());
                result.businessAdminName(budgetEO.getBusinessAdminName());
                result.pm(budgetEO.getPm());
            }

        }

        String approveUserId = "";
        if (StringUtils.isNotEmpty(task.getApproveUserId())) {
            /*
             *  负责人不为空回显
             */
            approveUserId = task.getApproveUserId();
        } else {
            /*
             * 负责人为空
             */

            if (StringUtils.isNotEmpty(task.getProjectId())) {
                //获取项目任务负责人id
                if (null != budgetEO) {
                    result.businessAdminId(budgetEO.getBusinessAdminId());
                    result.businessAdminName(budgetEO.getBusinessAdminName());
                    result.pm(budgetEO.getPm());
                }
                if (null != project) {
                    result.projectAdminId(project.getProjectAdminId());
                    result.projectAdminName(project.getProjectAdminName());

                    approveUserId = project.getProjectLeaderId();
                }
            } else {
                //获取业务任务负责人id
                if (null != budgetEO) {
                    approveUserId = budgetEO.getPm();
                }

            }
        }
        result.approveUserId(approveUserId);

        UserEO userEO = userEODao.get(approveUserId);
        if (null != userEO) {
            result.approveUserName(userEO.getUsname());
        }

        return result.build();
    }

    public List<TaskStatusVO> getTaskStatus(String taskId) {

        List<TaskStatusVO> list = new ArrayList<>();
        Task task = taskRepository.findByIdAndDelFlagNot(taskId, Boolean.TRUE);
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                                                 .must(QueryBuilders.termQuery("taskId", taskId))
                                                 .mustNot(QueryBuilders.termQuery("delFlag", true));
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(queryBuilder);
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("planStartTime").order(SortOrder.ASC);
        nativeSearchQueryBuilder.withSort(sortBuilder);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();
        ArrayList<ChildrenTask> childrenTasks = Lists.newArrayList(childTaskRepository.search(searchQuery));
        if (CollectionUtils.isEmpty(childrenTasks)) {
            return list;
        }
        int count = 0;
        for (ChildrenTask childrenTask : childrenTasks) {
            List<Integer> schedule = new ArrayList<>();
            int days = DateUtil.differentDaysByMillisecond
                (childrenTask.getPlanStartTime(), childrenTask.getPlanEndTime());
            schedule.add(days);
            TaskStatusVO taskStatusVO = new TaskStatusVO();

            if (count == 0) { taskStatusVO.setPriority("普通"); }
            if (count == 1) { taskStatusVO.setPriority("重要"); }
            if (count == 2) { taskStatusVO.setPriority("紧急"); }
            taskStatusVO.setId(childrenTask.getId());
            taskStatusVO.setName(childrenTask.getChildTaskName());
            taskStatusVO.setTaskName(childrenTask.getBelongTaskName());
            taskStatusVO.setStart(childrenTask.getPlanStartTime());
            taskStatusVO.setEnd(task.getPlanEndTime());
            taskStatusVO.setSchedule(schedule);
            list.add(taskStatusVO);
            count++;
            if (count == 3) { count = 0; }
        }
        return list;
    }

}
