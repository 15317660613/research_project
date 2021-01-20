package com.adc.da.statistics.entity;

import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-06-19
 */
@Data
public class TimeMapListEO {

    List<BudgetEO> budgetEOList;

    List<Project> projectList;

    List<Task> taskList;

    List<ChildrenTask> childrenTaskList;

    Map<String, Integer> childrenTaskListMap;

    Map<String, Integer> taskListMap;

    Map<String, Integer> projectListMap;

    Map<String, Integer> budgetEOListMap;

}
