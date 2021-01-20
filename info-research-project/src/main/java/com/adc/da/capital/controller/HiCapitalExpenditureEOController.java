package com.adc.da.capital.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.capital.entity.HiCapitalExpenditureEO;
import com.adc.da.capital.page.HiCapitalExpenditureEOPage;
import com.adc.da.capital.service.HiCapitalExpenditureEOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/capital/hiCapitalExpenditure")
@Api(tags = "|科研类项目模块-资金相关-变更|")
public class HiCapitalExpenditureEOController extends BaseController<HiCapitalExpenditureEO> {

    @Autowired
    private HiCapitalExpenditureEOService hiCapitalExpenditureEOService;

    @ApiOperation(value = "|HiCapitalExpenditureEO|查询")
    @GetMapping("")
    //@RequiresPermissions("capital:hiCapitalExpenditure:list")
    public ResponseMessage<List<HiCapitalExpenditureEO>> list(String procBusinessKey, String researchProjectId)
        throws Exception {
        HiCapitalExpenditureEOPage page = new HiCapitalExpenditureEOPage();
        page.setProcBusinessKey(procBusinessKey);
        page.setResearchProjectId(researchProjectId);
        page.setOrderBy("EXT_INFO_3_");
        return Result.success(hiCapitalExpenditureEOService.queryByList(page));
    }

    @ApiOperation(value = "|CapitalExpenditureEO|修改-批量")
    @PutMapping(value = "/multi", consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("capital:capitalExpenditure:update")
    public ResponseMessage<List<HiCapitalExpenditureEO>> updateMulti(
        @RequestBody List<HiCapitalExpenditureEO> capitalExpenditureEOList)
        throws Exception {
        hiCapitalExpenditureEOService.updateAndSetMask(capitalExpenditureEOList);
        return Result.success(capitalExpenditureEOList);
    }
}
