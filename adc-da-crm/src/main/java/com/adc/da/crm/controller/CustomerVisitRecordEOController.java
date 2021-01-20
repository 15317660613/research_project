package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.CustomerVisitRecordEO;
import com.adc.da.crm.page.CustomerVisitRecordEOPage;
import com.adc.da.crm.service.CustomerVisitRecordEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/customerVisitRecord")
@Api(description = "|CustomerVisitRecordEO|")
public class CustomerVisitRecordEOController extends BaseController<CustomerVisitRecordEO>{

    private static final Logger logger = LoggerFactory.getLogger(CustomerVisitRecordEOController.class);

    @Autowired
    private CustomerVisitRecordEOService customerVisitRecordEOService;

	@ApiOperation(value = "|CustomerVisitRecordEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:customerVisitRecord:page")
    public ResponseMessage<PageInfo<CustomerVisitRecordEO>> page(CustomerVisitRecordEOPage page) throws Exception {
        List<CustomerVisitRecordEO> rows = customerVisitRecordEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|CustomerVisitRecordEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:customerVisitRecord:list")
    public ResponseMessage<List<CustomerVisitRecordEO>> list(CustomerVisitRecordEOPage page) throws Exception {
        return Result.success(customerVisitRecordEOService.queryByList(page));
	}

    @ApiOperation(value = "|CustomerVisitRecordEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:customerVisitRecord:get")
    public ResponseMessage<CustomerVisitRecordEO> find(@PathVariable String id) throws Exception {
        return Result.success(customerVisitRecordEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|CustomerVisitRecordEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:customerVisitRecord:save")
    public ResponseMessage<CustomerVisitRecordEO> create(@RequestBody CustomerVisitRecordEO customerVisitRecordEO) throws Exception {
        customerVisitRecordEOService.insertSelective(customerVisitRecordEO);
        return Result.success(customerVisitRecordEO);
    }

    @ApiOperation(value = "|CustomerVisitRecordEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:customerVisitRecord:update")
    public ResponseMessage<CustomerVisitRecordEO> update(@RequestBody CustomerVisitRecordEO customerVisitRecordEO) throws Exception {
        customerVisitRecordEOService.updateByPrimaryKeySelective(customerVisitRecordEO);
        return Result.success(customerVisitRecordEO);
    }

    @ApiOperation(value = "|CustomerVisitRecordEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:customerVisitRecord:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        customerVisitRecordEOService.deleteByPrimaryKey(id);
        logger.info("delete from CUSTOMER_VISIT_RECORD where id = {}", id);
        return Result.success();
    }

}
