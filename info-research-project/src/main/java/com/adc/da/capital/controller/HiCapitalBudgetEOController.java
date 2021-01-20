package com.adc.da.capital.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.capital.entity.HiCapitalBudgetEO;
import com.adc.da.capital.page.HiCapitalBudgetEOPage;
import com.adc.da.capital.service.HiCapitalBudgetEOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/capital/hiCapitalBudget")
@Api(tags = "|科研类项目模块-资金相关-变更|")
@Slf4j
public class HiCapitalBudgetEOController extends BaseController<HiCapitalBudgetEO> {

    @Autowired
    private HiCapitalBudgetEOService hiCapitalBudgetEOService;

    @ApiOperation(value = "|HiCapitalBudgetEO|查询")
    @GetMapping("")
    //@RequiresPermissions("capital:hiCapitalBudget:list")
    public ResponseMessage<List<HiCapitalBudgetEO>> list(String procBusinessKey, String researchProjectId)
        throws Exception {
        HiCapitalBudgetEOPage page = new HiCapitalBudgetEOPage();
        page.setProcBusinessKey(procBusinessKey);
        page.setResearchProjectId(researchProjectId);
        return Result.success(hiCapitalBudgetEOService.queryByList(page));
    }

    @ApiOperation(value = "|HiCapitalBudgetEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("capital:hiCapitalBudget:update")
    public ResponseMessage<HiCapitalBudgetEO> update(@RequestBody HiCapitalBudgetEO hiCapitalBudgetEO)
        throws Exception {
        hiCapitalBudgetEOService.updateAndSetMask(hiCapitalBudgetEO);
        return Result.success(hiCapitalBudgetEO);
    }

}
