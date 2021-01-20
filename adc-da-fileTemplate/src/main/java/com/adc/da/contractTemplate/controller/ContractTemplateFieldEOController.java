package com.adc.da.contractTemplate.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.contractTemplate.entity.ContractTemplateFieldEO;
import com.adc.da.contractTemplate.page.ContractTemplateFieldEOPage;
import com.adc.da.contractTemplate.service.ContractTemplateFieldEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/contractTemplate/contractTemplateField")
@Api(description = "|ContractTemplateFieldEO|")
public class ContractTemplateFieldEOController extends BaseController<ContractTemplateFieldEO>{

    private static final Logger logger = LoggerFactory.getLogger(ContractTemplateFieldEOController.class);

    @Autowired
    private ContractTemplateFieldEOService contractTemplateFieldEOService;

	@ApiOperation(value = "|ContractTemplateFieldEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("contractTemplate:contractTemplateField:page")
    public ResponseMessage<PageInfo<ContractTemplateFieldEO>> page(ContractTemplateFieldEOPage page) throws Exception {
        List<ContractTemplateFieldEO> rows = contractTemplateFieldEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ContractTemplateFieldEO|查询")
    @GetMapping("")
//    @RequiresPermissions("contractTemplate:contractTemplateField:list")
    public ResponseMessage<List<ContractTemplateFieldEO>> list(ContractTemplateFieldEOPage page) throws Exception {
        return Result.success(contractTemplateFieldEOService.queryByList(page));
	}

    @ApiOperation(value = "|ContractTemplateFieldEO|详情")
    @GetMapping("/{fileId}")
//    @RequiresPermissions("contractTemplate:contractTemplateField:get")
    public ResponseMessage<ContractTemplateFieldEO> find(@PathVariable String fileId) throws Exception {
        return Result.success(contractTemplateFieldEOService.selectByPrimaryKey(fileId));
    }

    @ApiOperation(value = "|ContractTemplateFieldEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("contractTemplate:contractTemplateField:save")
    public ResponseMessage<ContractTemplateFieldEO> create(@RequestBody ContractTemplateFieldEO contractTemplateFieldEO) throws Exception {
        contractTemplateFieldEO.setFileId(UUID.randomUUID10());
        contractTemplateFieldEOService.insertSelective(contractTemplateFieldEO);
        return Result.success(contractTemplateFieldEO);
    }

    @ApiOperation(value = "|ContractTemplateFieldEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("contractTemplate:contractTemplateField:update")
    public ResponseMessage<ContractTemplateFieldEO> update(@RequestBody ContractTemplateFieldEO contractTemplateFieldEO) throws Exception {
        contractTemplateFieldEOService.updateByPrimaryKeySelective(contractTemplateFieldEO);
        return Result.success(contractTemplateFieldEO);
    }

    @ApiOperation(value = "|ContractTemplateFieldEO|删除")
    @DeleteMapping("/{fileId}")
//    @RequiresPermissions("contractTemplate:contractTemplateField:delete")
    public ResponseMessage delete(@PathVariable String fileId) throws Exception {
        contractTemplateFieldEOService.deleteByPrimaryKey(fileId);
        logger.info("delete from CONTRACT_TEMPLATE_FIELD where fileId = {}", fileId);
        return Result.success();
    }

}
