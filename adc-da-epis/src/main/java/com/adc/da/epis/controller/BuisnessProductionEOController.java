package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessProductionEO;
import com.adc.da.epis.page.BuisnessProductionEOPage;
import com.adc.da.epis.service.BuisnessProductionEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessProduction")
@Api(description = "|BuisnessProductionEO|")
public class BuisnessProductionEOController extends BaseController<BuisnessProductionEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessProductionEOController.class);

    @Autowired
    private BuisnessProductionEOService buisnessProductionEOService;

	@ApiOperation(value = "|BuisnessProductionEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessProduction:page")
    public ResponseMessage<PageInfo<BuisnessProductionEO>> page(BuisnessProductionEOPage page) throws Exception {
        List<BuisnessProductionEO> rows = buisnessProductionEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessProductionEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessProduction:list")
    public ResponseMessage<List<BuisnessProductionEO>> list(BuisnessProductionEOPage page) throws Exception {
        return Result.success(buisnessProductionEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessProductionEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("epis:buisnessProduction:get")
    public ResponseMessage<BuisnessProductionEO> find(@PathVariable String id) throws Exception {
        return Result.success(buisnessProductionEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BuisnessProductionEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessProduction:save")
    public ResponseMessage<BuisnessProductionEO> create(@RequestBody BuisnessProductionEO buisnessProductionEO) throws Exception {
        buisnessProductionEOService.insertSelective(buisnessProductionEO);
        return Result.success(buisnessProductionEO);
    }

    @ApiOperation(value = "|BuisnessProductionEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessProduction:update")
    public ResponseMessage<BuisnessProductionEO> update(@RequestBody BuisnessProductionEO buisnessProductionEO) throws Exception {
        buisnessProductionEOService.updateByPrimaryKeySelective(buisnessProductionEO);
        return Result.success(buisnessProductionEO);
    }

    @ApiOperation(value = "|BuisnessProductionEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("epis:buisnessProduction:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        buisnessProductionEOService.deleteByPrimaryKey(id);
        logger.info("delete from BUISNESS_PRODUCTION where id = {}", id);
        return Result.success();
    }

}
