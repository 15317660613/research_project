package com.adc.da.budget.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.ContractPartnerEO;
import com.adc.da.budget.page.ContractPartnerEOPage;
import com.adc.da.budget.service.ContractPartnerEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/budget/contractPartner")
@Api(description = "|ContractPartnerEO|")
public class ContractPartnerEOController extends BaseController<ContractPartnerEO>{

    private static final Logger logger = LoggerFactory.getLogger(ContractPartnerEOController.class);

    @Autowired
    private ContractPartnerEOService contractPartnerEOService;

	@ApiOperation(value = "|ContractPartnerEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("budget:contractPartner:page")
    public ResponseMessage<PageInfo<ContractPartnerEO>> page(ContractPartnerEOPage page) throws Exception {
        List<ContractPartnerEO> rows = contractPartnerEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ContractPartnerEO|查询")
    @GetMapping("")
//    @RequiresPermissions("budget:contractPartner:list")
    public ResponseMessage<List<ContractPartnerEO>> list(ContractPartnerEOPage page) throws Exception {
        return Result.success(contractPartnerEOService.queryByList(page));
	}

    @ApiOperation(value = "|ContractPartnerEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("budget:contractPartner:save")
    public ResponseMessage<ContractPartnerEO> create(@RequestBody ContractPartnerEO contractPartnerEO) throws Exception {
        contractPartnerEOService.insertSelective(contractPartnerEO);
        return Result.success(contractPartnerEO);
    }

}
