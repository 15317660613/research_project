package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessPersionEducateEO;
import com.adc.da.epis.page.BuisnessPersionEducateEOPage;
import com.adc.da.epis.service.BuisnessPersionEducateEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessPersionEducate")
@Api(description = "|BuisnessPersionEducateEO|")
public class BuisnessPersionEducateEOController extends BaseController<BuisnessPersionEducateEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessPersionEducateEOController.class);

    @Autowired
    private BuisnessPersionEducateEOService buisnessPersionEducateEOService;

	@ApiOperation(value = "|BuisnessPersionEducateEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessPersionEducate:page")
    public ResponseMessage<PageInfo<BuisnessPersionEducateEO>> page(BuisnessPersionEducateEOPage page) throws Exception {
        List<BuisnessPersionEducateEO> rows = buisnessPersionEducateEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessPersionEducateEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessPersionEducate:list")
    public ResponseMessage<List<BuisnessPersionEducateEO>> list(BuisnessPersionEducateEOPage page) throws Exception {
        return Result.success(buisnessPersionEducateEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessPersionEducateEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("epis:buisnessPersionEducate:get")
    public ResponseMessage<BuisnessPersionEducateEO> find(@PathVariable String id) throws Exception {
        return Result.success(buisnessPersionEducateEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BuisnessPersionEducateEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessPersionEducate:save")
    public ResponseMessage<BuisnessPersionEducateEO> create(@RequestBody BuisnessPersionEducateEO buisnessPersionEducateEO) throws Exception {
        buisnessPersionEducateEOService.insertSelective(buisnessPersionEducateEO);
        return Result.success(buisnessPersionEducateEO);
    }

    @ApiOperation(value = "|BuisnessPersionEducateEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessPersionEducate:update")
    public ResponseMessage<BuisnessPersionEducateEO> update(@RequestBody BuisnessPersionEducateEO buisnessPersionEducateEO) throws Exception {
        buisnessPersionEducateEOService.updateByPrimaryKeySelective(buisnessPersionEducateEO);
        return Result.success(buisnessPersionEducateEO);
    }

    @ApiOperation(value = "|BuisnessPersionEducateEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("epis:buisnessPersionEducate:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        buisnessPersionEducateEOService.deleteByPrimaryKey(id);
        logger.info("delete from BUISNESS_PERSION_EDUCATE where id = {}", id);
        return Result.success();
    }

}
