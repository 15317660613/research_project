package com.adc.da.statics.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.Arrays;
import java.util.List;

import com.adc.da.dashboard.vo.*;
import com.adc.da.statics.vo.StaticOperationCompanyVO;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.statics.entity.SStaticOperationAmountEO;
import com.adc.da.statics.page.SStaticOperationAmountEOPage;
import com.adc.da.statics.service.SStaticOperationAmountEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/${restPath}/statics/sStaticOperationAmount")
@Api(description = "|SStaticOperationAmountEO|")
public class SStaticOperationAmountEOController extends BaseController<SStaticOperationAmountEO>{

    private static final Logger logger = LoggerFactory.getLogger(SStaticOperationAmountEOController.class);

    @Autowired
    private SStaticOperationAmountEOService sStaticOperationAmountEOService;

    @ApiOperation(value = "|SStaticOperationAmountEO|分页查询")
    @GetMapping("/page")
    // @RequiresPermissions("statics:sStaticOperationAmount:page")
    public ResponseMessage<PageInfo<SStaticOperationAmountEO>> page(SStaticOperationAmountEOPage page) throws Exception {
        List<SStaticOperationAmountEO> rows = sStaticOperationAmountEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|SStaticOperationAmountEO|查询")
    @GetMapping("")
    // @RequiresPermissions("statics:sStaticOperationAmount:list")
    public ResponseMessage<List<SStaticOperationAmountEO>> list(SStaticOperationAmountEOPage page) throws Exception {
        return Result.success(sStaticOperationAmountEOService.queryByList(page));
    }

    @ApiOperation(value = "|SStaticOperationAmountEO|详情")
    @GetMapping("/{id}")
    //  @RequiresPermissions("statics:sStaticOperationAmount:get")
    public ResponseMessage<SStaticOperationAmountEO> find(@PathVariable String id) throws Exception {
        return Result.success(sStaticOperationAmountEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|SStaticOperationAmountEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //  @RequiresPermissions("statics:sStaticOperationAmount:save")
    public ResponseMessage<SStaticOperationAmountEO> create(@RequestBody SStaticOperationAmountEO t) throws Exception {
        sStaticOperationAmountEOService.insertSelective(t);
        return Result.success(t);
    }

    @ApiOperation(value = "|SStaticOperationAmountEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    // @RequiresPermissions("statics:sStaticOperationAmount:update")
    public ResponseMessage<SStaticOperationAmountEO> update(@RequestBody SStaticOperationAmountEO t) throws Exception {
        sStaticOperationAmountEOService.updateByPrimaryKeySelective(t);
        return Result.success(t);
    }

    @ApiOperation(value = "|SStaticOperationAmountEO|删除")
    @DeleteMapping("/{id}")
    //  @RequiresPermissions("statics:sStaticOperationAmount:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        sStaticOperationAmountEOService.deleteByPrimaryKey(id);
        logger.info("delete from S_STATIC_OPERATION_AMOUNT where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|SalaryManagementEO|批量逻辑删除")
    @DeleteMapping("/logicDelete/{ids}")
//    @RequiresPermissions("finance:salaryManagement:delete")
    public ResponseMessage delete(@PathVariable String[] ids) throws Exception {
        sStaticOperationAmountEOService.logicDeleteByPrimaryKey(Arrays.asList(ids));
        logger.info("logic delete from S_STATIC_OPERATION_AMOUNT where id = {}", ids);
        return Result.success();
    }


    @GetMapping("/getContractDashBoardHeaderVO")
    @ApiOperation(value = "看板1 头部四个块 type=111 累计合同额,type=112 累计开票额,type=113 累计进账额,type=114 累计应收金额" +
                                         "type=101 本月合同额,type=102 本月开票额,type=103 本月进账额")
    public ResponseMessage<ContractDashBoardHeaderVO> getContractDashBoardHeaderVO() {
        return Result.success(sStaticOperationAmountEOService.getContractDashBoardHeaderVO());
    }


    @GetMapping("/getContractDashBoardBodyVO/{type}")
    @ApiOperation(value = "看板1 主体柱状图 type = 121 合同维度， 122 开票维度  ， 123 进账维度")
    public ResponseMessage<ContractDashBoardBodyVO> getMonthDataVO(@PathVariable("type") int type) {
        return Result.success(sStaticOperationAmountEOService.getContractDashBoardBodyVO(type));
    }


    @GetMapping("/getMapData/{type}")
    @ApiOperation(value = "看板2 地图（左） type = 211 开票维度  ， 212 进账维度")
    public ResponseMessage<AreaDashBoardVO> getMapDataProvince(@PathVariable("type") int type) {
        return Result.success(sStaticOperationAmountEOService.getMapDataProvince(type));
    }

    @GetMapping("/getMapDataArea/{type}")
    @ApiOperation(value = "看板2 地图（右） type =  221 开票维度  ， 222 进账维度")
    public ResponseMessage<AreaDashBoardVO> getMapDataArea(@PathVariable("type") int type) {
        return Result.success(sStaticOperationAmountEOService.getMapDataArea(type));
    }


    @GetMapping("/getOrgChartDataVO/{type}")
    @ApiOperation(value = "看板2 本部年度合同统计柱状图 type = 231 合同维度， 232 开票维度")
    public ResponseMessage<OrgEODashBoardVO> getOrgChartDataVO(@PathVariable("type") int type) {
        return Result.success(sStaticOperationAmountEOService.getOrgChartDataVO(type));
    }


    @GetMapping("/getChartDataList")
    @ApiOperation(value = "看板2 各部门数据列表,type = 240 本月开票额,type = 241 累计开票额, isBigOrg 为 true 是本部级别 原型是高亮显示")
    public ResponseMessage<List<OrgContractInvoiceVO>> getChartDataList() {
        return Result.success(sStaticOperationAmountEOService.getChartDataList());
    }

    @GetMapping("/getCompanyBusinessVO")
    @ApiOperation(value = "看板3 各公司经营情况 type=311 合同额,type = 312 开票额,type=313 进账额")
    public ResponseMessage<List<StaticOperationCompanyVO>> getCompanyBusinessVO() {
        return Result.success(sStaticOperationAmountEOService.getCompanyBusinessVO());
    }

    @GetMapping("/getCompanyContractVO")
    @ApiOperation(value = "看板3 企业年度合同总额排行榜 type=320")
    public ResponseMessage<List<StaticOperationCompanyVO>> getCompanyContractVO() {
        return Result.success(sStaticOperationAmountEOService.getCompanyContractVO());
    }

    @GetMapping("/getSalesVolumeVO")
    @ApiOperation(value = "看板3 车企月度销量排行榜 type=330")
    public ResponseMessage<List<StaticOperationCompanyVO>> getSalesVolumeVO() {
        return Result.success(sStaticOperationAmountEOService.getSalesVolumeVO());
    }

    @GetMapping("/getDepartmentVO")
    @ApiOperation(value = "看板3 部门收款项排行榜 type=340")
    public ResponseMessage<List<StaticOperationCompanyVO>> getDepartmentVO() {
        return Result.success(sStaticOperationAmountEOService.getDepartmentVO());
    }

    @GetMapping("/getCompanyVO")
    @ApiOperation(value = "看板3 企业应收款项项排行榜 type=350")
    public ResponseMessage<List<StaticOperationCompanyVO>> getCompanyVO() {
        return Result.success(sStaticOperationAmountEOService.getCompanyVO());
    }
}
