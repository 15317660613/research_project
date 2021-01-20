package com.adc.da.budget.utils.ppt;

import com.adc.da.budget.dto.PPTDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.POIXMLDocumentPart;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xslf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * poi导出ppt工具类
 * created by chenhaidong 2018/12/4
 */
@Slf4j
public class PPTUtil {

    public static void main(String[] args) throws IOException, XmlException {
        Map<String, String> map = new HashMap<>();
        for (int j = 1; j < 7; j++) map.put("input" + j, 1000 * j + "");
        for (int j = 1; j < 7; j++) map.put("output" + j, 10000 * j + "");
        for (int j = 1; j < 7; j++) map.put("profit" + j, 10 * j + "");

        XMLSlideShow pptx = new XMLSlideShow(new FileInputStream("test222.pptx"));
        String title = "图表标题";
        List<String> groupTitle = new ArrayList<String>(Arrays.asList(new String[]{"分组1","分组2","分组3"}));
        List<String> names = new ArrayList<>(Arrays.asList(new String[] {"2018预算已执行", "2018预算金额","同期对比","2017年度","201801-08","201701-08"}));

        List<List<String>> inputValues = new ArrayList<>();
        List<List<String>> inputValues2 = new ArrayList<>();

        List<String> stringList1 = new ArrayList<String>(Arrays.asList(new String[]{map.get("input4"), map.get("input6"),map.get("input5"),map.get("input3"),map.get("input2"),map.get("input1")}));

        List<String> stringList2 = new ArrayList<String>(Arrays.asList(new String[]{map.get("input5"), map.get("input5"),map.get("input6"),map.get("input5"),map.get("input5"),map.get("input1")}));

        List<String> stringList3 = new ArrayList<String>(Arrays.asList(new String[]{map.get("input2"), map.get("input3"),map.get("input5"),map.get("input1"),map.get("input4"),map.get("input6")}));

        List<String> outputValues = new ArrayList<>(Arrays.asList(new String[]{map.get("output4"), map.get("output6"), map.get("output5"), map.get("output3"), map.get("output2"), map.get("output1")}));

        inputValues.add(stringList1);
        inputValues.add(stringList2);

        inputValues2.add(outputValues);
        inputValues2.add(stringList3);

        List<String> values = new ArrayList<>(Arrays.asList(new String[]{"1","2","3","4","5","6"}));
        List<String> values2 = new ArrayList<>(Arrays.asList(new String[]{"11","10","13","3","20","6"}));

        List<PPTDataDTO> pptDataDTOList = new ArrayList<>();
        PPTDataDTO pptDataDTO1 = new PPTDataDTO(pptx,title,names,groupTitle,inputValues,values);
        PPTDataDTO pptDataDTO2 = new PPTDataDTO(pptx,title,names,groupTitle,inputValues2,values2);
        pptDataDTOList.add(pptDataDTO1);
        pptDataDTOList.add(pptDataDTO2);

//        replaceTableData(pptx,map);
//        buildLineChart(pptx,title,names,values);
//        buildBarChart(pptx,title,groupTitle,names,inputValues);
        buildLineChartAndBarChart(pptDataDTOList);

        OutputStream out = new FileOutputStream("test333.pptx");
        pptx.write(out);
        out.close();
    }

    /**
     *  对ppt表格进行数据填充
     * @param pptx 幻灯片对象
     * @param dataMap 占位符-数据对应map
     * @throws XmlException
     */
    public static void replaceTableData(XMLSlideShow pptx, Map<String,String> dataMap) throws XmlException {
        // 获取第一个ppt页面
        XSLFSlide slide = pptx.getSlides().get(0);
        // 替换表格数据
        for (XSLFShape shape : slide) {
            //如果是表格类型
            if (shape instanceof XSLFTable) {
                CTTable ctTable = ((XSLFTable) shape).getCTTable();
                String xmlStr = ctTable.toString();
                //则将模板中的占位符替换
                for (String key : dataMap.keySet()) {
                    xmlStr = xmlStr.replace(key, dataMap.get(key));
                }
                XmlObject xobj = XmlObject.Factory.parse(xmlStr);
                ctTable.set(xobj);
            }
        }
    }

    /**
     * 渲染柱状图
     * @param pptDataDTOList        图表数据对象
     */
    public static void buildBarChart(List<PPTDataDTO> pptDataDTOList){
        getChart(pptDataDTOList,3);
    }

