package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.InvoiceApprovalEO;
import com.adc.da.crm.page.InvoiceApprovalEOPage;
import com.adc.da.crm.service.InvoiceApprovalEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/invoiceApproval")
@Api(description = "|InvoiceApprovalEO|")
public class InvoiceApprovalEOController extends BaseController<InvoiceApprovalEO>{

    private static final Logger logger = LoggerFactory.getLogger(InvoiceApprovalEOController.class);

    @Autowired
    private InvoiceApprovalEOService invoiceApprovalEOService;

	@ApiOperation(value = "|InvoiceApprovalEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:invoiceApproval:page")
    public ResponseMessage<PageInfo<InvoiceApprovalEO>> page(InvoiceApprovalEOPage page) throws Exception {
        List<InvoiceApprovalEO> rows = invoiceApprovalEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|InvoiceApprovalEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:invoiceApproval:list")
    public ResponseMessage<List<InvoiceApprovalEO>> list(InvoiceApprovalEOPage page) throws Exception {
        return Result.success(invoiceApprovalEOService.queryByList(page));
	}

    @ApiOperation(value = "|InvoiceApprovalEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:invoiceApproval:get")
    public ResponseMessage<InvoiceApprovalEO> find(@PathVariable String id) throws Exception {
        return Result.success(invoiceApprovalEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|InvoiceApprovalEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:invoiceApproval:save")
    public ResponseMessage<InvoiceApprovalEO> create(@RequestBody InvoiceApprovalEO invoiceApprovalEO) throws Exception {
        invoiceApprovalEOService.insertSelective(invoiceApprovalEO);
        return Result.success(invoiceApprovalEO);
    }

    @ApiOperation(value = "|InvoiceApprovalEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:invoiceApproval:update")
    public ResponseMessage<InvoiceApprovalEO> update(@RequestBody InvoiceApprovalEO invoiceApprovalEO) throws Exception {
        invoiceApprovalEOService.updateByPrimaryKeySelective(invoiceApprovalEO);
        return Result.success(invoiceApprovalEO);
    }

    @ApiOperation(value = "|InvoiceApprovalEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:invoiceApproval:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        invoiceApprovalEOService.deleteByPrimaryKey(id);
        logger.info("delete from INVOICE_APPROVAL where id = {}", id);
        return Result.success();
    }

}
