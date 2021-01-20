package com.adc.da.listener.listener.execution.update;

import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.file.dao.FileEODao;
import com.adc.da.file.entity.FileEO;
import com.adc.da.listener.utils.ListenerUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.workflow.utils.contants.Constants;
import com.adc.da.workflow.utils.contants.TaskCompleteType;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 更新任务 子任务状态 （新）
 * ${updateTaskStatus}
 */
@Component("updateTaskStatus")
@Slf4j
public class UpdateTaskStatus implements ExecutionListener {

    private static final long serialVersionUID = 2190559253653576032L;

    @Autowired
    private transient ChildTaskRepository childTaskRepository;

    @Autowired
    private transient TaskRepository taskRepository;

    @Autowired
    private transient FileEODao fileEODao;

    /**
     * 先获取流程设计时设置的受理组（即角色）信息，并删除该设置，
     * 然后查询和流程发起人同组织机构的该受理组人员，并设置为候选人
     *
     * @param delegateExecution
     */
    @Override
    public void notify(DelegateExecution delegateExecution) {

        String textValue = (String) delegateExecution.getVariable("approve");

        /*
         * 获取表单数据
         */
        Gson gson = new Gson();
        Map<String, Object> jsonMap = ListenerUtils
            .initGlobalFormDataMap((String) delegateExecution.getVariable(Constants.GLOBAL_FORM_DATA));

        String taskId = (String) jsonMap.get("task_id");
        if (StringUtils.isEmpty(taskId)) {
            throw new AdcDaBaseException("taskId error");
        }
        String status;
        String btnFlag;
        boolean finishedFlag;
        if (textValue == null) {
            status = (ProjectStatusEnums.AUDIT.getStatus());
            btnFlag = "1";
            finishedFlag = false;
        } else if (StringUtils.equals(textValue, "reject") || StringUtils.equals(textValue, TaskCompleteType.ROLLBACK)
            || StringUtils.equals(textValue, TaskCompleteType.WITHDRAW)) {
            //驳回  撤回 变进行中
            status = (ProjectStatusEnums.EXECUTE.getStatus());
            btnFlag = "1";
            finishedFlag = false;

        } else if (StringUtils.equals(textValue, "agree")) {
            //同意 表示完成
            String jsonString = gson.toJson(jsonMap.get("taskmilestoneResult"), ArrayList.class);
            Type typeList = new TypeToken<ArrayList<LinkedTreeMap<String, Object>>>() {
            }.getType();
            List<Map<String, Object>> jsonMapLevelTwo = gson.fromJson(jsonString, typeList);
            FileEO taskFileEO = new FileEO();
            for (Map<String, Object> map : jsonMapLevelTwo) {
                if (null != map.get("fileId")) {
                    taskFileEO.setFileId((String) map.get("fileId"));
                    taskFileEO.setRemark("");
                    /*
                     * 只更新 ForeignId
                     */
                    fileEODao.updateByPrimaryKeySelective(taskFileEO);
                }
            }

            status = (ProjectStatusEnums.COMPLETE.getStatus());
            btnFlag = "1";
            finishedFlag = true;

        } else if (StringUtils.equals(textValue, "shutdownProcess")) {
            //终止流程
            status = (ProjectStatusEnums.EXECUTE.getStatus());
            btnFlag = "0";
            finishedFlag = false;
        } else {
            throw new AdcDaBaseException("textValue error");
        }

        ChildrenTask childrenTask = childTaskRepository.findByIdAndDelFlagNot(taskId, true);
        if (null != childrenTask) {
            childrenTask.setStatus(status);
            childrenTask.setBtnFlag(btnFlag);
            if (finishedFlag) {
                childrenTask.setActualFinishedTime(new Date());
            }
            childTaskRepository.save(childrenTask);
        } else {
            Task task = taskRepository.findByIdAndDelFlagNot(taskId, true);
            if (task == null) {
                throw new AdcDaBaseException("task info  error");
            }
            task.setTaskStatus(status);
            task.setBtnFlag(btnFlag);
            if (finishedFlag) {
                task.setFinishedActualTime(new Date());
            }
            taskRepository.save(task);
        }
    }
}
