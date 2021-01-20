package com.adc.da.research.controller;


import com.adc.da.research.service.ReportService;
import com.adc.da.research.vo.HeaderVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/${restPath}/research/resProReport")
@Api(tags = "科研项目看板相关|")
public class ResearchProjectReportController {

    @Autowired
    private ReportService reportService;


    @ApiOperation(value = "计算看板的四个总额")
    @GetMapping("/getHeaderVO")
    public ResponseMessage getHeaderVO() {
        return Result.success(reportService.getHeaderVO());
    }

    @ApiOperation(value = "科研经费月度使用情况一览")
    @GetMapping("/getDataByType")
    public ResponseMessage getDataByType(int type){
        return Result.success(reportService.getDataByType(type));
    }

    @ApiOperation(value = "科研经费拨付详情/科研经费使用详情")
    @GetMapping("/projectAllocateDetail")
    public ResponseMessage projectAllocateDetail(int type){
        return Result.success(reportService.projectAllocateDetail(type));
    }

    @ApiOperation(value = "科研经费使用详情")
    @GetMapping("/getFundsUseDetail")
    public ResponseMessage getFundsUseDetail(int type){
        return Result.success(reportService.getFundsUseDetail(type));
    }

    @ApiOperation(value = "部门科研经费使用详情")
    @GetMapping("/getOrgFundsDetail")
    public ResponseMessage getOrgFundsDetail(int type ){
        return Result.success(reportService.getOrgFundsDetail(type));
    }

    @ApiOperation(value = "各部门科研经费申请分布")
    @GetMapping("/getOrgFundsDistribution")
    public ResponseMessage  getOrgFundsDistribution(int type) {
        return Result.success(reportService.getOrgFundsDistribution(type));
    }

    @ApiOperation(value = "重点课题排名")
    @GetMapping("/getTop5")
    public ResponseMessage  getTop5(int type){
        return Result.success(reportService.getTop5(type));
    }


    @ApiOperation(value = "科研经费历史趋势")
    @GetMapping("/getFundsHistoryTrend")
    public ResponseMessage  getFundsHistoryTrend(int type){
        return Result.success(reportService.getFundsHistoryTrend(type));
    }

    @ApiOperation(value = "课题申请数量历史趋势")
    @GetMapping("/getProjectHistoryTrend")
    public ResponseMessage  getProjectHistoryTrend(int type){
        return Result.success(reportService.getProjectHistoryTrend(type));
    }

}
