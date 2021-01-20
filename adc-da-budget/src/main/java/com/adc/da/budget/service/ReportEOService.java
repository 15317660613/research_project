package com.adc.da.budget.service;

import com.adc.da.budget.dto.PPTDataDTO;
import com.adc.da.budget.entity.ReportEO;
import com.adc.da.budget.repository.ReportEORepository;
import com.adc.da.budget.utils.ppt.PPTUtil;
import com.adc.da.util.utils.JRedisPoolConfig;
import com.adc.da.util.utils.JedisUtil;
import com.adc.da.util.utils.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class ReportEOService {

    private static final Logger logger = LoggerFactory.getLogger(ReportEOService.class);

    private boolean isRedis = true;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;

    @Autowired
    ResourceLoader resourceLoader;


    @Autowired
    private ReportEOService reportEOService;

    private JedisUtil.Strings jedisStrings;

    private JedisUtil.Strings getJedisUtilString(){
        if(jedisStrings == null){
            if(StringUtils.isEmpty(JRedisPoolConfig.getRedisIp())) {
                JRedisPoolConfig.setRedisIp(host);
                JRedisPoolConfig.setRedisPort(port);
                JRedisPoolConfig.setRedisPassword(password);
            }
            jedisStrings = JedisUtil.getInstance().new Strings();
        }
        return jedisStrings;
    }
    @Autowired
    private ReportEORepository reportEORepository;
    @Autowired
    private ObjectMapper objectMapper;
    public ReportEO get(String id) throws IOException {
        if(isRedis){
            String report = getJedisUtilString().get(id);
            List<List<String>> reports = objectMapper.readValue(report, new TypeReference<List<List<String>>>() {});
            return new ReportEO(id, reports);
        } else {
            return reportEORepository.findOne(id);
        }
    }
    public String getString(String id) throws IOException {
        ReportEO report = get(id);
        return objectMapper.writeValueAsString(report);
    }
    public void set(ReportEO reportEO){
        if(isRedis){
            getJedisUtilString().set(reportEO.getId(), reportEO.toArrayString());
        } else {
            reportEORepository.save(reportEO);
        }
    }
    public void setString(String id, String report) throws IOException {
        List<List<String>> reports = objectMapper.readValue(report, new TypeReference<List<List<String>>>() {});
        ReportEO reportEO= new ReportEO(id, reports);
        set(reportEO);
    }

    public XMLSlideShow exportPPT(String id, List<List<String>> reports){
        if (reports == null){
            return null;
        }
        try {
            switch (id){
                case "1":
                    return toFirstPPT(reports);
                case "2":
                    return toSecondPPT(reports);
                case "3":
                    return toThirdPPT(reports);
                case "4":
                    return toFourthPPT(reports);
                case "5":
                    return toFifthPPT(reports);
                case "6":
                    return toSixthPPT(reports);
                case "7":
                    return toSeventhPPT(reports);
                case "8":
                    return toEighthPPT(reports);
                default:
                    return null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

    }

    private XMLSlideShow toEighthPPT(List<List<String>> reports) throws IOException, XmlException {
        Resource resource = resourceLoader.getResource("classpath:pptTemplate\\template8.pptx");
        XMLSlideShow pptx = null;
        //图表柱行索引
        int[] barCharIndexs = {1,2};
        //打开ppt
        File pPTFile = resource.getFile();
        pptx = new XMLSlideShow(new FileInputStream(pPTFile));
        //表格数据
        Map<String, String> tableData = getTableData(reports, "p");
        List<String> chartLabel = getChartLabel(reports.get(0), 5, 0);
        //String title = "";  //图标标题
        List<String> groupNames = new ArrayList<>();
        for (int i=0; i<barCharIndexs.length; i++){
            groupNames.add(reports.get(barCharIndexs[i]).get(1));
        }
        List<List<String>> barChart = new ArrayList<>();
        //去掉表头数组
        for (int i=1; i<reports.size(); i++){
            List<String> row = new ArrayList<>();
            for (int j=5; j<reports.get(i).size(); j++){
                row.add(reports.get(i).get(j));
            }
            barChart.add(row);
        }
        List<PPTDataDTO> pptDataDTOList = new ArrayList<>();
        PPTDataDTO pptDataDTO = new PPTDataDTO(pptx, "", chartLabel, groupNames, barChart, null);
        pptDataDTOList.add(pptDataDTO);

        //第二表格,第三表格
        List<List<String>> reportSecond = this.get("report_81").getTableArray();
        Map<String, String> tableSecondData = getTableData(reportSecond, "q");
        List<List<String>> reportThird = this.get("report_82").getTableArray();
        Map<String, String> tableThirdData = getTableData(reportThird, "w");
        PPTUtil.replaceTableData(pptx, tableSecondData);
        PPTUtil.replaceTableData(pptx, tableThirdData);

        PPTUtil.replaceTableData(pptx, tableData);
        PPTUtil.buildBarChart(pptDataDTOList);
        return  pptx;
    }

    private XMLSlideShow toSeventhPPT(List<List<String>> reports) throws IOException, XmlException {
        Resource resource = resourceLoader.getResource("classpath:pptTemplate\\template7.pptx");
        XMLSlideShow pptx = null;
        //图表柱行索引
        //折线索引
        //打开ppt
        File pPTFile = resource.getFile();
        pptx = new XMLSlideShow(new FileInputStream(pPTFile));
        //String title = "";  //图标标题
        //表格数据
        Map<String, String> tableData = new HashMap<>();
        for (int i=0; i<reports.size(); i++){
            for (int j=0; j<reports.get(i).size(); j++){
                tableData.put("{p" + (i-1) + "-"+j+"}", reports.get(i).get(j));
            }
        }
        //柱状图数据
        //加入折线组名
        //折线数据
        PPTUtil.replaceTableData(pptx, tableData);
        return  pptx;
    }

    private XMLSlideShow toSixthPPT(List<List<String>> reports) throws IOException, XmlException {
        Resource resource = resourceLoader.getResource("classpath:pptTemplate\\template4.pptx");
        XMLSlideShow pptx = null;
        //图表柱行索引
        int[] barCharIndexs = {1,2};
        //折线索引
        int lineChartIndex = 4;
        //打开ppt
        File pPTFile = resource.getFile();
        pptx = new XMLSlideShow(new FileInputStream(pPTFile));
        //String title = "";  //图标标题
        List<String> groupNames = getGroupNames(reports, barCharIndexs);
        //表格数据
        Map<String, String> tableData = getTableData(reports, "p");
        List<String> chartLabel = getChartLabel(reports.get(0), 1, 1);
        //柱状图数据
        List<List<String>> barChart = new ArrayList<>();
        for (String groupName : groupNames){
            barChart.add(getbarChart(reports, groupName, 1, 1));
        }
        //加入折线组名
        groupNames.add(reports.get(lineChartIndex).get(0));
        //折线数据
        List<String> lineChart = getLineChart(reports.get(lineChartIndex));
        List<PPTDataDTO> pptDataDTOList = new ArrayList<>();
        PPTDataDTO pptDataDTO = new PPTDataDTO(pptx, "", chartLabel, groupNames, barChart, lineChart);
        pptDataDTOList.add(pptDataDTO);
        PPTUtil.replaceTableData(pptx, tableData);
        PPTUtil.buildLineChartAndBarChart(pptDataDTOList);
        return  pptx;
    }

    private XMLSlideShow toFifthPPT(List<List<String>> reports) throws IOException, XmlException {
        Resource resource = resourceLoader.getResource("classpath:pptTemplate\\template3.pptx");
        XMLSlideShow pptx = null;
        //数据需要行列互换
        List<List<String>> newData = rowsToColumns(reports);
        //表格数据
        Map<String, String> tableData = getTableData(reports, "p");
        //图表柱行索引
        int[] barCharIndexs = {1,2};
        //折线索引
        int lineChartIndex = 5;
        //打开ppt
        File pPTFile = resource.getFile();
        pptx = new XMLSlideShow(new FileInputStream(pPTFile));
        //String title = "";  //图标标题
        List<String> groupNames = getGroupNames(newData, barCharIndexs);

        List<String> chartLabel = getChartLabel(newData.get(0), 1, 1);
        //柱状图数据
        List<List<String>> barChart = new ArrayList<>();
        for (String groupName : groupNames){
            barChart.add(getbarChart(newData, groupName, 1, 1));
        }
        //加入折线组名
        groupNames.add(newData.get(lineChartIndex).get(0));
        //折线数据
        List<String> lineChart = getLineChart(newData.get(lineChartIndex));
        List<PPTDataDTO> pptDataDTOList = new ArrayList<>();
        PPTDataDTO pptDataDTO = new PPTDataDTO(pptx, "", chartLabel, groupNames, barChart, lineChart);
        pptDataDTOList.add(pptDataDTO);

        //第二张表
        //图表柱行索引
        int[] barCharIndexs1 = {3,4};
        //折线索引
        int lineChartIndex1 = 6;
        //String title = "";  //图标标题
        List<String> groupNames1 = getGroupNames(newData, barCharIndexs1);

        //柱状图数据
        List<List<String>> barChart1 = new ArrayList<>();
        for (String groupName : groupNames1){
            barChart1.add(getbarChart(newData, groupName, 1, 1));
        }
        //加入折线组名
        groupNames1.add(newData.get(lineChartIndex1).get(0));
        //折线数据
        List<String> lineChart1 = getLineChart(newData.get(lineChartIndex1));
        PPTDataDTO pptDataDTO1 = new PPTDataDTO(pptx, "", chartLabel, groupNames1, barChart1, lineChart1);


        pptDataDTOList.add(pptDataDTO1);

        PPTUtil.replaceTableData(pptx, tableData);
        PPTUtil.buildLineChartAndBarChart(pptDataDTOList);
        return  pptx;
    }

    private XMLSlideShow toFourthPPT(List<List<String>> reports) throws IOException, XmlException {
        Resource resource = resourceLoader.getResource("classpath:pptTemplate\\template6.pptx");
        XMLSlideShow pptx = null;
        //图表柱行索引
        int[] barCharIndexs = {1,2};
        //折线索引
        int lineChartIndex = 3;
        //打开ppt
        File pPTFile = resource.getFile();
        pptx = new XMLSlideShow(new FileInputStream(pPTFile));
        //String title = "";  //图标标题
        List<String> groupNames = getGroupNames(reports, barCharIndexs);
        //表格数据
        Map<String, String> tableData = getTableData(reports, "p");
        List<String> chartLabel = getChartLabel(reports.get(0), 1, 1);
        //柱状图数据
        List<List<String>> barChart = new ArrayList<>();
        for (String groupName : groupNames){
            barChart.add(getbarChart(reports, groupName, 1, 1));
        }
        //加入折线组名
        groupNames.add(reports.get(lineChartIndex).get(0));
        //折线数据
        List<String> lineChart = getLineChart(reports.get(lineChartIndex));
        List<PPTDataDTO> pptDataDTOList = new ArrayList<>();
        PPTDataDTO pptDataDTO = new PPTDataDTO(pptx, "", chartLabel, groupNames, barChart, lineChart);
        pptDataDTOList.add(pptDataDTO);
        PPTUtil.replaceTableData(pptx, tableData);
        PPTUtil.buildLineChartAndBarChart(pptDataDTOList);
        return  pptx;
    }

    private XMLSlideShow toThirdPPT(List<List<String>> reports) throws IOException, XmlException {
        Resource resource = resourceLoader.getResource("classpath:pptTemplate\\template5.pptx");
        XMLSlideShow pptx = null;
        //图表柱行索引
        int[] barCharIndexs = {1,2};
        //折线索引
        int lineChartIndex = 4;
        //打开ppt
        File pPTFile = resource.getFile();
        pptx = new XMLSlideShow(new FileInputStream(pPTFile));
        //String title = "";  //图标标题
        List<String> groupNames = getGroupNames(reports, barCharIndexs);
        //表格数据
        Map<String, String> tableData = getTableData(reports, "p");
        List<String> chartLabel = getChartLabel(reports.get(0), 1, 1);
        //柱状图数据
        List<List<String>> barChart = new ArrayList<>();
        for (String groupName : groupNames){
            barChart.add(getbarChart(reports, groupName, 1, 1));
        }
        //加入折线组名
        groupNames.add(reports.get(lineChartIndex).get(0));
        //折线数据
        List<String> lineChart = getLineChart(reports.get(lineChartIndex));
        List<PPTDataDTO> pptDataDTOList = new ArrayList<>();
        PPTDataDTO pptDataDTO = new PPTDataDTO(pptx, "", chartLabel, groupNames, barChart, lineChart);
        pptDataDTOList.add(pptDataDTO);
        PPTUtil.replaceTableData(pptx, tableData);
        PPTUtil.buildLineChartAndBarChart(pptDataDTOList);
        return  pptx;
    }

    private XMLSlideShow toSecondPPT(List<List<String>> reports) throws IOException, XmlException {
        Resource resource = resourceLoader.getResource("classpath:pptTemplate\\template2.pptx");
        XMLSlideShow pptx = null;
        //图表柱行索引
        int[] barCharIndexs = {2,4};
        //折线索引
        int lineChartIndex = 5;
        //打开ppt
        File pPTFile = resource.getFile();
        pptx = new XMLSlideShow(new FileInputStream(pPTFile));
        //String title = "";  //图标标题
        List<String> groupNames = getGroupNames(reports, barCharIndexs);
        //表格数据
        Map<String, String> tableData = getTableData(reports, "p");
        List<String> chartLabel = getChartLabel(reports.get(0), 1, 1);
        //柱状图数据
        List<List<String>> barChart = new ArrayList<>();
        for (String groupName : groupNames){
            barChart.add(getbarChart(reports, groupName, 1, 1));
        }
        //加入折线组名
        groupNames.add(reports.get(lineChartIndex).get(0));
        //折线数据
        List<String> lineChart = getLineChart(reports.get(lineChartIndex));
        List<PPTDataDTO> pptDataDTOList = new ArrayList<>();
        PPTDataDTO pptDataDTO = new PPTDataDTO(pptx, "", chartLabel, groupNames, barChart, lineChart);
        pptDataDTOList.add(pptDataDTO);
        PPTUtil.replaceTableData(pptx, tableData);
        PPTUtil.buildLineChartAndBarChart(pptDataDTOList);
        return pptx;
    }

    private XMLSlideShow toFirstPPT(List<List<String>> reports) throws IOException, XmlException {
        Resource resource = resourceLoader.getResource("classpath:pptTemplate\\template1.pptx");
        XMLSlideShow pptx = null;
        //打开ppt
        File pPTFile = resource.getFile();
        pptx = new XMLSlideShow(new FileInputStream(pPTFile));
        //String title = "";  //图标标题
        //表格数据
        Map<String, String> tableData = getTableData(reports, "p");
        List<PPTDataDTO> pptDataDTOList = new ArrayList<>();


        int year = 4;
        int rate = 6;
        List<String> leftLabel = new ArrayList<>();
        List<String> leftValue = new ArrayList<>();
        List<String> rightLabel = new ArrayList<>();
        List<String> rightValue = new ArrayList<>();
        for (int i=1; i<reports.size(); i++){
            if(("收入").equals(reports.get(i).get(0))){
                leftLabel.add(reports.get(0).get(year));
                leftLabel.add(reports.get(0).get(rate));
                int value1 = Integer.parseInt(StringUtils.substringBefore(reports.get(i).get(rate),"%"));
                int value2 = 100 - value1;
                leftValue.add(String.valueOf(value2));
                leftValue.add(String.valueOf(value1));
            }else if(("成本").equals(reports.get(i).get(0))){
                rightLabel.add(reports.get(0).get(year));
                rightLabel.add(reports.get(0).get(rate));
                int value1 = Integer.parseInt(StringUtils.substringBefore(reports.get(i).get(rate),"%"));
                int value2 = 100 - value1;
                rightValue.add(String.valueOf(value2));
                rightValue.add(String.valueOf(value1));
            }
        }
        PPTDataDTO pptDataDTO = new PPTDataDTO(pptx, "", leftLabel, null, null, leftValue);
        PPTDataDTO pptDataDTO1 = new PPTDataDTO(pptx, "", rightLabel, null, null, rightValue);
        pptDataDTOList.add(pptDataDTO);
        pptDataDTOList.add(pptDataDTO1);
        PPTUtil.replaceTableData(pptx, tableData);
        PPTUtil.buildDoughnutChart(pptDataDTOList);
        return pptx;
    }

    /**
     * 表格数据
     * @param data
     * @return
     */
    private Map<String, String> getTableData(List<List<String>> data, String name){
        Map<String, String> mapData = new HashMap<>();
        for (int i=0; i<data.size(); i++){
            for (int j=0; j<data.get(i).size(); j++){
                mapData.put("{" + name + (i) + "-"+j+"}", data.get(i).get(j));
            }
        }
        return mapData;
    }

    /**
     * 分组名数据
     * @param data
     * @param barCharIndexs
     * @return
     */
    private List<String> getGroupNames(List<List<String>> data, int[] barCharIndexs){
        List<String> groupNames = new ArrayList<>();
        for (int i=0; i<barCharIndexs.length; i++){
            groupNames.add(data.get(barCharIndexs[i]).get(0));
        }
        return groupNames;
    }

    /**
     * 图表项目
     * @param data
     * @return
     */
    private List<String> getChartLabel(List<String> data, int start, int last){
        //原数据此处发生变化
        int length = data.size();
        if(start >= length || start < 0){
            return null;
        }
        if (last >= length || last < 0){
            return null;
        }
        List<String> chartLabels = new ArrayList<>();
        for (int i=start; i<length-last; i++){
            chartLabels.add(data.get(i));
        }
        return chartLabels;
    }

    /**
     *
     * @param data
     * @param groupName
     * @param start
     * @param last  最后结束列
     * @return
     */
    private List<String> getbarChart(List<List<String>> data, String groupName,  int start, int last){
        if(StringUtils.isEmpty(data.size())){
            return null;
        }
        int length = data.get(0).size();
        if(start >= length || start < 0){
            return null;
        }
        if (last >= length || last < 0){
            return null;
        }
        //分组数去掉折线
        List<String> barChart = new ArrayList<>();
        //去掉表头数组
        for (int i=1; i<data.size(); i++){
            if(!data.get(i).get(0).equals(groupName)) {
                continue;
            }
            for (int j=start; j<data.get(i).size()-last; j++){
                barChart.add(data.get(i).get(j));
            }
            break;
        }
        return barChart;
    }

    private List<String> getLineChart(List<String> data){
        List<String> lines = new ArrayList<>();
        for (int k=1; k<data.size(); k++){
            lines.add("0." + StringUtils.substringBefore(data.get(k),"%"));
        }
        return lines;
    }

    private List<List<String>> rowsToColumns(List<List<String>> data){
        List<List<String>> newData = new ArrayList<>();
        for (int i=0; i<data.size(); i++){
            for(int j=0; j<data.get(i).size(); j++){
                if(newData.size() > j){
                    newData.get(j).add(data.get(i).get(j));
                }else{
                    List<String> newList = new ArrayList<>();
                    newList.add(data.get(i).get(j));
                    newData.add(newList);
                }

            }
        }
        return newData;
    }

}
