package com.adc.da.statistics.service;

import com.adc.da.statistics.dao.ContractAmountEODao;
import com.adc.da.statistics.dao.DataBoardOrgEODao;
import com.adc.da.statistics.dao.TaskWorktimeEODao;
import com.adc.da.statistics.entity.ContractAmountEO;
import com.adc.da.statistics.entity.DataBoardOrgEO;
import com.adc.da.statistics.entity.DataBoardTreeEO;
import com.adc.da.statistics.entity.TaskWorktimeEO;
import com.adc.da.statistics.vo.DataBoardGraphContractVO;
import com.adc.da.statistics.vo.DataBoardGraphVO;
import com.adc.da.statistics.vo.DataBoardMainVO;
import com.adc.da.statistics.vo.DataBoardOrgDetailVO;
import com.adc.da.statistics.vo.InterfaceDataBoardGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.adc.da.statistics.util.DataBoardUtils.INT_BEGIN;
import static com.adc.da.statistics.util.DataBoardUtils.INT_END;
import static com.adc.da.statistics.util.DataBoardUtils.initDate;

/**
 * <br>
 * <b>功能：</b>ST_BUSINESS_WORKTIME BusinessWorktimeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("dataBoardService")
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DataBoardService {

    @Autowired
    private ContractAmountEODao contractAmountEODao;

    @Autowired
    private DataBoardOrgEODao dataBoardOrgEODao;

    @Autowired
    private TaskWorktimeEODao taskWorktimeEODao;

    /** 组织维度 */
    private static final int ORG_LEVEL = 0;

    /** 业务维度 */
    private static final int BUSINESS_LEVEL = 1;

    /** 开票 */
    private static final int INT_BILLING = 0;

    /** 工时 */
    private static final int INT_WORK_TIME = 1;

    /** 进账 */
    private static final int INT_CREDIT = 2;

    /** 支出 */
    private static final int INT_EXPENDITURE = 3;

    /** 合同金额 */
    private static final int INT_CONTRACT = 4;

    /** 合同数量 */
    private static final int INT_SUM = 5;

    /**
     * 上方统计 框数量
     */
    private static final int ARRAY_LONG_SIZE = 6;

    /**
     * 下方图表数量合计
     */
    private static final int ARRAY_GRAPH_SIZE = 5;

    /**
     * 横坐标
     */
    private static final int X = 0;

    /**
     * 纵坐标
     */
    private static final int Y = 1;

    /**
     * 月份
     */
    private static final String[] MONTHS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

    /**
     * 处理合同相关数据
     *
     * @param contractAmountList
     * @param total
     * @param y
     */
    public void doContract(List<ContractAmountEO> contractAmountList, long[] total, String[][] y) {

        contractAmountList.forEach(eo -> {
            int monthIndex = Integer.parseInt(eo.getExtInfo01()) - 1;
            if (null != eo.getContractAmount()) {
                long amount = eo.getContractAmount().longValue();
                total[INT_CONTRACT] += amount;
                y[INT_CONTRACT][monthIndex] = String.valueOf(amount);
            }
            /*
             * 累加项目数量
             */
            if (null != eo.getExtInfo02()) {
                total[INT_SUM] += Integer.parseInt(eo.getExtInfo02());
            }

        });
        //todo 合同   total[INT_CONTRACT] += 往年结余数据
    }

    /**
     * 数据看板-主屏
     *
     * @param year
     * @return
     */
    public DataBoardMainVO mainBoard(int year, String[] deptIds) {
        long[] total = new long[ARRAY_LONG_SIZE];

        String[][][] pieChart = new String[2][ARRAY_GRAPH_SIZE][deptIds.length];
        String[][] yBarChart = new String[ARRAY_GRAPH_SIZE][12];
        Date[] date = initDate(year);
        /* 合同相关处理 */
        List<ContractAmountEO> contractAmountList = contractAmountEODao
            .countProjectByBudgetId(null, date[INT_BEGIN], date[INT_END]);
        doContract(contractAmountList, total, yBarChart);

        /*开票*/
        List<DataBoardOrgEO> billingList = dataBoardOrgEODao
            .queryInvoiceGroupByMonthBudget(null, date[INT_BEGIN], date[INT_END]);
        doBillingList(billingList, total, yBarChart);
        /*
         * 进账
         */
        List<DataBoardOrgEO> creditAndExpenditure = dataBoardOrgEODao.querySumGroupByMonth(year, null);
        doCreditAndExpenditure(creditAndExpenditure, total, yBarChart);

        /*type =1
         * 合同额 */
        List<DataBoardTreeEO> contractPieChart = contractAmountEODao
            .getMainBoardPieChart(date[INT_BEGIN], date[INT_END], 0b001, deptIds);

        doPieChart(contractPieChart, pieChart, INT_CONTRACT);

        /* type =2
         * 开票额*/
        List<DataBoardTreeEO> billingPieChart = contractAmountEODao
            .getMainBoardPieChart(date[INT_BEGIN], date[INT_END], 0b010, deptIds);
        doPieChart(billingPieChart, pieChart, INT_BILLING);

        /* type = 4
         * 进账*/
        List<DataBoardTreeEO> creditPieChart = contractAmountEODao
            .getMainBoardPieChart(date[INT_BEGIN], date[INT_END], 0b100, deptIds);
        doPieChart(creditPieChart, pieChart, INT_CREDIT);

        DataBoardMainVO.DataBoardMainVOBuilder result
            = DataBoardMainVO.builder()
                             .totalBilling(total[INT_BILLING])
                             .totalContract(total[INT_CONTRACT])
                             .totalCredit(total[INT_CREDIT]);
        initGraphMainBoard(result, yBarChart, pieChart);
        return result.build();
    }

    /**
     * 处理饼图
     *
     * @param contractPieChart
     * @param pieChart
     * @param intType
     */
    private void doPieChart(List<DataBoardTreeEO> contractPieChart, String[][][] pieChart, int intType) {
        List<String> amount = new ArrayList<>();
        List<String> name = new ArrayList<>();
        contractPieChart.forEach(eo -> {
            name.add(eo.getName());
            amount.add(String.valueOf(eo.getAmount()));
            //amount.add(eo.getAmount().toString());
        });
        pieChart[X][intType] = name.toArray(new String[0]);
        pieChart[Y][intType] = amount.toArray(new String[0]);
    }

    /**
     * 设置图
     *
     * @param builder
     * @param yBarChart
     * @param pieChart
     */
    private void initGraphMainBoard(DataBoardMainVO.DataBoardMainVOBuilder builder, String[][] yBarChart,
        String[][][] pieChart) {

        /* 设置饼图 */
        builder.billGraphPieChart(initGraphVO(pieChart[X][INT_BILLING], pieChart[Y][INT_BILLING]))
               .creditGraphPieChart(initGraphVO(pieChart[X][INT_CREDIT], pieChart[Y][INT_CREDIT]))
               .contractGraphPieChart(initGraphVO(pieChart[X][INT_CONTRACT], pieChart[Y][INT_CONTRACT]));


        /* 设置图 */
        builder.billGraph(initGraphVO(MONTHS, yBarChart[INT_BILLING]))
               .creditGraph(initGraphVO(MONTHS, yBarChart[INT_CREDIT]))
               .contractGraph(initGraphVO(MONTHS, yBarChart[INT_CONTRACT]));
    }

    /**
     * 业务维度看板数据
     *
     * @param budgetId
     * @param year
     * @return
     */
    public DataBoardOrgDetailVO budgetBoard(String budgetId, int year) {
        long[] total = new long[ARRAY_LONG_SIZE];
        /*
         * y 下方图的 纵坐标 范围 12个月
         */
        String[][] y = new String[ARRAY_GRAPH_SIZE][12];
        // 投入人力 2019-01-01  /  2019-12-31
        Date[] date = initDate(year);

        /* 合同相关处理 */
        List<ContractAmountEO> contractAmountList = contractAmountEODao
            .countProjectByBudgetId(budgetId, date[INT_BEGIN], date[INT_END]);
        doContract(contractAmountList, total, y);
        /** 详情 */
        List<ContractAmountEO> contractDetailList = contractAmountEODao
            .queryProjectByBudgetId(budgetId, date[INT_BEGIN], date[INT_END]);




        /*
         * 工时
         */
        List<TaskWorktimeEO> workTimeList = taskWorktimeEODao
            .getBusinessWorkTimeByBudgetId(budgetId, date[INT_BEGIN], date[INT_END]);
        doWorkTimeList(workTimeList, total, y);

        /*开票*/
        List<DataBoardOrgEO> billingList = dataBoardOrgEODao
            .queryInvoiceGroupByMonthBudget(budgetId, date[INT_BEGIN], date[INT_END]);
        doBillingList(billingList, total, y);

        DataBoardOrgDetailVO result = resultBuilder(total, y, BUSINESS_LEVEL);
        ((DataBoardGraphContractVO) result.getContractGraph()).setList(contractDetailList);
        return result;
    }

    /**
     * 组织维度看板数据
     *
     * @param deptId
     * @param year
     * @return
     */
    public DataBoardOrgDetailVO orgBoard(String deptId, int year) {
        long[] total = new long[ARRAY_LONG_SIZE];
        /*
         * y 下方四个图的 纵坐标 范围 12个月
         */
        String[][] y = new String[ARRAY_GRAPH_SIZE][12];

        // 投入人力 2019-01-01  /  2019-12-31
        Date[] date = initDate(year);
        /* 支出，进账 */
        List<DataBoardOrgEO> creditAndExpenditure = dataBoardOrgEODao.querySumGroupByMonth(year, deptId);
        doCreditAndExpenditure(creditAndExpenditure, total, y);

        /*查询 合同总数，合同金额总数 */
        ContractAmountEO amountEO = contractAmountEODao.countProjectByDeptId(deptId, date[INT_BEGIN], date[INT_END]);
        if (amountEO.getExtInfo02() != null) {
            total[INT_SUM] = Integer.parseInt(amountEO.getExtInfo02());
        }

        if (null != amountEO.getContractAmount()) {
            total[INT_CONTRACT] = amountEO.getContractAmount().intValue();
        }

        /*
         * 工时
         */
        List<TaskWorktimeEO> workTimeList = taskWorktimeEODao
            .getTaskWorkTimeByTopDeptId(deptId, date[INT_BEGIN], date[INT_END]);
        doWorkTimeList(workTimeList, total, y);

        //开票
        List<DataBoardOrgEO> billingList = dataBoardOrgEODao
            .queryBillingGroupByMonth(deptId, date[INT_BEGIN], date[INT_END]);
        doBillingList(billingList, total, y);


        /*
         * init builder
         */
        return resultBuilder(total, y, ORG_LEVEL);
    }

    private void doCreditAndExpenditure(
        List<DataBoardOrgEO> creditAndExpenditure, long[] total, String[][] y) {
        creditAndExpenditure.forEach(eo -> {
            int monthIndex = eo.getMonth() - 1;
            Double c = eo.getCredit();
            Double e = eo.getExpenditure();
            long credit = (c != null) ? c.longValue() : 0;
            long expenditure = (e != null) ? e.longValue() : 0;
            total[INT_CREDIT] += credit;
            total[INT_EXPENDITURE] += expenditure;
            y[INT_CREDIT][monthIndex] = String.valueOf(credit);
            y[INT_EXPENDITURE][monthIndex] = String.valueOf(expenditure);

        });
    }

    /**
     * 处理工时
     *
     * @param workTimeList
     * @param total
     * @param y
     */
    private void doWorkTimeList(List<TaskWorktimeEO> workTimeList, long[] total, String[][] y) {
        workTimeList.forEach(eo -> {
            int monthIndex = Integer.parseInt(eo.getMonth()) - 1;
            int amount = eo.getWorktime().intValue();
            total[INT_WORK_TIME] += amount;
            y[INT_WORK_TIME][monthIndex] = String.valueOf(amount);
        });
    }

    /**
     * 处理开票
     *
     * @param billingList
     * @param total
     * @param y
     */
    private void doBillingList(List<DataBoardOrgEO> billingList, long[] total, String[][] y) {
        billingList.forEach(eo -> {
            int monthIndex = eo.getMonth() - 1;
            long amount = eo.getBilling().longValue();
            total[INT_BILLING] += amount;
            y[INT_BILLING][monthIndex] = String.valueOf(amount);
        });
    }

    /**
     * 组装数据
     *
     * @param total
     * @param y
     * @return
     */
    private DataBoardOrgDetailVO resultBuilder(long[] total, String[][] y,
        int level) {
        DataBoardOrgDetailVO.DataBoardOrgDetailVOBuilder builder
            = DataBoardOrgDetailVO.builder()
                                  .sumProject(total[INT_SUM])
                                  .totalContract(total[INT_CONTRACT])
                                  .totalCredit(total[INT_CREDIT])
                                  .totalBilling(total[INT_BILLING])
                                  .totalExpenditure(total[INT_EXPENDITURE])
                                  .workTime(total[INT_WORK_TIME]);
        initGraph(builder, y, level);
        return builder.build();
    }

    /**
     * 设置图
     *
     * @param builder
     * @param y
     */
    private void initGraph(DataBoardOrgDetailVO.DataBoardOrgDetailVOBuilder builder, String[][] y, int level) {
        String[] months = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};

        /* 设置图 开票与工时 为公共属性*/
        builder.billGraph(initGraphVO(months, y[INT_BILLING]))
               .workTimeGraph(initGraphVO(months, y[INT_WORK_TIME]));

        if (level == ORG_LEVEL) {
            builder.creditGraph(initGraphVO(months, y[INT_CREDIT]))
                   .expenditureGraph(initGraphVO(months, y[INT_EXPENDITURE]));

        } else if (level == BUSINESS_LEVEL) {
            builder.contractGraph(initGraphContractVO(months, y[INT_CONTRACT]));

        }
    }

    /**
     * 生成图类
     *
     * @param x
     * @param y
     * @return
     */
    private DataBoardGraphVO initGraphVO(String[] x, String[] y) {
        return DataBoardGraphVO.builder()
                               .x(x).y(y)
                               .build();
    }

    /**
     * 生成图类
     *
     * @param x
     * @param y
     * @return
     */
    private InterfaceDataBoardGraph initGraphContractVO(String[] x, String[] y) {
        return DataBoardGraphContractVO.builder()
                                       .x(x).y(y).build();
    }

}
