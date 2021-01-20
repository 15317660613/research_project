package com.adc.da.statistics.service;

import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.util.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ComputeUserWorkTimeService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    public double getProjectWorkTimeByProjectIdAndUserId(Project project, List<String> userIdList,
        List<Daily> dailyList) {
        Double workTimeCount = 0.0;
        List<Task> taskList = taskRepository.findByProjectId(project.getId());
        for (Task task : taskList) {
            workTimeCount = workTimeCount + getTaskWorkTimeByTaskIdAndUserId(task, userIdList, dailyList);
        }
        return workTimeCount;
    }

    public double getTaskWorkTimeByTaskIdAndUserId(Task task, List<String> userIdList, List<Daily> dailyList) {
        Double workTimeCount = 0.0;
        workTimeCount = workTimeCount + getWorkTimeByTaskIdAndUserId(task.getId(), dailyList);
        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskId(task.getId());
        for (ChildrenTask childrenTask : childrenTaskList) {
            workTimeCount = workTimeCount + getChildTaskWorkTimeByTaskIdAndUserId(childrenTask, userIdList, dailyList);
        }
        return workTimeCount;

    }

    public double getChildTaskWorkTimeByTaskIdAndUserId(ChildrenTask childTask, List<String> userIdList,
        List<Daily> dailyList) {
        if (userIdList.contains(childTask.getCreateUserId())) {
            return getWorkTimeByTaskIdAndUserId(childTask.getId(), dailyList);
        }
        return 0.0;
    }

    public double getWorkTimeByTaskIdAndUserId(String taskId, List<Daily> dailyList) {
        Double workTimeCount = 0.0d;
        for (Daily daily : dailyList) {
            if (daily.getApproveState() == 1 && Arrays.asList(daily.getTaskIdArray()).contains(taskId)) {
                if (StringUtils.isEmpty(daily.getTimeSlot())) {
                    workTimeCount = workTimeCount + daily.getWorkCostTime();
                } else {
                    int taskIdArrayLength = daily.getTaskIdArray().length;
                    if (taskIdArrayLength > 0) {
                        workTimeCount = workTimeCount +   (4.0d / taskIdArrayLength);
                    }
                }
            }
        }
        return workTimeCount;
    }
}
