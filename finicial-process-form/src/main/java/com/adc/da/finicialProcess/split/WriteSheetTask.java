package com.adc.da.finicialProcess.split;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;


class WriteSheetTask implements Callable<Sheet> {
    private static final Logger logger =  LoggerFactory.getLogger(WriteSheetTask.class);
    public WriteSheetTask(Sheet sheet, List<Row> rowList ) {
        this.sheet = sheet;
        this.rowList = rowList;
    }

    private Sheet sheet;
    private List<Row> rowList;
    @Override
    public Sheet call(){
        Sheet result = sheet ;
        Workbook workbook = sheet.getWorkbook();
        CellStyle style  = workbook.createCellStyle() ;
        try {
             result = ExcelSplitUtil.insertRowToSheet(sheet, rowList,style);
        }catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return result;
        }
        return result;
    }
}
