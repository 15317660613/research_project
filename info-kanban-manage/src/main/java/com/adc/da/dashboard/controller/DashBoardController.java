package com.adc.da.dashboard.controller;

import com.adc.da.dashboard.service.DashBoardService;
import com.adc.da.dashboard.vo.*;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/${restPath}/dashboard")
@Api(tags = "|财务看板|DashBoardController")
public class DashBoardController {
    @Autowired
    private DashBoardService dashBoardService ;

    @GetMapping("/getContractDashBoardHeaderVO")
    @ApiOperation(value = "看板1 头部四个块")
    public ResponseMessage<ContractDashBoardHeaderVO> getContractDashBoardHeaderVO() {
        return Result.success(dashBoardService.getContractDashBoardHeaderVO());
    }

    @GetMapping("/getContractDashBoardBodyVO/{type}")
    @ApiOperation(value = "看板1 主体柱状图 type = 0 合同维度， 1 开票维度  ， 2 进账维度")
    public ResponseMessage<ContractDashBoardBodyVO> getContractDashBoardBodyVO(@PathVariable("type") int type) {
        return Result.success(dashBoardService.getContractDashBoardBodyVO(type));
    }

    @GetMapping("/getMapData/{type}")
    @ApiOperation(value = "看板2 地图 type = 0 合同维度， 1 开票维度  ， 2 进账维度")
    public ResponseMessage<AreaDashBoardVO> getMapData(@PathVariable("type") int type) {
        return Result.success(dashBoardService.getMapData(type));
    }

    @GetMapping("/getOrgChartDataVO/{type}")
    @ApiOperation(value = "看板2 本部年度合同统计柱状图 type = 0 合同维度， 1 开票维度  ， 2 进账维度")
    public ResponseMessage<OrgEODashBoardVO> getOrgChartDataVO(@PathVariable("type") int type) {
        return Result.success(dashBoardService.getOrgChartDataVO(type));
    }

    @GetMapping("/getChartDataList")
    @ApiOperation(value = "看板2 各部门数据列表 isBigOrg 为 true 是本部级别 原型是高亮显示")
    public ResponseMessage<List<OrgContractInvoiceVO>> getChartDataList() {
        return Result.success(dashBoardService.getChartDataList());
    }



}
