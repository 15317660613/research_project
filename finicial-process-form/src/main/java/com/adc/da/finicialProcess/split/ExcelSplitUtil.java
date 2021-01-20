package com.adc.da.finicialProcess.split;

import com.adc.da.finicialProcess.service.FinancialProcessTableEOService;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class ExcelSplitUtil {

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger LOGGER =  LoggerFactory.getLogger(ExcelSplitUtil.class);
    private static FormulaEvaluator evaluator;

    public static void main(String[] args) throws Exception {
        File srcFile = new File("E:/a.xlsx");
        File tplFile = new File("E:/tpl.xlsx");
        long startTime = System.currentTimeMillis() ;
        List<ResultFile> resultBigDeptFileList = doSplitBySmallDeptInMultiThreadAll(srcFile, "E:/A/");
        long endTime = System.currentTimeMillis() ;
        System.out.println( (endTime-startTime) / 1000 );
        System.out.println("=====结束====");
    }

    public static synchronized Workbook getWorkBookFromFile(File file) throws Exception {
        InputStream inputStream = new FileInputStream(file);
        //根据输入流创建Workbook对象
        Workbook wb = WorkbookFactory.create(inputStream);
        inputStream.close();
        return wb;
    }

//    public static List<ResultFile> doSplitByBigDept(File resRile, File tplFile, String basePath) throws Exception {
//
//        List<ResultFile> resultFileList = new ArrayList<>();
//        basePath = getBasePath(basePath);
//
//        //根据文件Workbook对象
//        Workbook wb = getWorkBookFromFile(resRile);
//        int[] sheet0keyCols  = {0} ;
//        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 3, sheet0keyCols);
//        int[] sheet1keyCols  = {0} ;
//        Map<String, List<Row>> sheet1Map = getRowMapFromSheet(wb.getSheetAt(1), 1, sheet1keyCols);
//        int[] sheet2keyCols  = {2} ;
//        Map<String, List<Row>> sheet2Map = getRowMapFromSheet(wb.getSheetAt(2), 2, sheet2keyCols);
//        int[] sheet3keyCols  = {6} ;
//        Map<String, List<Row>> sheet3Map = getRowMapFromSheet(wb.getSheetAt(3), 1, sheet3keyCols);
//        wb.close();
//        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
//            //原来加载的是模板的头，因为模板头总是变，改为用用户上传的excel去掉数据的部分做模板头
//            //Workbook workbook = WorkbookFactory.create(tplFile);
//            Workbook workbook =  getWorkBookFromFile(resRile);
//            ResultFile resultFile = new ResultFile();
//
//            String key = entry.getKey();
//            Sheet resSheet0 = workbook.getSheetAt(0) ;
//            removeDataRow(resSheet0,3);
//            Sheet sheet0 = insertRowToSheet(resSheet0, entry.getValue());
//
//            Sheet resSheet1 = workbook.getSheetAt(1) ;
//            removeDataRow(resSheet1,1);
//            Sheet sheet1 = insertRowToSheet(resSheet1, sheet1Map.get(key));
//
//            Sheet resSheet2 = workbook.getSheetAt(2) ;
//            removeDataRow(resSheet2,2);
//            Sheet sheet2 = insertRowToSheet(resSheet2, sheet2Map.get(key));
//
//            Sheet resSheet3 = workbook.getSheetAt(3) ;
//            removeDataRow(resSheet3,1);
//            Sheet sheet3 = insertRowToSheet(resSheet3, sheet3Map.get(key));
//
//
//            doMergeCell(sheet1, 4);
//            doMergeCell(sheet1, 5);
//            String filePath = basePath + key + "tpl.xlsx";
//
//            OutputStream outputStream = new FileOutputStream(filePath);
//            workbook.write(outputStream);
//            workbook.close();
//            outputStream.flush();
//            outputStream.close();
//
//            resultFile.setFileName(key + "tpl.xlsx");
//            resultFile.setFilePath(filePath);
//            resultFile.setKey(key);
//            resultFileList.add(resultFile);
//        }
//
//        return resultFileList;
//
//    }

//    public static List<ResultFile> doSplitByBigDeptInMultiThread(File resRile , String basePath) throws Exception {
//
//        //1.声明线程池
//        ExecutorService executor = Executors.newCachedThreadPool();
//
//
//        List<ResultFile> resultFileList = new ArrayList<>();
//        basePath = getBasePath(basePath);
//
//        //根据文件Workbook对象
//        Workbook wb = getWorkBookFromFile(resRile);
//        int[] sheet0keyCols  = {0} ;
//        ReadSheetTask task0 = new ReadSheetTask(wb.getSheetAt(0),3,sheet0keyCols);
//        FutureTask<Map<String, List<Row>>> futureTask0 = new FutureTask(task0);
//        executor.submit(futureTask0);
//
//
//
//        int[] sheet1keyCols  = {0} ;
//        ReadSheetTask task1 = new ReadSheetTask(wb.getSheetAt(1),1,sheet1keyCols);
//        FutureTask<Map<String, List<Row>>> futureTask1= new FutureTask(task1);
//        executor.submit(futureTask1);
//
//
//        int[] sheet2keyCols  = {2} ;
//        ReadSheetTask task2 = new ReadSheetTask(wb.getSheetAt(2),2,sheet2keyCols);
//        FutureTask<Map<String, List<Row>>> futureTask2= new FutureTask(task2);
//        executor.submit(futureTask2);
//
//
//        int[] sheet3keyCols  = {6} ;
//        ReadSheetTask task3 = new ReadSheetTask(wb.getSheetAt(3),1,sheet3keyCols);
//        FutureTask<Map<String, List<Row>>> futureTask3= new FutureTask(task3);
//        executor.submit(futureTask3);
//
//        Map<String, List<Row>> sheet0Map = futureTask0.get() ;
//        Map<String, List<Row>> sheet1Map = futureTask1.get() ;
//        Map<String, List<Row>> sheet2Map = futureTask2.get() ;
//        Map<String, List<Row>> sheet3Map = futureTask3.get() ;
//
//        wb.close();
//
//
//        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
//            //原来加载的是模板的头，因为模板头总是变，改为用用户上传的excel去掉数据的部分做模板头
//            //Workbook workbook = WorkbookFactory.create(tplFile);
//            Workbook workbook =  getWorkBookFromFile(resRile);
//            ResultFile resultFile = new ResultFile();
//
//            String key = entry.getKey();
//            Sheet resSheet0 = workbook.getSheetAt(0) ;
//            removeDataRow(resSheet0,3);
//            WriteSheetTask writeSheetTask0 = new WriteSheetTask(resSheet0,entry.getValue());
//            FutureTask<Sheet> writeSheetFuture0 = new FutureTask(writeSheetTask0);
//            executor.submit(writeSheetFuture0);
//
//
//
//            Sheet resSheet1 = workbook.getSheetAt(1) ;
//            removeDataRow(resSheet1,1);
//            WriteSheetTask writeSheetTask1 = new WriteSheetTask(resSheet1,sheet1Map.get(key));
//            FutureTask<Sheet> writeSheetFuture1 = new FutureTask(writeSheetTask1);
//            executor.submit(writeSheetFuture1);
//
//            Sheet resSheet2 = workbook.getSheetAt(2) ;
//            removeDataRow(resSheet2,2);
//            WriteSheetTask writeSheetTask2 = new WriteSheetTask(resSheet2,sheet2Map.get(key));
//            FutureTask<Sheet> writeSheetFuture2 = new FutureTask(writeSheetTask2);
//            executor.submit(writeSheetFuture2);
//
//            Sheet resSheet3 = workbook.getSheetAt(3) ;
//            removeDataRow(resSheet3,1);
//            WriteSheetTask writeSheetTask3 = new WriteSheetTask(resSheet3,sheet3Map.get(key));
//            FutureTask<Sheet> writeSheetFuture3 = new FutureTask(writeSheetTask3);
//            executor.submit(writeSheetFuture3);
//
//
//            Sheet sheet0 = writeSheetFuture0.get();
//            Sheet sheet1 = writeSheetFuture1.get();
//            Sheet sheet2 = writeSheetFuture2.get();
//            Sheet sheet3 = writeSheetFuture3.get();
//
//            doMergeCell(sheet1, 4);
//            doMergeCell(sheet1, 5);
//            String filePath = basePath + key + "tpl.xlsx";
//
//            OutputStream outputStream = new FileOutputStream(filePath);
//            workbook.write(outputStream);
//            workbook.close();
//            outputStream.flush();
//            outputStream.close();
//
//            resultFile.setFileName(key + "tpl.xlsx");
//            resultFile.setFilePath(filePath);
//            resultFile.setKey(key);
//            resultFileList.add(resultFile);
//        }
//
//        return resultFileList;
//
//    }

    public static List<ResultFile> doSplitByBigDeptInMultiThreadAll(File resRile , String basePath) throws Exception {
        //1.声明线程池
        ExecutorService executor = Executors.newCachedThreadPool();

        List<ResultFile> resultFileList = Collections.synchronizedList(new ArrayList<ResultFile>());
        basePath = getBasePath(basePath);

        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] sheet0keyCols  = {0} ;
        ReadSheetTask task0 = new ReadSheetTask(wb.getSheetAt(0),3,sheet0keyCols);
        FutureTask<Map<String, List<Row>>> futureTask0 = new FutureTask(task0);
        executor.submit(futureTask0);

        int[] sheet1keyCols  = {0} ;
        ReadSheetTask task1 = new ReadSheetTask(wb.getSheetAt(1),1,sheet1keyCols);
        FutureTask<Map<String, List<Row>>> futureTask1= new FutureTask(task1);
        executor.submit(futureTask1);

        int[] sheet2keyCols  = {2} ;
        ReadSheetTask task2 = new ReadSheetTask(wb.getSheetAt(2),2,sheet2keyCols);
        FutureTask<Map<String, List<Row>>> futureTask2= new FutureTask(task2);
        executor.submit(futureTask2);

        int[] sheet3keyCols  = {6} ;
        ReadSheetTask task3 = new ReadSheetTask(wb.getSheetAt(3),1,sheet3keyCols);
        FutureTask<Map<String, List<Row>>> futureTask3= new FutureTask(task3);
        executor.submit(futureTask3);

        Map<String, List<Row>> sheet0Map = futureTask0.get() ;
        Map<String, List<Row>> sheet1Map = futureTask1.get() ;
        Map<String, List<Row>> sheet2Map = futureTask2.get() ;
        Map<String, List<Row>> sheet3Map = futureTask3.get() ;
        wb.close();

        CountDownLatch countDownLatch = new CountDownLatch(sheet0Map.size());

        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            LoopTask loopTask = new LoopTask(countDownLatch,
                    resultFileList,
                    resRile,
                    basePath,
                    entry,
                    sheet1Map,
                    sheet2Map,
                    sheet3Map);
            FutureTask<Integer> loopFuture = new FutureTask(loopTask);
            executor.submit(loopFuture);
        }

        countDownLatch.await();

        return resultFileList;

    }

    public static List<ResultFile> doSplitByBigDeptInMultiThreadNotLoopTask(File resRile , String basePath) throws Exception {
        //1.声明线程池
        ExecutorService executor = Executors.newCachedThreadPool();

        List<ResultFile> resultFileList = Collections.synchronizedList(new ArrayList<ResultFile>());
        basePath = getBasePath(basePath);

        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] sheet0keyCols  = {0} ;
        ReadSheetTask task0 = new ReadSheetTask(wb.getSheetAt(0),3,sheet0keyCols);
        FutureTask<Map<String, List<Row>>> futureTask0 = new FutureTask(task0);
        executor.submit(futureTask0);

        int[] sheet1keyCols  = {0} ;
        ReadSheetTask task1 = new ReadSheetTask(wb.getSheetAt(1),1,sheet1keyCols);
        FutureTask<Map<String, List<Row>>> futureTask1= new FutureTask(task1);
        executor.submit(futureTask1);

        int[] sheet2keyCols  = {2} ;
        ReadSheetTask task2 = new ReadSheetTask(wb.getSheetAt(2),2,sheet2keyCols);
        FutureTask<Map<String, List<Row>>> futureTask2= new FutureTask(task2);
        executor.submit(futureTask2);

        int[] sheet3keyCols  = {6} ;
        ReadSheetTask task3 = new ReadSheetTask(wb.getSheetAt(3),1,sheet3keyCols);
        FutureTask<Map<String, List<Row>>> futureTask3= new FutureTask(task3);
        executor.submit(futureTask3);

        Map<String, List<Row>> sheet0Map = futureTask0.get() ;
        Map<String, List<Row>> sheet1Map = futureTask1.get() ;
        Map<String, List<Row>> sheet2Map = futureTask2.get() ;
        Map<String, List<Row>> sheet3Map = futureTask3.get() ;
        wb.close();

        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            ResultFile resultFile = new ResultFile();
            //原来加载的是模板的头，因为模板头总是变，改为用用户上传的excel去掉数据的部分做模板头
            //Workbook workbook = WorkbookFactory.create(tplFile);
            Workbook workbook = ExcelSplitUtil.getWorkBookFromFile(resRile);
            CellStyle style  = workbook.createCellStyle() ;
            String key = entry.getKey();
            Sheet resSheet0 = workbook.getSheetAt(0) ;
            ExcelSplitUtil.removeDataRow(resSheet0,3);
            resSheet0 = ExcelSplitUtil.insertRowToSheet(resSheet0, entry.getValue(),style);

            Sheet resSheet1 = workbook.getSheetAt(1) ;
            ExcelSplitUtil.removeDataRow(resSheet1,1);
            resSheet1 = ExcelSplitUtil.insertRowToSheet(resSheet1,sheet1Map.get(key),style);


            Sheet resSheet2 = workbook.getSheetAt(2) ;
            ExcelSplitUtil.removeDataRow(resSheet2,2);
            resSheet2 = ExcelSplitUtil.insertRowToSheet(resSheet2,sheet2Map.get(key),style);


            Sheet resSheet3 = workbook.getSheetAt(3) ;
            ExcelSplitUtil.removeDataRow(resSheet3,1);
            resSheet3 = ExcelSplitUtil.insertRowToSheet(resSheet3,sheet3Map.get(key),style);


            ExcelSplitUtil.autoSizeColumn(resSheet0);
            ExcelSplitUtil.autoSizeColumn(resSheet1);
            ExcelSplitUtil.autoSizeColumn(resSheet2);
            ExcelSplitUtil.autoSizeColumn(resSheet3);


            ExcelSplitUtil.doMergeCell(resSheet1, 4);
            ExcelSplitUtil.doMergeCell(resSheet1, 5);
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
        }

        return resultFileList;

    }





    public static void removeDataRow(Sheet sheet ,  int dataRowStartIndex){
        int rowSize = sheet.getLastRowNum() ;
        for (int i = rowSize  ; i >= dataRowStartIndex ; i--){
            Row row = sheet.getRow(i);
            if (null == row){
                continue;
            }
            sheet.removeRow(row);
        }

    }


