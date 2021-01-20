package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessProjectSchedulingEO;
import com.adc.da.epis.page.BuisnessProjectSchedulingEOPage;
import com.adc.da.epis.service.BuisnessProjectSchedulingEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessProjectScheduling")
@Api(description = "|BuisnessProjectSchedulingEO|")
public class BuisnessProjectSchedulingEOController extends BaseController<BuisnessProjectSchedulingEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessProjectSchedulingEOController.class);

    @Autowired
    private BuisnessProjectSchedulingEOService buisnessProjectSchedulingEOService;

	@ApiOperation(value = "|BuisnessProjectSchedulingEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessProjectScheduling:page")
    public ResponseMessage<PageInfo<BuisnessProjectSchedulingEO>> page(BuisnessProjectSchedulingEOPage page) throws Exception {
        List<BuisnessProjectSchedulingEO> rows = buisnessProjectSchedulingEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessProjectSchedulingEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessProjectScheduling:list")
    public ResponseMessage<List<BuisnessProjectSchedulingEO>> list(BuisnessProjectSchedulingEOPage page) throws Exception {
        return Result.success(buisnessProjectSchedulingEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessProjectSchedulingEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("epis:buisnessProjectScheduling:get")
    public ResponseMessage<BuisnessProjectSchedulingEO> find(@PathVariable String id) throws Exception {
        return Result.success(buisnessProjectSchedulingEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BuisnessProjectSchedulingEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessProjectScheduling:save")
    public ResponseMessage<BuisnessProjectSchedulingEO> create(@RequestBody BuisnessProjectSchedulingEO buisnessProjectSchedulingEO) throws Exception {
        buisnessProjectSchedulingEOService.insertSelective(buisnessProjectSchedulingEO);
        return Result.success(buisnessProjectSchedulingEO);
    }

    @ApiOperation(value = "|BuisnessProjectSchedulingEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessProjectScheduling:update")
    public ResponseMessage<BuisnessProjectSchedulingEO> update(@RequestBody BuisnessProjectSchedulingEO buisnessProjectSchedulingEO) throws Exception {
        buisnessProjectSchedulingEOService.updateByPrimaryKeySelective(buisnessProjectSchedulingEO);
        return Result.success(buisnessProjectSchedulingEO);
    }

    @ApiOperation(value = "|BuisnessProjectSchedulingEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("epis:buisnessProjectScheduling:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        buisnessProjectSchedulingEOService.deleteByPrimaryKey(id);
        logger.info("delete from BUISNESS_PROJECT_SCHEDULING where id = {}", id);
        return Result.success();
    }

}
