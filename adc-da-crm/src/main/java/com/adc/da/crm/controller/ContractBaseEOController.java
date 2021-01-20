package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.ContractBaseEO;
import com.adc.da.crm.page.ContractBaseEOPage;
import com.adc.da.crm.service.ContractBaseEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/contractBase")
@Api(description = "|ContractBaseEO|")
public class ContractBaseEOController extends BaseController<ContractBaseEO>{

    private static final Logger logger = LoggerFactory.getLogger(ContractBaseEOController.class);

    @Autowired
    private ContractBaseEOService contractBaseEOService;

	@ApiOperation(value = "|ContractBaseEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:contractBase:page")
    public ResponseMessage<PageInfo<ContractBaseEO>> page(ContractBaseEOPage page) throws Exception {
        List<ContractBaseEO> rows = contractBaseEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ContractBaseEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:contractBase:list")
    public ResponseMessage<List<ContractBaseEO>> list(ContractBaseEOPage page) throws Exception {
        return Result.success(contractBaseEOService.queryByList(page));
	}

    @ApiOperation(value = "|ContractBaseEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:contractBase:get")
    public ResponseMessage<ContractBaseEO> find(@PathVariable String id) throws Exception {
        return Result.success(contractBaseEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ContractBaseEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:contractBase:save")
    public ResponseMessage<ContractBaseEO> create(@RequestBody ContractBaseEO contractBaseEO) throws Exception {
        contractBaseEOService.insertSelective(contractBaseEO);
        return Result.success(contractBaseEO);
    }

    @ApiOperation(value = "|ContractBaseEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:contractBase:update")
    public ResponseMessage<ContractBaseEO> update(@RequestBody ContractBaseEO contractBaseEO) throws Exception {
        contractBaseEOService.updateByPrimaryKeySelective(contractBaseEO);
        return Result.success(contractBaseEO);
    }

    @ApiOperation(value = "|ContractBaseEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:contractBase:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        contractBaseEOService.deleteByPrimaryKey(id);
        logger.info("delete from CONTRACT_BASE where id = {}", id);
        return Result.success();
    }

}
