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
import com.adc.da.business.entity.DeptBudgetEO;
import com.adc.da.business.page.DeptBudgetEOPage;
import com.adc.da.business.service.DeptBudgetEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/${restPath}/business/deptBudget")
@Api(description = "|DeptBudgetEO|")
public class DeptBudgetEOController extends BaseController<DeptBudgetEO>{

    private static final Logger logger = LoggerFactory.getLogger(DeptBudgetEOController.class);

    @Autowired
    private DeptBudgetEOService deptBudgetEOService;

	@ApiOperation(value = "|DeptBudgetEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<PageInfo<DeptBudgetEO>> page(DeptBudgetEOPage page) throws Exception {
        List<DeptBudgetEO> rows = deptBudgetEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|DeptBudgetEO|查询")
    @GetMapping("")
//    @RequiresPermissions("business:deptBudget:list")
    public ResponseMessage<List<DeptBudgetEO>> list(DeptBudgetEOPage page) throws Exception {
        return Result.success(deptBudgetEOService.queryByList(page));
	}

    @ApiOperation(value = "|DeptBudgetEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("business:deptBudget:get")
    public ResponseMessage<DeptBudgetEO> find(@PathVariable String id) throws Exception {
        return Result.success(deptBudgetEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|DeptBudgetEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("business:deptBudget:save")
    public ResponseMessage<DeptBudgetEO> create(@RequestBody DeptBudgetEO deptBudgetEO) throws Exception {
        deptBudgetEOService.insertSelective(deptBudgetEO);
        return Result.success(deptBudgetEO);
    }

    @ApiOperation(value = "|DeptBudgetEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("business:deptBudget:update")
    public ResponseMessage<DeptBudgetEO> update(@RequestBody DeptBudgetEO deptBudgetEO) throws Exception {
        deptBudgetEOService.updateByPrimaryKeySelective(deptBudgetEO);
        return Result.success(deptBudgetEO);
    }

    @ApiOperation(value = "|DeptBudgetEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("business:deptBudget:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        deptBudgetEOService.deleteByPrimaryKey(id);
        logger.info("delete from TS_DEPT_BUDGET where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "Excel单sheet导入")
    @PostMapping("/excelImport")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        return Result.success(deptBudgetEOService.excelImport(is, params));
    }

}
