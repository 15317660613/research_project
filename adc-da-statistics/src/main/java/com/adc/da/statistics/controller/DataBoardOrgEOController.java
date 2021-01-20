package com.adc.da.statistics.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.statistics.entity.DataBoardOrgEO;
import com.adc.da.statistics.service.DataBoardOrgEOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/statistics/dataBoardOrg")
@Api(tags = "|统计-数据看板|")
@Slf4j
public class DataBoardOrgEOController extends BaseController<DataBoardOrgEO> {

    @Autowired
    private DataBoardOrgEOService dataBoardOrgEOService;

    @ApiOperation(value = "|DataBoardOrgEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("statistics:dataBoardOrg:save")
    public ResponseMessage<DataBoardOrgEO> create(@RequestBody DataBoardOrgEO dataBoardOrgEO) throws Exception {
        dataBoardOrgEOService.insertSelective(dataBoardOrgEO);
        return Result.success(dataBoardOrgEO);
    }

    /**
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "支出excel导入")
    @PostMapping("/excelImportExpenditure")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file, Integer year) {
        if (null == year) {
            return Result.error("-1", "year not input");
        }
        try (InputStream is = file.getInputStream();
             //根据输入流创建Workbook对象
             Workbook wb = WorkbookFactory.create(is)) {

            dataBoardOrgEOService.insertExpenditureDataFromExcel(wb, year);
        } catch (IOException | InvalidFormatException e) {
            log.error(e.getMessage());
        }

        return Result.success();
    }

    /**
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "进账excel导入")
    @PostMapping("/excelImportCredit")
    public ResponseMessage excelImportCredit(@RequestParam("file") MultipartFile file) {

        try (InputStream is = file.getInputStream();
             //根据输入流创建Workbook对象
             Workbook wb = WorkbookFactory.create(is)) {
            dataBoardOrgEOService.insertCreditDataFromExcel(wb);
        } catch (IOException | InvalidFormatException e) {
            log.error(e.getMessage());
        }

        return Result.success();
    }
}
