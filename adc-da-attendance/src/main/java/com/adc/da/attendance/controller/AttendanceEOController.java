package com.adc.da.attendance.controller;

import com.adc.da.attendance.entity.AttendanceEO;
import com.adc.da.attendance.page.AttendanceEOPage;
import com.adc.da.attendance.service.AttendanceEOService;
import com.adc.da.base.web.BaseController;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/attendance/attendance")
@Api(tags = "|考勤模块|")

@Slf4j
public class AttendanceEOController extends BaseController<AttendanceEO> {

    @Autowired
    private AttendanceEOService attendanceEOService;

    @ApiOperation(value = "|AttendanceEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("attendance:attendance:page")
    public ResponseMessage<PageInfo<AttendanceEO>> page(AttendanceEOPage page) throws Exception {
        List<AttendanceEO> rows = attendanceEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|AttendanceEO|查询")
    @GetMapping("")
    //@RequiresPermissions("attendance:attendance:list")
    public ResponseMessage<List<AttendanceEO>> list(AttendanceEOPage page) throws Exception {
        return Result.success(attendanceEOService.queryByList(page));
    }

    @ApiOperation(value = "|AttendanceEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("attendance:attendance:get")
    public ResponseMessage<AttendanceEO> find(@PathVariable String id) throws Exception {
        return Result.success(attendanceEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|AttendanceEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("attendance:attendance:save")
    public ResponseMessage<AttendanceEO> create(@RequestBody AttendanceEO attendanceEO) throws Exception {
        attendanceEOService.insertSelective(attendanceEO);
        return Result.success(attendanceEO);
    }

    @ApiOperation(value = "|AttendanceEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("attendance:attendance:update")
    public ResponseMessage<AttendanceEO> update(@RequestBody AttendanceEO attendanceEO) throws Exception {
        attendanceEOService.updateByPrimaryKeySelective(attendanceEO);
        return Result.success(attendanceEO);
    }

    /**
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "支出excel导入")
    @PostMapping("/excelImportExpenditure")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file, Long beginLong, Long endLong,
        boolean saveFlag) {
        if (null == beginLong || null == endLong) {
            return Result.error("-1", "year not input");
        }
        try (InputStream is = file.getInputStream();
             //根据输入流创建Workbook对象
             Workbook wb = WorkbookFactory.create(is)) {

            attendanceEOService.importAttendanceDataFromExcel(wb, beginLong, endLong, saveFlag);
        } catch (IOException | InvalidFormatException e) {
            log.error(e.getMessage());
        }

        return Result.success();
    }


}
