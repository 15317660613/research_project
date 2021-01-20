package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessTravelEO;
import com.adc.da.epis.page.BuisnessTravelEOPage;
import com.adc.da.epis.service.BuisnessTravelEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessTravel")
@Api(description = "|BuisnessTravelEO|")
public class BuisnessTravelEOController extends BaseController<BuisnessTravelEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessTravelEOController.class);

    @Autowired
    private BuisnessTravelEOService buisnessTravelEOService;

	@ApiOperation(value = "|BuisnessTravelEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessTravel:page")
    public ResponseMessage<PageInfo<BuisnessTravelEO>> page(BuisnessTravelEOPage page) throws Exception {
        List<BuisnessTravelEO> rows = buisnessTravelEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessTravelEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessTravel:list")
    public ResponseMessage<List<BuisnessTravelEO>> list(BuisnessTravelEOPage page) throws Exception {
        return Result.success(buisnessTravelEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessTravelEO|详情")
    @GetMapping("/{travelId}")
    @RequiresPermissions("epis:buisnessTravel:get")
    public ResponseMessage<BuisnessTravelEO> find(@PathVariable String travelId) throws Exception {
        return Result.success(buisnessTravelEOService.selectByPrimaryKey(travelId));
    }

    @ApiOperation(value = "|BuisnessTravelEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessTravel:save")
    public ResponseMessage<BuisnessTravelEO> create(@RequestBody BuisnessTravelEO buisnessTravelEO) throws Exception {
        buisnessTravelEOService.insertSelective(buisnessTravelEO);
        return Result.success(buisnessTravelEO);
    }

    @ApiOperation(value = "|BuisnessTravelEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessTravel:update")
    public ResponseMessage<BuisnessTravelEO> update(@RequestBody BuisnessTravelEO buisnessTravelEO) throws Exception {
        buisnessTravelEOService.updateByPrimaryKeySelective(buisnessTravelEO);
        return Result.success(buisnessTravelEO);
    }

    @ApiOperation(value = "|BuisnessTravelEO|删除")
    @DeleteMapping("/{travelId}")
    @RequiresPermissions("epis:buisnessTravel:delete")
    public ResponseMessage delete(@PathVariable String travelId) throws Exception {
        buisnessTravelEOService.deleteByPrimaryKey(travelId);
        logger.info("delete from BUISNESS_TRAVEL where travelId = {}", travelId);
        return Result.success();
    }

}
