package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.CustomerContactEO;
import com.adc.da.crm.page.CustomerContactEOPage;
import com.adc.da.crm.service.CustomerContactEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/customerContact")
@Api(description = "|CustomerContactEO|")
public class CustomerContactEOController extends BaseController<CustomerContactEO>{

    private static final Logger logger = LoggerFactory.getLogger(CustomerContactEOController.class);

    @Autowired
    private CustomerContactEOService customerContactEOService;

	@ApiOperation(value = "|CustomerContactEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("crm:customerContact:page")
    public ResponseMessage<PageInfo<CustomerContactEO>> page(CustomerContactEOPage page) throws Exception {
        List<CustomerContactEO> rows = customerContactEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|CustomerContactEO|查询")
    @GetMapping("")
//    @RequiresPermissions("crm:customerContact:list")
    public ResponseMessage<List<CustomerContactEO>> list(CustomerContactEOPage page) throws Exception {
        return Result.success(customerContactEOService.queryByList(page));
	}

    @ApiOperation(value = "|CustomerContactEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("crm:customerContact:get")
    public ResponseMessage<CustomerContactEO> find(@PathVariable String id) throws Exception {
        return Result.success(customerContactEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|CustomerContactEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("crm:customerContact:save")
    public ResponseMessage<CustomerContactEO> create(@RequestBody CustomerContactEO customerContactEO) throws Exception {
        customerContactEOService.insertSelective(customerContactEO);
        return Result.success(customerContactEO);
    }

    @ApiOperation(value = "|CustomerContactEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("crm:customerContact:update")
    public ResponseMessage<CustomerContactEO> update(@RequestBody CustomerContactEO customerContactEO) throws Exception {
        customerContactEOService.updateByPrimaryKeySelective(customerContactEO);
        return Result.success(customerContactEO);
    }

    @ApiOperation(value = "|CustomerContactEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("crm:customerContact:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        customerContactEOService.deleteByPrimaryKey(id);
        logger.info("delete from CUSTOMER_CONTACT where id = {}", id);
        return Result.success();
    }

}