//    public  static List<ResultFile> doSplitBySmallDeptInMultiThread(File resRile, String basePath) throws Exception {
//        //1.声明线程池
//        ExecutorService executor = Executors.newCachedThreadPool();
//
//        List<ResultFile> resultFileList = new ArrayList<>();
//        basePath = getBasePath(basePath);
//        //根据文件Workbook对象
//        Workbook wb = getWorkBookFromFile(resRile);
//
//        int[] sheet0keyCols  = {1} ;
//        ReadSheetTask task0 = new ReadSheetTask(wb.getSheetAt(0),3,sheet0keyCols);
//        FutureTask<Map<String, List<Row>>> futureTask0 = new FutureTask(task0);
//        executor.submit(futureTask0);
//
//        int[] sheet1keyCols  = {1} ;
//        ReadSheetTask task1 = new ReadSheetTask(wb.getSheetAt(1),1,sheet1keyCols);
//        FutureTask<Map<String, List<Row>>> futureTask1 = new FutureTask(task1);
//        executor.submit(futureTask1);
//
//        int[] sheet2keyCols  = {1} ;
//        ReadSheetTask task2 = new ReadSheetTask(wb.getSheetAt(2),2,sheet2keyCols);
//        FutureTask<Map<String, List<Row>>> futureTask2 = new FutureTask(task2);
//        executor.submit(futureTask2);
//
//        int[] sheet3keyCols  = {5} ;
//        ReadSheetTask task3 = new ReadSheetTask(wb.getSheetAt(3),1,sheet3keyCols);
//        FutureTask<Map<String, List<Row>>> futureTask3 = new FutureTask(task3);
//        executor.submit(futureTask3);
//
//        Map<String, List<Row>> sheet0Map = futureTask0.get() ;
//        Map<String, List<Row>> sheet1Map = futureTask1.get() ;
//        Map<String, List<Row>> sheet2Map = futureTask2.get() ;
//        Map<String, List<Row>> sheet3Map = futureTask3.get() ;
//        wb.close();
//
//
//
//        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
//            ResultFile resultFile = new ResultFile();
//            //原来加载的是模板的头，因为模板头总是变，改为用用户上传的excel去掉数据的部分做模板头
//            //Workbook workbook = WorkbookFactory.create(tplFile);
//            Workbook workbook = getWorkBookFromFile(resRile);
//            String key = entry.getKey();
//            Sheet resSheet0 = workbook.getSheetAt(0) ;
//            removeDataRow(resSheet0,3);
//            WriteSheetTask writeSheetTask0 = new WriteSheetTask(resSheet0,entry.getValue());
//            FutureTask<Sheet> writeSheetFuture0 = new FutureTask(writeSheetTask0);
//            executor.submit(writeSheetFuture0);
//
//            Sheet resSheet1 = workbook.getSheetAt(1) ;
//            removeDataRow(resSheet1,1);
//            WriteSheetTask writeSheetTask1 = new WriteSheetTask(resSheet1,sheet1Map.get(key));
//            FutureTask<Sheet> writeSheetFuture1 = new FutureTask(writeSheetTask1);
//            executor.submit(writeSheetFuture1);
//
//            Sheet resSheet2 = workbook.getSheetAt(2) ;
//            removeDataRow(resSheet2,2);
//            WriteSheetTask writeSheetTask2 = new WriteSheetTask(resSheet2,sheet2Map.get(key));
//            FutureTask<Sheet> writeSheetFuture2 = new FutureTask(writeSheetTask2);
//            executor.submit(writeSheetFuture2);
//
//            Sheet resSheet3 = workbook.getSheetAt(3) ;
//            removeDataRow(resSheet3,1);
//            WriteSheetTask writeSheetTask3 = new WriteSheetTask(resSheet3, sheet3Map.get(key));
//            FutureTask<Sheet> writeSheetFuture3 = new FutureTask(writeSheetTask3);
//            executor.submit(writeSheetFuture3);
//
//            Sheet sheet0 = writeSheetFuture0.get();
//            Sheet sheet1 = writeSheetFuture1.get();
//            Sheet sheet2 = writeSheetFuture2.get();
//            Sheet sheet3 = writeSheetFuture3.get();
//
//
//            doMergeCell(sheet1, 4);
//            doMergeCell(sheet1, 5);
//            String filePath = basePath + key + "tpl.xlsx";
//            OutputStream outputStream = new FileOutputStream(filePath);
//            workbook.write(outputStream);
//            outputStream.flush();
//            outputStream.close();
//
//            resultFile.setFileName(key + "tpl.xlsx");
//            resultFile.setFilePath(filePath);
//            resultFile.setKey(key);
//            resultFileList.add(resultFile);
//        }
//        return resultFileList;
//    }


    public  static List<ResultFile> doSplitBySmallDeptInMultiThreadAll(File resRile, String basePath) throws Exception {
        //1.声明线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        List<ResultFile> resultFileList = Collections.synchronizedList(new ArrayList<ResultFile>());

        basePath = getBasePath(basePath);
        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);

        int[] sheet0keyCols  = {1} ;
        ReadSheetTask task0 = new ReadSheetTask(wb.getSheetAt(0),3,sheet0keyCols);
        FutureTask<Map<String, List<Row>>> futureTask0 = new FutureTask(task0);
        executor.submit(futureTask0);

        int[] sheet1keyCols  = {1} ;
        ReadSheetTask task1 = new ReadSheetTask(wb.getSheetAt(1),1,sheet1keyCols);
        FutureTask<Map<String, List<Row>>> futureTask1 = new FutureTask(task1);
        executor.submit(futureTask1);

        int[] sheet2keyCols  = {1} ;
        ReadSheetTask task2 = new ReadSheetTask(wb.getSheetAt(2),2,sheet2keyCols);
        FutureTask<Map<String, List<Row>>> futureTask2 = new FutureTask(task2);
        executor.submit(futureTask2);

        int[] sheet3keyCols  = {5} ;
        ReadSheetTask task3 = new ReadSheetTask(wb.getSheetAt(3),1,sheet3keyCols);
        FutureTask<Map<String, List<Row>>> futureTask3 = new FutureTask(task3);
        executor.submit(futureTask3);

        Map<String, List<Row>> sheet0Map = futureTask0.get() ;
        Map<String, List<Row>> sheet1Map = futureTask1.get() ;
        Map<String, List<Row>> sheet2Map = futureTask2.get() ;
        Map<String, List<Row>> sheet3Map = futureTask3.get() ;
        wb.close();

        CountDownLatch countDownLatch = new CountDownLatch(sheet0Map.size());

        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            LoopTask loopTask = new LoopTask(countDownLatch,
                    resultFileList,
                    resRile,
                    basePath,
                    entry,
                    sheet1Map,
                    sheet2Map,
                    sheet3Map);
            FutureTask<Integer> loopFuture = new FutureTask(loopTask);
            executor.submit(loopFuture);
        }
        countDownLatch.await();
        return resultFileList;
    }

    public  static List<ResultFile> doSplitBySmallDeptInMultiThreadNotLoopTask(File resRile, String basePath) throws Exception {
        //1.声明线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        List<ResultFile> resultFileList = Collections.synchronizedList(new ArrayList<ResultFile>());

        basePath = getBasePath(basePath);
        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);

        int[] sheet0keyCols  = {1} ;
        ReadSheetTask task0 = new ReadSheetTask(wb.getSheetAt(0),3,sheet0keyCols);
        FutureTask<Map<String, List<Row>>> futureTask0 = new FutureTask(task0);
        executor.submit(futureTask0);

        int[] sheet1keyCols  = {1} ;
        ReadSheetTask task1 = new ReadSheetTask(wb.getSheetAt(1),1,sheet1keyCols);
        FutureTask<Map<String, List<Row>>> futureTask1 = new FutureTask(task1);
        executor.submit(futureTask1);

        int[] sheet2keyCols  = {1} ;
        ReadSheetTask task2 = new ReadSheetTask(wb.getSheetAt(2),2,sheet2keyCols);
        FutureTask<Map<String, List<Row>>> futureTask2 = new FutureTask(task2);
        executor.submit(futureTask2);

        int[] sheet3keyCols  = {5} ;
        ReadSheetTask task3 = new ReadSheetTask(wb.getSheetAt(3),1,sheet3keyCols);
        FutureTask<Map<String, List<Row>>> futureTask3 = new FutureTask(task3);
        executor.submit(futureTask3);

        Map<String, List<Row>> sheet0Map = futureTask0.get() ;
        Map<String, List<Row>> sheet1Map = futureTask1.get() ;
        Map<String, List<Row>> sheet2Map = futureTask2.get() ;
        Map<String, List<Row>> sheet3Map = futureTask3.get() ;
        wb.close();


        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            ResultFile resultFile = new ResultFile();
            //原来加载的是模板的头，因为模板头总是变，改为用用户上传的excel去掉数据的部分做模板头
            //Workbook workbook = WorkbookFactory.create(tplFile);
            Workbook workbook = ExcelSplitUtil.getWorkBookFromFile(resRile);
            CellStyle style  = workbook.createCellStyle() ;
            String key = entry.getKey();
            Sheet resSheet0 = workbook.getSheetAt(0) ;
            ExcelSplitUtil.removeDataRow(resSheet0,3);
            resSheet0 = ExcelSplitUtil.insertRowToSheet(resSheet0, entry.getValue(),style);

            Sheet resSheet1 = workbook.getSheetAt(1) ;
            ExcelSplitUtil.removeDataRow(resSheet1,1);
            resSheet1 = ExcelSplitUtil.insertRowToSheet(resSheet1,sheet1Map.get(key),style);


            Sheet resSheet2 = workbook.getSheetAt(2) ;
            ExcelSplitUtil.removeDataRow(resSheet2,2);
            resSheet2 = ExcelSplitUtil.insertRowToSheet(resSheet2,sheet2Map.get(key),style);


            Sheet resSheet3 = workbook.getSheetAt(3) ;
            ExcelSplitUtil.removeDataRow(resSheet3,1);
            resSheet3 = ExcelSplitUtil.insertRowToSheet(resSheet3,sheet3Map.get(key),style);


            ExcelSplitUtil.autoSizeColumn(resSheet0);
            ExcelSplitUtil.autoSizeColumn(resSheet1);
            ExcelSplitUtil.autoSizeColumn(resSheet2);
            ExcelSplitUtil.autoSizeColumn(resSheet3);


            ExcelSplitUtil.doMergeCell(resSheet1, 4);
            ExcelSplitUtil.doMergeCell(resSheet1, 5);
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
        }

        return resultFileList;
    }





