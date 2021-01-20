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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.finance.entity.ProfitManagementEO;
import com.adc.da.finance.page.ProfitManagementEOPage;
import com.adc.da.finance.service.ProfitManagementEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/${restPath}/finance/profitManagement")
@Api(description = "|ProfitManagementEO|利润管理")
@Slf4j
public class ProfitManagementEOController extends BaseController<ProfitManagementEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProfitManagementEOController.class);

    @Autowired
    private ProfitManagementEOService profitManagementEOService;

	@ApiOperation(value = "|ProfitManagementEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("finance:profitManagement:page")
    public ResponseMessage<PageInfo<ProfitManagementEO>> page(ProfitManagementEOPage page) throws Exception {
        List<ProfitManagementEO> rows = profitManagementEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ProfitManagementEO|查询")
    @GetMapping("/list")
    //@RequiresPermissions("finance:profitManagement:list")
    public ResponseMessage<List<ProfitManagementEO>> list(ProfitManagementEOPage page) throws Exception {
        return Result.success(profitManagementEOService.queryByPage(page));
	}

    @ApiOperation(value = "|ProfitManagementEO|详情")
    @GetMapping("/find/{id}")
    //@RequiresPermissions("finance:profitManagement:get")
    public ResponseMessage<ProfitManagementEO> find(@PathVariable String id) throws Exception {
        return Result.success(profitManagementEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProfitManagementEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/create")
    //@RequiresPermissions("finance:profitManagement:save")
    public ResponseMessage<ProfitManagementEO> create(@RequestBody ProfitManagementEO profitManagementEO) throws Exception {
        profitManagementEOService.create(profitManagementEO);
        return Result.success(profitManagementEO);
    }

    @ApiOperation(value = "|ProfitManagementEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/update")
    //@RequiresPermissions("finance:profitManagement:update")
    public ResponseMessage<ProfitManagementEO> update(@RequestBody ProfitManagementEO profitManagementEO) throws Exception {
        profitManagementEOService.update(profitManagementEO);
        return Result.success(profitManagementEO);
    }
/*

    @ApiOperation(value = "|ProfitManagementEO|删除")
    @DeleteMapping("/delete/{id}")
    //@RequiresPermissions("finance:profitManagement:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        profitManagementEOService.deleteByPrimaryKey(id);
        logger.info("delete from F_PROFIT_MANAGEMENT where id = {}", id);
        return Result.success();
    }
*/

    @ApiOperation(value = "|ProfitManagementEO|导出利润管理数据")
    @GetMapping("/exportProfitManagement")
    public ResponseMessage exportProfitManagement(HttpServletResponse response,String fileName,ProfitManagementEOPage eoPage) throws Exception{
        String finalFileName = "";
        if (StringUtils.isEmpty(fileName)){
            finalFileName = "利润管理导出数据.xlsx";
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
            Workbook workbook = profitManagementEOService.exportProfitManagement(exportParams,eoPage);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

}
