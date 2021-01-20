package com.adc.da.business.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.adc.da.excel.poi.excel.entity.ImportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.business.entity.OperatingBudgetEO;
import com.adc.da.business.page.OperatingBudgetEOPage;
import com.adc.da.business.service.OperatingBudgetEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/${restPath}/business/operatingBudget")
@Api(description = "|OperatingBudgetEO|")
public class OperatingBudgetEOController extends BaseController<OperatingBudgetEO>{

    private static final Logger logger = LoggerFactory.getLogger(OperatingBudgetEOController.class);

    @Autowired
    private OperatingBudgetEOService operatingBudgetEOService;


    @ApiOperation(value = "|OperatingBudgetEO|查询有数据的年份")
    @GetMapping("/queryAllYear")
    public ResponseMessage<List<String>> queryAllYear(){
        return Result.success( operatingBudgetEOService.queryAllYear() );
    }

	@ApiOperation(value = "|OperatingBudgetEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("business:operatingBudget:page")
    public ResponseMessage<PageInfo<OperatingBudgetEO>> page(OperatingBudgetEOPage page) throws Exception {
        List<OperatingBudgetEO> rows = operatingBudgetEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|OperatingBudgetEO|查询")
    @GetMapping("")
//    @RequiresPermissions("business:operatingBudget:list")
    public ResponseMessage<List<OperatingBudgetEO>> list(OperatingBudgetEOPage page) throws Exception {
        return Result.success(operatingBudgetEOService.queryByList(page));
	}

    @ApiOperation(value = "|OperatingBudgetEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("business:operatingBudget:get")
    public ResponseMessage<OperatingBudgetEO> find(@PathVariable String id) throws Exception {
        return Result.success(operatingBudgetEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|OperatingBudgetEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("business:operatingBudget:save")
    public ResponseMessage<OperatingBudgetEO> create(@RequestBody OperatingBudgetEO operatingBudgetEO) throws Exception {
        operatingBudgetEOService.insertSelective(operatingBudgetEO);
        return Result.success(operatingBudgetEO);
    }

    @ApiOperation(value = "|OperatingBudgetEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("business:operatingBudget:update")
    public ResponseMessage<OperatingBudgetEO> update(@RequestBody OperatingBudgetEO operatingBudgetEO) throws Exception {
        operatingBudgetEOService.updateByPrimaryKeySelective(operatingBudgetEO);
        return Result.success(operatingBudgetEO);
    }

    @ApiOperation(value = "|OperatingBudgetEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("business:operatingBudget:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        operatingBudgetEOService.deleteByPrimaryKey(id);
        logger.info("delete from TS_OPERATING_BUDGET where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "Excel单sheet导入")
    @PostMapping("/excelImport")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        return Result.success(operatingBudgetEOService.excelImport(is, params));
    }

    @ApiOperation(value = "根据业务名称和年份获取业务下当年每月的收入统计")
    @GetMapping("/getBusinessYearData/{bizName}/{year}")
    public ResponseMessage<String[]> getBusinessYearData(@PathVariable("bizName") String bizName ,@PathVariable("year") String year) {
        return Result.success(operatingBudgetEOService.getBusinessYearData(bizName,year));
    }

}
