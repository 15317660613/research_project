package com.adc.da.export.controller;

import com.adc.da.budget.service.DailyExportService;
 import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.export.service.ProjectExportService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

/**
 * 日报导出
 *
 * @author liuzixi
 * @date 2019-03-09
 */
@RestController
@RequestMapping("/${restPath}/Daily")
@Api(tags = "|日报-项目-导出|")
@Slf4j
public class DailyExportController {

    /**
     * 业务逻辑层
     */
    @Autowired
    private DailyExportService dailyExportService;

    @Autowired
    private ProjectExportService projectExportService;

    /**
     * |Daily|日报数据导出
     *
     * Long a = 1554048000000L
     * Long b = 1561910400000L
     *
     * @param deptId
     * @return
     */
    @GetMapping("/dataExport")
//    @RequiresPermissions("fin:daily:dataexport")
    @ApiOperation(value = "|Daily|日报数据导出", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage exportDailyData(String deptId, Long startLong, Long endLong) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
            .getResponse();
        OutputStream out = null;
        Workbook workbook = null;
        if (null == startLong || null == endLong) {
            throw new AdcDaBaseException("请指定时间范围");
        }
        try {
            workbook = dailyExportService.getDailyWorkbook(deptId, startLong, endLong);
            // 文件名
            response.setHeader(
                "Content-Disposition",
                "attachment; filename=" + Encodes.urlEncode("日报数据.xls"));
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
        return Result.success("导出成功");
    }

    @GetMapping("/dataExportWithbusinessNO")
//    @RequiresPermissions("fin:daily:dataexport")
    @ApiOperation(value = "|Daily|日报数据导出", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessage dataExportWithbusinessNO(String deptId, Long startLong, Long endLong,HttpServletResponse response) {


        if (null == startLong || null == endLong) {
            throw new AdcDaBaseException("请指定时间范围");
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode("日报导出表.xlsx"));
        response.setContentType("application/force-download");
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);

        try (OutputStream os = response.getOutputStream();
             Workbook workbook = dailyExportService.getDailyWorkbookNew(deptId, startLong, endLong)) {
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success("导出成功");
    }



    @ApiOperation(value = "|Project|导出")
    @GetMapping("/projectExport")
    //@RequiresPermissions("fin:project:export")
    public ResponseMessage excelImport(HttpServletResponse response, String fileName, Long startLong, Long endLong) {
        String resultName;
        if (null == startLong || null == endLong) {
            throw new AdcDaBaseException("请指定时间范围");
        }

        if (StringUtils.isEmpty(fileName)) {
            resultName = "项目导出数据.xlsx";
        } else {
            if (!fileName.contains(".xlsx")) {
                resultName = fileName + ".xlsx";
            } else {
                resultName = fileName;
            }
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(resultName));
        response.setContentType("application/force-download");
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);

        try (OutputStream os = response.getOutputStream();
             Workbook workbook = projectExportService.excelExport(params, startLong, endLong)) {
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }
}
