package com.adc.da.attendance.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.adc.da.attendance.entity.ExportExcleDTO;
import com.adc.da.attendance.entity.ExportExcleRealDTO;
import com.adc.da.attendance.service.AttendanceService;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.attendance.entity.AttendanceInfoEO;
import com.adc.da.attendance.page.AttendanceInfoEOPage;
import com.adc.da.attendance.service.AttendanceInfoEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/${restPath}/attendance/attendanceInfo")
@Api(description = "|AttendanceInfoEO|")
public class AttendanceInfoEOController extends BaseController<AttendanceInfoEO>{

    private static final Logger logger = LoggerFactory.getLogger(AttendanceInfoEOController.class);

    @Autowired
    private AttendanceInfoEOService attendanceInfoEOService;

    @Autowired
    AttendanceService attendanceService;

	@ApiOperation(value = "|AttendanceInfoEO|分页查询")
    @GetMapping("/page")
  //  @RequiresPermissions("attendance:attendanceInfo:page")
    public ResponseMessage<PageInfo<AttendanceInfoEO>> page(AttendanceInfoEOPage page) throws Exception {
        List<AttendanceInfoEO> rows = attendanceInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }


    @ApiOperation(value = "|Attendance|录入考勤")
    @PostMapping("/AttendanceImport")
    @RequiresPermissions("fin:project:import")
    public ResponseMessage AttendanceImport(HttpServletResponse response,@RequestParam("file") MultipartFile file) throws Exception {
        OutputStream os = null;

        Workbook workbook = attendanceService.AttendanceImport(file);
        BufferedWriter writer = null;
        String destDirName="/attendanceinfo";
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        OutputStream stream=null;
        //创建目录
        Date date = new Date();
        System.out.println(file+"file");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
        }
        File file2 = new File(destDirName+ file.getOriginalFilename().split("\\.")[0] +sdf.format(date)+ ".xls");

        stream = new FileOutputStream(file2);
         workbook.write(stream);

        return Result.success(file2.getName());
    }

    @ApiOperation(value = "|Attendance|获取异常考勤记录生成excel")
    @GetMapping("/AttendanceExport")
    @RequiresPermissions("fin:project:import")
    public ResponseMessage AttendanceExport(HttpServletResponse response, @RequestParam String url) throws Exception {
        File excelFile = new File("/attendanceinfo/"+url);
        OutputStream os =null;
        HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(excelFile));
                try {
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(excelFile.getName()));
            response.setContentType("application/force-download");
            os = response.getOutputStream();
                    wb.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            os = response.getOutputStream();
            os.write(e.getMessage().getBytes("GBK"));
            os.flush();
        } finally {
            IOUtils.closeQuietly(os);
        }
        return Result.success();
    }
//    @ApiOperation(value = "|Attendance|获取异常考勤记录生成excel")
//    @GetMapping("/AttendanceExport")
//    //@RequiresPermissions("fin:project:import")
//    public ResponseMessage AttendanceExport(HttpServletResponse response, @RequestParam String startDate, @RequestParam String endDate, @RequestParam String fileName) throws Exception {
//        ExportExcleDTO exportExcleDTO = new ExportExcleDTO();
//        exportExcleDTO.setStartDate(startDate);
//        exportExcleDTO.setEndDate(endDate);
//        exportExcleDTO.setFileName(fileName);
//        if (StringUtils.isEmpty(exportExcleDTO.getFileName() )) {
//            exportExcleDTO.setFileName( "数据表格.xls") ;
//        } else {
//            if (!exportExcleDTO.getFileName().contains(".xls")) {
//                exportExcleDTO.setFileName(exportExcleDTO.getFileName()+ ".xls") ;
//            }
//        }
//        OutputStream os = null;
//        Workbook workbook = null;
//        try {
//            response.setHeader("Content-Disposition",
//                    "attachment; filename=" + Encodes.urlEncode(exportExcleDTO.getFileName()));
//            response.setContentType("application/force-download");
//            ExportParams params = new ExportParams();
//            params.setType(ExcelType.XSSF);
//            exportExcleDTO.setParams(params);
//            workbook = attendanceService.AttendanceExport(exportExcleDTO);
//            os = response.getOutputStream();
//            workbook.write(os);
//            os.flush();
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            os = response.getOutputStream();
//            os.write(e.getMessage().getBytes("GBK"));
//            os.flush();
//        } finally {
//            IOUtils.closeQuietly(os);
//        }
//        return Result.success();
//
//    }







    @ApiOperation(value = "|AttendanceInfoEO|查询")
    @GetMapping("")
   // @RequiresPermissions("attendance:attendanceInfo:list")
    public ResponseMessage<List<AttendanceInfoEO>> list(AttendanceInfoEOPage page) throws Exception {
        return Result.success(attendanceInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|AttendanceInfoEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("attendance:attendanceInfo:get")
    public ResponseMessage<AttendanceInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(attendanceInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|AttendanceInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
  //  @RequiresPermissions("attendance:attendanceInfo:save")
    public ResponseMessage<AttendanceInfoEO> create(@RequestBody AttendanceInfoEO attendanceInfoEO) throws Exception {
        attendanceInfoEOService.insertSelective(attendanceInfoEO);
        return Result.success(attendanceInfoEO);
    }

    @ApiOperation(value = "|AttendanceInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("attendance:attendanceInfo:update")
    public ResponseMessage<AttendanceInfoEO> update(@RequestBody AttendanceInfoEO attendanceInfoEO) throws Exception {
        attendanceInfoEOService.updateByPrimaryKeySelective(attendanceInfoEO);
        return Result.success(attendanceInfoEO);
    }

    @ApiOperation(value = "|AttendanceInfoEO|删除")
    @DeleteMapping("/{id}")
  //  @RequiresPermissions("attendance:attendanceInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        attendanceInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from ATTENDANCE_INFO where id = {}", id);
        return Result.success();
    }

}
