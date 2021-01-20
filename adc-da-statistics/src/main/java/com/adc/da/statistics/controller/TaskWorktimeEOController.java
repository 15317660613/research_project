package com.adc.da.statistics.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.ProjectStatistics;
import com.adc.da.login.util.UserUtils;
import com.adc.da.statistics.entity.TaskWorktimeEO;
import com.adc.da.statistics.service.TaskWorktimeEOService;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/${restPath}/statistics/taskWorktime")
@Api(tags = "|定制化工时统计|")
public class TaskWorktimeEOController extends BaseController<TaskWorktimeEO> {


    @Autowired
    private TaskWorktimeEOService taskWorktimeEOService;

    @Autowired
    private OrgEOService orgEOService;

    @ApiOperation(value = "|BusinessWorktimeEO|根据自定义时间段进行工时查询")
    @GetMapping("/getTaskWorkTimeByProId")
    //@RequiresPermissions("statistics:businessWorktime:list")
    public ResponseMessage<ProjectStatistics> getBusTaskWorkTime(String projectId, String beginTime, String endTime)
        throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        return Result.success(taskWorktimeEOService.getTaskWorkTime(projectId, beginTime, endTime, userId));
    }

    @ApiOperation(value = "|BusinessWorktimeEO|日报工时")
    @GetMapping("/getTaskWorkTimeByTaskIdAndYearGroupByMonth/{taskId}/{year}")
    //@RequiresPermissions("statistics:businessWorktime:list")
    public ResponseMessage<List<TaskWorktimeEO>> getTaskWorkTimeByTaskIdAndYearGroupByMonth(@PathVariable("year") String year, @PathVariable("taskId")String taskId){
        return Result.success(taskWorktimeEOService.getTaskWorkTimeByTaskIdAndYearGroupByMonth(year,taskId));
    }
}
