package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessPersionInputEO;
import com.adc.da.epis.page.BuisnessPersionInputEOPage;
import com.adc.da.epis.service.BuisnessPersionInputEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessPersionInput")
@Api(description = "|BuisnessPersionInputEO|")
public class BuisnessPersionInputEOController extends BaseController<BuisnessPersionInputEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessPersionInputEOController.class);

    @Autowired
    private BuisnessPersionInputEOService buisnessPersionInputEOService;

	@ApiOperation(value = "|BuisnessPersionInputEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessPersionInput:page")
    public ResponseMessage<PageInfo<BuisnessPersionInputEO>> page(BuisnessPersionInputEOPage page) throws Exception {
        List<BuisnessPersionInputEO> rows = buisnessPersionInputEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessPersionInputEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessPersionInput:list")
    public ResponseMessage<List<BuisnessPersionInputEO>> list(BuisnessPersionInputEOPage page) throws Exception {
        return Result.success(buisnessPersionInputEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessPersionInputEO|详情")
    @GetMapping("/{humaninputId}")
    @RequiresPermissions("epis:buisnessPersionInput:get")
    public ResponseMessage<BuisnessPersionInputEO> find(@PathVariable String humaninputId) throws Exception {
        return Result.success(buisnessPersionInputEOService.selectByPrimaryKey(humaninputId));
    }

    @ApiOperation(value = "|BuisnessPersionInputEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessPersionInput:save")
    public ResponseMessage<BuisnessPersionInputEO> create(@RequestBody BuisnessPersionInputEO buisnessPersionInputEO) throws Exception {
        buisnessPersionInputEOService.insertSelective(buisnessPersionInputEO);
        return Result.success(buisnessPersionInputEO);
    }

    @ApiOperation(value = "|BuisnessPersionInputEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessPersionInput:update")
    public ResponseMessage<BuisnessPersionInputEO> update(@RequestBody BuisnessPersionInputEO buisnessPersionInputEO) throws Exception {
        buisnessPersionInputEOService.updateByPrimaryKeySelective(buisnessPersionInputEO);
        return Result.success(buisnessPersionInputEO);
    }

    @ApiOperation(value = "|BuisnessPersionInputEO|删除")
    @DeleteMapping("/{humaninputId}")
    @RequiresPermissions("epis:buisnessPersionInput:delete")
    public ResponseMessage delete(@PathVariable String humaninputId) throws Exception {
        buisnessPersionInputEOService.deleteByPrimaryKey(humaninputId);
        logger.info("delete from BUISNESS_PERSION_INPUT where humaninputId = {}", humaninputId);
        return Result.success();
    }

}
