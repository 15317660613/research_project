package com.adc.da.util;



import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelReaderUtil {
    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
    public static Map<Integer,List<String>> readExcel(InputStream is, int sheetIndex, int rowStartIndex,
                                                      int startColumnIndex,int columns) {
        Sheet sheet ;
        try {
            Workbook workbook = getWorkBookFromInputStream(is);
            sheet = workbook.getSheetAt(sheetIndex);
        }catch (Exception e){
            throw new AdcDaBaseException("读取excel 出错，请检查或联系系统管理员！");
        }
        Map<Integer,List<String>> rowNumValueStrListMap = new LinkedHashMap<>();
        // sheet.getRows()返回该页的总行数
        for (int rowIndex = rowStartIndex; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            List<String> rowValueStrList=new ArrayList();
            Row row = sheet.getRow(rowIndex);
            for (int columnIndex = startColumnIndex; columnIndex < columns; columnIndex++) {
                Cell cell = row.getCell(columnIndex);
                String value = getCellValueStr(cell);
                rowValueStrList.add(value);
            }
            rowNumValueStrListMap.put(rowIndex,rowValueStrList);
        }
        return rowNumValueStrListMap;

    }
    public static String getCellValueStr(Cell cell) {
        String cellString = "";
        if (cell == null) {
            return cellString;
        }
        FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                cellString = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellString = cell.getDateCellValue().toString();
                } else {
                    cellString = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_STRING:
                cellString = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_FORMULA:
                try {
                    cellString= String.valueOf(evaluator.evaluate(cell));
                }catch (Exception e){
                    cellString = "";
                }
                break;
            default:
                cellString = String.valueOf(cell.getRichStringCellValue());
        }
        return cellString;
    }

    public static Workbook getWorkBookFromInputStream(InputStream inputStream) throws Exception {
        //根据输入流创建Workbook对象
        Workbook wb = WorkbookFactory.create(inputStream);
        inputStream.close();
        return wb;
    }
}
