package com.adc.da.search.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.query.task.TaskQuery;
import com.adc.da.budget.service.MyService;
import com.adc.da.search.service.TaskSearchService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Lee Kwanho 李坤澔
 * date 2019-10-18
 */
@RestController
@RequestMapping("/${restPath}/budget/task")
@Api(tags = "|Search|")
public class TaskSearchController extends BaseController<Task> {
    @Autowired
    private TaskSearchService taskService;

    @Autowired
    private MyService myService;

    @ApiOperation("项目搜索-任务查询页")
    @PostMapping("/findByPage")
    public ResponseMessage<PageInfo<Task>> findByPage(@RequestBody TaskQuery page) {
        List<Task> rows = taskService.findByPage(page);
        //回显字段不在ES中，只能通过Sql去查询
        for (Task task : rows) {
            if (task.getCreateUserId() != null && !"".equals(task.getCreateUserId())) {
                task.setCreateUserName(myService.getUserNameByUserId(task.getCreateUserId()));
            }
            task.setWorkTime(myService.getWorkTimeByTaskId(task.getId()));
        }
        return Result.success(getPageInfo(page.getPager(), rows));
    }
}
