package com.adc.da.budget.utils;

import com.adc.da.util.exception.AdcDaBaseException;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NewExcelImportUtil {
    public static List<String[]> importExcel(InputStream inputstream) throws Exception{
        List<String[]> result = new ArrayList();
        Workbook book = null;
        if (!((InputStream)inputstream).markSupported()) {
            inputstream = new PushbackInputStream((InputStream)inputstream, 8);
        }
        if (POIFSFileSystem.hasPOIFSHeader((InputStream)inputstream)) {
            book = new HSSFWorkbook((InputStream)inputstream);
        } else if (POIXMLDocument.hasOOXMLHeader((InputStream)inputstream)) {
            book = new XSSFWorkbook(OPCPackage.open((InputStream)inputstream));
        } else {
            throw new AdcDaBaseException("cannot get workbook from inputStream.");
        }
        Sheet sheet = book.getSheetAt(0);
        for(int k = 0; k <= sheet.getLastRowNum(); k++ ) {
            Row row = sheet.getRow(k);
            if(row == null){
                break;
            }
            short le = row.getLastCellNum();
            String[] tmp = new String[le];
            for(int i = row.getFirstCellNum(); i < le; ++i) {
                Cell cell = row.getCell(i);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                tmp[i] = cell.getStringCellValue();
            }
            result.add(tmp);
        }
        return result;
    }
}
