package com.adc.da.exchangeplan.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.result.ExcelVerifyHanlderErrorResult;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.exchangeplan.entity.ExchangePlanEO;
import com.adc.da.exchangeplan.page.ExchangePlanEOPage;
import com.adc.da.exchangeplan.service.ExchangePlanEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/${restPath}/exchangeplan/exchangePlan")
@Api(description = "|ExchangePlanEO|近期交流计划")
public class ExchangePlanEOController extends BaseController<ExchangePlanEO>{

    private static final Logger logger = LoggerFactory.getLogger(ExchangePlanEOController.class);

    @Autowired
    private ExchangePlanEOService exchangePlanEOService;

	@ApiOperation(value = "|ExchangePlanEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("exchangeplan:exchangePlan:page")
    public ResponseMessage<PageInfo<ExchangePlanEO>> page(ExchangePlanEOPage page) throws Exception {
	    if (StringUtils.isNotEmpty(page.getExt1())) {
            String dateStr = DateUtils.dateToString(DateUtils.getOnlyYMD(new Date()), "yyyy-MM-dd HH:mm:ss");
            page.setEpDate(dateStr);
            page.setEpDateOperator(">=");
            page.setSql_filter(" order by EP_DATE asc");
             page.setExt1(null);
	    }else {
            if (StringUtils.isEmpty(page.getSql_filter())){
                page.setSql_filter(" order by EP_DATE desc");
            }
        }

        List<ExchangePlanEO> rows = exchangePlanEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ExchangePlanEO|近期交流计划")
    @GetMapping("/pad")
    //@RequiresPermissions("exchangeplan:exchangePlan:page")
    public ResponseMessage getCurrentExchangePlanList() throws Exception {
        List<ExchangePlanEO> rows = exchangePlanEOService.getCurrentExchangePlanList();
        return Result.success(rows);
    }


	@ApiOperation(value = "|ExchangePlanEO|查询")
    @GetMapping("/list")
    //@RequiresPermissions("exchangeplan:exchangePlan:list")
    public ResponseMessage<List<ExchangePlanEO>> list(ExchangePlanEOPage page) throws Exception {
        return Result.success(exchangePlanEOService.queryByList(page));
	}

    @ApiOperation(value = "|ExchangePlanEO|详情")
    @GetMapping("/find/{id}")
    //@RequiresPermissions("exchangeplan:exchangePlan:get")
    public ResponseMessage<ExchangePlanEO> find(@PathVariable String id) throws Exception {
        return Result.success(exchangePlanEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ExchangePlanEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/create")
    //@RequiresPermissions("exchangeplan:exchangePlan:save")
    public ResponseMessage<ExchangePlanEO> create(@RequestBody ExchangePlanEO exchangePlanEO) throws Exception {
        exchangePlanEOService.create(exchangePlanEO);
        return Result.success(exchangePlanEO);
    }

    @ApiOperation(value = "|ExchangePlanEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/update")
    //@RequiresPermissions("exchangeplan:exchangePlan:update")
    public ResponseMessage<ExchangePlanEO> update(@RequestBody ExchangePlanEO exchangePlanEO) throws Exception {
        exchangePlanEOService.update(exchangePlanEO);
        return Result.success(exchangePlanEO);
    }

    @ApiOperation(value = "|ExchangePlanEO|删除")
    @DeleteMapping("/delete/{id}")
    //@RequiresPermissions("exchangeplan:exchangePlan:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        exchangePlanEOService.logicDeleteByPrimaryKey(id);
        return Result.success();
    }
    @ApiOperation(value = "|ExchangePlanEO|删除")
    @DeleteMapping("/logicDelete/{ids}")
    //@RequiresPermissions("exchangeplan:exchangePlan:delete")
    public ResponseMessage logicDelete(@PathVariable String[] ids) throws Exception {
	    if (CollectionUtils.isEmpty(ids)){
	        return Result.error("请选择要删除的数据！");
        }
        exchangePlanEOService.logicDeleteByPrimaryKeys(Arrays.asList(ids));
        return Result.success();
    }

    @ApiOperation(value = "|ExchangePlanEO|近期交流计划导入")
    @PostMapping(value = "/excelImportExchangePlan")
    public ResponseMessage excelImportExchangePlan(@RequestParam("file") MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ImportParams importParams = new ImportParams();
        List<ExcelVerifyHanlderErrorResult> errors =
                exchangePlanEOService.excelImportVerify(importParams, inputStream);
        ResponseMessage result = Result.success();
        if (errors!=null && !errors.isEmpty()){
            result.setMessage(errors.toString());
        }
        return result;
    }

}