//    public static List<ResultFile> doSplitBySmallDept(File resRile, File tplFile, String basePath) throws Exception {
//        List<ResultFile> resultFileList = new ArrayList<>();
//        basePath = getBasePath(basePath);
//        //根据文件Workbook对象
//        Workbook wb = getWorkBookFromFile(resRile);
//        int[] sheet0keyCols  = {1} ;
//        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 3, sheet0keyCols);
//        int[] sheet1keyCols  = {1} ;
//        Map<String, List<Row>> sheet1Map = getRowMapFromSheet(wb.getSheetAt(1), 1, sheet1keyCols);
//        int[] sheet2keyCols  = {1} ;
//        Map<String, List<Row>> sheet2Map = getRowMapFromSheet(wb.getSheetAt(2), 2, sheet2keyCols);
//        int[] sheet3keyCols  = {5} ;
//        Map<String, List<Row>> sheet3Map = getRowMapFromSheet(wb.getSheetAt(3), 1, sheet3keyCols);
//        wb.close();
//
//        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
//            ResultFile resultFile = new ResultFile();
//            //原来加载的是模板的头，因为模板头总是变，改为用用户上传的excel去掉数据的部分做模板头
//            //Workbook workbook = WorkbookFactory.create(tplFile);
//            Workbook workbook = getWorkBookFromFile(resRile);
//
//            String key = entry.getKey();
//            Sheet resSheet0 = workbook.getSheetAt(0) ;
//            removeDataRow(resSheet0,3);
//            Sheet sheet0 = insertRowToSheet(resSheet0, entry.getValue());
//
//            Sheet resSheet1 = workbook.getSheetAt(1) ;
//            removeDataRow(resSheet1,1);
//            Sheet sheet1 = insertRowToSheet(resSheet1, sheet1Map.get(key));
//
//            Sheet resSheet2 = workbook.getSheetAt(2) ;
//            removeDataRow(resSheet2,2);
//            Sheet sheet2 = insertRowToSheet(resSheet2, sheet2Map.get(key));
//
//            Sheet resSheet3 = workbook.getSheetAt(3) ;
//            removeDataRow(resSheet3,1);
//            Sheet sheet3 = insertRowToSheet(resSheet3, sheet3Map.get(key));
//
//
//            doMergeCell(sheet1, 4);
//            doMergeCell(sheet1, 5);
//            String filePath = basePath + key + "tpl.xlsx";
//            OutputStream outputStream = new FileOutputStream(filePath);
//            workbook.write(outputStream);
//            outputStream.flush();
//            outputStream.close();
//
//            resultFile.setFileName(key + "tpl.xlsx");
//            resultFile.setFilePath(filePath);
//            resultFile.setKey(key);
//            resultFileList.add(resultFile);
//        }
//        return resultFileList;
//    }

    public static void doMergeCell(Sheet sheet, int mergeColIndex) {
        int rowStart = 1;
        int deptColIndex = 1;
        for (int i = rowStart; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (null == row){
                break;
            }
            Cell cell = row.getCell(deptColIndex);
            Cell dataCell_i = row.getCell(mergeColIndex);
            if (null == dataCell_i) {
                continue;
            }
            String value_i = getCellValueStr(dataCell_i);
            dataCell_i.setCellType(Cell.CELL_TYPE_STRING);
            dataCell_i.setCellValue(value_i);
            String deptCellValue = getCellValueStr(cell);
            for (int j = i + 1; j <= sheet.getLastRowNum(); j++) {
                Row tmpRow = sheet.getRow(j);
                Cell tmpCell = tmpRow.getCell(deptColIndex);
                String tmpDeptCellValue = getCellValueStr(tmpCell);
                Cell dataCell_j = tmpRow.getCell(mergeColIndex);
                if (null == dataCell_j) {
                    continue;
                }
                String value_j = getCellValueStr(dataCell_j);

                if (!StringUtils.equals(deptCellValue, tmpDeptCellValue) || !StringUtils.equals(value_i, value_j)) {
//                    row.getCell(mergeColIndex).setCellValue(getCellValueStr(tmpRow.getCell(mergeColIndex)));
                    // 合并日期占两行(4个参数，分别为起始行，结束行，起始列，结束列)
                    // 行和列都是从0开始计数，且起始结束都会合并
                    // 这里是合并excel中日期的两行为一行
                    if (i != j - 1) {
                        CellRangeAddress region = new CellRangeAddress(i, j - 1, mergeColIndex, mergeColIndex);
                        sheet.addMergedRegion(region);
                        i = j - 1;
                        break;
                    } else {
                        i = j - 1;
                        break;
                    }

                } else if (j == sheet.getLastRowNum()&& StringUtils.equals(deptCellValue, tmpDeptCellValue) && StringUtils.equals(value_i, value_j)) {
                    CellRangeAddress region = new CellRangeAddress(i, j, mergeColIndex, mergeColIndex);
                    sheet.addMergedRegion(region);
                    i = j;
                    break;
                }
            }
        }
    }

    private static String getBasePath(String basePath) {

        String dateStr = dateFormat.format(new Date());
        String result = basePath + File.separator + dateStr + File.separator + UUID.randomUUID10() + File.separator;
        createDirectory(result);
        return result;

    }

    private static void createDirectory(String path) {
        if (StringUtils.isEmpty(path)) {
            return;
        }
        try {
            // 获得文件对象
            File f = new File(path);
            if (!f.exists()) {
                // 如果路径不存在,则创建
                f.mkdirs();
            }
        } catch (Exception e) {
            LOGGER.error("创建目录错误.path=" + path, e);
        }
    }

    private static void createFile(String path)  {
        if (StringUtils.isEmpty(path)) {
            return;
        }
        try {
            // 获得文件对象
            File f = new File(path);
            if (f.exists()) {
                return;
            }
            // 如果路径不存在,则创建
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            f.createNewFile();
        } catch (Exception e) {
            LOGGER.error("创建文件错误.path=" + path, e);
        }
    }

    public static Map<String, List<Row>> getRowMapFromSheet(Sheet sheet, int rowStart, int[] keyCols) throws Exception {
        Map<String, List<Row>> map = new HashMap<>();
        for (int i = rowStart; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (null == row) {
                break;
            }
            String key = "";
            for (int keyCol : keyCols) {
                Cell keyCell = row.getCell(keyCol);
                if (null == keyCell) {
                    break;
                }
                if (StringUtils.isEmpty(key)) {
                    key = getCellValueStr(keyCell);
                    if (StringUtils.isEmpty(key)){
                        break;
                    }
                }else {
                    key = key + "_" +  getCellValueStr(keyCell);
                }
            }
            if (StringUtils.isEmpty(key)){
                continue;
            }
            if(keyCols.length==3) {
                String[] keyArr = key.split("_") ;
                if (keyArr.length < 3){
                    continue;
                }
                String deptName = keyArr[0];
                String userNames = keyArr[1];
                String className =keyArr[2];
                if (StringUtils.isEmpty(deptName)||StringUtils.isEmpty(userNames)||StringUtils.isEmpty(className)){
                    continue;
                }
                String [] userNamesArr = keyArr[1].split("\\|");
                for (String name : userNamesArr) {
                    List<Row> rowList = map.get(deptName+"_"+name+"_"+className);

                    //如果以当前行第一列值作为键值取不到工作表
                    if (rowList == null) {
                        rowList = new ArrayList<>();
                    }
                    rowList.add(row);
                    map.put(deptName+"_"+name+"_"+className, rowList);
                }
            }else{
                    List<Row> rowList = map.get(key);
                    //如果以当前行第一列值作为键值取不到工作表
                    if (rowList == null) {
                        rowList = new ArrayList<>();
                    }
                    rowList.add(row);
                    map.put(key, rowList);
            }
        }
        return map;
    }

    public static Sheet insertRowToSheet(Sheet sheet, List<Row> rowList)throws Exception {
        if (CollectionUtils.isEmpty(rowList)) {
            return sheet;
        }
        Workbook workbook = sheet.getWorkbook() ;
        CellStyle style = workbook.createCellStyle() ;
        for (Row row : rowList) {
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            insertCellToRow(newRow, row,style);
        }
        return sheet;
    }
    public static Sheet insertRowToSheet(Sheet sheet, List<Row> rowList,CellStyle style) throws Exception{
        if (CollectionUtils.isEmpty(rowList)) {
            return sheet;
        }
        for (Row row : rowList) {
            Row newRow = sheet.createRow(sheet.getLastRowNum() + 1);
            insertCellToRow(newRow, row, style);
        }
        return sheet;
    }

    public static void insertCellToRow(Row newRow, Row row,CellStyle style)throws Exception {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (null == cell || null == cell.getCellStyle()){
                continue;
            }
            Cell newCell = newRow.createCell(i);

            setCellValue(newCell, cell,style);
        }

    }

    //将一个单元格的值赋给另一个单元格
    public static void setCellValue(Cell newCell, Cell cell, CellStyle style)  throws Exception{

//        style.cloneStyleFrom(cell.getCellStyle());
//        newCell.setCellStyle(style);

        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_BOOLEAN:
            newCell.setCellValue(cell.getBooleanCellValue());
            break;
        case Cell.CELL_TYPE_NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                newCell.setCellType(Cell.CELL_TYPE_STRING);
                try {
                    newCell.setCellValue(getDateStr(cell));
                }catch (Exception e){
                    newCell.setCellValue(getCellValueStr(cell));
                    LOGGER.error(e.getLocalizedMessage());
                }

                break;
            } else {
                //读取数字
                double num = cell.getNumericCellValue();
                if (!isIntegerForDouble(num)) {
                    newCell.setCellValue(num);
                    break;
                } else {
                    newCell.setCellType(Cell.CELL_TYPE_STRING);
                    newCell.setCellValue(String.valueOf(new Double(num).intValue()));
                }
                break;
            }
        case Cell.CELL_TYPE_FORMULA:
