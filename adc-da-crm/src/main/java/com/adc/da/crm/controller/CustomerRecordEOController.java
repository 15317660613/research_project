package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.CustomerRecordEO;
import com.adc.da.crm.page.CustomerRecordEOPage;
import com.adc.da.crm.service.CustomerRecordEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/${restPath}/crm/customerRecord")
@Api(description = "|CustomerRecordEO|")
public class CustomerRecordEOController extends BaseController<CustomerRecordEO>{

    private static final Logger logger = LoggerFactory.getLogger(CustomerRecordEOController.class);

    @Autowired
    private CustomerRecordEOService customerRecordEOService;

	@ApiOperation(value = "|CustomerRecordEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("crm:customerRecord:page")
    public ResponseMessage<PageInfo<CustomerRecordEO>> page(CustomerRecordEOPage page) throws Exception {
        List<CustomerRecordEO> rows = customerRecordEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|CustomerRecordEO|查询")
    @GetMapping("")
//    @RequiresPermissions("crm:customerRecord:list")
    public ResponseMessage<List<CustomerRecordEO>> list(CustomerRecordEOPage page) throws Exception {
        return Result.success(customerRecordEOService.queryByList(page));
	}

    @ApiOperation(value = "|CustomerRecordEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("crm:customerRecord:get")
    public ResponseMessage<CustomerRecordEO> find(@PathVariable String id) throws Exception {
        return Result.success(customerRecordEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|CustomerRecordEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("crm:customerRecord:save")
    public ResponseMessage<CustomerRecordEO> create(@RequestBody CustomerRecordEO customerRecordEO) throws Exception {
        customerRecordEOService.insertSelective(customerRecordEO);
        return Result.success(customerRecordEO);
    }

    @ApiOperation(value = "|CustomerRecordEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("crm:customerRecord:update")
    public ResponseMessage<CustomerRecordEO> update(@RequestBody CustomerRecordEO customerRecordEO) throws Exception {
        customerRecordEOService.updateByPrimaryKeySelective(customerRecordEO);
        return Result.success(customerRecordEO);
    }

    @ApiOperation(value = "|CustomerRecordEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("crm:customerRecord:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        customerRecordEOService.deleteByPrimaryKey(id);
        logger.info("delete from CUSTOMER_RECORD where id = {}", id);
        return Result.success();
    }


//    @ApiOperation(value = "|Project|导出")
//    @GetMapping("/export")
//    public ResponseMessage excelImport(HttpServletResponse response, String fileName) {
//        if (StringUtils.isEmpty(fileName)) {
//            fileName = "数据表格.xlsx";
//        } else {
//            if (!fileName.contains(".xlsx")) {
//                fileName += ".xlsx";
//            }
//        }
//        OutputStream os = null;
//        Workbook workbook = null;
//
//        try {
//            response.setHeader("Content-Disposition",
//                    "attachment; filename=" + Encodes.urlEncode(fileName));
//            response.setContentType("application/force-download");
//            ExportParams params = new ExportParams();
//            params.setType(ExcelType.XSSF);
//            workbook = customerRecordEOService.excelExport(params);
//            os = response.getOutputStream();
//            workbook.write(os);
//            os.flush();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            throw new AdcDaBaseException("导出失败，请重试");
//        } finally {
//            IOUtils.closeQuietly(os);
//        }
//        return Result.success();
//    }

    @ApiOperation(value = "|Project|无验证的Excel单sheet导入")
    @PostMapping("/excelImport")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {throw new AdcDaBaseException("请登录！");}
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        params.setHeadRows(2);
        String message = customerRecordEOService.excelImport(is, params);
        return Result.success(message);
    }

}
