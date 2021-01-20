package com.adc.da.business.controller;

import com.adc.da.base.page.BasePage;
import com.adc.da.business.entity.BudgetIncomeCalculateEO;
import com.adc.da.business.entity.DeptBudgetEO;
import com.adc.da.business.entity.OperatingBudgetEO;
import com.adc.da.business.service.BudgetIncomeCalculateService;
import com.adc.da.business.service.DeptBudgetEOService;
import com.adc.da.business.service.IncomeProfitAndCostService;
import com.adc.da.business.service.OperatingBudgetEOService;
import com.adc.da.business.service.TableReportService;
import com.adc.da.business.vo.DealIncomeVO;
import com.adc.da.business.vo.DeptOperationVO;
import com.adc.da.business.vo.ReceivableAccountVO;
import com.adc.da.sys.dao.MyOrgEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//该Controller主要包含各种图表数据的返回
@RestController
@RequestMapping("/${restPath}/business/tableReport")
@Api(tags = "|该Controller主要包含各种图表数据的返回|")
@Slf4j
public class TableReportController {

    @Autowired
    private OperatingBudgetEOService operatingBudgetEOService;

    @Autowired
    private DeptBudgetEOService deptBudgetEOService;

    @Autowired
    private OrgEOService orgEOService;

    @Autowired
    private MyOrgEODao orgEODao;

    @Autowired
    private TableReportService tableReportService;

    @Autowired
    private DicTypeEOService dicTypeEOService;

    @ApiOperation(value = "|OperatingBudgetEO|两年同月-1.1整体经营情况")
    @GetMapping("/getSameMonthTermData")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<Map<String, Map>> page(String thisYear, String lastYear, String startMonth,
        String endMonth) {
        List<OperatingBudgetEO> rows1 = operatingBudgetEOService.queryYearData(thisYear, startMonth, endMonth);
        List<OperatingBudgetEO> rows2 = operatingBudgetEOService.queryYearData(lastYear, startMonth, endMonth);
        Float thisYearContributionAmount = 0f;  // 进账额
        Float thisYearInvoiceAmount = 0f;   // 开票额
        Float thisYearContractSum = 0f; // 合同额
        Float lastYearContributionAmount = 0f;  // 进账额
        Float lastYearInvoiceAmount = 0f;   // 开票额
        Float lastYearContractSum = 0f; // 合同额
        for (OperatingBudgetEO o : rows1) {
            thisYearContributionAmount += o.getContributionAmount();
            thisYearInvoiceAmount += o.getInvoiceAmount();
            thisYearContractSum += o.getContractSum();
        }
        for (OperatingBudgetEO o : rows2) {
            lastYearContributionAmount += o.getContributionAmount();
            lastYearInvoiceAmount += o.getInvoiceAmount();
            lastYearContractSum += o.getContractSum();
        }
        Map<String, Map> returnMap = new HashMap<>();
        // 上一年度同期累计
        Map<String, Object> lastYearMap = new HashMap<>();
        lastYearMap.put("contractSum", lastYearContractSum);
        lastYearMap.put("invoiceAmount", lastYearInvoiceAmount);
        lastYearMap.put("contributionAmount", lastYearContributionAmount);
        returnMap.put("lastYearMap", lastYearMap);
        // 当年累计
        Map<String, Object> thisYearMap = new HashMap<>();
        thisYearMap.put("contractSum", thisYearContractSum);
        thisYearMap.put("invoiceAmount", thisYearInvoiceAmount);
        thisYearMap.put("contributionAmount", thisYearContributionAmount);
        returnMap.put("thisYearMap", thisYearMap);
        // 同比增长
        Map<String, Object> annualRatioMap = new HashMap<>();

        if (lastYearContributionAmount <= 0.0f) {
            annualRatioMap.put(
                "contributionAmount", 1.0f);
        } else {
            annualRatioMap.put(
                "contributionAmount",
                (thisYearInvoiceAmount - lastYearInvoiceAmount) / lastYearInvoiceAmount);
        }

        if (lastYearInvoiceAmount <= 0.0f) {
            annualRatioMap.put(
                "invoiceAmount", 1.0f);
        } else {
            annualRatioMap
                .put("invoiceAmount", (thisYearInvoiceAmount - lastYearInvoiceAmount) / lastYearInvoiceAmount);
        }

        if (lastYearContractSum <= 0.0f) {
            annualRatioMap.put(
                "contractSum", 1.0f);
        } else {
            annualRatioMap.put("contractSum", (thisYearContractSum - lastYearContractSum) / lastYearContractSum);
        }

        returnMap.put("annualRatioMap", annualRatioMap);
        return Result.success(returnMap);
    }

