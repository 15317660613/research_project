package com.adc.da.finance.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adc.da.finance.dto.ReceivablesManagementDto;
import com.adc.da.finance.handler.ReceivablesManagementDtoHandler;
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
import com.adc.da.finance.entity.ReceivablesManagementEO;
import com.adc.da.finance.page.ReceivablesManagementEOPage;
import com.adc.da.finance.service.ReceivablesManagementEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/${restPath}/finance/receivablesManagement")
@Api(description = "|ReceivablesManagementEO|应收账款数据管理")
@Slf4j
public class ReceivablesManagementEOController extends BaseController<ReceivablesManagementEO>{

    private static final Logger logger = LoggerFactory.getLogger(ReceivablesManagementEOController.class);

    @Autowired
    private ReceivablesManagementEOService receivablesManagementEOService;

	@ApiOperation(value = "|ReceivablesManagementEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("finance:receivablesManagement:page")
    public ResponseMessage<PageInfo<ReceivablesManagementEO>> page(ReceivablesManagementEOPage page) throws Exception {
        List<ReceivablesManagementEO> rows = receivablesManagementEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ReceivablesManagementEO|查询")
    @GetMapping("/list")
    //@RequiresPermissions("finance:receivablesManagement:list")
    public ResponseMessage<List<ReceivablesManagementEO>> list(ReceivablesManagementEOPage page) throws Exception {
        return Result.success(receivablesManagementEOService.queryByPage(page));
	}

    @ApiOperation(value = "|ReceivablesManagementEO|详情")
    @GetMapping("/find/{id}")
    //@RequiresPermissions("finance:receivablesManagement:get")
    public ResponseMessage<ReceivablesManagementEO> find(@PathVariable String id) throws Exception {
        return Result.success(receivablesManagementEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ReceivablesManagementEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/create")
    //@RequiresPermissions("finance:receivablesManagement:save")
    public ResponseMessage<ReceivablesManagementEO> create(@RequestBody ReceivablesManagementEO receivablesManagementEO) throws Exception {
        receivablesManagementEOService.create(receivablesManagementEO);
        return Result.success(receivablesManagementEO);
    }

    @ApiOperation(value = "|ReceivablesManagementEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/update")
    //@RequiresPermissions("finance:receivablesManagement:update")
    public ResponseMessage<ReceivablesManagementEO> update(@RequestBody ReceivablesManagementEO receivablesManagementEO) throws Exception {
        receivablesManagementEOService.update(receivablesManagementEO);
        return Result.success(receivablesManagementEO);
    }

    @ApiOperation(value = "|ReceivablesManagementEO|单条/批量逻辑删除删除")
    @DeleteMapping("/logicDelete/{ids}")
    //@RequiresPermissions("finance:receivablesManagement:delete")
    public ResponseMessage logicDelete(@PathVariable List<String> ids) throws Exception {
        receivablesManagementEOService.logicDelete(ids);
        return Result.success();
    }

    @ApiOperation(value = "|ReceivablesManagementEO|导入应收账款数据")
    @PostMapping("/importReceivablesManagement")
    public ResponseMessage importReceivablesManagement(@RequestParam("file")MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ImportParams importParams = new ImportParams();
        importParams.setNeedVerfiy(true);
        importParams.setVerifyHandler(new ReceivablesManagementDtoHandler());
        String errors = receivablesManagementEOService.importReceivablesManagement(inputStream, importParams);
        if (StringUtils.isNotEmpty(errors)){
            return Result.error(errors);
        }
        return Result.success();
    }

    @ApiOperation(value = "|ReceivablesManagementEO|导出应收账款数据")
    @GetMapping("/exportReceivablesManagement")
    public ResponseMessage exportReceivablesManagement(HttpServletResponse response,String fileName,ReceivablesManagementEOPage eoPage) throws Exception{
        String finalFileName = "";
        if (StringUtils.isEmpty(fileName)){
            finalFileName = "应收账款数据导出数据.xlsx";
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
            Workbook workbook = receivablesManagementEOService.exportReceivablesManagement(exportParams,eoPage);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

}