//            newCell.setCellValue(cell.getCellFormula());
            //执行公式
//            try {
////                newCell.setCellValue(String.valueOf(getCellValueStr(cell)));
////            } catch (IllegalStateException e) {
////                newCell.setCellValue(get2f(cell.getNumericCellValue()));
////            }
            try {
                newCell.setCellValue(String.valueOf(cell.getStringCellValue()));
            } catch (IllegalStateException e) {
                newCell.setCellValue(get2f(cell.getNumericCellValue()));
            }
            break;
        case Cell.CELL_TYPE_STRING:
            newCell.setCellValue(cell.getStringCellValue());
            break;
        default:
            newCell.setCellValue("");
        }

    }

    public static void autoSizeColumn(Sheet sheet){
        for(Row row : sheet){
            if (null != row){
                for (Cell cell : row){
                    if (null != cell){
                        sheet.autoSizeColumn(cell.getColumnIndex());
                    }
                }
            }
            break;
        }
    }

    /**
     * 判断double是否是整数
     */
    public static boolean isIntegerForDouble(double num) {
        double eps = 1e-10;  // 精度范围
        return num - Math.floor(num) < eps;
    }

    private static String getDateStr(Cell cell) {
        //1、判断是否是数值格式
        short format = cell.getCellStyle().getDataFormat();
        SimpleDateFormat sdf = null;
        if (format == 14 || format == 31 || format == 57 || format == 58) {
            //日期
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        } else if (format == 20 || format == 32) {
            //时间
            sdf = new SimpleDateFormat("HH:mm");
        }
        double value = cell.getNumericCellValue();
        Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);

        return sdf.format(date);
    }

    private static String get2f(double num) {
        return String.format("%.2f", num);
    }

    //拆分本部  课题执行情况表
    public static List<ResultFile> doSplitByBigDept4TopicImplementation(File resRile, String fileName, String basePath,
            FinancialProcessTableEOService financialProcessTableEOService) throws Exception {

        List<ResultFile> resultFileList = new ArrayList<>();

        basePath = getBasePath(basePath);

        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] keyCols = {2};
        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 2, keyCols);
        Map<String, List<Row>> sheet1Map = getRowMapFromSheet(wb.getSheetAt(1), 3, keyCols);

        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);

            String key = entry.getKey();
            insertRowToSheet(workbook.getSheetAt(0), entry.getValue());
            insertRowToSheet(workbook.getSheetAt(1), sheet1Map.get(key));

            String filePath = basePath + key + "tpl.xlsx";

            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

            resultFile.setFileName(key + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(key);
            resultFileList.add(resultFile);
        }
        return resultFileList;
    }

    //拆分  课题执行情况表
    public static List<ResultFile> doSplitByDept4TopicImplementation(File resRile, String fileName, String basePath,
            FinancialProcessTableEOService financialProcessTableEOService) throws Exception {

        List<ResultFile> resultFileList = new ArrayList<>();

        basePath = getBasePath(basePath);

        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] keyCols = {3};
        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 2, keyCols);

        Map<String, List<Row>> sheet1Map = getRowMapFromSheet(wb.getSheetAt(1), 3, keyCols);
        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);

            String key = entry.getKey();
            insertRowToSheet(workbook.getSheetAt(0), entry.getValue());
            insertRowToSheet(workbook.getSheetAt(1), sheet1Map.get(key));

            String filePath = basePath + key + "tpl.xlsx";

            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

            resultFile.setFileName(key + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(key);
            resultFileList.add(resultFile);
        }
        return resultFileList;
    }


    //拆分  课题执行情况表,根据联络人
    public static synchronized List<ResultFile> doSplitByCaller4TopicImplementation(File resRile, String fileName,String basePath,
            FinancialProcessTableEOService financialProcessTableEOService) throws Exception {

        List<ResultFile> resultFileList = new ArrayList<>();

        basePath = getBasePath(basePath);

        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] keyCols = {3,5,1};
        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 2, keyCols);
        int[] keyCols1 = {5};
        Map<String, List<Row>> sheet1Map = getRowMapFromSheet(wb.getSheetAt(1), 3, keyCols1);

        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            ResultFile resultFile = new ResultFile();
            Workbook workbook = WorkbookFactory.create(tplFile);

            String key = entry.getKey();
            String[] keyArr = key.split("_") ;
            if (keyArr.length < 3){
                continue;
            }
            String deptName = keyArr[0];
            String userNames = keyArr[1];
            String className =keyArr[2];
            if (StringUtils.isEmpty(deptName)||StringUtils.isEmpty(userNames)||StringUtils.isEmpty(className)){
                continue;
            }
            String newKey =  keyArr[0]+"_"+keyArr[1]; //部门加人名
