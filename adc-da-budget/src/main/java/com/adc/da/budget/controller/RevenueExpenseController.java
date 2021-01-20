package com.adc.da.budget.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.Business;
import com.adc.da.budget.entity.RevenueExpense;
import com.adc.da.budget.service.RevenueExpenseService;
import com.adc.da.budget.vo.RevenueVO;
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
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/19 13:09
 * @Description: /*
 */
@RestController
@RequestMapping("/${restPath}/budget/revenueExpense")
@Api("|revenueExpense|")
public class RevenueExpenseController extends BaseController<Business> {
    private static final Logger logger = LoggerFactory.getLogger(RevenueExpenseController.class);

    @Autowired
    private RevenueExpenseService revenueExpenseService;

    /**
     * @Description:增删改
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 13:10
     */

    @ApiOperation("新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<RevenueExpense> insert(@RequestBody @Valid RevenueVO revenueExpenseVO,
                                                    BindingResult bindingResult) {
        RevenueExpense revenueExpense = revenueExpenseService.insert(revenueExpenseVO);
        return Result.success(revenueExpense);
    }


    @ApiOperation("删除可批量删除")
    @DeleteMapping("/{ids}")
    public ResponseMessage<String> delete(@PathVariable(value = "ids") String ids) {
        return Result.success(revenueExpenseService.deleteBatch(ids));
    }

    @ApiOperation("清空数据")
    @DeleteMapping("/delete/all")
    public ResponseMessage deleteAll() {
        revenueExpenseService.deleteAll();
        return Result.success();
    }

    @ApiOperation("修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<RevenueExpense> update(@RequestBody @Valid RevenueVO revenueExpenseVO,
                                                        BindingResult bindingResult) {
        return Result.success(revenueExpenseService.update(revenueExpenseVO));
    }

    @ApiOperation("查找")
    @GetMapping("/query/{id}")
    public ResponseMessage<RevenueExpense> query(@PathVariable("id") String id) {
        return Result.success(revenueExpenseService.querySingle(id));
    }


    @ApiOperation("查找所有")
    @GetMapping("/query/all")
    public ResponseMessage<List<RevenueExpense>> queryAll() {
        return Result.success(revenueExpenseService.queryAll());
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public ResponseMessage<PageDTO> page(@RequestParam Integer page, @RequestParam Integer size) {
        return Result.success(revenueExpenseService.queryByPage(page == null ? 1 : page, size == null ? 10 : size));
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
            workbook = revenueExpenseService.excelExport(params);
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
        revenueExpenseService.excelImport(is, params);
        return Result.success();
    }

}
