package com.adc.da.finance.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.finance.entity.CashflowManagementEO;
import com.adc.da.finance.page.CashflowManagementEOPage;
import com.adc.da.finance.service.CashflowManagementEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/${restPath}/finance/cashflowManagement")
@Api(description = "|CashflowManagementEO|现金流量管理")
@Slf4j
public class CashflowManagementEOController extends BaseController<CashflowManagementEO>{

    private static final Logger logger = LoggerFactory.getLogger(CashflowManagementEOController.class);

    @Autowired
    private CashflowManagementEOService cashflowManagementEOService;

	@ApiOperation(value = "|CashflowManagementEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("finance:cashflowManagement:page")
    public ResponseMessage<PageInfo<CashflowManagementEO>> page(CashflowManagementEOPage page) throws Exception {
        List<CashflowManagementEO> rows = cashflowManagementEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|CashflowManagementEO|查询")
    @GetMapping("/list")
    //@RequiresPermissions("finance:cashflowManagement:list")
    public ResponseMessage<List<CashflowManagementEO>> list(CashflowManagementEOPage page) throws Exception {
        return Result.success(cashflowManagementEOService.queryByPage(page));
	}

    @ApiOperation(value = "|CashflowManagementEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("finance:cashflowManagement:get")
    public ResponseMessage<CashflowManagementEO> find(@PathVariable String id) throws Exception {
        return Result.success(cashflowManagementEOService.selectByPrimaryKey(id));
    }

   /* @ApiOperation(value = "|CashflowManagementEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("finance:cashflowManagement:save")
    public ResponseMessage<CashflowManagementEO> create(@RequestBody CashflowManagementEO cashflowManagementEO) throws Exception {
        cashflowManagementEOService.insertSelective(cashflowManagementEO);
        return Result.success(cashflowManagementEO);
    }

    @ApiOperation(value = "|CashflowManagementEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("finance:cashflowManagement:update")
    public ResponseMessage<CashflowManagementEO> update(@RequestBody CashflowManagementEO cashflowManagementEO) throws Exception {
        cashflowManagementEOService.updateByPrimaryKeySelective(cashflowManagementEO);
        return Result.success(cashflowManagementEO);
    }

    @ApiOperation(value = "|CashflowManagementEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("finance:cashflowManagement:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        cashflowManagementEOService.deleteByPrimaryKey(id);
        logger.info("delete from F_CASHFLOW_MANAGEMENT where id = {}", id);
        return Result.success();
    }*/

    @ApiOperation(value = "|CashflowManagementEO|导出现金流量")
    @GetMapping("/exportCashflowManagement")
    public ResponseMessage exportCashflowManagement(HttpServletResponse response,String fileName,CashflowManagementEOPage eoPage) throws Exception{
        String finalFileName = "";
        if (StringUtils.isEmpty(fileName)){
            finalFileName = "现金流量导出数据.xlsx";
        }else{
            if (!fileName.contains(".xlsx") || !fileName.contains("xls")){
                finalFileName = fileName + ".xlsx";
            }else{
                finalFileName = fileName;
            }
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(finalFileName));
        response.setContentType("application/force-download");
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        try {
            OutputStream outputStream = response.getOutputStream();
            Workbook workbook = cashflowManagementEOService.exportCashflowManagement(exportParams,eoPage);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

}
