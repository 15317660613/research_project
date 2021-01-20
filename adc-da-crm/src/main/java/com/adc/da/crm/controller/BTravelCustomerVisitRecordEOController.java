package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.BTravelCustomerVisitRecordEO;
import com.adc.da.crm.page.BTravelCustomerVisitRecordEOPage;
import com.adc.da.crm.service.BTravelCustomerVisitRecordEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/bTravelCustomerVisitRecord")
@Api(description = "|BTravelCustomerVisitRecordEO|")
public class BTravelCustomerVisitRecordEOController extends BaseController<BTravelCustomerVisitRecordEO>{

    private static final Logger logger = LoggerFactory.getLogger(BTravelCustomerVisitRecordEOController.class);

    @Autowired
    private BTravelCustomerVisitRecordEOService bTravelCustomerVisitRecordEOService;

	@ApiOperation(value = "|BTravelCustomerVisitRecordEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:bTravelCustomerVisitRecord:page")
    public ResponseMessage<PageInfo<BTravelCustomerVisitRecordEO>> page(BTravelCustomerVisitRecordEOPage page) throws Exception {
        List<BTravelCustomerVisitRecordEO> rows = bTravelCustomerVisitRecordEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BTravelCustomerVisitRecordEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:bTravelCustomerVisitRecord:list")
    public ResponseMessage<List<BTravelCustomerVisitRecordEO>> list(BTravelCustomerVisitRecordEOPage page) throws Exception {
        return Result.success(bTravelCustomerVisitRecordEOService.queryByList(page));
	}

    @ApiOperation(value = "|BTravelCustomerVisitRecordEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:bTravelCustomerVisitRecord:get")
    public ResponseMessage<BTravelCustomerVisitRecordEO> find(@PathVariable String id) throws Exception {
        return Result.success(bTravelCustomerVisitRecordEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BTravelCustomerVisitRecordEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:bTravelCustomerVisitRecord:save")
    public ResponseMessage<BTravelCustomerVisitRecordEO> create(@RequestBody BTravelCustomerVisitRecordEO bTravelCustomerVisitRecordEO) throws Exception {
        bTravelCustomerVisitRecordEOService.insertSelective(bTravelCustomerVisitRecordEO);
        return Result.success(bTravelCustomerVisitRecordEO);
    }

    @ApiOperation(value = "|BTravelCustomerVisitRecordEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:bTravelCustomerVisitRecord:update")
    public ResponseMessage<BTravelCustomerVisitRecordEO> update(@RequestBody BTravelCustomerVisitRecordEO bTravelCustomerVisitRecordEO) throws Exception {
        bTravelCustomerVisitRecordEOService.updateByPrimaryKeySelective(bTravelCustomerVisitRecordEO);
        return Result.success(bTravelCustomerVisitRecordEO);
    }

    @ApiOperation(value = "|BTravelCustomerVisitRecordEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:bTravelCustomerVisitRecord:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        bTravelCustomerVisitRecordEOService.deleteByPrimaryKey(id);
        logger.info("delete from B_TRAVEL_CUSTOMER_VISIT_RECORD where id = {}", id);
        return Result.success();
    }

}
