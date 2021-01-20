package com.adc.da.finicialProcess.split;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class LoopTask implements Callable<Integer> {

    private CountDownLatch countDownLatch;
    private List<ResultFile> resultFileList;
    private File resRile ;
    private String basePath ;
    private Map.Entry<String, List<Row>> entry ;
    private Map<String, List<Row>> sheet1Map ;
    private Map<String, List<Row>> sheet2Map ;
    private Map<String, List<Row>> sheet3Map ;

    public LoopTask(CountDownLatch countDownLatch, List<ResultFile> resultFileList, File resRile, String basePath,
            Map.Entry<String, List<Row>> entry,
            Map<String, List<Row>> sheet1Map,
            Map<String, List<Row>> sheet2Map,
            Map<String, List<Row>> sheet3Map) {
        this.countDownLatch = countDownLatch;
        this.resultFileList = resultFileList;
        this.resRile = resRile;
        this.basePath = basePath;
        this.entry = entry;
        this.sheet1Map = sheet1Map;
        this.sheet2Map = sheet2Map;
        this.sheet3Map = sheet3Map;
    }

    @Override
    public Integer call() throws Exception {
        //1.声明线程池
        ExecutorService executor = Executors.newCachedThreadPool();

        ResultFile resultFile = new ResultFile();
        //原来加载的是模板的头，因为模板头总是变，改为用用户上传的excel去掉数据的部分做模板头
        //Workbook workbook = WorkbookFactory.create(tplFile);
        Workbook workbook = ExcelSplitUtil.getWorkBookFromFile(resRile);
        String key = entry.getKey();
        Sheet resSheet0 = workbook.getSheetAt(0) ;
        ExcelSplitUtil.removeDataRow(resSheet0,3);
        WriteSheetTask writeSheetTask0 = new WriteSheetTask(resSheet0,entry.getValue());
        FutureTask<Sheet> writeSheetFuture0 = new FutureTask(writeSheetTask0);
        executor.submit(writeSheetFuture0);

        Sheet resSheet1 = workbook.getSheetAt(1) ;
        ExcelSplitUtil.removeDataRow(resSheet1,1);
        WriteSheetTask writeSheetTask1 = new WriteSheetTask(resSheet1,sheet1Map.get(key));
        FutureTask<Sheet> writeSheetFuture1 = new FutureTask(writeSheetTask1);
        executor.submit(writeSheetFuture1);


        Sheet resSheet2 = workbook.getSheetAt(2) ;
        ExcelSplitUtil.removeDataRow(resSheet2,2);
        WriteSheetTask writeSheetTask2 = new WriteSheetTask(resSheet2,sheet2Map.get(key));
        FutureTask<Sheet> writeSheetFuture2 = new FutureTask(writeSheetTask2);
        executor.submit(writeSheetFuture2);


        Sheet resSheet3 = workbook.getSheetAt(3) ;
        ExcelSplitUtil.removeDataRow(resSheet3,1);
        WriteSheetTask writeSheetTask3 = new WriteSheetTask(resSheet3, sheet3Map.get(key));
        FutureTask<Sheet> writeSheetFuture3 = new FutureTask(writeSheetTask3);
        executor.submit(writeSheetFuture3);

        Sheet sheet0 = writeSheetFuture0.get();
        ExcelSplitUtil.autoSizeColumn(sheet0);
        Sheet sheet1 = writeSheetFuture1.get();
        ExcelSplitUtil.autoSizeColumn(sheet1);
        Sheet sheet2 = writeSheetFuture2.get();
        ExcelSplitUtil.autoSizeColumn(sheet2);
        Sheet sheet3 = writeSheetFuture3.get();
        ExcelSplitUtil.autoSizeColumn(sheet3);


        ExcelSplitUtil.doMergeCell(sheet1, 4);
        ExcelSplitUtil.doMergeCell(sheet1, 5);
        String filePath = basePath + key + "tpl.xlsx";
        OutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        workbook.close();
        outputStream.flush();
        outputStream.close();

        resultFile.setFileName(key + "tpl.xlsx");
        resultFile.setFilePath(filePath);
        resultFile.setKey(key);
        resultFileList.add(resultFile);
        countDownLatch.countDown();
        return 0;
    }
}