    @ApiOperation(value = "|OperatingBudgetEO|两年同季度-1.2经营合同同期对标")
    @GetMapping("/getSameSeasonTermContractAmount")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<List<Object>> getSameSeasonTermContractAmount(String thisYear, String targetYear,
        String startMonth, String endMonth) {

        List<Object> resulList = new ArrayList<>();

        List<OperatingBudgetEO> rows1 = operatingBudgetEOService.queryYearData(thisYear, startMonth, endMonth);
        List<OperatingBudgetEO> rows2 = operatingBudgetEOService.queryYearData(targetYear, startMonth, endMonth);

        List<OperatingBudgetEO>[] operatingBudgetEOS1 = new ArrayList[13];
        List<OperatingBudgetEO>[] operatingBudgetEOS2 = new ArrayList[13];

        for (int i = 0; i < 13; i++) {
            operatingBudgetEOS1[i] = new ArrayList<>();
            operatingBudgetEOS2[i] = new ArrayList<>();
        }

        // 构造数组
        for (OperatingBudgetEO o : rows1) {
            operatingBudgetEOS1[o.getMonth().intValue()].add(o);
        }

        for (OperatingBudgetEO o : rows2) {
            operatingBudgetEOS2[o.getMonth().intValue()].add(o);
        }

        Map<String, Object> thisYearSeasonDataMap = new LinkedHashMap<>();
        Map<String, Object> targetYearSeasonDataMap = new LinkedHashMap<>();
        Map<String, Object> incrasePercentMap = new LinkedHashMap<>();

        for (int i = 0; i < 4; i++) {
            float sumRow1 = 0;
            float sumRow2 = 0;
            for (int j = i * 3 + 1; j <= i * 3 + 3; j++) {
                if (null != operatingBudgetEOS1[j]) {
                    for (OperatingBudgetEO operatingBudgetEO : operatingBudgetEOS1[j]) {
                        sumRow1 += operatingBudgetEO.getContractSum();
                    }
                }

                if (null != operatingBudgetEOS2[j]) {
                    for (OperatingBudgetEO operatingBudgetEO : operatingBudgetEOS2[j]) {
                        sumRow2 += operatingBudgetEO.getContractSum();
                    }
                }
            }

            sumRow1 = Math.round(sumRow1 * 100) * 1.0f / 100;
            sumRow2 = Math.round(sumRow2 * 100) * 1.0f / 100;

            thisYearSeasonDataMap.put(i + "season", sumRow1);
            targetYearSeasonDataMap.put(i + "season", sumRow2);
            float inc = 0;
            if (sumRow2 != 0) {
                inc = (sumRow1 - sumRow2) / sumRow2;
            }
            incrasePercentMap.put(i + "season", inc);

        }

        thisYearSeasonDataMap.put("year", thisYear);
        targetYearSeasonDataMap.put("year", targetYear);
        incrasePercentMap.put("year", "同比增长");

        resulList.add(thisYearSeasonDataMap);
        resulList.add(targetYearSeasonDataMap);
        resulList.add(incrasePercentMap);

        return Result.success(resulList);
    }

