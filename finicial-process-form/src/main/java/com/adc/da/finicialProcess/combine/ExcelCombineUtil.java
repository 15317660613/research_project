package com.adc.da.finicialProcess.combine;

import com.adc.da.finicialProcess.split.ExcelSplitUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelCombineUtil {

  /*  public static void main(String[] args) throws Exception {
        ArrayList<File> srcFiles = new ArrayList<>();
        srcFiles.add(new File("E:/A/a.xlsx"));
        srcFiles.add(new File("E:/A/b.xlsx"));
        File destFile = new File("E:/A/c.xlsx");
        combineExcelsToOne(srcFiles, destFile, , 0, 2);
    }
*/




    //将多个Excel合并成一个Excel
    public static void combineExcelsToOne(ArrayList<File> srcFiles, File tplFile, String destFilePath,int sheetNum, int startRow) throws Exception {
        Workbook destWorkbook = ExcelSplitUtil.getWorkBookFromFile(tplFile);
        List<Row> resultList = new ArrayList<>();
        for (int i=0;i<srcFiles.size(); i++){
            Workbook srcWorkbook = ExcelSplitUtil.getWorkBookFromFile(srcFiles.get(i));
            getRowsFromSheet(srcWorkbook, sheetNum, resultList, startRow);
        }
        ExcelSplitUtil.insertRowToSheet(destWorkbook.getSheetAt(sheetNum), resultList);
        OutputStream outputStream = new FileOutputStream(destFilePath);
        destWorkbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    public static List<Row> getRowsFromSheet(Workbook wb, int sheetNum, List<Row> list, int startRow){
        Sheet sheet = wb.getSheetAt(sheetNum);
        for (int i=startRow; i<=sheet.getLastRowNum();i++){
            list.add(sheet.getRow(i));
        }
        return list;
    }
}