    /**
     * 渲染折线图
     * @param pptDataDTOList        图表数据对象
     */
    public static void buildLineChart(List<PPTDataDTO> pptDataDTOList){
        getChart(pptDataDTOList,1);
    }

    /**
     * 渲染组合图-折线图和柱状图
     * @param pptDataDTOList        图表数据对象
     */
    public static void buildLineChartAndBarChart(List<PPTDataDTO> pptDataDTOList){
        getChart(pptDataDTOList,4);
    }


    /**
     * 渲染环状图
     * @param pptDataDTOList        图表数据对象
     */
    public static void buildDoughnutChart(List<PPTDataDTO> pptDataDTOList){
        getChart(pptDataDTOList,2);
    }

    /**
     * 处理图表
     * @param pptDataDTOList        图表数据对象
     * @param chartType             处理类型   1-折线图  2-环状图   3-柱状图   4-组合图（柱状图+折线图）
     */
    public static void getChart(List<PPTDataDTO> pptDataDTOList, int chartType){
        //获取幻灯片对象（默认只有一张）
        XMLSlideShow pptx = pptDataDTOList.get(0).getPptx();
        int i = 0;

        // 获取第一个ppt页面
        XSLFSlide slide = pptx.getSlides().get(0);
        //遍历第一页元素找到图表
        XSLFChart chart = null;
        for (POIXMLDocumentPart part : slide.getRelations()) {
            try {
                if (part instanceof XSLFChart) {
                    chart = (XSLFChart) part;
                    if (chart == null) continue;

                    // 获取图形
                    POIXMLDocumentPart xlsPart = chart.getRelations().get(0);
                    //创建一个工作簿
                    XSSFWorkbook wb = new XSSFWorkbook();
                    XSSFSheet sheet = wb.createSheet();
                    //获取图形区域
                    CTChart ctChart = chart.getCTChart();
                    CTPlotArea plotArea = ctChart.getPlotArea();
                    //获取需要填充的对应图表数据
                    PPTDataDTO pptDataDTO = pptDataDTOList.get(i);
                    //图表标题
                    String title = pptDataDTO.getTitle();
                    //lable
                    List<String> names = pptDataDTO.getNames();
                    //分组标题
                    List<String> groupTitle = pptDataDTO.getGroupTitle();
                    //分组数据
                    List<List<String>> groupValue = pptDataDTO.getGroupValue();
                    //非分组数据
                    List<String> inputValue = pptDataDTO.getValues();

                    // 处理折线图
                    if(chartType == 1){
                        // 获取折线图形
                        CTLineChart[] ctLineCharts = plotArea.getLineChartArray();
                        //处理折线图
                        if (ctLineCharts.length != 0) handlerLineChart(ctLineCharts[0], title, names, inputValue, sheet,1);
                    }

                    // 处理环状图
                    if(chartType == 2){
                        // 获取环状图形
                        CTDoughnutChart[] ctDoughnutCharts = plotArea.getDoughnutChartArray();
                        //处理环形图
                        if (ctDoughnutCharts.length != 0) handlerDoughnutChart(ctDoughnutCharts[0], title, names, inputValue, sheet);
                    }

                    // 处理柱状图
                    if(chartType == 3){
                        // 获取柱状图形
                        CTBarChart[] ctBarCharts = plotArea.getBarChartArray();
                        //处理柱状图
                        if (ctBarCharts.length != 0) handlerBarChart(ctBarCharts[0], title, groupTitle, names, groupValue, sheet);
                    }


                    if(chartType == 4){
                        // 获取折线图形
                        CTLineChart[] ctLineCharts = plotArea.getLineChartArray();
                        // 获取柱状图形
                        CTBarChart[] ctBarCharts = plotArea.getBarChartArray();
                        if(ctBarCharts.length != 0 && ctLineCharts.length != 0){
                            handlerBarChart(ctBarCharts[0], title, groupTitle, names, groupValue, sheet);
                            int index = groupTitle.size();
                            handlerLineChart(ctLineCharts[0], groupTitle.get(index-1), names, inputValue, sheet,index);
                        }
                    }

                    //更新嵌入的workbook
                    OutputStream xlsOut = xlsPart.getPackagePart().getOutputStream();
                    wb.write(xlsOut);
                    xlsOut.close();
                    i++;
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
                i++;
                continue;
            }
        }
    }


    /**
     * 处理柱状图
     * @param ctBarChart    柱状图对象
     * @param title          图表标题
     * @param groupTitle     分组标题
     * @param names          label数组
     * @param values         分组数据
     * @param sheet          工作簿对象
     */
    public static void handlerBarChart(CTBarChart ctBarChart, String title, List<String> groupTitle, List<String> names, List<List<String>> values, XSSFSheet sheet) {

        //获取图表的系列
        int j = 0;
        //创建Excel表格第一行
        XSSFRow headRow = sheet.createRow(0);
        //设置第一列为图表标题
        XSSFCell firstCell = headRow.createCell(0);
        //设置图表标题
//        firstCell.setCellValue(title);
        String titleRef = new CellReference(sheet.getSheetName(), 0, 1, true, true).formatAsString();
        //遍历所有分组
        for (CTBarSer ser : ctBarChart.getSerArray()) {
//        CTBarSer ser = ctBarChart.getSerArray(0);
            // Series Text
            CTSerTx tx = ser.getTx();
            tx.getStrRef().getStrCache().getPtArray(0).setV(groupTitle.get(j));
            tx.getStrRef().setF(titleRef);
            //excel设置分组名称
            headRow.createCell(j + 1).setCellValue(groupTitle.get(j));

            // Category Axis Data
            CTAxDataSource cat = ser.getCat();
            CTStrData strData = cat.getStrRef().getStrCache();

            //获取图表的值
            CTNumDataSource val = ser.getVal();
            CTNumData numData = val.getNumRef().getNumCache();
            strData.setPtArray(null);  // unset old axis text
            numData.setPtArray(null);  // unset old values

            //遍历分组上的label进行渲染数据
            // label位置
            int idx = 0;
            //行数
            int rownum = 1;
            for (int i = 0; i < names.size(); i++) {
                XSSFRow row = null;
                //如果是第一个分组，则excel新创建一行
                if (j == 0) {
                    //excel表格添加一行
                    row = sheet.createRow(rownum++);
                    //图表增加label
                    CTStrVal sVal = strData.addNewPt();
                    //设置label位置
                    sVal.setIdx(idx);
                    //设置label名称
                    sVal.setV(names.get(i));
                    //excel表格增加数据名称
                    row.createCell(0).setCellValue(names.get(i));
                } else {
                    //如果不是第一个分组，则直接获取该行
                    row = sheet.getRow(rownum++);
                }
                //图表增加图形数据
                CTNumVal numVal = numData.addNewPt();
                //设置图形数据位置
                numVal.setIdx(idx);
                //设置图形数据大小
                numVal.setV(values.get(j).get(i));
                //excel表格增加数据
                row.createCell(j + 1).setCellValue(Double.valueOf(values.get(j).get(i)));
                idx++;
            }
            numData.getPtCount().setVal(idx);
            strData.getPtCount().setVal(idx);
            //设置该分组下的数据在excel中的范围
            String numDataRange = new CellRangeAddress(1, rownum - 1, j + 1, j + 1).formatAsString(sheet.getSheetName(), true);
            val.getNumRef().setF(numDataRange);
            //label在excel中范围
            String axisDataRange = new CellRangeAddress(1, rownum - 1, 0, 0).formatAsString(sheet.getSheetName(), true);
            cat.getStrRef().setF(axisDataRange);
            j++;
        }

    }


    /**
     * 处理环状图
     * @param ctDoughnutChart  环状图对象
     * @param chartTitle        图表标题
     * @param names             label数组
     * @param values            分组数据
     * @param sheet             工作簿对象
     * @throws IOException
     */
    public static void handlerDoughnutChart(CTDoughnutChart ctDoughnutChart, String chartTitle, List<String> names, List<String> values, XSSFSheet sheet) throws IOException {
        //获取图表的系列
        CTPieSer ser = ctDoughnutChart.getSerArray(0);
        // Series Text
        CTSerTx tx = ser.getTx();
        tx.getStrRef().getStrCache().getPtArray(0).setV(chartTitle);
        sheet.createRow(0).createCell(1).setCellValue(chartTitle);
        String titleRef = new CellReference(sheet.getSheetName(), 0, 1, true, true).formatAsString();
        tx.getStrRef().setF(titleRef);

        // Category Axis Data
        CTAxDataSource cat = ser.getCat();
        CTStrData strData = cat.getStrRef().getStrCache();

        //获取图表的值
        CTNumDataSource val = ser.getVal();
        CTNumData numData = val.getNumRef().getNumCache();

        strData.setPtArray(null);  // unset old axis text
        numData.setPtArray(null);  // unset old values

        // set model
        int idx = 0;
        int rownum = 1;
        String ln;
        for (int i = 0; i < names.size(); i++) {
            CTNumVal numVal = numData.addNewPt();
            numVal.setIdx(idx);
            numVal.setV(values.get(i));

            CTStrVal sVal = strData.addNewPt();
            sVal.setIdx(idx);
            sVal.setV(names.get(i));

            idx++;
            XSSFRow row = sheet.createRow(rownum++);
            row.createCell(0).setCellValue(names.get(i));
            row.createCell(1).setCellValue(Double.valueOf(values.get(i)));
        }
        numData.getPtCount().setVal(idx);
        strData.getPtCount().setVal(idx);

        String numDataRange = new CellRangeAddress(1, rownum - 1, 1, 1).formatAsString(sheet.getSheetName(), true);
        val.getNumRef().setF(numDataRange);
        String axisDataRange = new CellRangeAddress(1, rownum - 1, 0, 0).formatAsString(sheet.getSheetName(), true);
        cat.getStrRef().setF(axisDataRange);
    }

    /**
     * 处理折线图
     * @param ctLineChart 折线图对象
     * @param chartTitle  图表标题
     * @param names        label数组
     * @param values       分组数据
     * @param sheet        工作簿对象
     * @throws IOException
     */
    public static void handlerLineChart(CTLineChart ctLineChart, String chartTitle, List<String> names, List<String> values, XSSFSheet sheet, int index) throws IOException {

        //获取图表的系列
        CTLineSer ser = ctLineChart.getSerArray(0);

        // Series Text
        CTSerTx tx = ser.getTx();
        tx.getStrRef().getStrCache().getPtArray(0).setV(chartTitle);
//        sheet.createRow(0).createCell(1).(setCellValuechartTitle);
        String titleRef = new CellReference(sheet.getSheetName(), 0, index, true, true).formatAsString();
        tx.getStrRef().setF(titleRef);

        // Category Axis Data
        CTAxDataSource cat = ser.getCat();
        CTStrData strData = cat.getStrRef().getStrCache();

        XSSFCell firstCell = null;
        if(index == 1){
            //创建Excel表格第一行
            //设置第一列为图表标题
            firstCell = sheet.createRow(0).createCell(index);
        }else{
            //创建Excel表格第一行
            //设置第一列为图表标题
            firstCell = sheet.getRow(0).createCell(index);
        }
        firstCell.setCellValue(chartTitle);

        //获取图表的值
        CTNumDataSource val = ser.getVal();
        CTNumData numData = val.getNumRef().getNumCache();

        strData.setPtArray(null);  // unset old axis text
        numData.setPtArray(null);  // unset old values

        // set model
        int idx = 0;
        int rownum = 1;
        String ln;
        for (int i = 0; i < names.size(); i++) {
            CTNumVal numVal = numData.addNewPt();
            numVal.setIdx(idx);
            numVal.setV(values.get(i));

            CTStrVal sVal = strData.addNewPt();
            sVal.setIdx(idx);
            sVal.setV(names.get(i));

            idx++;
            XSSFRow row = null;
            if(index == 1){
                row = sheet.createRow(rownum++);
                row.createCell(0).setCellValue(names.get(i));
            }else{
                row = sheet.getRow(rownum++);
            }
            row.createCell(index).setCellValue(Double.valueOf(values.get(i)));
        }
        numData.getPtCount().setVal(idx);
        strData.getPtCount().setVal(idx);

        String numDataRange = new CellRangeAddress(1, rownum - 1, index, index).formatAsString(sheet.getSheetName(), true);
        val.getNumRef().setF(numDataRange);
        String axisDataRange = new CellRangeAddress(1, rownum - 1, 0, 0).formatAsString(sheet.getSheetName(), true);
        cat.getStrRef().setF(axisDataRange);
    }


}