    @ApiOperation(value = "|OperatingBudgetEO|两年-1.3开票收入同期对标")
    @GetMapping("/getSameSeasonTermInvoiceAmount")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<List<Object>> getSameSeasonTermInvoiceAmount(String thisYear, String targetYear,
        String startMonth, String endMonth) {

        List<Object> resulList = new ArrayList<>();

        List<OperatingBudgetEO> rows1 = operatingBudgetEOService.queryYearData(thisYear, startMonth, endMonth);
        List<OperatingBudgetEO> rows2 = operatingBudgetEOService.queryYearData(targetYear, startMonth, endMonth);

        List<OperatingBudgetEO>[] operatingBudgetEOS1 = new ArrayList[13];
        List<OperatingBudgetEO>[] operatingBudgetEOS2 = new ArrayList[13];

        for (int i = 0; i < 13; i++) {
            operatingBudgetEOS1[i] = new ArrayList<>();
            operatingBudgetEOS2[i] = new ArrayList<>();
        }

        // 构造数组
        for (OperatingBudgetEO o : rows1) {
            operatingBudgetEOS1[o.getMonth().intValue()].add(o);
        }

        for (OperatingBudgetEO o : rows2) {
            operatingBudgetEOS2[o.getMonth().intValue()].add(o);
        }

        Map<String, Object> thisYearSeasonDataMap = new LinkedHashMap<>();
        Map<String, Object> targetYearSeasonDataMap = new LinkedHashMap<>();
        Map<String, Object> incrasePercentMap = new LinkedHashMap<>();

        for (int i = 0; i < 4; i++) {
            float sumRow1 = 0;
            float sumRow2 = 0;
            for (int j = i * 3 + 1; j <= i * 3 + 3; j++) {

                if (null != operatingBudgetEOS1[j]) {
                    for (OperatingBudgetEO operatingBudgetEO : operatingBudgetEOS1[j]) {
                        sumRow1 += operatingBudgetEO.getInvoiceAmount();
                    }
                }

                if (null != operatingBudgetEOS2[j]) {
                    for (OperatingBudgetEO operatingBudgetEO : operatingBudgetEOS2[j]) {
                        sumRow2 += operatingBudgetEO.getInvoiceAmount();
                    }
                }
            }
            sumRow1 = Math.round(sumRow1 * 100) * 1.0f / 100;
            sumRow2 = Math.round(sumRow2 * 100) * 1.0f / 100;

            thisYearSeasonDataMap.put(i + "season", sumRow1);
            targetYearSeasonDataMap.put(i + "season", sumRow2);
            float inc = 0;
            if (sumRow2 != 0) {
                inc = (sumRow1 - sumRow2) / sumRow2;
            }
            incrasePercentMap.put(i + "season", inc);

        }

        thisYearSeasonDataMap.put("year", thisYear);
        targetYearSeasonDataMap.put("year", targetYear);
        incrasePercentMap.put("year", "同比增长");

        resulList.add(thisYearSeasonDataMap);
        resulList.add(targetYearSeasonDataMap);
        resulList.add(incrasePercentMap);

        return Result.success(resulList);
    }

