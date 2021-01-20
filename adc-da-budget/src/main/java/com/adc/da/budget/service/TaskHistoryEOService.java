package com.adc.da.budget.service;

import com.adc.da.budget.entity.Task;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.TaskHistoryEODao;
import com.adc.da.budget.entity.TaskHistoryEO;

import java.util.Date;


/**
 *
 * <br>
 * <b>功能：</b>_TASK_HISTORY TaskHistoryEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("taskHistoryEOService")
@Transactional(rollbackFor = Throwable.class)
public class TaskHistoryEOService extends BaseService<TaskHistoryEO, String> {


    @Autowired
    private TaskHistoryEODao dao;
    @Autowired
    private BeanMapper beanMapper;

    public TaskHistoryEODao getDao() {
        return dao;
    }

    public int saveTaskHistory(Task task,String operateTYpe,String operateTime) {
        TaskHistoryEO taskHistoryEO = beanMapper.map(task,TaskHistoryEO.class);
        taskHistoryEO.setId(UUID.randomUUID());
        taskHistoryEO.setTaskId(task.getId());
        taskHistoryEO.setTaskName(task.getName());
        taskHistoryEO.setMemberIds(StringUtils.join(task.getMemberIds(),","));
        taskHistoryEO.setMemberNames(StringUtils.join(task.getMemberNames(),","));
        taskHistoryEO.setOperateDate(new Date());
        taskHistoryEO.setOperateUser(UserUtils.getUserId());
        if(StringUtils.equals("insert",operateTYpe)) {
            taskHistoryEO.setOperateType("insert");
        }else if(StringUtils.equals("delete",operateTYpe)){
            taskHistoryEO.setOperateType("delete");
        }else if(StringUtils.equals("update1",operateTYpe)) {
            taskHistoryEO.setOperateTime(operateTime);
            taskHistoryEO.setOperateType("update1");
        }else if(StringUtils.equals("update2",operateTYpe)) {
            taskHistoryEO.setOperateTime(operateTime);
            taskHistoryEO.setOperateType("update2");
        }
        return dao.insertSelective(taskHistoryEO);
    }

}
