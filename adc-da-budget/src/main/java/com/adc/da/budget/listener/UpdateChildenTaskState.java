package com.adc.da.budget.listener;

import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.utils.ApplicationContextUtil;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;
import com.adc.da.budget.enums.ProjectStatusEnums;


@Component("updateChildenTaskState")
@Slf4j
public class UpdateChildenTaskState implements ExecutionListener {

    private static final long serialVersionUID = 2190559253653576032L;

    /**
     * 先获取流程设计时设置的受理组（即角色）信息，并删除该设置，
     * 然后查询和流程发起人同组织机构的该受理组人员，并设置为候选人
     *
     * @param delegateExecution
     */
    @Override
    public void notify(DelegateExecution delegateExecution) {
        ChildTaskRepository childTaskRepository = ApplicationContextUtil.getBean(ChildTaskRepository.class);
        TaskRepository taskRepository = ApplicationContextUtil.getBean(TaskRepository.class);
        String btnType = (String) delegateExecution.getVariable("btnType");
        String textValue = (String) delegateExecution.getVariable("approve");
        String taskId = delegateExecution.getProcessBusinessKey();
        ChildrenTask childrenTask = childTaskRepository.findByIdAndDelFlagNot(taskId,true);
        if(null!=childrenTask){
            if(StringUtils.equals(textValue,"reject")||StringUtils.equals(textValue,"100")) {
                //驳回  撤回 变进行中
                childrenTask.setStatus(ProjectStatusEnums.EXECUTE.getStatus());
            }else if(StringUtils.equals(textValue,"agree")) {
                //同意
                childrenTask.setStatus(ProjectStatusEnums.COMPLETE.getStatus());
            }else if(StringUtils.equals(textValue,"shutdownProcess")) {
                //终止流程
                childrenTask.setStatus(ProjectStatusEnums.EXECUTE.getStatus());
                childrenTask.setBtnFlag("0");
            }
            childTaskRepository.save(childrenTask);
        } else {
            Task task = taskRepository.findByIdAndDelFlagNot(taskId,true);
            if(StringUtils.equals(textValue,"reject")||StringUtils.equals(textValue,"100")) {
                //驳回  撤回 变进行中
                task.setTaskStatus(ProjectStatusEnums.EXECUTE.getStatus());
            }else if(StringUtils.equals(textValue,"agree")) {
                //同意
                task.setTaskStatus(ProjectStatusEnums.COMPLETE.getStatus());
            }else if(StringUtils.equals(textValue,"shutdownProcess")) {
                //终止流程
                task.setTaskStatus(ProjectStatusEnums.EXECUTE.getStatus());
                task.setBtnFlag("0");
            }
            taskRepository.save(task);
        }
    }
}
