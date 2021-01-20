package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.InvoiceInfoEO;
import com.adc.da.crm.page.InvoiceInfoEOPage;
import com.adc.da.crm.service.InvoiceInfoEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/invoiceInfo")
@Api(description = "|InvoiceInfoEO|")
public class InvoiceInfoEOController extends BaseController<InvoiceInfoEO>{

    private static final Logger logger = LoggerFactory.getLogger(InvoiceInfoEOController.class);

    @Autowired
    private InvoiceInfoEOService invoiceInfoEOService;

	@ApiOperation(value = "|InvoiceInfoEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:invoiceInfo:page")
    public ResponseMessage<PageInfo<InvoiceInfoEO>> page(InvoiceInfoEOPage page) throws Exception {
        List<InvoiceInfoEO> rows = invoiceInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|InvoiceInfoEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:invoiceInfo:list")
    public ResponseMessage<List<InvoiceInfoEO>> list(InvoiceInfoEOPage page) throws Exception {
        return Result.success(invoiceInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|InvoiceInfoEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:invoiceInfo:get")
    public ResponseMessage<InvoiceInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(invoiceInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|InvoiceInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:invoiceInfo:save")
    public ResponseMessage<InvoiceInfoEO> create(@RequestBody InvoiceInfoEO invoiceInfoEO) throws Exception {
        invoiceInfoEOService.insertSelective(invoiceInfoEO);
        return Result.success(invoiceInfoEO);
    }

    @ApiOperation(value = "|InvoiceInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:invoiceInfo:update")
    public ResponseMessage<InvoiceInfoEO> update(@RequestBody InvoiceInfoEO invoiceInfoEO) throws Exception {
        invoiceInfoEOService.updateByPrimaryKeySelective(invoiceInfoEO);
        return Result.success(invoiceInfoEO);
    }

    @ApiOperation(value = "|InvoiceInfoEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:invoiceInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        invoiceInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from INVOICE_INFO where id = {}", id);
        return Result.success();
    }

}