    @ApiOperation(value = "|OperatingBudgetEO|两年1.4进账同期对标")
    @GetMapping("/getSameSeasonTermContributionAmount")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<List<Object>> getSameSeasonTermContributionAmount(String thisYear, String targetYear,
        String startMonth, String endMonth) {
        List<Object> resulList = new ArrayList<>();

        List<OperatingBudgetEO> rows1 = operatingBudgetEOService.queryYearData(thisYear, startMonth, endMonth);
        List<OperatingBudgetEO> rows2 = operatingBudgetEOService.queryYearData(targetYear, startMonth, endMonth);

        List<OperatingBudgetEO>[] operatingBudgetEOS1 = new ArrayList[13];
        List<OperatingBudgetEO>[] operatingBudgetEOS2 = new ArrayList[13];

        for (int i = 0; i < 13; i++) {
            operatingBudgetEOS1[i] = new ArrayList<>();
            operatingBudgetEOS2[i] = new ArrayList<>();
        }

        // 构造数组
        for (OperatingBudgetEO o : rows1) {
            operatingBudgetEOS1[o.getMonth().intValue()].add(o);
        }

        for (OperatingBudgetEO o : rows2) {
            operatingBudgetEOS2[o.getMonth().intValue()].add(o);
        }

        Map<String, Object> thisYearSeasonDataMap = new LinkedHashMap<>();
        Map<String, Object> targetYearSeasonDataMap = new LinkedHashMap<>();
        Map<String, Object> incrasePercentMap = new LinkedHashMap<>();

        for (int i = 0; i < 4; i++) {
            float sumRow1 = 0;
            float sumRow2 = 0;
            for (int j = i * 3 + 1; j <= i * 3 + 3; j++) {
                if (null != operatingBudgetEOS1[j]) {
                    for (OperatingBudgetEO operatingBudgetEO : operatingBudgetEOS1[j]) {
                        sumRow1 += operatingBudgetEO.getContributionAmount();
                    }
                }

                if (null != operatingBudgetEOS2[j]) {
                    for (OperatingBudgetEO operatingBudgetEO : operatingBudgetEOS2[j]) {
                        sumRow2 += operatingBudgetEO.getContributionAmount();
                    }
                }
            }
            sumRow1 = Math.round(sumRow1 * 100) * 1.0f / 100;
            sumRow2 = Math.round(sumRow2 * 100) * 1.0f / 100;
            thisYearSeasonDataMap.put(i + "season", sumRow1);
            targetYearSeasonDataMap.put(i + "season", sumRow2);
            float inc = 0;
            if (sumRow2 != 0) {
                inc = (sumRow1 - sumRow2) / sumRow2;
            }
            incrasePercentMap.put(i + "season", inc);

        }

        thisYearSeasonDataMap.put("year", thisYear);
        targetYearSeasonDataMap.put("year", targetYear);
        incrasePercentMap.put("year", "同比增长");

        resulList.add(thisYearSeasonDataMap);
        resulList.add(targetYearSeasonDataMap);
        resulList.add(incrasePercentMap);

        return Result.success(resulList);
    }

