package com.adc.da.budget.controller;

import com.adc.da.budget.entity.Task;
import com.adc.da.budget.entity.TaskPriority;
import com.adc.da.budget.repository.TaskPriorityRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.service.TaskPriorityService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @description: 任务优先级
 * @author: qichunxu
 * @create: 2019-03-18 10:43
 **/
@RestController
@RequestMapping("/${restPath}/TaskPriority")
@Api("|TaskPriority|")
public class TaskPriorityController {

    @Autowired
    private TaskPriorityService taskPriorityService;
    @Autowired
    private TaskRepository taskRepository ;
    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    /**
     * 查询全部
     */
    @GetMapping("/list")
    @ApiOperation(value = "|TaskPriority|列表")
    public ResponseMessage<List<Map<String,List<TaskPriority>>>> list() {
        return Result.success(taskPriorityService.findAll());
    }

    /**
     * 用来更新任务首页面板任务优先级
     */
    @GetMapping("/updateOldTaskPriority")
    @ApiOperation(value = "|TaskPriority|用来更新任务首页面板任务优先级")
    public ResponseMessage<TaskPriority> list(String taskId) {
        Task task = taskRepository.findById(taskId);
        //主要更新成员变动
        taskPriorityRepository.save(new TaskPriority(task.getId(), task.getName(), 0, task.getMemberIds()));
        return Result.success();
    }

    /**
     * 修改
     *
     * @param taskPriority
     * @return
     */
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "|TaskPriority|修改")
    public ResponseMessage<TaskPriority> update(@RequestBody TaskPriority taskPriority) {
        return Result.success(taskPriorityService.update(taskPriority));
    }
    /**
     * 清空数据
     *
     * @return
     */
    @Deprecated
    @ApiOperation("|TaskPriority|清空数据，慎用此方法")
    @DeleteMapping("/delete/all")
    public ResponseMessage<String> deleteAll() {
        return Result.success(taskPriorityService.deleteAll());
    }
}
