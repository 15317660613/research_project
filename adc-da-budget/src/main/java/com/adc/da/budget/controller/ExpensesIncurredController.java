package com.adc.da.budget.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.Business;
import com.adc.da.budget.entity.ExpensesIncurred;
import com.adc.da.budget.service.ExpensesIncurredService;
import com.adc.da.budget.vo.ExpensesIncurredVO;
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
 * @Auther: chenhaidong
 * @Date: 2018/11/20
 * @Description: /*
 */
@RestController
@RequestMapping("/${restPath}/budget/expensesIncurred")
@Api("|expensesIncurred|")
public class ExpensesIncurredController extends BaseController<Business> {
    private static final Logger logger = LoggerFactory.getLogger(ExpensesIncurredController.class);

    @Autowired
    private ExpensesIncurredService expensesIncurredService;

    /**
     * @Description:增删改
     * @auther: chenhaidong
     * @params:
     * @return:
     * @date: 2018/11/19 13:10
     */

    @ApiOperation("新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<ExpensesIncurred> insert(@RequestBody @Valid ExpensesIncurredVO expensesIncurredVO,
                                                    BindingResult bindingResult) {
        ExpensesIncurred expensesIncurred = expensesIncurredService.insert(expensesIncurredVO);
        return Result.success(expensesIncurred);
    }


    @ApiOperation("删除可批量删除")
    @DeleteMapping("/{ids}")
    public ResponseMessage<String> delete(@PathVariable(value = "ids") String ids) {
        return Result.success(expensesIncurredService.deleteBatch(ids));
    }


    @ApiOperation("清空数据")
    @DeleteMapping("/delete/all")
    public ResponseMessage deleteAll() {
        expensesIncurredService.deleteAll();
        return Result.success();
    }

    @ApiOperation("修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<ExpensesIncurred> update(@RequestBody @Valid ExpensesIncurredVO expensesIncurred,
                                                    BindingResult bindingResult) {
        return Result.success(expensesIncurredService.update(expensesIncurred));
    }

    @ApiOperation("查找")
    @GetMapping("/query/{id}")
    public ResponseMessage<ExpensesIncurred> query(@PathVariable("id") String id) {
        return Result.success(expensesIncurredService.querySingle(id));
    }


    @ApiOperation("查找所有信息")
    @GetMapping("/query/all")
    public ResponseMessage<List<ExpensesIncurred>> queryAll() {
        return Result.success(expensesIncurredService.queryAll());
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public ResponseMessage<PageDTO> page(@RequestParam Integer page, @RequestParam Integer size) throws Exception {
        return Result.success(expensesIncurredService.queryByPage(page == null ? 1 : page, size == null ? 10 : size));
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
            workbook = expensesIncurredService.excelExport(params);
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
        expensesIncurredService.excelImport(is, params);
        return Result.success();
    }

}
