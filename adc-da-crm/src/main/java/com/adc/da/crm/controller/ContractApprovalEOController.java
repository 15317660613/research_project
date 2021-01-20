package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.ContractApprovalEO;
import com.adc.da.crm.page.ContractApprovalEOPage;
import com.adc.da.crm.service.ContractApprovalEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/contractApproval")
@Api(description = "|ContractApprovalEO|")
public class ContractApprovalEOController extends BaseController<ContractApprovalEO>{

    private static final Logger logger = LoggerFactory.getLogger(ContractApprovalEOController.class);

    @Autowired
    private ContractApprovalEOService contractApprovalEOService;

	@ApiOperation(value = "|ContractApprovalEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:contractApproval:page")
    public ResponseMessage<PageInfo<ContractApprovalEO>> page(ContractApprovalEOPage page) throws Exception {
        List<ContractApprovalEO> rows = contractApprovalEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ContractApprovalEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:contractApproval:list")
    public ResponseMessage<List<ContractApprovalEO>> list(ContractApprovalEOPage page) throws Exception {
        return Result.success(contractApprovalEOService.queryByList(page));
	}

    @ApiOperation(value = "|ContractApprovalEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:contractApproval:get")
    public ResponseMessage<ContractApprovalEO> find(@PathVariable String id) throws Exception {
        return Result.success(contractApprovalEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ContractApprovalEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:contractApproval:save")
    public ResponseMessage<ContractApprovalEO> create(@RequestBody ContractApprovalEO contractApprovalEO) throws Exception {
        contractApprovalEOService.insertSelective(contractApprovalEO);
        return Result.success(contractApprovalEO);
    }

    @ApiOperation(value = "|ContractApprovalEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:contractApproval:update")
    public ResponseMessage<ContractApprovalEO> update(@RequestBody ContractApprovalEO contractApprovalEO) throws Exception {
        contractApprovalEOService.updateByPrimaryKeySelective(contractApprovalEO);
        return Result.success(contractApprovalEO);
    }

    @ApiOperation(value = "|ContractApprovalEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:contractApproval:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        contractApprovalEOService.deleteByPrimaryKey(id);
        logger.info("delete from CONTRACT_APPROVAL where id = {}", id);
        return Result.success();
    }

}
