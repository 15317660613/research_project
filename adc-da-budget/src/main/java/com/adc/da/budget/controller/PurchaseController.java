package com.adc.da.budget.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.Purchase;
import com.adc.da.budget.service.PurchaseService;
import com.adc.da.budget.vo.PurchaseVO;
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
 * @Date: 2019/1/11
 * @Description: /*
 */
@RestController
@RequestMapping("/${restPath}/budget/purchase")
@Api("|Purchase|")
public class PurchaseController extends BaseController<Purchase> {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

    @Autowired
    private PurchaseService purchaseService;

    /**
     * @Description:增删改
     * @auther: chenhaidong
     * @params:
     * @return:
     * @date: 2019/1/11
     */

    @ApiOperation(value = "|Purchase|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("fin:purchase:save")
    public ResponseMessage<Purchase> insert(@RequestBody @Valid PurchaseVO purchaseVO) {
        Purchase purchase = purchaseService.insert(purchaseVO);
        return Result.success(purchase);
    }


    @ApiOperation("|Purchase|删除可批量删除")
    @DeleteMapping("/{ids}")
//    @RequiresPermissions("fin:purchase:delete")
    public ResponseMessage<String> delete(@PathVariable(value = "ids") String ids) {
        return Result.success(purchaseService.deleteBatch(ids));
    }


    @ApiOperation("|Purchase|清空数据")
    @DeleteMapping("/delete/all")
    public ResponseMessage<String> deleteAll() {
        purchaseService.deleteAll();
        return Result.success();
    }

    @ApiOperation("|Purchase|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("fin:purchase:update")
    public ResponseMessage<Purchase> update(@RequestBody @Valid PurchaseVO purchaseVO) {
        return Result.success(purchaseService.update(purchaseVO));
    }

    @ApiOperation("|Purchase|查找")
    @GetMapping("/query/{id}")
//    @RequiresPermissions("fin:purchase:get")
    public ResponseMessage<Purchase> query(@PathVariable("id") String id) {
        return Result.success(purchaseService.querySingle(id));
    }

    @ApiOperation("|Purchase|查找所有信息")
    @GetMapping("/query/all")
//    @RequiresPermissions("fin:purchase:list")
    public ResponseMessage<List<Purchase>> queryAll() {
        return Result.success(purchaseService.queryAll());
    }

    @ApiOperation(value = "|Purchase|分页查询")
    @GetMapping("/page")
    public ResponseMessage<PageDTO> page(@RequestParam Integer page, @RequestParam Integer size) {
        PageDTO pageDTO = purchaseService.queryByPage(page == null ? 1 : page, size == null ? 10 : size);
        return Result.success(pageDTO);
    }


    @ApiOperation(value = "|Purchase|导出")
    @GetMapping("/export")
//    @RequiresPermissions("fin:purchase:export")
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
            workbook = purchaseService.excelExport(params);
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

    @ApiOperation(value = "|Purchase|无验证的Excel单sheet导入")
    @PostMapping("/excelImport")
//    @RequiresPermissions("fin:purchase:import")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        String message = purchaseService.excelImport(is, params);
        return Result.success(message);
    }

    
    
    
}