    @ApiOperation(value = "|OperatingBudgetEO|应收账款-1.5应收账款占比分析")
    @GetMapping("/getAR")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<List<ReceivableAccountVO>> getAR(String thisYear) {

        Map<String, Object> resultMap = new HashMap<>();
        List<OperatingBudgetEO> thisYearDataList = operatingBudgetEOService.queryYearData(thisYear, "0", "12");
        float forwardYearAR = operatingBudgetEOService.queryForwardYearAR(thisYear);
        float thisYearAR = operatingBudgetEOService.queryThisYearAR(thisYear);

        List<OperatingBudgetEO> fullMonthsList = new ArrayList<>();
        List<ReceivableAccountVO> receivableAccountVOList = new ArrayList<>();
        ReceivableAccountVO receivableAccountVO = new ReceivableAccountVO();
        receivableAccountVO.setPeriodTitle("应收账款");
        receivableAccountVO.setForwardYearTotal(forwardYearAR);
        receivableAccountVO.setCurrentYearTotal(thisYearAR);
        for (int i = 1; i < 13; i++) {
            boolean flag = false;
            for (OperatingBudgetEO operatingBudgetEO : thisYearDataList) {
                if (String.valueOf(operatingBudgetEO.getMonth()).equals(String.valueOf(i))) {
                    flag = true;
                    fullMonthsList.add(operatingBudgetEO);
                    tableReportService.setReceivableAccountVO(i, receivableAccountVO, operatingBudgetEO);
                    continue;
                }
            }
            if (!flag) {
                OperatingBudgetEO operatingBudgetEO = new OperatingBudgetEO();
                operatingBudgetEO.setYear(Long.valueOf(thisYear));
                operatingBudgetEO.setMonth(Long.valueOf(i));
                fullMonthsList.add(operatingBudgetEO);
            }
            resultMap.put(thisYear, fullMonthsList);
        }
        resultMap.put("thisYearAR", thisYearAR);
        resultMap.put("forwardYearAR", forwardYearAR);
        receivableAccountVOList.add(receivableAccountVO);
        ReceivableAccountVO receivableAccountVO1 = new ReceivableAccountVO();
        receivableAccountVO1.setPeriodTitle("占比");
        receivableAccountVO1.setForwardYearTotal(forwardYearAR / thisYearAR);
        receivableAccountVO1.setCurrentMonth1(receivableAccountVO.getCurrentMonth1() / thisYearAR);
        receivableAccountVO1.setCurrentMonth2(receivableAccountVO.getCurrentMonth2() / thisYearAR);
        receivableAccountVO1.setCurrentMonth3(receivableAccountVO.getCurrentMonth3() / thisYearAR);
        receivableAccountVO1.setCurrentMonth4(receivableAccountVO.getCurrentMonth4() / thisYearAR);
        receivableAccountVO1.setCurrentMonth5(receivableAccountVO.getCurrentMonth5() / thisYearAR);
        receivableAccountVO1.setCurrentMonth6(receivableAccountVO.getCurrentMonth6() / thisYearAR);
        receivableAccountVO1.setCurrentMonth7(receivableAccountVO.getCurrentMonth7() / thisYearAR);
        receivableAccountVO1.setCurrentMonth8(receivableAccountVO.getCurrentMonth8() / thisYearAR);
        receivableAccountVO1.setCurrentMonth9(receivableAccountVO.getCurrentMonth9() / thisYearAR);
        receivableAccountVO1.setCurrentMonth10(receivableAccountVO.getCurrentMonth10() / thisYearAR);
        receivableAccountVO1.setCurrentMonth11(receivableAccountVO.getCurrentMonth11() / thisYearAR);
        receivableAccountVO1.setCurrentMonth12(receivableAccountVO.getCurrentMonth12() / thisYearAR);
        receivableAccountVOList.add(receivableAccountVO1);
        return Result.success(receivableAccountVOList);
    }

    @ApiOperation(value = "|OperatingBudgetEO|某个本部的实际利润-1.6本部经营情况，实现1.6需求需配和描述为中心实际利润的接口")
    @GetMapping("/getBigDeptOperatingStatus")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<List<Map<String, Object>>> getBigDeptOperatingStatus(String thisYear) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        float centerProfit = deptBudgetEOService.queryCenterProfit(thisYear);
        float contractAmount = deptBudgetEOService.queryCenterContractAmountByYear(thisYear);
        float invoiceAmount = deptBudgetEOService.queryCenterInvoiceAmountByYear(thisYear);

        List<OrgEO> bigDeptIdList = orgEODao.queryBigDeptList();
        for (OrgEO o : bigDeptIdList) {
            Map<String, Object> resMap = new HashMap<>();
            List<String> subDeptIdList = orgEOService.getSubOrgId(o.getId());

            Float bigDeptContractAmount = deptBudgetEOService.queryBigDeptContractAmountByYear(subDeptIdList, thisYear);
            Float bigDeptInvoiceAmountByYear = deptBudgetEOService
                .queryBigDeptInvoiceAmountByYear(subDeptIdList, thisYear);
            Float bigDeptProfit = deptBudgetEOService.queryBigDeptProfit(subDeptIdList, thisYear);
            resMap.put("bigDeptContractAmount", bigDeptContractAmount);
            resMap.put("bigDeptInvoiceAmountByYear", bigDeptInvoiceAmountByYear);
            resMap.put("bigDeptProfit", bigDeptProfit);

            resMap.put("centerProfit", centerProfit);
            resMap.put("contractAmount", contractAmount);
            resMap.put("invoiceAmount", invoiceAmount);
            resMap.put("deptName", o.getName());

            resultList.add(resMap);
        }
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("bigDeptProfit", centerProfit);
        resMap.put("bigDeptContractAmount", contractAmount);
        resMap.put("bigDeptInvoiceAmountByYear", invoiceAmount);
        resMap.put("deptName", "数据资源中心");

