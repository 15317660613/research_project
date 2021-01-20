package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.SalesVlaueEO;
import com.adc.da.crm.page.SalesVlaueEOPage;
import com.adc.da.crm.service.SalesVlaueEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/salesVlaue")
@Api(description = "|SalesVlaueEO|")
public class SalesVlaueEOController extends BaseController<SalesVlaueEO>{

    private static final Logger logger = LoggerFactory.getLogger(SalesVlaueEOController.class);

    @Autowired
    private SalesVlaueEOService salesVlaueEOService;

	@ApiOperation(value = "|SalesVlaueEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:salesVlaue:page")
    public ResponseMessage<PageInfo<SalesVlaueEO>> page(SalesVlaueEOPage page) throws Exception {
        List<SalesVlaueEO> rows = salesVlaueEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|SalesVlaueEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:salesVlaue:list")
    public ResponseMessage<List<SalesVlaueEO>> list(SalesVlaueEOPage page) throws Exception {
        return Result.success(salesVlaueEOService.queryByList(page));
	}

    @ApiOperation(value = "|SalesVlaueEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:salesVlaue:get")
    public ResponseMessage<SalesVlaueEO> find(@PathVariable String id) throws Exception {
        return Result.success(salesVlaueEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|SalesVlaueEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:salesVlaue:save")
    public ResponseMessage<SalesVlaueEO> create(@RequestBody SalesVlaueEO salesVlaueEO) throws Exception {
        salesVlaueEOService.insertSelective(salesVlaueEO);
        return Result.success(salesVlaueEO);
    }

    @ApiOperation(value = "|SalesVlaueEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:salesVlaue:update")
    public ResponseMessage<SalesVlaueEO> update(@RequestBody SalesVlaueEO salesVlaueEO) throws Exception {
        salesVlaueEOService.updateByPrimaryKeySelective(salesVlaueEO);
        return Result.success(salesVlaueEO);
    }

    @ApiOperation(value = "|SalesVlaueEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:salesVlaue:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        salesVlaueEOService.deleteByPrimaryKey(id);
        logger.info("delete from SALES_VLAUE where id = {}", id);
        return Result.success();
    }

}
