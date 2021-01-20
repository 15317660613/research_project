package com.adc.da.budget.controller;

import com.adc.da.budget.service.BudgetExportService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * 导出业务项目
 *
 * @author liuzixi
 * date 2019-03-18
 */
@RestController
@RequestMapping("/${restPath}/budget")
@Api("导出业务项目")
@Slf4j
public class BudgetExportController {

    /**
     * 业务逻辑层
     */
    @Autowired
    private BudgetExportService budgetExportService;

    /**
     * 导出业务项目
     * @author liuzixi
     * date 2019-03-18
     */
    @GetMapping("/exportBudget")
    @ApiOperation(value = "导出业务项目")
    public void budgetExport(HttpServletResponse response) {
        OutputStream out = null;
        Workbook workbook = null;
        try {
            workbook = budgetExportService.getBudgetData();
            // 文件名
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode("业务数据.xls"));
            response.setContentType("application/force-download");
            out = response.getOutputStream();
            workbook.write(out);
            out.flush();
        } catch (Exception e) {
            log.error("导出失败", e);
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(out);
        }
    }
}
