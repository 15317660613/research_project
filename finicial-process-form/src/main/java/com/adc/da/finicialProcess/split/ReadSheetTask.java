package com.adc.da.finicialProcess.split;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class ReadSheetTask implements Callable<Map<String, List<Row>> > {
    private static final Logger logger =  LoggerFactory.getLogger(ReadSheetTask.class);
    public ReadSheetTask(Sheet sheet, int rowStart, int[] sheetKeyCols) {
        this.sheet = sheet;
        this.rowStart = rowStart;
        this.sheetKeyCols = sheetKeyCols;
    }

    private Sheet sheet;
    private int rowStart;
    int[] sheetKeyCols ;

    @Override
    public Map<String, List<Row>> call() throws Exception {
        Map<String, List<Row>> sheet0Map= new HashMap<>();
        try {
            sheet0Map = ExcelSplitUtil.getRowMapFromSheet(sheet, rowStart, sheetKeyCols);
        }catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return sheet0Map;
        }
       return sheet0Map;
    }
}
