package com.adc.da.ext.sys.branchedleaders.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.ext.sys.branchedleaders.entity.BranchedLeadersEO;
import com.adc.da.ext.sys.branchedleaders.page.BranchedLeadersEOPage;
import com.adc.da.ext.sys.branchedleaders.service.BranchedLeadersEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/branchedleaders/branchedLeaders")
@Api(description = "|BranchedLeadersEO(分管领导接口)|")
public class BranchedLeadersEOController extends BaseController<BranchedLeadersEO>{

    private static final Logger logger = LoggerFactory.getLogger(BranchedLeadersEOController.class);

    @Autowired
    private BranchedLeadersEOService branchedLeadersEOService;

	@ApiOperation(value = "|BranchedLeadersEO|分页查询(new)")
    @GetMapping("/page")
    //@RequiresPermissions("branchedleaders:branchedLeaders:page")
    public ResponseMessage<PageInfo<BranchedLeadersEO>> page(BranchedLeadersEOPage page) throws Exception {
        List<BranchedLeadersEO> rows = branchedLeadersEOService.getPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BranchedLeadersEO|查询")
    @GetMapping("")
    //@RequiresPermissions("branchedleaders:branchedLeaders:list")
    public ResponseMessage<List<BranchedLeadersEO>> list(BranchedLeadersEOPage page) throws Exception {
        return Result.success(branchedLeadersEOService.queryByList(page));
	}

    @ApiOperation(value = "|BranchedLeadersEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("branchedleaders:branchedLeaders:get")
    public ResponseMessage<BranchedLeadersEO> find(@PathVariable String id) throws Exception {
        return Result.success(branchedLeadersEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BranchedLeadersEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
   // @RequiresPermissions("branchedleaders:branchedLeaders:save")
    public ResponseMessage<BranchedLeadersEO> create(@RequestBody BranchedLeadersEO branchedLeadersEO) throws Exception {
        branchedLeadersEOService.insertSelective(branchedLeadersEO);
        return Result.success(branchedLeadersEO);
    }

    @ApiOperation(value = "|BranchedLeadersEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("branchedleaders:branchedLeaders:update")
    public ResponseMessage<BranchedLeadersEO> update(@RequestBody BranchedLeadersEO branchedLeadersEO) throws Exception {
        branchedLeadersEOService.updateByPrimaryKeySelective(branchedLeadersEO);
        return Result.success(branchedLeadersEO);
    }

    @ApiOperation(value = "|BranchedLeadersEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("branchedleaders:branchedLeaders:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        branchedLeadersEOService.deleteByPrimaryKey(id);
        logger.info("delete from TS_BRANCHED_LEADERS where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|BranchedLeadersEO|通过部门名称删除(new)")
    @DeleteMapping("/delete/{orgName}")
    public ResponseMessage deleteByName(@PathVariable String orgName) throws Exception {
        branchedLeadersEOService.deleteByOrgName(orgName);
        return Result.success();
    }

    @ApiOperation(value = "|BranchedLeadersEO|修改(new)")
    @PutMapping(value="update2" ,consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<BranchedLeadersEO> update2(@RequestBody BranchedLeadersEO branchedLeadersEO) throws Exception {
        branchedLeadersEOService.save(branchedLeadersEO);
        return Result.success(branchedLeadersEO);
    }


    @ApiOperation(value = "|BranchedLeadersEO|新增(new)")
    @PostMapping(value="add",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<BranchedLeadersEO> add(@RequestBody BranchedLeadersEO branchedLeadersEO) throws Exception {
        branchedLeadersEOService.add(branchedLeadersEO);
        return Result.success(branchedLeadersEO);
    }





}
