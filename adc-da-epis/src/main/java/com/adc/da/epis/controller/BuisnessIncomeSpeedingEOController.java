package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuisnessIncomeSpeedingEO;
import com.adc.da.epis.page.BuisnessIncomeSpeedingEOPage;
import com.adc.da.epis.service.BuisnessIncomeSpeedingEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buisnessIncomeSpeeding")
@Api(description = "|BuisnessIncomeSpeedingEO|")
public class BuisnessIncomeSpeedingEOController extends BaseController<BuisnessIncomeSpeedingEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuisnessIncomeSpeedingEOController.class);

    @Autowired
    private BuisnessIncomeSpeedingEOService buisnessIncomeSpeedingEOService;

	@ApiOperation(value = "|BuisnessIncomeSpeedingEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buisnessIncomeSpeeding:page")
    public ResponseMessage<PageInfo<BuisnessIncomeSpeedingEO>> page(BuisnessIncomeSpeedingEOPage page) throws Exception {
        List<BuisnessIncomeSpeedingEO> rows = buisnessIncomeSpeedingEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuisnessIncomeSpeedingEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buisnessIncomeSpeeding:list")
    public ResponseMessage<List<BuisnessIncomeSpeedingEO>> list(BuisnessIncomeSpeedingEOPage page) throws Exception {
        return Result.success(buisnessIncomeSpeedingEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuisnessIncomeSpeedingEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buisnessIncomeSpeeding:save")
    public ResponseMessage<BuisnessIncomeSpeedingEO> create(@RequestBody BuisnessIncomeSpeedingEO buisnessIncomeSpeedingEO) throws Exception {
        buisnessIncomeSpeedingEOService.insertSelective(buisnessIncomeSpeedingEO);
        return Result.success(buisnessIncomeSpeedingEO);
    }

}
