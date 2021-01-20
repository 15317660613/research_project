package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuinessPersonnaltimedataEO;
import com.adc.da.epis.page.BuinessPersonnaltimedataEOPage;
import com.adc.da.epis.service.BuinessPersonnaltimedataEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buinessPersonnaltimedata")
@Api(description = "|BuinessPersonnaltimedataEO|")
public class BuinessPersonnaltimedataEOController extends BaseController<BuinessPersonnaltimedataEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuinessPersonnaltimedataEOController.class);

    @Autowired
    private BuinessPersonnaltimedataEOService buinessPersonnaltimedataEOService;

	@ApiOperation(value = "|BuinessPersonnaltimedataEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buinessPersonnaltimedata:page")
    public ResponseMessage<PageInfo<BuinessPersonnaltimedataEO>> page(BuinessPersonnaltimedataEOPage page) throws Exception {
        List<BuinessPersonnaltimedataEO> rows = buinessPersonnaltimedataEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuinessPersonnaltimedataEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buinessPersonnaltimedata:list")
    public ResponseMessage<List<BuinessPersonnaltimedataEO>> list(BuinessPersonnaltimedataEOPage page) throws Exception {
        return Result.success(buinessPersonnaltimedataEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuinessPersonnaltimedataEO|详情")
    @GetMapping("/{timedataId}")
    @RequiresPermissions("epis:buinessPersonnaltimedata:get")
    public ResponseMessage<BuinessPersonnaltimedataEO> find(@PathVariable String timedataId) throws Exception {
        return Result.success(buinessPersonnaltimedataEOService.selectByPrimaryKey(timedataId));
    }

    @ApiOperation(value = "|BuinessPersonnaltimedataEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buinessPersonnaltimedata:save")
    public ResponseMessage<BuinessPersonnaltimedataEO> create(@RequestBody BuinessPersonnaltimedataEO buinessPersonnaltimedataEO) throws Exception {
        buinessPersonnaltimedataEOService.insertSelective(buinessPersonnaltimedataEO);
        return Result.success(buinessPersonnaltimedataEO);
    }

    @ApiOperation(value = "|BuinessPersonnaltimedataEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buinessPersonnaltimedata:update")
    public ResponseMessage<BuinessPersonnaltimedataEO> update(@RequestBody BuinessPersonnaltimedataEO buinessPersonnaltimedataEO) throws Exception {
        buinessPersonnaltimedataEOService.updateByPrimaryKeySelective(buinessPersonnaltimedataEO);
        return Result.success(buinessPersonnaltimedataEO);
    }

    @ApiOperation(value = "|BuinessPersonnaltimedataEO|删除")
    @DeleteMapping("/{timedataId}")
    @RequiresPermissions("epis:buinessPersonnaltimedata:delete")
    public ResponseMessage delete(@PathVariable String timedataId) throws Exception {
        buinessPersonnaltimedataEOService.deleteByPrimaryKey(timedataId);
        logger.info("delete from BUINESS_PERSONNALTIMEDATA where timedataId = {}", timedataId);
        return Result.success();
    }

}
