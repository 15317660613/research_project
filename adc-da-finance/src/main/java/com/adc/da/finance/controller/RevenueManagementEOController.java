package com.adc.da.finance.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adc.da.finance.dto.RevenueManagementDto;
import com.adc.da.finance.handler.RevenueManagementDtoHandler;
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
import com.adc.da.finance.entity.RevenueManagementEO;
import com.adc.da.finance.page.RevenueManagementEOPage;
import com.adc.da.finance.service.RevenueManagementEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/${restPath}/finance/revenueManagement")
@Api(description = "|RevenueManagementEO|收入数据管理")
@Slf4j
public class RevenueManagementEOController extends BaseController<RevenueManagementEO>{

    private static final Logger logger = LoggerFactory.getLogger(RevenueManagementEOController.class);

    @Autowired
    private RevenueManagementEOService revenueManagementEOService;

	@ApiOperation(value = "|RevenueManagementEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("finance:revenueManagement:page")
    public ResponseMessage<PageInfo<RevenueManagementEO>> page(RevenueManagementEOPage page) throws Exception {
        List<RevenueManagementEO> rows = revenueManagementEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|RevenueManagementEO|查询")
    @GetMapping("/list")
    //@RequiresPermissions("finance:revenueManagement:list")
    public ResponseMessage<List<RevenueManagementEO>> list(RevenueManagementEOPage page) throws Exception {
        return Result.success(revenueManagementEOService.queryByPage(page));
	}

    @ApiOperation(value = "|RevenueManagementEO|详情")
    @GetMapping("/find/{id}")
    //@RequiresPermissions("finance:revenueManagement:get")
    public ResponseMessage<RevenueManagementEO> find(@PathVariable String id) throws Exception {
        return Result.success(revenueManagementEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|RevenueManagementEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/create")
    //@RequiresPermissions("finance:revenueManagement:save")
    public ResponseMessage<RevenueManagementEO> create(@RequestBody RevenueManagementEO revenueManagementEO) throws Exception {
        revenueManagementEOService.create(revenueManagementEO);
        return Result.success(revenueManagementEO);
    }

    @ApiOperation(value = "|RevenueManagementEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/update")
    //@RequiresPermissions("finance:revenueManagement:update")
    public ResponseMessage<RevenueManagementEO> update(@RequestBody RevenueManagementEO revenueManagementEO) throws Exception {
        revenueManagementEOService.update(revenueManagementEO);
        return Result.success(revenueManagementEO);
    }

    @ApiOperation(value = "|RevenueManagementEO|删除")
    @DeleteMapping("/logicDelete/{ids}")
    //@RequiresPermissions("finance:revenueManagement:delete")
    public ResponseMessage delete(@PathVariable List<String> ids) throws Exception {
        revenueManagementEOService.logicDelete(ids);
        return Result.success();
    }

    @ApiOperation(value = "|RevenueManagementEO|导入收入数据")
    @PostMapping("/importRevenueManagement")
    public ResponseMessage importRevenueManagement(@RequestParam("file")MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ImportParams importParams = new ImportParams();
        importParams.setNeedVerfiy(true);
        importParams.setVerifyHandler(new RevenueManagementDtoHandler());
        String errors = revenueManagementEOService.importRevenueManagement(inputStream, importParams);
        if (StringUtils.isNotEmpty(errors)){
            return Result.error(errors);
        }
        return Result.success();
    }

    @ApiOperation(value = "|RevenueManagementEO|导出收入数据")
    @GetMapping("/exportRevenueManagement")
    public ResponseMessage importRevenueManagement(HttpServletResponse response,String fileName,RevenueManagementEOPage eoPage) throws Exception{
        String finalFileName = "";
        if (StringUtils.isEmpty(fileName)){
            finalFileName = "收入数据导出数据.xlsx";
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
            Workbook workbook = revenueManagementEOService.exportRevenueManagement(exportParams,eoPage);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

}
