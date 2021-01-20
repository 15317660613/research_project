package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.BTravelProjectVisitRecordEO;
import com.adc.da.crm.page.BTravelProjectVisitRecordEOPage;
import com.adc.da.crm.service.BTravelProjectVisitRecordEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/bTravelProjectVisitRecord")
@Api(description = "|BTravelProjectVisitRecordEO|")
public class BTravelProjectVisitRecordEOController extends BaseController<BTravelProjectVisitRecordEO>{

    private static final Logger logger = LoggerFactory.getLogger(BTravelProjectVisitRecordEOController.class);

    @Autowired
    private BTravelProjectVisitRecordEOService bTravelProjectVisitRecordEOService;

	@ApiOperation(value = "|BTravelProjectVisitRecordEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:bTravelProjectVisitRecord:page")
    public ResponseMessage<PageInfo<BTravelProjectVisitRecordEO>> page(BTravelProjectVisitRecordEOPage page) throws Exception {
        List<BTravelProjectVisitRecordEO> rows = bTravelProjectVisitRecordEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BTravelProjectVisitRecordEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:bTravelProjectVisitRecord:list")
    public ResponseMessage<List<BTravelProjectVisitRecordEO>> list(BTravelProjectVisitRecordEOPage page) throws Exception {
        return Result.success(bTravelProjectVisitRecordEOService.queryByList(page));
	}

    @ApiOperation(value = "|BTravelProjectVisitRecordEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:bTravelProjectVisitRecord:get")
    public ResponseMessage<BTravelProjectVisitRecordEO> find(@PathVariable String id) throws Exception {
        return Result.success(bTravelProjectVisitRecordEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BTravelProjectVisitRecordEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:bTravelProjectVisitRecord:save")
    public ResponseMessage<BTravelProjectVisitRecordEO> create(@RequestBody BTravelProjectVisitRecordEO bTravelProjectVisitRecordEO) throws Exception {
        bTravelProjectVisitRecordEOService.insertSelective(bTravelProjectVisitRecordEO);
        return Result.success(bTravelProjectVisitRecordEO);
    }

    @ApiOperation(value = "|BTravelProjectVisitRecordEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:bTravelProjectVisitRecord:update")
    public ResponseMessage<BTravelProjectVisitRecordEO> update(@RequestBody BTravelProjectVisitRecordEO bTravelProjectVisitRecordEO) throws Exception {
        bTravelProjectVisitRecordEOService.updateByPrimaryKeySelective(bTravelProjectVisitRecordEO);
        return Result.success(bTravelProjectVisitRecordEO);
    }

    @ApiOperation(value = "|BTravelProjectVisitRecordEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:bTravelProjectVisitRecord:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        bTravelProjectVisitRecordEOService.deleteByPrimaryKey(id);
        logger.info("delete from B_TRAVEL_PROJECT_VISIT_RECORD where id = {}", id);
        return Result.success();
    }

}
