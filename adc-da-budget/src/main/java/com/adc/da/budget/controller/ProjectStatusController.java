package com.adc.da.budget.controller;

import com.adc.da.budget.service.ProjectStatusService;
import com.adc.da.budget.vo.MemberStatusVO;
import com.adc.da.budget.vo.ProjectStatusFinishedVO;
import com.adc.da.budget.vo.ProjectWorkTimeChartVO;
import com.adc.da.budget.vo.ShowProjectAnalyzeVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/22 10:49
 * @Description: 项目职责及状况
 */
@RestController
@Api("项目职责及状况ProjectStatusController")
@RequestMapping("/${restPath}/budget/project/status")
public class ProjectStatusController {


    @Autowired
    private ProjectStatusService projectStatusService;

    @ApiOperation("查询页面信息")
    @GetMapping("/query/page/{id}")
    public ResponseMessage<ShowProjectAnalyzeVO> queryShowProjectStatus(@PathVariable(value = "id") String id)
                                                                            throws Exception {

        ShowProjectAnalyzeVO showProjectAnalyzeVO = projectStatusService.showProjectAnalyzeVO(id);
        return Result.success(showProjectAnalyzeVO);
    }
    @ApiOperation("状态信息更新")
    @PutMapping("/update/{id}")
    public ResponseMessage statusUpdate(@PathVariable(value = "id") String id) {
        if (null == id || "".equals(id)) {
            return Result.error("参数不能为空");
        }
        String massage = projectStatusService.dailyStatusUpdate(id);
        return Result.success("",massage);
    }

    @ApiOperation("查询项目任务甘特图")
    @GetMapping("/query/{id}/gantt")
    public ResponseMessage<List<ProjectStatusFinishedVO>> queryShowProjectGantt(@PathVariable(value = "id") String id) {
        return Result.success(projectStatusService.queryFinishedStatus(id));
    }

    @ApiOperation("查询项目任务甘特图")
    @GetMapping("/query/gantt")
    public ResponseMessage<List<ProjectStatusFinishedVO>> ganttByCurrentUser() {
        return Result.success(projectStatusService.queryFinishedStatusByUserId());
    }

    @ApiOperation("查询项目工时图标")
    @GetMapping("/query/{id}/queryWorkTime")
    public ResponseMessage<ProjectWorkTimeChartVO> queryWorkTime(@PathVariable(value = "id") String id) {
        return Result.success(projectStatusService.queryWorkTime(id));
    }

    @ApiOperation("查询项目财务图标")
    @GetMapping("/query/{id}/getFinanceChart")
    public ResponseMessage<Map<String, Integer[]>> getFinanceChart(@PathVariable(value = "id") String id) {
        return Result.success(projectStatusService.getFinanceChart(id));
    }
    @ApiOperation("查询项目人员任务进度")
    @GetMapping("/query/{id}/getMemberStatus")
    public ResponseMessage<List<MemberStatusVO>> getMemberStatus(@PathVariable String id) {
        return Result.success(projectStatusService.getMemberStatus(id));
    }

    @ApiOperation("查询项目统计信息")
    @GetMapping("/query/{id}/queryTotle")
    public ResponseMessage<List<Object[]>> queryTotle(@PathVariable(value = "id") String id) {
        return Result.success(projectStatusService.queryTotle(id));
    }
}
