package com.adc.da.attendance.utils.excel;

import com.adc.da.attendance.entity.AttendanceInfoEO;
import com.adc.da.attendance.entity.AttendanceInfoExcelEO;
import com.adc.da.attendance.entity.TimeExcptionDTO;
import com.adc.da.attendance.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * @Auther: wupeilin
 * @Date: 2018/11/21 09:35
 * @Description: Excel导入工具类封装
 */
public class ExcelUitls {

    private final static Logger logger = LoggerFactory.getLogger(ExcelUitls.class);

    /**
     * @Description: 获取WorkBook
     * @auther: wupeilin
     * @return:
     * @date: 2019年1月23日11:08:39
     */
    public static Workbook getWorkBook(File file) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        InputStream fis = null;
        Workbook wookbook = null;
        // 判断是不是excel类型
        if (multipartFile.getOriginalFilename().endsWith(".xls") && multipartFile.getOriginalFilename().endsWith(".xlsx")) {
            logger.error("文件不是excel类型");
        } else {
            try {
                fis = multipartFile.getInputStream();
                if (multipartFile.getOriginalFilename().endsWith(".xls")) {
                    wookbook = new HSSFWorkbook(fis);
                }
                if (multipartFile.getOriginalFilename().endsWith(".xlsx")) {
                    wookbook = new XSSFWorkbook(fis);
                }
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        return wookbook;
    }


    public static HSSFWorkbook exportExcle(Map<Integer, AttendanceInfoExcelEO> resultMap, Date startDate, Date endDate, List<Date> workDateLst) throws Exception {

        List<String> titles = new ArrayList<>();
        Date startDateown = startDate;
        titles.add("序号");
        titles.add("工号");
        titles.add("姓名");
        while (!DateUtils.later(startDate, endDate)) {
            String day = DateUtils.getDateStr(startDate, "yyyy-MM-dd");
            titles.add(day);
            startDate = DateUtils.addDays(startDate, 1);
        }


        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
        CellStyle style = wb.createCellStyle();
        //合并单元格
        CellRangeAddress region4 = new CellRangeAddress(1, 2, 2, 2);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号

        CellRangeAddress region = new CellRangeAddress(1, 2, 0, 0);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
        CellRangeAddress region2 = new CellRangeAddress(1, 2, 1, 1);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
        CellRangeAddress region3 = new CellRangeAddress(0, 0, 0, 20);// 下标从0开始 起始行号，终止行号， 起始列号，终止列号
        style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setWrapText(true);

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        CellStyle style2 = wb.createCellStyle();
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        style2.setWrapText(true);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        Font fontData = wb.createFont();
        fontData.setColor(Font.COLOR_RED); //字体颜色
        CellStyle redColorStyle = wb.createCellStyle();
        redColorStyle.setFont(fontData);
        redColorStyle.setAlignment(CellStyle.ALIGN_CENTER);
        redColorStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        redColorStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        redColorStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        redColorStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        CellStyle centerStyle = wb.createCellStyle();
        centerStyle.setAlignment(CellStyle.ALIGN_CENTER); //水平布局：居中
        centerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        centerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        centerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        centerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
        //头标题
        Font fontData2 = wb.createFont();
        fontData2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  //字体颜色
        fontData2.setFontHeightInPoints((short) 20);
        HSSFCellStyle fontStyleBold = wb.createCellStyle();
        fontStyleBold.setFont(fontData2);
        fontStyleBold.setAlignment(CellStyle.ALIGN_CENTER);
        fontStyleBold.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        fontStyleBold.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        fontStyleBold.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        fontStyleBold.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

//建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet("考勤信息");
        sheet.autoSizeColumn(1, true);
        sheet.addMergedRegion(region);
        sheet.addMergedRegion(region2);
        sheet.addMergedRegion(region3);
        sheet.addMergedRegion(region4);
        //设置表头
        HSSFRow row3 = sheet.createRow(0);
        HSSFRow row = sheet.createRow(1);
        HSSFRow row2 = sheet.createRow(2);
        String startStr = DateUtils.getDateStr(startDateown, "yyyy-MM-dd");
        String endStr = DateUtils.getDateStr(endDate, "yyyy-MM-dd");
        HSSFCell cell1 = row3.createCell(0);
        cell1.setCellValue(startStr + "~" + endStr + "异常考勤记录汇总");
        cell1.setCellStyle(fontStyleBold);

        //为合并的单元格，加边框

        RegionUtil.setBorderBottom(1, region, sheet); // 下边框
        RegionUtil.setBorderLeft(1, region, sheet); // 左边框
        RegionUtil.setBorderRight(1, region, sheet); // 有边框
        RegionUtil.setBorderTop(1, region, sheet); // 上边框

        RegionUtil.setBorderBottom(1, region2, sheet); // 下边框
        RegionUtil.setBorderLeft(1, region2, sheet); // 左边框
        RegionUtil.setBorderRight(1, region2, sheet); // 有边框
        RegionUtil.setBorderTop(1, region2, sheet); // 上边框

        RegionUtil.setBorderBottom(1, region3, sheet); // 下边框
        RegionUtil.setBorderLeft(1, region3, sheet); // 左边框
        RegionUtil.setBorderRight(1, region3, sheet); // 有边框
        RegionUtil.setBorderTop(1, region3, sheet); // 上边框

        RegionUtil.setBorderBottom(1, region4, sheet); // 下边框
        RegionUtil.setBorderLeft(1, region4, sheet); // 左边框
        RegionUtil.setBorderRight(1, region4, sheet); // 有边框
        RegionUtil.setBorderTop(1, region4, sheet); // 上边框

        for (int i = 0; i < titles.size(); i++) {
            if (i > 2) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(titles.get(i).split("-")[2]);
                HSSFCell cell2 = row2.createCell(i);
                Date date = DateUtils.parseDate(titles.get(i), "yyyy-MM-dd");
                String weekOfDate = DateUtils.getWeekOfDate(date);
                cell2.setCellValue(weekOfDate);
                cell.setCellStyle(centerStyle);
                cell2.setCellStyle(centerStyle);
                if (!workDateLst.contains(date)) {
                    cell2.setCellStyle(redColorStyle);

                }
            } else {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(titles.get(i));
                cell.setCellStyle(style2);
                cell.setCellStyle(centerStyle);
            }

        }
        Integer i = 3;
        //遍历数据
        for (Map.Entry<Integer, AttendanceInfoExcelEO> entry : resultMap.entrySet()) {
            AttendanceInfoExcelEO attendanceInfoExcelEO = entry.getValue();
            HSSFRow rowcontents = sheet.createRow(i++);
            HSSFCell cell2 = rowcontents.createCell(0);
            cell2.setCellValue(i - 3);
            cell2.setCellStyle(centerStyle);
            HSSFCell cell3 = rowcontents.createCell(1);
            cell3.setCellValue(attendanceInfoExcelEO.getWorkId());
            cell3.setCellStyle(centerStyle);
            HSSFCell cell4 = rowcontents.createCell(2);
            cell4.setCellValue(attendanceInfoExcelEO.getHumanName());
            cell4.setCellStyle(centerStyle);
            //遍历考勤信息
            for (int j = 3; j < titles.size(); j++) {
                TimeExcptionDTO timeExcptionDTO = attendanceInfoExcelEO.getAttendanceTime().get(titles.get(j));
                HSSFCell cell = rowcontents.createCell(j);
                if(timeExcptionDTO==null|| !timeExcptionDTO.getIsException().equals("1") ){
                    cell.setCellStyle(style2);
                    continue;
                }
                setCellStyle(cell,timeExcptionDTO,style,style2);
            }
        }
        return wb;
    }
    public static void setCellStyle(HSSFCell cell,TimeExcptionDTO timeExcptionDTO,CellStyle style,CellStyle style2){
        if (!StringUtils.isEmpty(timeExcptionDTO.getTime())) {
            cell.setCellValue(timeExcptionDTO.getTime().replace(",", "\n"));
        } else {
            cell.setCellValue("");
        }
        if (timeExcptionDTO.getIsException().equals("1")) {

            cell.setCellStyle(style);

        } else {

            cell.setCellStyle(style2);
        }

    }


}