//            key = key + UUID.randomUUID10();
            insertRowToSheet(workbook.getSheetAt(0), entry.getValue());
            insertRowToSheet(workbook.getSheetAt(1), sheet1Map.get(className));
            String filePath = basePath + newKey + "tpl.xlsx";

            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();

            resultFile.setFileName(newKey +  "tpl.xlsx");
            resultFile.setFilePath(filePath);

            resultFile.setKey(newKey);
            resultFileList.add(resultFile);
        }
        return resultFileList;
    }


    //拆分本部  往来账款催办表
    public static List<ResultFile> doSplitByBigDept4ReciprocalAccountRequest(File resRile, String fileName, String basePath,
            FinancialProcessTableEOService financialProcessTableEOService) throws Exception {

        List<ResultFile> resultFileList = new ArrayList<>();

        basePath = getBasePath(basePath);

        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] keyCols = {4};
        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 1, keyCols);

        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);

            String key = entry.getKey();
            insertRowToSheet(workbook.getSheetAt(0), entry.getValue());

            String filePath = basePath + key + "tpl.xlsx";

            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

            resultFile.setFileName(key + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(key);
            resultFileList.add(resultFile);
        }

        return resultFileList;

    }

    //拆分  往来账款催办表
    public static List<ResultFile> doSplitByDept4ReciprocalAccountRequest(File resRile, String fileName, String basePath,
            FinancialProcessTableEOService financialProcessTableEOService) throws Exception {

        List<ResultFile> resultFileList = new ArrayList<>();

        basePath = getBasePath(basePath);

        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] keyCols = {5};
        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 1, keyCols);

        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);

            String key = entry.getKey();
            insertRowToSheet(workbook.getSheetAt(0), entry.getValue());

            String filePath = basePath + key + "tpl.xlsx";

            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

            resultFile.setFileName(key + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(key);
            resultFileList.add(resultFile);
        }

        return resultFileList;

    }

    //拆分 支出合同明细表
    public static List<ResultFile> doSplitByExpenditureContractDetails(File resRile, String fileName, String basePath,
            FinancialProcessTableEOService financialProcessTableEOService) throws Exception {
        List<ResultFile> resultFileList = new ArrayList<>();
        basePath = getBasePath(basePath);
        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] keyCols = {1};
        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 2, keyCols);
        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);
            String key = entry.getKey();
            insertRowToSheet(workbook.getSheetAt(0), entry.getValue());
            String filePath = basePath + key + "tpl.xlsx";
            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

            resultFile.setFileName(key + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(key);
            resultFileList.add(resultFile);
        }

        return resultFileList;

    }

    //拆分本部  部门成本明细表
    public static List<ResultFile> doSplitByBigDept4DepartCostDetails(File resRile, String fileName, String basePath,
            FinancialProcessTableEOService financialProcessTableEOService) throws Exception {
        List<ResultFile> resultFileList = new ArrayList<>();
        basePath = getBasePath(basePath);
        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] keyCols = {4};
        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 2, keyCols);
        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);
            String key = entry.getKey();
            insertRowToSheet(workbook.getSheetAt(0), entry.getValue());
            String filePath = basePath + key + "tpl.xlsx";
            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

            resultFile.setFileName(key + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(key);
            resultFileList.add(resultFile);
        }

        return resultFileList;

    }

    //拆分  部门成本明细表
    public static List<ResultFile> doSplitByDept4DepartCostDetails(File resRile, String fileName, String basePath,
            FinancialProcessTableEOService financialProcessTableEOService) throws Exception {
        List<ResultFile> resultFileList = new ArrayList<>();
        basePath = getBasePath(basePath);
        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] keyCols = {5};
        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 2, keyCols);
        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);
            String key = entry.getKey();
            insertRowToSheet(workbook.getSheetAt(0), entry.getValue());
            String filePath = basePath + key + "tpl.xlsx";
            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

            resultFile.setFileName(key + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(key);
            resultFileList.add(resultFile);
        }

        return resultFileList;

    }


    //拆分本部  应收账款
    public static List<ResultFile> doSplitByBigDept4AccountsReceivable(File resRile, String  fileName, String basePath,
            FinancialProcessTableEOService financialProcessTableEOService) throws Exception {
        List<ResultFile> resultFileList = new ArrayList<>();
        basePath = getBasePath(basePath);
        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] keyCols = {2};
        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 2, keyCols);

        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);
            String key = entry.getKey();
            insertRowToSheet(workbook.getSheetAt(0), entry.getValue());
            String filePath = basePath + key + "tpl.xlsx";
            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

            resultFile.setFileName(key + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(key);
            resultFileList.add(resultFile);
        }

        return resultFileList;

    }

    //拆分  应收账款
    public static List<ResultFile> doSplitByDept4AccountsReceivable(File resRile, String fileName, String basePath,
            FinancialProcessTableEOService financialProcessTableEOService) throws Exception {
        List<ResultFile> resultFileList = new ArrayList<>();
        basePath = getBasePath(basePath);
        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] keyCols = {3};
        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 2, keyCols);

        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);
            String key = entry.getKey();
            insertRowToSheet(workbook.getSheetAt(0), entry.getValue());
            String filePath = basePath + key + "tpl.xlsx";
            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

            resultFile.setFileName(key + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(key);
            resultFileList.add(resultFile);
        }

        return resultFileList;

    }


    //拆分 项目支出

    /**
     * @Description: 只根据部门拆分然后根据部门去找本部（根据layer 去确定，为3的都不发了）
     * @Param:
     * @Return:
     * @Author: dingqiang
     * @Date: 8:49
    **/
    public static List<ResultFile> doSplitByDept4ProjectCost(File resRile, String fileName, String basePath,
            FinancialProcessTableEOService financialProcessTableEOService, OrgEODao orgEODao) throws Exception {
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();

        List<ResultFile> resultFileList = new ArrayList<>();
        basePath = getBasePath(basePath);
        //根据文件Workbook对象
        Workbook wb = getWorkBookFromFile(resRile);
        int[] sheet0KeyCols = {11}; //部门
        Map<String, List<Row>> sheet0Map = getRowMapFromSheet(wb.getSheetAt(0), 1, sheet0KeyCols);
        int[] sheet1KeyCols = {5}; //部门
        Map<String, List<Row>> sheet1Map = getRowMapFromSheet(wb.getSheetAt(1), 1, sheet1KeyCols);

        Set<String> sentBySheet0OrgNameSet = new HashSet<>();

        Map<String, List<Row>> bigOrgSheet0Map = new HashMap<>();
        Map<String, List<Row>> bigOrgSheet1Map = new HashMap<>();

        //三种情况
        //1. sheet0 有a 部门 sheet1有/没有 a 部门     那么该循环能解决
        //2. sheet0 有a 部门 sheet1 没有 a 部门     那么该循环能解决
        //3. sheet0 没有a 部门 sheet1有a部门     那么该循环不能解决，这种问题，将在第二个循环处理
        for (Map.Entry<String, List<Row>> entry : sheet0Map.entrySet()) {
            String orgKey = entry.getKey();
            OrgEO orgEO = getOrgByName(orgEOList,orgKey);
            if (null == orgEO){
                //throw new AdcDaBaseException(orgKey+"部门未找到！");
                continue; //不存在的部门 静默跳过即可
            }
            sentBySheet0OrgNameSet.add(orgKey);
            if (StringUtils.equals(orgEO.getLayer(),"3")){  // layer == 3 的 如管理部 财务部  都当作本部来发 这里就不处理了
                addRowToBigOrgSheetMap(orgEOList,orgEO,sheet0Map.get(orgKey),bigOrgSheet0Map); //根据当前部门，找本部 放到本部map 里去 sheet 0
                addRowToBigOrgSheetMap(orgEOList,orgEO,sheet1Map.get(orgKey),bigOrgSheet1Map); //根据当前部门，找本部 放到本部map 里去 sheet 1
                continue;
            }
            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);
            insertRowToSheet(workbook.getSheetAt(0), entry.getValue());
            addRowToBigOrgSheetMap(orgEOList,orgEO,sheet0Map.get(orgKey),bigOrgSheet0Map); //根据当前部门，找本部 放到本部map 里去 sheet 0

            insertRowToSheet(workbook.getSheetAt(1),sheet1Map.get(orgKey));
            addRowToBigOrgSheetMap(orgEOList,orgEO,sheet1Map.get(orgKey),bigOrgSheet1Map); //根据当前部门，找本部 放到本部map 里去 sheet 1


            String filePath = basePath + orgKey + "tpl.xlsx";
            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            resultFile.setFileName(orgKey + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(orgKey);
            resultFile.setLayer(4);
            resultFile.setOrgId(orgEO.getId());
            resultFileList.add(resultFile);
        }
        //1. sheet0 没有a 部门 sheet1有a部门
        for (Map.Entry<String, List<Row>> entry : sheet1Map.entrySet()) {
            String orgKey = entry.getKey();
            OrgEO orgEO = getOrgByName(orgEOList,orgKey);
            if (null == orgEO){
                throw new AdcDaBaseException(orgKey+"部门未找到！");
            }
            if (sentBySheet0OrgNameSet.contains(orgKey)){
                continue;
            }
            if (StringUtils.equals(orgEO.getLayer(),"3")){  // layer == 3 的 如管理部 财务部  都当作本部来发 这里就不处理了
                addRowToBigOrgSheetMap(orgEOList,orgEO,sheet0Map.get(orgKey),bigOrgSheet0Map); //根据当前部门，找本部 放到本部map 里去 sheet 0
                addRowToBigOrgSheetMap(orgEOList,orgEO,sheet1Map.get(orgKey),bigOrgSheet1Map); //根据当前部门，找本部 放到本部map 里去 sheet 1
                continue;
            }
            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);
            insertRowToSheet(workbook.getSheetAt(0),sheet0Map.get(orgKey));
            addRowToBigOrgSheetMap(orgEOList,orgEO,sheet0Map.get(orgKey),bigOrgSheet0Map); //根据当前部门，找本部 放到本部map 里去 sheet 0

            insertRowToSheet(workbook.getSheetAt(1), entry.getValue());
            addRowToBigOrgSheetMap(orgEOList,orgEO,sheet1Map.get(orgKey),bigOrgSheet1Map); //根据当前部门，找本部 放到本部map 里去 sheet 1

            String filePath = basePath + orgKey + "tpl.xlsx";
            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            resultFile.setFileName(orgKey + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(orgKey);
            resultFile.setLayer(4);
            resultFile.setOrgId(orgEO.getId());
            resultFileList.add(resultFile);
        }

        // 找本部
        //1. sheet0 有a 部门 sheet1有/没有 a 部门     那么该循环能解决
        //2. sheet0 有a 部门 sheet1 没有 a 部门     那么该循环能解决
        //3. sheet0 没有a 部门 sheet1有a部门     那么该循环不能解决，这种问题，将在第二个循环处理
        for (Map.Entry<String, List<Row>> entry : bigOrgSheet0Map.entrySet()) {
            String orgKey = entry.getKey();
            OrgEO orgEO = getOrgByName(orgEOList,orgKey);

            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);
            insertRowToSheet(workbook.getSheetAt(0), entry.getValue());
            insertRowToSheet(workbook.getSheetAt(1),bigOrgSheet1Map.get(orgKey));
            sentBySheet0OrgNameSet.add(orgKey);
            String filePath = basePath + orgKey + "tpl.xlsx";
            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            resultFile.setFileName(orgKey + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(orgKey);
            resultFile.setLayer(3);
            resultFile.setOrgId(orgEO.getId());
            resultFileList.add(resultFile);
        }

        for (Map.Entry<String, List<Row>> entry : bigOrgSheet1Map.entrySet()) {
            String orgKey = entry.getKey();
            OrgEO orgEO = getOrgByName(orgEOList,orgKey);
            if (sentBySheet0OrgNameSet.contains(orgKey)){
                continue;
            }
            ResultFile resultFile = new ResultFile();
            File tplFile = financialProcessTableEOService.getResFile(fileName);
            Workbook workbook = WorkbookFactory.create(tplFile);
            insertRowToSheet(workbook.getSheetAt(1), entry.getValue());
            insertRowToSheet(workbook.getSheetAt(0),bigOrgSheet0Map.get(orgKey));

            String filePath = basePath + orgKey + "tpl.xlsx";
            OutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            resultFile.setFileName(orgKey + "tpl.xlsx");
            resultFile.setFilePath(filePath);
            resultFile.setKey(orgKey);
            resultFile.setLayer(3);
            resultFile.setOrgId(orgEO.getId());
            resultFileList.add(resultFile);
        }


        return resultFileList;

    }

    private  static void addRowToBigOrgSheetMap(List<OrgEO> orgEOList,OrgEO orgEO ,List<Row> rowList, Map<String, List<Row>> sheetMap) {
        if (StringUtils.isEmpty(orgEO.getParentId())) {
            return;
        }
        if (CollectionUtils.isEmpty(rowList)) {
            return;
        }
        OrgEO parentOrgEO;
        if (StringUtils.equals(orgEO.getLayer(), "3")) { // 如果lyaer 是 3  那么它自己就是本部级别
            parentOrgEO = orgEO;
        } else{
            parentOrgEO = getOrgById(orgEOList, orgEO.getParentId());
        }
        if (null == parentOrgEO){
            return;
        }
        List<Row> tempRowList = sheetMap.get(parentOrgEO.getName());
        if (CollectionUtils.isEmpty(tempRowList)){
            tempRowList = new ArrayList<>();
        }
        tempRowList.addAll(rowList);
        sheetMap.put(parentOrgEO.getName(),tempRowList);

    }






    private static OrgEO getOrgByName(List<OrgEO> orgEOList , String orgName){
        for (OrgEO orgEO : orgEOList){
            if (StringUtils.equals(orgName,orgEO.getName())){
                return orgEO;
            }
        }
        return null;
    }
    private static OrgEO getOrgById(List<OrgEO> orgEOList , String orgId){
        for (OrgEO orgEO : orgEOList){
            if (StringUtils.equals(orgId,orgEO.getId())){
                return orgEO;
            }
        }
        return null;
    }



    public static String getCellValueStr(Cell cell) {
        evaluator=cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        String cellString = "";
        if (cell == null) {
            return cellString;
        }
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

    public static List<String> readerHeader(Sheet sheet, int headerRowStart, int headerRowSize, int colNum) {
        List<String> headerList = new ArrayList<>();

        for (int i = headerRowStart; i < headerRowStart + headerRowSize; i++) {
            Row row = sheet.getRow(i);
            if (null == row) {
                break;
            }
            for (int j = 0; j < colNum; j++) {
                Cell keyCell = row.getCell(j);
                if (null == keyCell) {
                    break;
                }
                try {
                    String title = getCellValueStr(keyCell) ;
                    if (StringUtils.isNotEmpty(title)) {
                        headerList.add(title);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return headerList;
    }

    public static boolean isSameHeader(List<String> srcStringList, List<String> stringList) {
        if (stringList.size() == srcStringList.size()) {
            for (int i = 0; i < stringList.size(); i++) {
                if (!(stringList.get(i).equals(srcStringList.get(i)))) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

}
