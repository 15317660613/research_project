package com.adc.da.budget.controller;

import com.adc.da.budget.service.FinWorkTimeStatisticsService;
import com.adc.da.budget.service.PersonalWorkTimeService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * add by liqiushi
 * 2019/03/14
 * 信息化系统新首页四种维度的工时统计
 */
@RestController
@RequestMapping("/${restPath}/budget/queryWorkTime")
@Api("|WorkTime|")
public class PersonalWorkTimeController {

    @Autowired
    private PersonalWorkTimeService service;

    @Autowired
    private FinWorkTimeStatisticsService finWorkTimeStatisticsService;

    /**
     * @param paramType 前端传来的统计单位周，月，季度，年
     * @return {项目：时长}
     */
    @ApiOperation("|WorkTime|个人工时统计")
    @GetMapping("/getWorkTimeByPerson/{paramType}")
    public ResponseMessage<List<Map<String, Double>>> getWorkTimeByPerson(@PathVariable("paramType") String paramType) {
        String userId = UserUtils.getUserId();

        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆过期，请重新登陆！");
        }
        Map<String, Double> map = finWorkTimeStatisticsService.getBudgetNameWorkTimeMapByUserIdAndTimeRange(userId, paramType);
        List<Map<String, Double>> mapList = new ArrayList<>();
        mapList.add(map);
        return Result.success(mapList);
    }

    /**
     * 以大组为维度统计工时。
     * * @param paramType 前段的时间单位周，月，季度，年
     * * @return {小组 or 个人：时长}
     * @deprecated 已废弃  statistics.getWorkTimeByHQNew
     */
    @Deprecated
    @ApiOperation("|WorkTime|大组工时统计")
    @GetMapping("/getWorkTimeByGroup/{paramType}")
    public ResponseMessage<List<Map<String, Double>>> getWorkTimeByGroup(@PathVariable("paramType") String paramType) {
        return Result.success(service.getWorkTimeByGroup(paramType));
    }

    /**
     * 以部门为维度进行统计
     * 统计部门下各大组所化工时占比
     *
     * @deprecated 已废弃 统一使用   statistics.getWorkTimeByHQNew
     */
    @Deprecated
    @ApiOperation("|WorkTime|部门工时统计")
    @GetMapping("/getWorkTimeByDep/{paramType}")
    public ResponseMessage<List<Map<String, Double>>> getWorkTimeByDep(@PathVariable("paramType") String paramType) {
        return Result.success(service.getWorkTimeByDep(paramType, "部"));
    }

    /**
     * 按部门统计工时
     * 就统计一下 本部 下各个部门花费工时的占比
     * 约定是本部的人访问这里
     * @deprecated 已废弃
     *    statistics.getWorkTimeByHQNew
     */
    @Deprecated
    @ApiOperation("|WorkTime|工时统计组长及以上")
    @GetMapping("/getWorkTimeByHQ/{paramType}")
    public ResponseMessage<List<Map<String, Double>>> getWorkTimeByHQ(@PathVariable("paramType") String paramType) {
        return Result.success(service.getWorkTimeByHQNew(paramType));
    }

    /**
     * @param paramType
     * @param userId
     * @return
     * @deprecated 已废弃 ，统一调用
     * @see #getWorkTimeByPerson
     */
    @ApiOperation("|WorkTime|个人工时统计")
    @GetMapping("/getWorkTimeByUserId/")
    @Deprecated
    public ResponseMessage<List<Map<String, Double>>> getWorkTimeByUserId(@RequestParam String paramType,
        @RequestParam String userId) {
        return Result.success(service.getWorkTimeByPerson(paramType, userId));
    }

}
