package com.adc.da.research.config.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.config.entity.FundRulesEO;
import com.adc.da.research.config.page.FundRulesEOPage;
import com.adc.da.research.config.service.FundRulesEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/${restPath}/config/fundRules")
@Api(tags = "科研系统|经费科目规则|FundRulesEOController")
public class FundRulesEOController extends BaseController<FundRulesEO> {

    private static final Logger logger = LoggerFactory.getLogger(FundRulesEOController.class);

    @Autowired
    private FundRulesEOService fundRulesEOService;

    @ApiOperation(value = "|FundRulesEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("config:fundRules:page")
    public ResponseMessage<PageInfo<FundRulesEO>> page(FundRulesEOPage page) throws Exception {
        page.setFundTemplateNameOperator("LIKE");
        List<FundRulesEO> rows = fundRulesEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|FundRulesEO|查询")
    @GetMapping("")
//    @RequiresPermissions("config:fundRules:list")
    public ResponseMessage<List<FundRulesEO>> list(FundRulesEOPage page) throws Exception {
        return Result.success(fundRulesEOService.queryByList(page));
    }

    @ApiOperation(value = "|FundRulesEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("config:fundRules:get")
    public ResponseMessage<FundRulesEO> find(@PathVariable String id) throws Exception {
        return Result.success(fundRulesEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|FundRulesEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("config:fundRules:save")
    public ResponseMessage<FundRulesEO> create(@RequestBody FundRulesEO fundRulesEO) throws Exception {
        fundRulesEOService.insertSelective(fundRulesEO);
        return Result.success(fundRulesEO);
    }

    @ApiOperation(value = "|FundRulesEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("config:fundRules:update")
    public ResponseMessage<FundRulesEO> update(@RequestBody FundRulesEO fundRulesEO) throws Exception {
        fundRulesEOService.updateByPrimaryKeySelective(fundRulesEO);
        return Result.success(fundRulesEO);
    }

    @ApiOperation(value = "|FundRulesEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("config:fundRules:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        fundRulesEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_FUND_RULES where id = {}", id);
        return Result.success();
    }

}
