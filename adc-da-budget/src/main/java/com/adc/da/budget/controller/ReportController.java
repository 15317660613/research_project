package com.adc.da.budget.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.ReportEO;
import com.adc.da.budget.service.ReportEOService;
import com.adc.da.budget.utils.NewExcelImportUtil;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.excel.poi.excel.entity.params.ExcelExportEntity;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/${restPath}/report")
@Api("|report|")
public class ReportController extends BaseController<ReportEO> {

    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
    @Autowired
    private ReportEOService reportEOService;
    @Autowired
    private ObjectMapper objectMapper;

    @ApiOperation(value = "|ReportEO|查询")
    @GetMapping("/{id}")
//    @RequiresPermissions("report:budget:list")
    public ResponseMessage<List<List<String>>> list(@PathVariable(required = true) String id) {
        try {
            ReportEO reportEO = reportEOService.get("report_" + id);
            if (reportEO != null) {
                return Result.success(reportEO.getTableArray());
            } else {
                return Result.error("没有数据");
            }
        } catch (Exception e) {
            //return Result.error(e.getMessage());
            return Result.error("接口调用异常");
        }
    }

    @ApiOperation(value = "|ReportEO|保存")
    @PostMapping("/save/{id}")
//@RequiresPermissions("report:budget:list")
    public ResponseMessage<List<List<String>>> save(@PathVariable(required = true) String id,
                                                        @RequestBody(required = true) List<List<String>> reports)
                                                            throws IOException {
        ReportEO reportEO = new ReportEO("report_" + id, reports);
        reportEOService.set(reportEO);
        ReportEO report = reportEOService.get("report_" + id);
        return Result.success(report.getTableArray());
    }

    @ApiOperation(value = "|ReportEO|导出")
    @GetMapping("/export/{id}")
//    @RequiresPermissions("report:budget:list")
    public void export(HttpServletResponse response, @PathVariable(required = true) String id, String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "数据表格.xlsx";
        } else {
            if (!fileName.contains(".xlsx")) {
                fileName += ".xlsx";
            }
        }
        List<List<String>> reports = null;
        try {
            reports = reportEOService.get("report_" + id).getTableArray();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Result.error("下载失败，请联系管理员");
        }
        OutputStream os = null;
        Workbook workbook = null;
        try {
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");

            /**
             * 数据存储结构， 二维数组，第二维维String数组
             */
            //构造测试数据

            // 构造动态导出列
            List<ExcelExportEntity> beanList = new ArrayList<>();
            if (null != reports) {
                for (int i = 0; i < reports.get(0).size(); i++) {
                    beanList.add(new ExcelExportEntity(String.valueOf(i), String.valueOf(i)));
                }
            }
            // 设置导出参数为07版本Excel
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            params.setCreateHeadRows(false);
            // 构造数据
            List<Map<String, Object>> list = new ArrayList<>();
            if (null != reports) {
                for (int i = 0; i < reports.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    for (int j = 0; j < reports.get(i).size(); j++) {
                        map.put(String.valueOf(j), reports.get(i).get(j));
                    }
                    list.add(map);
                }
            }
            //打开数据
            // 导出数据
            workbook = ExcelExportUtil.exportExcel(params, beanList, list);

            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            Result.error("下载失败，请联系管理员");
        } finally {
            try {
                IOUtils.closeQuietly(os);
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                Result.error("下载失败，请联系管理员");
            }
        }
    }

    @ApiOperation(value = "|ReportEO|导入")
    @PostMapping("/import/{id}")
//@RequiresPermissions("report:budget:list")
    public ResponseMessage excelImport(@PathVariable(required = true) String id,
                                            @RequestParam("file") MultipartFile file) {
        try {
            List<String[]> beanList = NewExcelImportUtil.importExcel(file.getInputStream());
            String reportsString = objectMapper.writeValueAsString(beanList);
            reportEOService.setString("report_" + id, reportsString);
            return Result.success();
        } catch (Exception e) {
            //return Result.error(e.getMessage());
            return Result.error("导入发生异常");
        }
    }

    @ApiOperation(value = "|ReportEO|导出PPT")
    @GetMapping("/export/{id}/ppt")
    public void exportPPT(HttpServletResponse response, @PathVariable(required = true) String id) {
        OutputStream os = null;
        List<List<String>> reports = null;
        XMLSlideShow pptx = null;
        String fileName;
        try {
            //获取数据
            reports = reportEOService.get("report_" + id).getTableArray();
            switch (id){
                case "1":
                    fileName = "财务指标分析.pptx";
                    break;
                case "2":
                    fileName = "2017-2018审批项目-费用执行情况.pptx";
                    break;
                case "3":
                    fileName = "2017年支出合同-2018年执行情况.pptx";
                    break;
                case "4":
                    fileName = "整体费用预算执行情况.pptx";

                    break;
                case "5":
                    fileName = "2017-2018年审批项目--本部执行情况.pptx";
                    break;
                case "6":
                    fileName = "2018年支出合同执行汇总表.pptx";
                    break;
                case "7":
                    fileName = "部门费用预算执行情况.pptx";
                    break;
                case "8":
                    fileName = "专项预算费用监控-科研费用预算执行.pptx";
                    break;
                default:
                    fileName = "ppt.pptx";
            }
            // 构造ppt
            pptx = reportEOService.exportPPT(id, reports);
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            os = response.getOutputStream();
            pptx.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            Result.error("下载失败，请联系管理员");
        } finally {
            try {
                IOUtils.closeQuietly(os);
                if (pptx != null) {
                    pptx.close();
                }
            } catch (IOException e) {
                Result.error("下载失败，请联系管理员");
            }
        }


    }
}