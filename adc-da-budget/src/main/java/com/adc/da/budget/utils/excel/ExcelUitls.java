package com.adc.da.budget.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/21 09:35
 * @Description: Excel导入工具类封装
 */
public class ExcelUitls {

    private final static Logger logger = LoggerFactory.getLogger(ExcelUitls.class);

    /**
     * @Description: 获取WorkBook
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/21 9:50
     */
    public static Workbook getWorkBook(MultipartFile file) {
        InputStream fis = null;
        Workbook wookbook = null;
        // 判断是不是excel类型
        if (file.getOriginalFilename().endsWith(".xls") && file.getOriginalFilename().endsWith(".xlsx")) {
            logger.error("文件不是excel类型");
        } else {
            try {
                fis = file.getInputStream();
                wookbook = new XSSFWorkbook(fis);
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        }
        return wookbook;
    }


    public static void sheetExport(MultipartFile file) {
        Workbook workBook = getWorkBook(file);
        if (null != workBook) {
            // 循环所有sheet页
            for (int i = 0; i < 8; i++) {

            }
        }
    }
}
