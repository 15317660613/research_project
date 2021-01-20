package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessPersionInfoEO;
import com.adc.da.epis.page.BuisnessPersionInfoEOPage;
import com.adc.da.epis.service.BuisnessPersionInfoEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessPersionInfo")
@Api(description = "|BuisnessPersionInfoEO|")
public class BuisnessPersionInfoEOController extends BaseController<BuisnessPersionInfoEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessPersionInfoEOController.class);

    @Autowired
    private BuisnessPersionInfoEOService buisnessPersionInfoEOService;

	@ApiOperation(value = "|BuisnessPersionInfoEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessPersionInfo:page")
    public ResponseMessage<PageInfo<BuisnessPersionInfoEO>> page(BuisnessPersionInfoEOPage page) throws Exception {
        List<BuisnessPersionInfoEO> rows = buisnessPersionInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessPersionInfoEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessPersionInfo:list")
    public ResponseMessage<List<BuisnessPersionInfoEO>> list(BuisnessPersionInfoEOPage page) throws Exception {
        return Result.success(buisnessPersionInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessPersionInfoEO|详情")
    @GetMapping("/{personnelId}")
    @RequiresPermissions("epis:buisnessPersionInfo:get")
    public ResponseMessage<BuisnessPersionInfoEO> find(@PathVariable String personnelId) throws Exception {
        return Result.success(buisnessPersionInfoEOService.selectByPrimaryKey(personnelId));
    }

    @ApiOperation(value = "|BuisnessPersionInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessPersionInfo:save")
    public ResponseMessage<BuisnessPersionInfoEO> create(@RequestBody BuisnessPersionInfoEO buisnessPersionInfoEO) throws Exception {
        buisnessPersionInfoEOService.insertSelective(buisnessPersionInfoEO);
        return Result.success(buisnessPersionInfoEO);
    }

    @ApiOperation(value = "|BuisnessPersionInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessPersionInfo:update")
    public ResponseMessage<BuisnessPersionInfoEO> update(@RequestBody BuisnessPersionInfoEO buisnessPersionInfoEO) throws Exception {
        buisnessPersionInfoEOService.updateByPrimaryKeySelective(buisnessPersionInfoEO);
        return Result.success(buisnessPersionInfoEO);
    }

    @ApiOperation(value = "|BuisnessPersionInfoEO|删除")
    @DeleteMapping("/{personnelId}")
    @RequiresPermissions("epis:buisnessPersionInfo:delete")
    public ResponseMessage delete(@PathVariable String personnelId) throws Exception {
        buisnessPersionInfoEOService.deleteByPrimaryKey(personnelId);
        logger.info("delete from BUISNESS_PERSION_INFO where personnelId = {}", personnelId);
        return Result.success();
    }

}
