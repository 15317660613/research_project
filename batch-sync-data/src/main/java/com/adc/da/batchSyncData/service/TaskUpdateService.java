package com.adc.da.batchSyncData.service;

import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
public class TaskUpdateService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public void updateBudgetSearchId() {
        List<Task> rows = Lists.newArrayList(taskRepository.findAll());
        List<Task> updateBudgetTaskList = new ArrayList<>();
        List<Task> updateProjectTaskList = new ArrayList<>();

        String budgetId;
        Set<String> projectIds = new HashSet<>();
        String projectId;
        for (Task task : rows) {

            budgetId = task.getBudgetId();
            projectId = task.getProjectId();
            /* 找寻负责人与实际部门id不符的记录 */
            /*  */
            try {
                if (StringUtils.isNotEmpty(budgetId)) {
                    task.setSearchBudgetId(budgetId);
                    updateBudgetTaskList.add(task);
                } else {
                    projectIds.add(projectId);
                    updateProjectTaskList.add(task);
                }
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        }
        List<Project> projectList = Lists.newArrayList(projectRepository.findAll(projectIds));
        Map<String, String> map = new HashMap<>();
        for (Project project : projectList) {
            map.put(project.getId(), project.getBudgetId());
        }
        for (Task task : updateProjectTaskList) {
            String projectId1 = task.getProjectId();
            if ((map.containsKey(projectId1))) {
                task.setSearchBudgetId(map.get(projectId1));
            }
        }

        log.warn("total need updateBudgetTaskList {} ", updateBudgetTaskList.size());
        log.warn("total need updateProjectTaskList {} ", updateProjectTaskList.size());

        if (false) {
            throw new AdcDaBaseException("该方法仅开发人员使用");
        } else {
            taskRepository.save(updateBudgetTaskList);
            taskRepository.save(updateProjectTaskList);
        }
        log.warn("total  done ");

    }

}
