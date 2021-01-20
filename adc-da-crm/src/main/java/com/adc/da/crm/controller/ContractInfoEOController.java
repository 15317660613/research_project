package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.ContractInfoEO;
import com.adc.da.crm.page.ContractInfoEOPage;
import com.adc.da.crm.service.ContractInfoEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/contractInfo")
@Api(description = "|ContractInfoEO|")
public class ContractInfoEOController extends BaseController<ContractInfoEO>{

    private static final Logger logger = LoggerFactory.getLogger(ContractInfoEOController.class);

    @Autowired
    private ContractInfoEOService contractInfoEOService;

	@ApiOperation(value = "|ContractInfoEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:contractInfo:page")
    public ResponseMessage<PageInfo<ContractInfoEO>> page(ContractInfoEOPage page) throws Exception {
        List<ContractInfoEO> rows = contractInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ContractInfoEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:contractInfo:list")
    public ResponseMessage<List<ContractInfoEO>> list(ContractInfoEOPage page) throws Exception {
        return Result.success(contractInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|ContractInfoEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:contractInfo:get")
    public ResponseMessage<ContractInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(contractInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ContractInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:contractInfo:save")
    public ResponseMessage<ContractInfoEO> create(@RequestBody ContractInfoEO contractInfoEO) throws Exception {
        contractInfoEOService.insertSelective(contractInfoEO);
        return Result.success(contractInfoEO);
    }

    @ApiOperation(value = "|ContractInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:contractInfo:update")
    public ResponseMessage<ContractInfoEO> update(@RequestBody ContractInfoEO contractInfoEO) throws Exception {
        contractInfoEOService.updateByPrimaryKeySelective(contractInfoEO);
        return Result.success(contractInfoEO);
    }

    @ApiOperation(value = "|ContractInfoEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:contractInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        contractInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from CONTRACT_INFO where id = {}", id);
        return Result.success();
    }

}
