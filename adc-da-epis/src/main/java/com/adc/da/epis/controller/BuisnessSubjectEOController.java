package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessSubjectEO;
import com.adc.da.epis.page.BuisnessSubjectEOPage;
import com.adc.da.epis.service.BuisnessSubjectEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessSubject")
@Api(description = "|BuisnessSubjectEO|")
public class BuisnessSubjectEOController extends BaseController<BuisnessSubjectEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessSubjectEOController.class);

    @Autowired
    private BuisnessSubjectEOService buisnessSubjectEOService;

	@ApiOperation(value = "|BuisnessSubjectEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessSubject:page")
    public ResponseMessage<PageInfo<BuisnessSubjectEO>> page(BuisnessSubjectEOPage page) throws Exception {
        List<BuisnessSubjectEO> rows = buisnessSubjectEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessSubjectEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessSubject:list")
    public ResponseMessage<List<BuisnessSubjectEO>> list(BuisnessSubjectEOPage page) throws Exception {
        return Result.success(buisnessSubjectEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessSubjectEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("epis:buisnessSubject:get")
    public ResponseMessage<BuisnessSubjectEO> find(@PathVariable String id) throws Exception {
        return Result.success(buisnessSubjectEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BuisnessSubjectEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessSubject:save")
    public ResponseMessage<BuisnessSubjectEO> create(@RequestBody BuisnessSubjectEO buisnessSubjectEO) throws Exception {
        buisnessSubjectEOService.insertSelective(buisnessSubjectEO);
        return Result.success(buisnessSubjectEO);
    }

    @ApiOperation(value = "|BuisnessSubjectEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessSubject:update")
    public ResponseMessage<BuisnessSubjectEO> update(@RequestBody BuisnessSubjectEO buisnessSubjectEO) throws Exception {
        buisnessSubjectEOService.updateByPrimaryKeySelective(buisnessSubjectEO);
        return Result.success(buisnessSubjectEO);
    }

    @ApiOperation(value = "|BuisnessSubjectEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("epis:buisnessSubject:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        buisnessSubjectEOService.deleteByPrimaryKey(id);
        logger.info("delete from BUISNESS_SUBJECT where id = {}", id);
        return Result.success();
    }

}
