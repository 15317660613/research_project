package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessReturnInfoEO;
import com.adc.da.epis.page.BuisnessReturnInfoEOPage;
import com.adc.da.epis.service.BuisnessReturnInfoEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessReturnInfo")
@Api(description = "|BuisnessReturnInfoEO|")
public class BuisnessReturnInfoEOController extends BaseController<BuisnessReturnInfoEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessReturnInfoEOController.class);

    @Autowired
    private BuisnessReturnInfoEOService buisnessReturnInfoEOService;

	@ApiOperation(value = "|BuisnessReturnInfoEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessReturnInfo:page")
    public ResponseMessage<PageInfo<BuisnessReturnInfoEO>> page(BuisnessReturnInfoEOPage page) throws Exception {
        List<BuisnessReturnInfoEO> rows = buisnessReturnInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessReturnInfoEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessReturnInfo:list")
    public ResponseMessage<List<BuisnessReturnInfoEO>> list(BuisnessReturnInfoEOPage page) throws Exception {
        return Result.success(buisnessReturnInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessReturnInfoEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("epis:buisnessReturnInfo:get")
    public ResponseMessage<BuisnessReturnInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(buisnessReturnInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BuisnessReturnInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessReturnInfo:save")
    public ResponseMessage<BuisnessReturnInfoEO> create(@RequestBody BuisnessReturnInfoEO buisnessReturnInfoEO) throws Exception {
        buisnessReturnInfoEOService.insertSelective(buisnessReturnInfoEO);
        return Result.success(buisnessReturnInfoEO);
    }

    @ApiOperation(value = "|BuisnessReturnInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessReturnInfo:update")
    public ResponseMessage<BuisnessReturnInfoEO> update(@RequestBody BuisnessReturnInfoEO buisnessReturnInfoEO) throws Exception {
        buisnessReturnInfoEOService.updateByPrimaryKeySelective(buisnessReturnInfoEO);
        return Result.success(buisnessReturnInfoEO);
    }

    @ApiOperation(value = "|BuisnessReturnInfoEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("epis:buisnessReturnInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        buisnessReturnInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from BUISNESS_RETURN_INFO where id = {}", id);
        return Result.success();
    }

}
