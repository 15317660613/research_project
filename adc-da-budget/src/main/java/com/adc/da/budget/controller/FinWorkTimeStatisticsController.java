package com.adc.da.budget.controller;

import com.adc.da.budget.service.FinWorkTimeStatisticsService;
import com.adc.da.budget.vo.UserProjectWorkTimeVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @description: 财务系统工时统计
 * @author: qichunxu
 * @create: 2019-03-21 09:58
 **/
@RestController
@RequestMapping("/${restPath}/FinWorkTimeStatistics")
@Api("|FinWorkTimeStatistics财务系统工时统计|")
public class FinWorkTimeStatisticsController {

    @Autowired
    private FinWorkTimeStatisticsService statisticsService;

    /**
     * 前端工时统计使用
     *
     * @param userId
     * @param beginTime
     * @param endTime
     * @return
     */
    @GetMapping("/findUserWorkTimeDiy")
    @ApiOperation(value = "|findUserWorkTimeDiy|人员工时")
    public ResponseMessage<List<Map<String, Object>>> findUserWorkTimeDiy(String userId, String beginTime,
        String endTime) {
        List<Map<String, Object>> result = null;
        try {
            result = statisticsService.getUserWorkTimeDiy(userId, beginTime, endTime);
        } catch (Exception e) {
            Result.error("日期格式可能不正确！");

        }

        return Result.success(result);
    }

    /**
     * 自定义表单 以及 前端工时统计页面使用
     *
     * @param projectId
     * @return
     */
    @GetMapping("/findUserWorkTimeByProjectId")
    @ApiOperation(value = "|findUserWorkTimeByProjectId|根据项目Id查询人员工时")
    public ResponseMessage<List<UserProjectWorkTimeVO>> findUserWorkTimeByProjectId(String projectId) {

        List<UserProjectWorkTimeVO> result = null;
        result = statisticsService.findUserWorkTimeByProjectId(projectId);
        return Result.success(result);
    }

    /**
     * 自定义表单 使用
     *
     * @param budgetId
     * @return
     */
    @GetMapping("/findUserWorkTimeByBudgetId")
    @ApiOperation(value = "|findUserWorkTimeByBudgetId|根据业务Id查询人员工时")
    public ResponseMessage<List<UserProjectWorkTimeVO>> findUserWorkTimeByBudgetId(String budgetId) {

        List<UserProjectWorkTimeVO> result = null;
        result = statisticsService.findUserWorkTimeByBudgetId(budgetId);
        return Result.success(result);
    }

}
