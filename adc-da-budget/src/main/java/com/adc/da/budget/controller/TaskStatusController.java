package com.adc.da.budget.controller;

import com.adc.da.budget.service.TaskStatusService;
import com.adc.da.budget.vo.TaskDetailVO;
import com.adc.da.budget.vo.TaskStatusVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api("任务状态TaskStatus")
@RequestMapping("/${restPath}/budget/TaskStatus")
public class TaskStatusController {

    @Autowired
    private TaskStatusService taskStatusService;

    @ApiOperation("查询页面信息")
    @GetMapping("/query/page/{id}")
    public ResponseMessage<TaskDetailVO> queryShowTaskStatus(@PathVariable("id") String id) throws Exception {
        TaskDetailVO taskDetailVO = taskStatusService.queryShowTaskStatus(id);
        return Result.success(taskDetailVO);
    }

    @ApiOperation("查询项目任务甘特图")
    @GetMapping("/gantt")
    public ResponseMessage<List<TaskStatusVO>> queryShowProjectGantt(@RequestParam String taskId) {
        return Result.success(taskStatusService.getTaskStatus(taskId));
    }

}
