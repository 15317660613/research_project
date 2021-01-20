package com.adc.da.budget.listener;

import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.utils.ApplicationContextUtil;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("updateProjectState")
@Slf4j
public class UpdateProjectState implements ExecutionListener {
    /**
     * 先获取流程设计时设置的受理组（即角色）信息，并删除该设置，
     * 然后查询和流程发起人同组织机构的该受理组人员，并设置为候选人
     */
    @Override
    public void notify(DelegateExecution delegateExecution) {
        ProjectRepository projectRepository = ApplicationContextUtil.getBean(ProjectRepository.class);
        TaskRepository taskRepository = ApplicationContextUtil.getBean(TaskRepository.class);
        ChildTaskRepository childTaskRepository = ApplicationContextUtil.getBean(ChildTaskRepository.class);
        String testValue = (String) delegateExecution.getVariable("approve");
        String projectId = delegateExecution.getProcessBusinessKey();
        Project project = projectRepository.findByIdAndDelFlagNot(projectId,true);

        if (StringUtils.equals(testValue, "reject") || StringUtils.equals(testValue, "100")||StringUtils.equals(testValue,"shutdownProcess")) {
            //驳回 或者 撤回
            project.setFinishedStatus(ProjectStatusEnums.EXECUTE.getStatus());
            projectRepository.save(project);
            List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(projectId, true);
            if (CollectionUtils.isNotEmpty(taskList)) {
                for (Task task : taskList) {
                    updateTaskState(task, taskRepository, childTaskRepository);
                }
            }
        } else if (StringUtils.equals(testValue, "agree")) {
            //同意
            project.setFinishedStatus(ProjectStatusEnums.COMPLETE.getStatus());
            projectRepository.save(project);
            List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(projectId, true);
            if (CollectionUtils.isNotEmpty(taskList)) {
                for (Task task : taskList) {
                    updateTaskCompleteState(task, taskRepository, childTaskRepository);
                }
            }
        }
    }

    private void updateTaskState(Task task,TaskRepository taskRepository,ChildTaskRepository childTaskRepository){
        if(!StringUtils.equals(task.getTaskStatus(),ProjectStatusEnums.COMPLETE.getStatus())) {
            task.setTaskStatus(ProjectStatusEnums.EXECUTE.getStatus());
            taskRepository.save(task);
        }
        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdAndDelFlagNot(task.getId(),true);
        if(CollectionUtils.isNotEmpty(childrenTaskList)) {
            for(ChildrenTask childrenTask : childrenTaskList) {
                if(StringUtils.equals(childrenTask.getStatus(),ProjectStatusEnums.AUDIT.getStatus())) {
                    childrenTask.setStatus(ProjectStatusEnums.EXECUTE.getStatus());
                    childTaskRepository.save(childrenTask);
                }
            }
        }
    }

    private void updateTaskCompleteState(Task task,TaskRepository taskRepository,ChildTaskRepository childTaskRepository){
        task.setTaskStatus(ProjectStatusEnums.COMPLETE.getStatus());
        taskRepository.save(task);
        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdAndDelFlagNot(task.getId(),true);
        if(CollectionUtils.isNotEmpty(childrenTaskList)) {
            for(ChildrenTask childrenTask : childrenTaskList) {
                childrenTask.setStatus(ProjectStatusEnums.COMPLETE.getStatus());
                childTaskRepository.save(childrenTask);
            }
        }
    }
}
