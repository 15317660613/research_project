package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessProjectEO;
import com.adc.da.epis.page.BuisnessProjectEOPage;
import com.adc.da.epis.service.BuisnessProjectEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessProject")
@Api(description = "|BuisnessProjectEO|")
public class BuisnessProjectEOController extends BaseController<BuisnessProjectEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessProjectEOController.class);

    @Autowired
    private BuisnessProjectEOService buisnessProjectEOService;

	@ApiOperation(value = "|BuisnessProjectEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessProject:page")
    public ResponseMessage<PageInfo<BuisnessProjectEO>> page(BuisnessProjectEOPage page) throws Exception {
        List<BuisnessProjectEO> rows = buisnessProjectEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessProjectEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessProject:list")
    public ResponseMessage<List<BuisnessProjectEO>> list(BuisnessProjectEOPage page) throws Exception {
        return Result.success(buisnessProjectEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessProjectEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("epis:buisnessProject:get")
    public ResponseMessage<BuisnessProjectEO> find(@PathVariable String id) throws Exception {
        return Result.success(buisnessProjectEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BuisnessProjectEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessProject:save")
    public ResponseMessage<BuisnessProjectEO> create(@RequestBody BuisnessProjectEO buisnessProjectEO) throws Exception {
        buisnessProjectEOService.insertSelective(buisnessProjectEO);
        return Result.success(buisnessProjectEO);
    }

    @ApiOperation(value = "|BuisnessProjectEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessProject:update")
    public ResponseMessage<BuisnessProjectEO> update(@RequestBody BuisnessProjectEO buisnessProjectEO) throws Exception {
        buisnessProjectEOService.updateByPrimaryKeySelective(buisnessProjectEO);
        return Result.success(buisnessProjectEO);
    }

    @ApiOperation(value = "|BuisnessProjectEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("epis:buisnessProject:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        buisnessProjectEOService.deleteByPrimaryKey(id);
        logger.info("delete from BUISNESS_PROJECT where id = {}", id);
        return Result.success();
    }

}
