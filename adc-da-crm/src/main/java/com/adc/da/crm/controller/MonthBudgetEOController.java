package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.MonthBudgetEO;
import com.adc.da.crm.page.MonthBudgetEOPage;
import com.adc.da.crm.service.MonthBudgetEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/monthBudget")
@Api(description = "|MonthBudgetEO|")
public class MonthBudgetEOController extends BaseController<MonthBudgetEO>{

    private static final Logger logger = LoggerFactory.getLogger(MonthBudgetEOController.class);

    @Autowired
    private MonthBudgetEOService monthBudgetEOService;

	@ApiOperation(value = "|MonthBudgetEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:monthBudget:page")
    public ResponseMessage<PageInfo<MonthBudgetEO>> page(MonthBudgetEOPage page) throws Exception {
        List<MonthBudgetEO> rows = monthBudgetEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|MonthBudgetEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:monthBudget:list")
    public ResponseMessage<List<MonthBudgetEO>> list(MonthBudgetEOPage page) throws Exception {
        return Result.success(monthBudgetEOService.queryByList(page));
	}

    @ApiOperation(value = "|MonthBudgetEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:monthBudget:get")
    public ResponseMessage<MonthBudgetEO> find(@PathVariable String id) throws Exception {
        return Result.success(monthBudgetEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|MonthBudgetEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:monthBudget:save")
    public ResponseMessage<MonthBudgetEO> create(@RequestBody MonthBudgetEO monthBudgetEO) throws Exception {
        monthBudgetEOService.insertSelective(monthBudgetEO);
        return Result.success(monthBudgetEO);
    }

    @ApiOperation(value = "|MonthBudgetEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:monthBudget:update")
    public ResponseMessage<MonthBudgetEO> update(@RequestBody MonthBudgetEO monthBudgetEO) throws Exception {
        monthBudgetEOService.updateByPrimaryKeySelective(monthBudgetEO);
        return Result.success(monthBudgetEO);
    }

    @ApiOperation(value = "|MonthBudgetEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:monthBudget:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        monthBudgetEOService.deleteByPrimaryKey(id);
        logger.info("delete from MONTH_BUDGET where id = {}", id);
        return Result.success();
    }

}
