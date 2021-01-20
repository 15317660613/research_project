package com.adc.da.budget.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.dto.ProjectScheduleRequestDTO;
import com.adc.da.budget.dto.ProjectScheduleResponseDTO;
import com.adc.da.budget.entity.Business;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.service.ProjectScheduleService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Auther: chenhaidong
 * @Date: 2018/11/22
 * @Description:项目日程控制层
/* */
@RestController
@RequestMapping("/${restPath}/budget/projectSchedule")
@Api("|ProjectSchedule|")
public class ProjectScheduleController extends BaseController<Business> {

    @Autowired
    private ProjectScheduleService projectScheduleService;

    @ApiOperation(value = "查询项目日程")
    @PostMapping("/query")
    public ResponseMessage<List<ProjectScheduleResponseDTO>> query(@RequestBody
                                            ProjectScheduleRequestDTO projectScheduleRequestDTO){
        if (projectScheduleRequestDTO == null || projectScheduleRequestDTO.getScheduleBeginDate() == null
                || projectScheduleRequestDTO.getScheduleEndDate() == null
                || projectScheduleRequestDTO.getProjectId() == null) {
            return Result.error("参数不能为空");
        }
        if (projectScheduleRequestDTO.getScheduleBeginDate().getTime() >=
                projectScheduleRequestDTO.getScheduleEndDate().getTime()) {
            return Result.error("结束日期必须大于开始日期");
        }
        return Result.success(projectScheduleService.queryDailySchedule(projectScheduleRequestDTO.getProjectId(),
                projectScheduleRequestDTO.getScheduleBeginDate(),projectScheduleRequestDTO.getScheduleEndDate()));
    }

    @ApiOperation(value = "查询当前用户所在的项目列表")
    @PostMapping("/queryProjectByUserId")
    public ResponseMessage<List<Project>> queryProjectByUserId(){
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        return Result.success(projectScheduleService.queryProjectByUserId(userId));
    }



}
