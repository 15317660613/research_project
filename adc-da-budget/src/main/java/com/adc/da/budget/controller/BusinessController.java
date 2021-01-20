package com.adc.da.budget.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.Business;
import com.adc.da.budget.service.BuisnessService;
import com.adc.da.budget.vo.BusinessVO;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/19 13:09
 * @Description: /*
 */
@RestController
@RequestMapping("/${restPath}/budget/business")
@Api("|business|")
public class BusinessController extends BaseController<Business> {
    private static final Logger logger = LoggerFactory.getLogger(BusinessController.class);

    @Autowired
    private BuisnessService buisnessService;

    /**
     * @Description:增删改
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 13:10
     */

    @ApiOperation(value = "|Business|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("fin:business:save")
    public ResponseMessage<Business> insert(@RequestBody @Valid BusinessVO business, BindingResult bindingResult) {
        Business bui = buisnessService.insert(business);
        return Result.success(bui);
    }


    @ApiOperation("删除可批量删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("fin:business:delete")
    public ResponseMessage<String> delete(@PathVariable(value = "ids") String ids) {
        if(buisnessService.deleteBatch(ids)){
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    @ApiOperation("修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("fin:business:update")
    public ResponseMessage<Business> update(@RequestBody @Valid BusinessVO business, BindingResult bindingResult) {
        return Result.success(buisnessService.update(business));
    }

    @ApiOperation("查找")
    @GetMapping("/query/{id}")
    @RequiresPermissions("fin:business:query")
    public ResponseMessage<Business> query(@PathVariable("id") String id) {
        return Result.success(buisnessService.querySingle(id));
    }


    @ApiOperation("查找所有信息")
    @GetMapping("/query/all")
    @RequiresPermissions("fin:business:queryAll")
    public ResponseMessage<List<Business>> queryAll() {
        return Result.success(buisnessService.queryAll());
    }


    @ApiOperation(value = "导出")
    @GetMapping("/export")
    public ResponseMessage excelImport(HttpServletResponse response, String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "数据表格.xlsx";
        } else {
            if (!fileName.contains(".xlsx")) {
                fileName += ".xlsx";
            }
        }
        OutputStream os = null;
        Workbook workbook = null;

        try {
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            workbook = buisnessService.excelExport(params);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(os);
        }
        return Result.success();
    }

    @ApiOperation(value = "buisnessService无验证的Excel单sheet导入")
    @PostMapping("/excelImport")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        buisnessService.excelImport(is, params);
        return Result.success();
    }

}