        resultList.add(resMap);

        return Result.success(resultList);
    }

    @ApiOperation(value = "|OperatingBudgetEO|中心的实际利润")
    @GetMapping("/getCenterOperatState")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<Map<String, Object>> getCenterOperatState(String thisYear) {
        Map<String, Object> resultMap = new HashMap<>();
        //中心的实际利润
        Float CenterProfit = deptBudgetEOService.queryCenterProfit(thisYear);
        resultMap.put("CenterProfit", CenterProfit);
        return Result.success(resultMap);
    }

    @ApiOperation(value = "|OperatingBudgetEO|本部某个区间的总合同额，总开票额")
    @GetMapping("/getSumAmountByYearAndMonths")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<Map<String, Object>> getContractAmountByYearAndMonths(String deptId,
        String thisYear, String startMonth, String endMonth) {

        Map<String, Object> resultMap = new HashMap<>();
        List<String> deptIds = orgEOService.getSubOrgId(deptId);

        //本部某个区间的合同额
        Float contractAmount = operatingBudgetEOService
            .queryContractAmountByYearAndMonths(deptIds, thisYear, startMonth, endMonth);
        Float invoiceAmount = operatingBudgetEOService
            .queryInvoiceAmountByYearAndMonths(deptIds, thisYear, startMonth, endMonth);

        resultMap.put("contractAmount", contractAmount);
        resultMap.put("invoiceAmount", invoiceAmount);

        return Result.success(resultMap);
    }

    //////////////////////////////////////////////////
    ////
    ////  1.7   部门经营情况
    ////
    //////////////////////////////////////////////////
    @Autowired
    private IncomeProfitAndCostService incomeProfitAndCostService;

    @ApiOperation(value = "|OperatingBudgetEO|查询部门的实际利润及预算利润,实际成本及预算成本-1.7部门经营情况")
    @GetMapping("/getIncomeProfitAndCostByYearAndMonths")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<List<DeptOperationVO>> getIncomeProfitAndCostByYearAndMonths(
        String deptTypeCode, String thisYear, String startMonth, String endMonth) {

        if (StringUtils.isEmpty(thisYear)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            Date date = new Date();
            thisYear = sdf.format(date);
        }
        if (StringUtils.isEmpty(startMonth)) {
            startMonth = "0";
        }
        if (StringUtils.isEmpty(endMonth)) {
            endMonth = "12";
        }
        String deptTypeId = "";
        if (StringUtils.isNotEmpty(deptTypeCode)) {
            DicTypeEO dicTypeEO = dicTypeEOService.getDicTypeEOByCode(deptTypeCode);
            if (null != dicTypeEO) {
                deptTypeId = dicTypeEO.getId();
            }
        }
        List<DeptOperationVO> deptOperationVOList = new ArrayList<>();

        List<DeptBudgetEO> deptBudgetEOList = tableReportService
            .deptBudgetEOList(deptTypeId, thisYear, startMonth, endMonth);

        for (DeptBudgetEO deptBudgetEO : deptBudgetEOList) {

            Integer pCount = orgEODao.queryDeptPersonCount(deptBudgetEO.getDeptId());
            String deptName = orgEODao.selectByPrimaryKey(deptBudgetEO.getDeptId()).getName();

            DeptOperationVO deptOperationVO = new DeptOperationVO();

            deptOperationVO.setWorkerNumber(pCount);

            deptOperationVO.setActualProfit(deptBudgetEO.getActualProfit());
            deptOperationVO.setProfitBudget(deptBudgetEO.getProfitBudget());

            float profitBudget = deptBudgetEO.getProfitBudget();
            if (profitBudget <= 0) {
                profitBudget = 1.0f;
            }

            deptOperationVO.setProfitCompleteRate(deptBudgetEO.getActualProfit() / profitBudget);

            deptOperationVO.setActualIncome(deptBudgetEO.getActualTicketOpening());
            deptOperationVO.setIncomeBudget(deptBudgetEO.getInvoicingBudget());

            float invoicingBudget = deptBudgetEO.getInvoicingBudget();
            if (invoicingBudget <= 0) {
                invoicingBudget = 1.0f;
            }
            deptOperationVO.setBudgetCompleteRate(deptBudgetEO.getActualTicketOpening() / invoicingBudget);

            deptOperationVO.setActualCost(deptBudgetEO.getActualCost());
            deptOperationVO.setCostBudget(deptBudgetEO.getCostBudget());
            float costBudget = deptBudgetEO.getInvoicingBudget();
            if (costBudget <= 0) {
                costBudget = 1.0f;
            }

            deptOperationVO.setCostCompleteRate(deptBudgetEO.getActualCost() / costBudget);

            deptOperationVO.setDeptName(deptName);
            deptOperationVO.setDeptId(deptBudgetEO.getDeptId());

            deptOperationVOList.add(deptOperationVO);
        }

        // 对工时进行操作

        return Result
            .success(incomeProfitAndCostService.initWorkTime(deptOperationVOList, thisYear, startMonth, endMonth));

    }

    //////////////////////////////////////////////////
    ////
    ////  1.8     业务经营情况
    ////
    //////////////////////////////////////////////////

    @Autowired
    private BudgetIncomeCalculateService budgetIncomeCalculateService;

    @ApiOperation(value = "|OperatingBudgetEO|按年份展示-1.8 业务经营情况")
    @GetMapping("/getBusinessStatus")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<PageInfo<BudgetIncomeCalculateEO>> budgetIncomeCalculate(Integer pageNo, Integer pageSize,
        String thisYear, String startMonth,
        String endMonth, String businessName) {

        String searchName = null;
        /*
         * 对传入参数进行校验
         */
        if (org.apache.commons.lang.StringUtils.isNotEmpty(businessName)) {
            searchName = '%' + businessName + '%';
        }
        BasePage page = new BasePage();
        if (pageNo != null) {
            page.setPage(pageNo);
        }
        if (pageSize != null) {
            page.setPageSize(pageSize);
        }
        if (StringUtils.isEmpty(thisYear)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            Date date = new Date();
            thisYear = sdf.format(date);
        }
        if (StringUtils.isEmpty(startMonth)) {
            startMonth = "0";
        }
        if (StringUtils.isEmpty(endMonth)) {
            endMonth = "12";
        }

        /*
         * 更新分页信息
         */
        page.setStartIndex(0);
        page.setEndIndex(0);
        /*
         *获取数据
         */
        List<BudgetIncomeCalculateEO> rows;
        try {
            rows = budgetIncomeCalculateService
                .newQeryInvoiceAndBudgetByNameYearAndMonths(searchName, thisYear, startMonth, endMonth, page);
        } catch (Exception e) {
            log.error("|OperatingBudgetEO|按年份展示-1.8 业务经营情况", e);
            //return Result.error("-1", e.getMessage());
            return Result.error("-1", "接口调用异常");
        }

        PageInfo<BudgetIncomeCalculateEO> pageInfo = new PageInfo<>();
        pageInfo.setList(rows);
        pageInfo.setCount((long) budgetIncomeCalculateService
            .countInvoiceAndBudgetByNameYearAndMonths(searchName, thisYear, startMonth, endMonth));
        pageInfo.setPageSize(page.getPageSize());
        pageInfo.setPageNo(pageNo);

        return Result.success(pageInfo);
    }

    @ApiOperation(value = "|OperatingBudgetEO|查询所有部门年度办公用品的费用-1.9部门成本使用情况-办公用品")
    @GetMapping("/getOfficeCostByYear")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<List<Map<String, Object>>> queryOfficeCostByYear(String thisYear) {

        List<Map<String, Object>> resultList = deptBudgetEOService.queryOfficeCostByYear(thisYear);
        List<Map> pCountList = orgEODao.querySubDeptPersonCount();

        for (Map map : resultList) {
            int pCount = 0;
            float deptCost = 0.0f;

            for (Map map1 : pCountList) {
                String deptName = map.get("DEPTNAME").toString();
                if (deptName.equals(map1.get("DEPTNAME").toString())) {

                    pCount = Integer.valueOf(map1.get("PCOUNT").toString());
                    deptCost = Float.valueOf(map.get("VALUE").toString());
                }
            }
            if (pCount > 0) {
                map.put("singlePersonCost", deptCost / pCount);
            } else {
                map.put("singlePersonCost", 0);
            }

        }

        return Result.success(resultList);

    }

    @ApiOperation(value = "|OperatingBudgetEO|查询所有部门耗材年度的费用-1.10部门成本使用情况-耗材")
    @GetMapping("/getConsumableCostByYear")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<List<Map<String, Object>>> queryConsumableCostByYear(String thisYear) {
        List<Map<String, Object>> resultList = deptBudgetEOService.queryConsumableCostByYear(thisYear);
        List<Map> pCountList = orgEODao.querySubDeptPersonCount();

        for (Map map : resultList) {
            int pCount = 0;
            float deptCost = 0.0f;

            for (Map map1 : pCountList) {
                String deptName = map.get("DEPTNAME").toString();
                if (deptName.equals(map1.get("DEPTNAME").toString())) {

                    pCount = Integer.valueOf(map1.get("PCOUNT").toString());
                    deptCost = Float.valueOf(map.get("VALUE").toString());
                }
            }
            if (pCount > 0) {
                map.put("singlePersonCost", deptCost / pCount);
            } else {
                map.put("singlePersonCost", 0);
            }

        }

        return Result.success(resultList);
    }

    @ApiOperation(value = "|OperatingBudgetEO|查询所有部门差旅年度的费用-1.11部门成本使用情况-差旅")
    @GetMapping("/getTravelCostByYear")
