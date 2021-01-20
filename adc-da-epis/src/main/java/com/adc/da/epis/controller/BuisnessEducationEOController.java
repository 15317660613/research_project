package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessEducationEO;
import com.adc.da.epis.page.BuisnessEducationEOPage;
import com.adc.da.epis.service.BuisnessEducationEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessEducation")
@Api(description = "|BuisnessEducationEO|")
public class BuisnessEducationEOController extends BaseController<BuisnessEducationEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessEducationEOController.class);

    @Autowired
    private BuisnessEducationEOService buisnessEducationEOService;

	@ApiOperation(value = "|BuisnessEducationEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessEducation:page")
    public ResponseMessage<PageInfo<BuisnessEducationEO>> page(BuisnessEducationEOPage page) throws Exception {
        List<BuisnessEducationEO> rows = buisnessEducationEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessEducationEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessEducation:list")
    public ResponseMessage<List<BuisnessEducationEO>> list(BuisnessEducationEOPage page) throws Exception {
        return Result.success(buisnessEducationEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessEducationEO|详情")
    @GetMapping("/{trainingprogrmId}")
    @RequiresPermissions("epis:buisnessEducation:get")
    public ResponseMessage<BuisnessEducationEO> find(@PathVariable String trainingprogrmId) throws Exception {
        return Result.success(buisnessEducationEOService.selectByPrimaryKey(trainingprogrmId));
    }

    @ApiOperation(value = "|BuisnessEducationEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessEducation:save")
    public ResponseMessage<BuisnessEducationEO> create(@RequestBody BuisnessEducationEO buisnessEducationEO) throws Exception {
        buisnessEducationEOService.insertSelective(buisnessEducationEO);
        return Result.success(buisnessEducationEO);
    }

    @ApiOperation(value = "|BuisnessEducationEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessEducation:update")
    public ResponseMessage<BuisnessEducationEO> update(@RequestBody BuisnessEducationEO buisnessEducationEO) throws Exception {
        buisnessEducationEOService.updateByPrimaryKeySelective(buisnessEducationEO);
        return Result.success(buisnessEducationEO);
    }

    @ApiOperation(value = "|BuisnessEducationEO|删除")
    @DeleteMapping("/{trainingprogrmId}")
    @RequiresPermissions("epis:buisnessEducation:delete")
    public ResponseMessage delete(@PathVariable String trainingprogrmId) throws Exception {
        buisnessEducationEOService.deleteByPrimaryKey(trainingprogrmId);
        logger.info("delete from BUISNESS_EDUCATION where trainingprogrmId = {}", trainingprogrmId);
        return Result.success();
    }

}
