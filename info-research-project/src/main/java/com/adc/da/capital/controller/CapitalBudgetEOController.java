package com.adc.da.capital.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.capital.entity.CapitalBudgetEO;
import com.adc.da.capital.page.CapitalBudgetEOPage;
import com.adc.da.capital.service.CapitalBudgetEOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/capital/capitalBudget")
@Api(tags = "|科研类项目模块-资金相关|")
@Slf4j
public class CapitalBudgetEOController extends BaseController<CapitalBudgetEO> {

    @Autowired
    private CapitalBudgetEOService capitalBudgetEOService;

    @ApiOperation(value = "|CapitalBudgetEO|查询")
    @GetMapping("")
    //@RequiresPermissions"capital:capitalBudget:list")
    public ResponseMessage<List<CapitalBudgetEO>> list(String researchProjectId) throws Exception {
        CapitalBudgetEOPage page = new CapitalBudgetEOPage();
        page.setResearchProjectId(researchProjectId);
        return Result.success(capitalBudgetEOService.queryByList(page));
    }

    @ApiOperation(value = "|CapitalBudgetEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions"capital:capitalBudget:save")
    public ResponseMessage<CapitalBudgetEO> create(@RequestBody CapitalBudgetEO capitalBudgetEO) throws Exception {
        capitalBudgetEOService.insertSelective(capitalBudgetEO);
        return Result.success(capitalBudgetEO);
    }

    @ApiOperation(value = "|CapitalBudgetEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions"capital:capitalBudget:update")
    public ResponseMessage<CapitalBudgetEO> update(@RequestBody CapitalBudgetEO capitalBudgetEO) throws Exception {
        capitalBudgetEOService.updateByPrimaryKeySelective(capitalBudgetEO);
        return Result.success(capitalBudgetEO);
    }

}
