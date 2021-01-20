package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessBugDetailEO;
import com.adc.da.epis.page.BuisnessBugDetailEOPage;
import com.adc.da.epis.service.BuisnessBugDetailEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessBugDetail")
@Api(description = "|BuisnessBugDetailEO|")
public class BuisnessBugDetailEOController extends BaseController<BuisnessBugDetailEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessBugDetailEOController.class);

    @Autowired
    private BuisnessBugDetailEOService buisnessBugDetailEOService;

	@ApiOperation(value = "|BuisnessBugDetailEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessBugDetail:page")
    public ResponseMessage<PageInfo<BuisnessBugDetailEO>> page(BuisnessBugDetailEOPage page) throws Exception {
        List<BuisnessBugDetailEO> rows = buisnessBugDetailEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessBugDetailEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessBugDetail:list")
    public ResponseMessage<List<BuisnessBugDetailEO>> list(BuisnessBugDetailEOPage page) throws Exception {
        return Result.success(buisnessBugDetailEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessBugDetailEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("epis:buisnessBugDetail:get")
    public ResponseMessage<BuisnessBugDetailEO> find(@PathVariable String id) throws Exception {
        return Result.success(buisnessBugDetailEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BuisnessBugDetailEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessBugDetail:save")
    public ResponseMessage<BuisnessBugDetailEO> create(@RequestBody BuisnessBugDetailEO buisnessBugDetailEO) throws Exception {
        buisnessBugDetailEOService.insertSelective(buisnessBugDetailEO);
        return Result.success(buisnessBugDetailEO);
    }

    @ApiOperation(value = "|BuisnessBugDetailEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessBugDetail:update")
    public ResponseMessage<BuisnessBugDetailEO> update(@RequestBody BuisnessBugDetailEO buisnessBugDetailEO) throws Exception {
        buisnessBugDetailEOService.updateByPrimaryKeySelective(buisnessBugDetailEO);
        return Result.success(buisnessBugDetailEO);
    }

    @ApiOperation(value = "|BuisnessBugDetailEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("epis:buisnessBugDetail:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        buisnessBugDetailEOService.deleteByPrimaryKey(id);
        logger.info("delete from BUISNESS_BUG_DETAIL where id = {}", id);
        return Result.success();
    }

}
