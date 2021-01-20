package com.adc.da.capital.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.capital.entity.CapitalExpenditureEO;
import com.adc.da.capital.page.CapitalExpenditureEOPage;
import com.adc.da.capital.service.CapitalExpenditureEOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.UUID;
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
@RequestMapping("/${restPath}/capital/capitalExpenditure")
@Api(tags = "|科研类项目模块-资金相关|")
@Slf4j
public class CapitalExpenditureEOController extends BaseController<CapitalExpenditureEO> {

    @Autowired
    private CapitalExpenditureEOService capitalExpenditureEOService;

    @ApiOperation(value = "|CapitalExpenditureEO|查询")
    @GetMapping("")
    //@RequiresPermissions("capital:capitalExpenditure:list")
    public ResponseMessage<List<CapitalExpenditureEO>> list(String researchProjectId) throws Exception {
        CapitalExpenditureEOPage page = new CapitalExpenditureEOPage();
        page.setResearchProjectId(researchProjectId);
        page.setOrderBy("EXT_INFO_3_");
        return Result.success(capitalExpenditureEOService.queryByList(page));
    }

    @ApiOperation(value = "|CapitalExpenditureEO|新增-批量")
    @PostMapping(value = "/multi", consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("capital:capitalExpenditure:save")
    public ResponseMessage<List<CapitalExpenditureEO>> createMulti(
        @RequestBody List<CapitalExpenditureEO> capitalExpenditureEOList) {
        int i = 1;
        String baseId = UUID.randomUUID10();
        for (CapitalExpenditureEO capitalExpenditureEO : capitalExpenditureEOList) {
            capitalExpenditureEO.setId(baseId + "_" + i++);
        }
        capitalExpenditureEOService.insertSelectiveAll(capitalExpenditureEOList);
        return Result.success(capitalExpenditureEOList);
    }

    @ApiOperation(value = "|CapitalExpenditureEO|修改-批量")
    @PutMapping(value = "/multi", consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("capital:capitalExpenditure:update")
    public ResponseMessage<List<CapitalExpenditureEO>> updateMulti(
        @RequestBody List<CapitalExpenditureEO> capitalExpenditureEOList)
        throws Exception {
        for (CapitalExpenditureEO capitalExpenditureEO : capitalExpenditureEOList) {
            capitalExpenditureEOService.updateByPrimaryKeySelective(capitalExpenditureEO);
        }
        return Result.success(capitalExpenditureEOList);
    }

}
