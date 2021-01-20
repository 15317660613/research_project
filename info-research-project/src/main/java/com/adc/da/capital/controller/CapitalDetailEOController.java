package com.adc.da.capital.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.capital.entity.CapitalExpenditureDetailEO;
import com.adc.da.capital.page.CapitalExpenditureDetailEOPage;
import com.adc.da.capital.service.CapitalExpenditureDetailEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/capital/capitalExpenditureDetail")
@Api(tags = "|科研类项目模块-资金相关|")
@Slf4j
public class CapitalDetailEOController extends BaseController<CapitalExpenditureDetailEO> {

    @Autowired
    private CapitalExpenditureDetailEOService service;

    @ApiOperation(value = "|CapitalExpenditureDetailEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions"capital:capitalExpenditureDetail:page")
    public ResponseMessage<PageInfo<CapitalExpenditureDetailEO>> page(CapitalExpenditureDetailEOPage page)
        throws Exception {
        List<CapitalExpenditureDetailEO> rows = service.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|CapitalExpenditureDetailEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions"capital:capitalExpenditureDetail:save")
    public ResponseMessage<CapitalExpenditureDetailEO> create(
        @RequestBody CapitalExpenditureDetailEO capitalExpenditureDetailEO) throws Exception {
        capitalExpenditureDetailEO.setId(UUID.randomUUID10());
        service.insertSelective(capitalExpenditureDetailEO);
        return Result.success(capitalExpenditureDetailEO);
    }

    @ApiOperation(value = "|CapitalExpenditureDetailEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions"capital:capitalExpenditureDetail:update")
    public ResponseMessage<CapitalExpenditureDetailEO> update(
        @RequestBody CapitalExpenditureDetailEO capitalExpenditureDetailEO) throws Exception {
        service.updateByPrimaryKeySelective(capitalExpenditureDetailEO);
        return Result.success(capitalExpenditureDetailEO);
    }

    @ApiOperation(value = "|CapitalExpenditureDetailEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions"capital:capitalExpenditureDetail:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        service.deleteByPrimaryKey(id);
        log.info("delete from RS_CAPITAL_EXPENDITURE_DETAIL where id = {}", id);
        return Result.success();
    }

}
