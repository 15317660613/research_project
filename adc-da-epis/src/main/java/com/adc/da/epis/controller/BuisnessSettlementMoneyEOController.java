package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessSettlementMoneyEO;
import com.adc.da.epis.page.BuisnessSettlementMoneyEOPage;
import com.adc.da.epis.service.BuisnessSettlementMoneyEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessSettlementMoney")
@Api(description = "|BuisnessSettlementMoneyEO|")
public class BuisnessSettlementMoneyEOController extends BaseController<BuisnessSettlementMoneyEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessSettlementMoneyEOController.class);

    @Autowired
    private BuisnessSettlementMoneyEOService buisnessSettlementMoneyEOService;

	@ApiOperation(value = "|BuisnessSettlementMoneyEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessSettlementMoney:page")
    public ResponseMessage<PageInfo<BuisnessSettlementMoneyEO>> page(BuisnessSettlementMoneyEOPage page) throws Exception {
        List<BuisnessSettlementMoneyEO> rows = buisnessSettlementMoneyEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessSettlementMoneyEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessSettlementMoney:list")
    public ResponseMessage<List<BuisnessSettlementMoneyEO>> list(BuisnessSettlementMoneyEOPage page) throws Exception {
        return Result.success(buisnessSettlementMoneyEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessSettlementMoneyEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("epis:buisnessSettlementMoney:get")
    public ResponseMessage<BuisnessSettlementMoneyEO> find(@PathVariable String id) throws Exception {
        return Result.success(buisnessSettlementMoneyEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BuisnessSettlementMoneyEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessSettlementMoney:save")
    public ResponseMessage<BuisnessSettlementMoneyEO> create(@RequestBody BuisnessSettlementMoneyEO buisnessSettlementMoneyEO) throws Exception {
        buisnessSettlementMoneyEOService.insertSelective(buisnessSettlementMoneyEO);
        return Result.success(buisnessSettlementMoneyEO);
    }

    @ApiOperation(value = "|BuisnessSettlementMoneyEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessSettlementMoney:update")
    public ResponseMessage<BuisnessSettlementMoneyEO> update(@RequestBody BuisnessSettlementMoneyEO buisnessSettlementMoneyEO) throws Exception {
        buisnessSettlementMoneyEOService.updateByPrimaryKeySelective(buisnessSettlementMoneyEO);
        return Result.success(buisnessSettlementMoneyEO);
    }

    @ApiOperation(value = "|BuisnessSettlementMoneyEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("epis:buisnessSettlementMoney:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        buisnessSettlementMoneyEOService.deleteByPrimaryKey(id);
        logger.info("delete from BUISNESS_SETTLEMENT_MONEY where id = {}", id);
        return Result.success();
    }

}
