package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessContractInfoEO;
import com.adc.da.epis.page.BuisnessContractInfoEOPage;
import com.adc.da.epis.service.BuisnessContractInfoEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessContractInfo")
@Api(description = "|BuisnessContractInfoEO|")
public class BuisnessContractInfoEOController extends BaseController<BuisnessContractInfoEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessContractInfoEOController.class);

    @Autowired
    private BuisnessContractInfoEOService buisnessContractInfoEOService;

	@ApiOperation(value = "|BuisnessContractInfoEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessContractInfo:page")
    public ResponseMessage<PageInfo<BuisnessContractInfoEO>> page(BuisnessContractInfoEOPage page) throws Exception {
        List<BuisnessContractInfoEO> rows = buisnessContractInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessContractInfoEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessContractInfo:list")
    public ResponseMessage<List<BuisnessContractInfoEO>> list(BuisnessContractInfoEOPage page) throws Exception {
        return Result.success(buisnessContractInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessContractInfoEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("epis:buisnessContractInfo:get")
    public ResponseMessage<BuisnessContractInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(buisnessContractInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BuisnessContractInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessContractInfo:save")
    public ResponseMessage<BuisnessContractInfoEO> create(@RequestBody BuisnessContractInfoEO buisnessContractInfoEO) throws Exception {
        buisnessContractInfoEOService.insertSelective(buisnessContractInfoEO);
        return Result.success(buisnessContractInfoEO);
    }

    @ApiOperation(value = "|BuisnessContractInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessContractInfo:update")
    public ResponseMessage<BuisnessContractInfoEO> update(@RequestBody BuisnessContractInfoEO buisnessContractInfoEO) throws Exception {
        buisnessContractInfoEOService.updateByPrimaryKeySelective(buisnessContractInfoEO);
        return Result.success(buisnessContractInfoEO);
    }

    @ApiOperation(value = "|BuisnessContractInfoEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("epis:buisnessContractInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        buisnessContractInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from BUISNESS_CONTRACT_INFO where id = {}", id);
        return Result.success();
    }

}