//    @RequiresPermissions("business:deptBudget:page")
    public ResponseMessage<List<Map<String, Object>>> queryTravelCostByYear(String thisYear) {

        List<Map<String, Object>> resultList = deptBudgetEOService.queryTravelCostByYear(thisYear);
        List<Map> pCountList = orgEODao.querySubDeptPersonCount();

        for (Map map : resultList) {
            int pCount = 0;
            float deptCost = 0.0f;

            for (Map map1 : pCountList) {
                String deptName = map.get("DEPTNAME").toString();
                if (deptName.equals(map1.get("DEPTNAME").toString())) {

                    pCount = Integer.valueOf(map1.get("PCOUNT").toString());
                    deptCost = Float.valueOf(map.get("VALUE").toString());
                }
            }
            if (pCount > 0) {
                map.put("singlePersonCost", deptCost / pCount);
            } else {
                map.put("singlePersonCost", 0);
            }

        }

        return Result.success(resultList);
    }

    //////////////////////////////////////////////////
    ////
    ////  1.12 人员工时使用情况
    ////
    //////////////////////////////////////////////////

    @GetMapping("/topTen")
    @ApiOperation(value = "查看支出、创收前十名")
    public ResponseMessage<Map<String, List<DealIncomeVO>>>
    queryDealIncome(Integer year, Integer pageSize, boolean oldData) {
        if (year == null) {
            year = 2019;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        return Result.success(tableReportService.getDataFromDataBase(year, pageSize, oldData));
    }

}
