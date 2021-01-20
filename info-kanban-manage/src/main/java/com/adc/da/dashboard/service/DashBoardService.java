package com.adc.da.dashboard.service;

import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.utils.DateUtil;
import com.adc.da.customerresourcemanage.dao.EnterpriseEODao;
import com.adc.da.customerresourcemanage.entity.EnterpriseEO;
import com.adc.da.dashboard.dao.ProvinceAreaEODao;
import com.adc.da.dashboard.entity.ProvinceAreaEO;
import com.adc.da.dashboard.vo.*;
import com.adc.da.industymeeting.dao.BudgetManagementInfoEODao;
import com.adc.da.industymeeting.dao.ReceivableIncomeEODao;
import com.adc.da.industymeeting.dao.ReceivableIncomeFiledEODao;
import com.adc.da.industymeeting.entity.BudgetManagementInfoEO;
import com.adc.da.industymeeting.entity.ReceivableIncomeEO;
import com.adc.da.industymeeting.entity.ReceivableIncomeFiledEO;
import com.adc.da.processform.dao.ProjectContractInvoiceEODao;
import com.adc.da.processform.entity.ProjectContractInvoiceEO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.util.DoubleUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class DashBoardService {
    @Autowired
    private ProjectRepository projectRepository ;

    @Autowired
    ProjectContractInvoiceEODao projectContractInvoiceEODao;

    @Autowired
    ReceivableIncomeEODao receivableIncomeEODao;

    @Autowired
    ReceivableIncomeFiledEODao receivableIncomeFiledEODao;

    @Autowired
    ProvinceAreaEODao provinceAreaEODao;

    @Autowired
    OrgEODao orgEODao;

    @Autowired
    BudgetManagementInfoEODao budgetManagementInfoEODao;

    @Autowired
    EnterpriseEODao enterpriseEODao;

    BigDecimal TEN_THOUSAND = BigDecimal.valueOf(10000);

    BigDecimal ZERO = BigDecimal.valueOf(0);

    /**
     * @Description: 数据资源中心经营数据看板-1
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 8:21
     **/
    public ContractDashBoardHeaderVO getContractDashBoardHeaderVO(){
        ContractDashBoardHeaderVO contractDashBoardHeaderVO = new ContractDashBoardHeaderVO();
        getContractDashBoardHeaderVOContractAmountYear(contractDashBoardHeaderVO);
        getContractDashBoardHeaderVOContractAmountMonth(contractDashBoardHeaderVO);
        getContractDashBoardHeaderVOInvoiceAmountYear(contractDashBoardHeaderVO);
        getContractDashBoardHeaderVOInvoiceAmountMonth(contractDashBoardHeaderVO);

        getContractDashBoardHeaderVOIncomeAmountYear(contractDashBoardHeaderVO);
        getContractDashBoardHeaderVOIncomeAmountMonth(contractDashBoardHeaderVO);

        getContractDashBoardHeaderVOReceivableAmount(contractDashBoardHeaderVO);

        return contractDashBoardHeaderVO;
    }

    //计算年累计合同额
    //2020-06-18
    // 新需求修改现在是2020年6月3日，则
    //今年累计合同额：加和所有年=2020年的合同额数据；
    //去年截止到本月累计合同额：年=2019，筛选月=1&2&3&4&5的数据加和，并与（年=2019，月=6的合同额）÷30*3
    //注：30为2019年6月的天数，3为6月3号在2019年6月的第三天
    public void getContractDashBoardHeaderVOContractAmountYear(ContractDashBoardHeaderVO contractDashBoardHeaderVO){
        DateTime dateTime = new DateTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime.toDate());

        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear(); //当前月份
        Integer currentDay = dateTime.getDayOfMonth();//当前日期是当月第几天
        Integer currentMonthTotalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 当前月份共有多少天

        List<Project> currentYearProjectList = projectRepository.findByProjectYearAndProjectTypeAndDelFlagNot(currentYear, 0, true);
        List<Project> lastYearProjectList = new ArrayList<>();
        // 2019年的合同数据由于历史原因 虽然是删除状态但需要统计
        if(lastYear==2019){
            lastYearProjectList = projectRepository.findByProjectYearAndProjectType(lastYear, 0);
        } else {
            lastYearProjectList = projectRepository.findByProjectYearAndProjectTypeAndDelFlagNot(lastYear, 0,true);
        }

        BigDecimal currentYearAmount = sumProjectContractAmount(currentYearProjectList);
        BigDecimal lastYearAmount = sumProjectContractAmountBetweenMonth(lastYearProjectList,1,currentMonth-1);

        BigDecimal lastYearCurrentMonthAmount = sumProjectContractAmountBetweenMonth(lastYearProjectList,currentMonth,currentMonth);

        BigDecimal bigDecimal = lastYearCurrentMonthAmount
                .divide(BigDecimal.valueOf(currentMonthTotalDays),4,RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(currentDay));
        lastYearAmount = lastYearAmount.add(bigDecimal);

        Double yearAmountRate = culRate(currentYearAmount, lastYearAmount);

        contractDashBoardHeaderVO.setYearContractAmount(getScale2(currentYearAmount));
        contractDashBoardHeaderVO.setYearContractAmountRate(yearAmountRate);
    }

    //计算月累计合同额
    public ContractDashBoardHeaderVO getContractDashBoardHeaderVOContractAmountMonth(ContractDashBoardHeaderVO contractDashBoardHeaderVO){
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();

        List<Project> currentYearProjectList = projectRepository.findByProjectYearAndProjectMonthAndDelFlagNot(currentYear, currentMonth, true);
        List<Project> lastYearProjectList = projectRepository.findByProjectYearAndProjectMonthAndDelFlagNot(lastYear, currentMonth, true);
        BigDecimal currentMonthAmount = sumProjectContractAmount(currentYearProjectList);
        BigDecimal lastMonthAmount = sumProjectContractAmount(lastYearProjectList);
        Double monthAmountRate = culRate(currentMonthAmount, lastMonthAmount);
        contractDashBoardHeaderVO.setMonthContractAmount(getScale2(currentMonthAmount));
        contractDashBoardHeaderVO.setMonthContractAmountRate(monthAmountRate);

        return contractDashBoardHeaderVO;
    }

    //计算年累计开票额
    // 例如：现在是2020年6月3日，则
    // 今年累计开票额：加和所有实际开票时间在2020年的数据；
    // 去年截止到本月累计合同额：年=2019，筛选2019-01-01至2019-06-03的实际开票额数据加和。
    public ContractDashBoardHeaderVO getContractDashBoardHeaderVOInvoiceAmountYear(ContractDashBoardHeaderVO contractDashBoardHeaderVO) {
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        Integer currentday = dateTime.getDayOfMonth();

        Calendar beginTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        beginTime.set(lastYear,0,1,0,0,0);
        endTime.set(lastYear,currentMonth,currentday,23,59,59);


//        ProjectContractInvoiceEO currentYearProjectContractInvoiceEO = projectContractInvoiceEODao.sumByYear(currentYear);
        ProjectContractInvoiceEO currentYearProjectContractInvoiceEO = new ProjectContractInvoiceEO();
        BigDecimal currentYearActualInvoiceAmount = projectContractInvoiceEODao.sumActualInvoiceAmountByYear(currentYear);
        currentYearProjectContractInvoiceEO.setActualInvoiceAmount(currentYearActualInvoiceAmount);
        BigDecimal currentYearChangeInvoiceAmount= projectContractInvoiceEODao.sumChangeInvoiceAmountByYear(currentYear);
        currentYearProjectContractInvoiceEO.setChangeInvoiceAmount(currentYearChangeInvoiceAmount);
        BigDecimal currentYearOriginInvoiceAmount= projectContractInvoiceEODao.sumOriginInvoiceAmountByYear(currentYear);
        currentYearProjectContractInvoiceEO.setOriginInvoiceAmount(currentYearOriginInvoiceAmount);
        BigDecimal currentYearBackInvoiceAmount= projectContractInvoiceEODao.sumBackInvoiceAmountByYear(currentYear);
        currentYearProjectContractInvoiceEO.setBackInvoiceAmount(currentYearBackInvoiceAmount);

//        ProjectContractInvoiceEO lastYearProjectContractInvoiceEO = projectContractInvoiceEODao.sumByDateBetween(beginTime.getTime(),endTime.getTime());
        ProjectContractInvoiceEO lastYearProjectContractInvoiceEO = new ProjectContractInvoiceEO();
        BigDecimal lastYearActualInvoiceAmount = projectContractInvoiceEODao.sumActualInvoiceAmountByDateBetween(beginTime.getTime(),endTime.getTime());
        lastYearProjectContractInvoiceEO.setActualInvoiceAmount(lastYearActualInvoiceAmount);
        BigDecimal lastYearChangeInvoiceAmount= projectContractInvoiceEODao.sumChangeInvoiceAmountByDateBetween(beginTime.getTime(),endTime.getTime());
        lastYearProjectContractInvoiceEO.setChangeInvoiceAmount(lastYearChangeInvoiceAmount);
        BigDecimal lastYearOriginInvoiceAmount= projectContractInvoiceEODao.sumOriginInvoiceAmountByDateBetween(beginTime.getTime(),endTime.getTime());
        lastYearProjectContractInvoiceEO.setChangeInvoiceAmount(lastYearOriginInvoiceAmount);
        BigDecimal lastYearBackInvoiceAmount= projectContractInvoiceEODao.sumBackInvoiceAmountByDateBetween(beginTime.getTime(),endTime.getTime());
        lastYearProjectContractInvoiceEO.setBackInvoiceAmount(lastYearBackInvoiceAmount);


        BigDecimal currentYearInvoiceAmount = BigDecimal.ZERO;
        BigDecimal lastYearInvoiceAmount = BigDecimal.ZERO;
        if (null!=currentYearProjectContractInvoiceEO) {
            currentYearInvoiceAmount = sumProjectContractInvoiceEOAmount(currentYearProjectContractInvoiceEO);
        }
        if (null!=lastYearProjectContractInvoiceEO) {
            lastYearInvoiceAmount = sumProjectContractInvoiceEOAmount(lastYearProjectContractInvoiceEO);
        }

        Double rate = culRate(currentYearInvoiceAmount, lastYearInvoiceAmount);

        contractDashBoardHeaderVO.setYearInvoiceAmount(getScale2(currentYearInvoiceAmount));
        contractDashBoardHeaderVO.setLastYearInvoiceAmount(getScale2(lastYearInvoiceAmount));

        contractDashBoardHeaderVO.setYearInvoiceAmountRate(rate);
        return contractDashBoardHeaderVO;
    }

    //计算年累计月开票额
    public ContractDashBoardHeaderVO getContractDashBoardHeaderVOInvoiceAmountMonth(ContractDashBoardHeaderVO contractDashBoardHeaderVO) {
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        BigDecimal currentMonthInvoiceAmount = BigDecimal.ZERO;
//        ProjectContractInvoiceEO currentMonthProjectContractInvoiceEO = projectContractInvoiceEODao.sumByYearAndMonth(currentYear, currentMonth);

        ProjectContractInvoiceEO currentMonthProjectContractInvoiceEO = new ProjectContractInvoiceEO();
        BigDecimal currentMonthActualInvoiceAmount = projectContractInvoiceEODao.sumActualInvoiceAmountByYearAndMonth(currentYear, currentMonth);
        currentMonthProjectContractInvoiceEO.setActualInvoiceAmount(currentMonthActualInvoiceAmount);
        BigDecimal currentMonthChangeInvoiceAmount= projectContractInvoiceEODao.sumChangeInvoiceAmountByYearAndMonth(currentYear, currentMonth);
        currentMonthProjectContractInvoiceEO.setChangeInvoiceAmount(currentMonthChangeInvoiceAmount);
        BigDecimal currentMonthOriginInvoiceAmount= projectContractInvoiceEODao.sumChangeInvoiceAmountByYearAndMonth(currentYear, currentMonth);
        currentMonthProjectContractInvoiceEO.setOriginInvoiceAmount(currentMonthOriginInvoiceAmount);
        BigDecimal currentMonthBackInvoiceAmount= projectContractInvoiceEODao.sumBackInvoiceAmountByYearAndMonth(currentYear, currentMonth);
        currentMonthProjectContractInvoiceEO.setBackInvoiceAmount(currentMonthBackInvoiceAmount);
        if (null!=currentMonthProjectContractInvoiceEO) {
            currentMonthInvoiceAmount = sumProjectContractInvoiceEOAmount(currentMonthProjectContractInvoiceEO);
        }
        contractDashBoardHeaderVO.setMonthInvoiceAmount(getScale2(currentMonthInvoiceAmount));
        return contractDashBoardHeaderVO;
    }

    //计算年累计进账额 【本年累计开票额 + 历史年份到账额 - 所有应收账款余额】 需求是本年的  但是跟丁丁对过了 改为所有应收
    public ContractDashBoardHeaderVO getContractDashBoardHeaderVOIncomeAmountYear(ContractDashBoardHeaderVO contractDashBoardHeaderVO) {
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        //看板管理--归档数据中，年=当年年份，“当月进账额（元）”的累计加和
        BigDecimal currentYearIncomeAmount = receivableIncomeFiledEODao.sumIncomeAmountByYearEqual(currentYear);
        BigDecimal lastYearIncomeAmount = receivableIncomeFiledEODao.sumIncomeAmountByYearAndMonthLte(lastYear,currentMonth);
        if (null == currentYearIncomeAmount) {
            currentYearIncomeAmount = ZERO;
        } else {
            currentYearIncomeAmount = currentYearIncomeAmount.divide(TEN_THOUSAND);
        }
        if (null == lastYearIncomeAmount) {
            lastYearIncomeAmount = ZERO;
        } else {
            lastYearIncomeAmount = lastYearIncomeAmount.divide(TEN_THOUSAND);
        }
        Double rate = culRate(currentYearIncomeAmount, lastYearIncomeAmount);
        contractDashBoardHeaderVO.setYearIncomeAmount(getScale2(currentYearIncomeAmount));
        contractDashBoardHeaderVO.setLastYearIncomeAmount(getScale2(lastYearIncomeAmount));
        contractDashBoardHeaderVO.setYearIncomeAmountRate(rate);
        return contractDashBoardHeaderVO;
    }

    //计算月累计进账额 【本年累计进账额-本年历史月份归档进账】
    //月度进账额逻辑更新 ：
    //采用看板管理--归档数据中，年=当年年份，月=各个月份，“当月进账额（元）”的累计加和
    public ContractDashBoardHeaderVO getContractDashBoardHeaderVOIncomeAmountMonth(ContractDashBoardHeaderVO contractDashBoardHeaderVO) {
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();

        BigDecimal currentMonthIncomeAmount = receivableIncomeFiledEODao.sumIncomeAmountByYearAndMonthEqual(currentYear, currentMonth);
        BigDecimal lastMonthIncomeAmount = receivableIncomeFiledEODao.sumIncomeAmountByYearAndMonthEqual(lastYear, currentMonth);

        if (null == currentMonthIncomeAmount) {
            currentMonthIncomeAmount = ZERO;
        } else {
            currentMonthIncomeAmount = currentMonthIncomeAmount.divide(TEN_THOUSAND);
        }

        if (null == lastMonthIncomeAmount) {
            lastMonthIncomeAmount = ZERO;
        } else {
            lastMonthIncomeAmount = lastMonthIncomeAmount.divide(TEN_THOUSAND);
        }

        Double monthAmountRate = culRate(currentMonthIncomeAmount, lastMonthIncomeAmount);
        contractDashBoardHeaderVO.setMonthIncomeAmount(getScale2(currentMonthIncomeAmount));
        contractDashBoardHeaderVO.setMonthIncomeAmountRate(monthAmountRate);
        return contractDashBoardHeaderVO;
    }

    //计算应收 【本年累计进账额-本年历史月份归档进账】
    public ContractDashBoardHeaderVO getContractDashBoardHeaderVOReceivableAmount(ContractDashBoardHeaderVO contractDashBoardHeaderVO) {
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();

        //本年应收
        BigDecimal currentReceivableAmount = receivableIncomeEODao.sumReceivableAmountByYearLte(currentYear);
        BigDecimal lastReceivableAmount = receivableIncomeFiledEODao.sumReceivableAmountByYearAndMonthEqual(lastYear, currentMonth);

        if (null == currentReceivableAmount) {
            currentReceivableAmount = ZERO;
        } else {
            currentReceivableAmount = currentReceivableAmount.divide(TEN_THOUSAND);
        }
        if (null == lastReceivableAmount) {
            lastReceivableAmount = ZERO;
        } else {
            lastReceivableAmount = lastReceivableAmount.divide(TEN_THOUSAND);
        }

        Double rate = culFinishRate(currentReceivableAmount.doubleValue(), lastReceivableAmount.doubleValue());
        contractDashBoardHeaderVO.setReceivableAmount(getScale2(currentReceivableAmount.doubleValue()));
        contractDashBoardHeaderVO.setReceivableAmountRate(rate);
        return contractDashBoardHeaderVO;
    }

    public ContractDashBoardBodyVO getContractDashBoardBodyVO(int type) {
        if (type == 0) { // 合同额
            return culContractAmountByMonth();
        } else if (type == 1) { //开票额
            return culInvoiceAmountByMonth();
        } else if (type == 2) {//进账额
            return culIncomeAmountByMonth();
        } else {
            throw new AdcDaBaseException("参数异常");
        }
    }

    //计算合同额数据
    private ContractDashBoardBodyVO culContractAmountByMonth() {
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        ContractDashBoardBodyVO contractDashBoardBodyVO = new ContractDashBoardBodyVO();
        List<Double> currentTimeDataList = contractDashBoardBodyVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = contractDashBoardBodyVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = contractDashBoardBodyVO.getRateList(); //同比变化率

        List<Project> currentYearProjectList = projectRepository.findByProjectYearAndProjectTypeAndDelFlagNot(currentYear, 0,true);
        List<Project> lastYearProjectList = projectRepository.findByProjectYearAndProjectType(lastYear,0);

        for (int i = 1; i <= 12; i++) {
            BigDecimal currentMonthAmount =BigDecimal.ZERO;
            BigDecimal lastMonthAmount = sumProjectContractAmountByMonth(lastYearProjectList, i);
            if (i <= currentMonth) {
                currentMonthAmount = sumProjectContractAmountByMonth(currentYearProjectList, i);
                Double rate = culRate(currentMonthAmount, lastMonthAmount);
                rateList.add(rate);
            }else {
                rateList.add(0.0D);
            }
            currentTimeDataList.add(getScale2(currentMonthAmount));
            lastTimeDataList.add(getScale2(lastMonthAmount));
        }

        return contractDashBoardBodyVO;
    }

    private ContractDashBoardBodyVO culInvoiceAmountByMonth() {
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        ContractDashBoardBodyVO contractDashBoardBodyVO = new ContractDashBoardBodyVO();
        List<Double> currentTimeDataList = contractDashBoardBodyVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = contractDashBoardBodyVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = contractDashBoardBodyVO.getRateList(); //同比变化率

        List<ProjectContractInvoiceEO> currentYearProjectContractInvoiceEOList = projectContractInvoiceEODao.sumByYearGroupByMonth(currentYear);
        List<ProjectContractInvoiceEO> lastYearProjectContractInvoiceEOList = projectContractInvoiceEODao.sumByYearGroupByMonth(lastYear);

        for (int i = 1; i <= 12; i++) {
            BigDecimal currentMonthAmount = BigDecimal.ZERO;
            BigDecimal lastMonthAmount =  BigDecimal.ZERO;
            for (ProjectContractInvoiceEO projectContractInvoiceEO : currentYearProjectContractInvoiceEOList){
                if (i == projectContractInvoiceEO.getMonth()){
                    currentMonthAmount = sumProjectContractInvoiceEOAmount(projectContractInvoiceEO);
                    break;
                }
            }
            for (ProjectContractInvoiceEO projectContractInvoiceEO : lastYearProjectContractInvoiceEOList){
                if (i == projectContractInvoiceEO.getMonth()){
                    lastMonthAmount = sumProjectContractInvoiceEOAmount(projectContractInvoiceEO);
                    break;
                }
            }
            if (i <= currentMonth) {
                Double rate = culRate(currentMonthAmount, lastMonthAmount);
                rateList.add(rate);
            }else {
                rateList.add(0.0D);
            }
            currentTimeDataList.add(getScale2(currentMonthAmount));
            lastTimeDataList.add(getScale2(lastMonthAmount));
        }

        return contractDashBoardBodyVO;
    }

    private ContractDashBoardBodyVO culIncomeAmountByMonth() {
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        ContractDashBoardBodyVO contractDashBoardBodyVO = new ContractDashBoardBodyVO();
        List<Double> currentTimeDataList = contractDashBoardBodyVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = contractDashBoardBodyVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = contractDashBoardBodyVO.getRateList(); //同比变化率

        for (int i = 1; i <= 12; i++) {
            BigDecimal currentMonthIncomeAmount = receivableIncomeFiledEODao.sumIncomeAmountByYearAndMonthEqual(currentYear, i);
            BigDecimal lastMonthIncomeAmount = receivableIncomeFiledEODao.sumIncomeAmountByYearAndMonthEqual(lastYear, i);

            if (null == currentMonthIncomeAmount) {
                currentMonthIncomeAmount = ZERO;
            } else {
                currentMonthIncomeAmount = currentMonthIncomeAmount.divide(TEN_THOUSAND);
            }

            if (null == lastMonthIncomeAmount) {
                lastMonthIncomeAmount = ZERO;
            } else {
                lastMonthIncomeAmount = lastMonthIncomeAmount.divide(TEN_THOUSAND);
            }
            if (i <= currentMonth) {
                Double rate = culRate(currentMonthIncomeAmount, lastMonthIncomeAmount);
                rateList.add(rate);
            }else {
                rateList.add(0.0D);
            }

            currentTimeDataList.add(getScale2(currentMonthIncomeAmount));
            lastTimeDataList.add(getScale2(lastMonthIncomeAmount));

        }

        return contractDashBoardBodyVO;
    }

    public BigDecimal sumProjectContractInvoiceEOAmount(ProjectContractInvoiceEO projectContractInvoiceEO) {
        BigDecimal amount = ZERO;
        if (null != projectContractInvoiceEO.getActualInvoiceAmount()){ //实际开票
            amount = amount.add(projectContractInvoiceEO.getActualInvoiceAmount());
        }

        if (null != projectContractInvoiceEO.getChangeInvoiceAmount()){  // 改票
            amount = amount.add(projectContractInvoiceEO.getChangeInvoiceAmount());
        }
        if (null != projectContractInvoiceEO.getOriginInvoiceAmount()){
            amount = amount.subtract(projectContractInvoiceEO.getOriginInvoiceAmount());
        }

        if (null != projectContractInvoiceEO.getBackInvoiceAmount()){ //退款
            amount = amount.subtract(projectContractInvoiceEO.getBackInvoiceAmount());
        }
        return amount.divide(TEN_THOUSAND,4,BigDecimal.ROUND_HALF_UP);
    }




    public BigDecimal sumProjectContractInvoiceEOAmount(List<ProjectContractInvoiceEO> projectContractInvoiceEOList) {
        BigDecimal amount = ZERO;
        for (ProjectContractInvoiceEO projectContractInvoiceEO : projectContractInvoiceEOList) {

            amount =  amount.add(sumProjectContractInvoiceEOAmount(projectContractInvoiceEO));
        }
        return amount;
    }


//
//    public Double sumProjectContractInvoiceEOAmountUntilDate(List<ProjectContractInvoiceEO> projectContractInvoiceEOList,Date date) {
////        DateTime dateTime = new DateTime(date);
////        dateTime.plusDays(1);//当月的明天
//        long timestamp = DateUtils.getOnlyYMD(date).getTime()-1; //减去1秒
//
//        BigDecimal amount = ZERO;
//        for (ProjectContractInvoiceEO projectContractInvoiceEO : projectContractInvoiceEOList) {
//            if (null == projectContractInvoiceEO.getInvoiceAmount() || null == projectContractInvoiceEO.getInvoiceDate()) {
//                continue;
//            }
//            DateTime invoiceDate = new DateTime(projectContractInvoiceEO.getInvoiceDate());
//            if (invoiceDate.getMillis() <= timestamp) {
//                amount = amount.add(projectContractInvoiceEO.getInvoiceAmount());
//            }
//        }
//        return amount.divide(TEN_THOUSAND, 4, RoundingMode.HALF_UP).doubleValue();
//    }

    public Double sumProjectContractInvoiceEOAmountUntilDate(List<ProjectContractInvoiceEO> projectContractInvoiceEOList,
                                                             Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);
        /*DateTime dateTime = new DateTime(date);
        dateTime.plusDays(1);//当月的明天*/
        long timestamp = DateUtils.getOnlyYMD(calendar.getTime()).getTime()-1; //减去1秒

        BigDecimal amount = ZERO;
        for (ProjectContractInvoiceEO projectContractInvoiceEO : projectContractInvoiceEOList) {
            if (null != projectContractInvoiceEO.getInvoiceDate() && projectContractInvoiceEO.getInvoiceDate().getTime() >timestamp){
                continue;
            }
            if (null == projectContractInvoiceEO.getInvoiceAmount() || null == projectContractInvoiceEO.getInvoiceDate()) {
                continue;
            }
            amount = amount.add(projectContractInvoiceEO.getInvoiceAmount());
        }
        return amount.divide(TEN_THOUSAND, 4, RoundingMode.HALF_UP).doubleValue();
    }

    public BigDecimal sumProjectContractInvoiceEOAmountBigDecimal(List<ProjectContractInvoiceEO> projectContractInvoiceEOList) {
        BigDecimal amount = ZERO;
        for (ProjectContractInvoiceEO projectContractInvoiceEO : projectContractInvoiceEOList) {
            if (null == projectContractInvoiceEO.getInvoiceAmount() || null == projectContractInvoiceEO.getInvoiceDate()) {
                continue;
            }
            amount = amount.add(sumProjectContractInvoiceEOAmount(projectContractInvoiceEO));
        }
        return amount;
    }

    public Double sumIncomeEOAmount(List<ReceivableIncomeEO> receivableIncomeEOList, String time, int timeType) {
        BigDecimal amount = ZERO;
        for (ReceivableIncomeEO receivableIncomeEO : receivableIncomeEOList) {
            if (null == receivableIncomeEO.getAccountTime() || null == receivableIncomeEO.getWeeklyArrival()) {
                continue;
            }
            if (timeType == 0) { // 按照年份计算总额
                String dateStr = receivableIncomeEO.getAccountTime().substring(0, 4);
                if (StringUtils.equals(dateStr, time)) {
                    amount = amount.add(receivableIncomeEO.getWeeklyArrival());
                }
            } else if (timeType == 1) { // 按照年月计算
                String dateStr = receivableIncomeEO.getAccountTime().substring(5, 6);
                if (StringUtils.equals(dateStr, time)) {
                    amount = amount.add(receivableIncomeEO.getWeeklyArrival());
                }
            } else if (timeType == 2) { // 按照历史月计算 即月份小于传入月份的
                int month = Integer.valueOf(receivableIncomeEO.getAccountTime().substring(5, 6));
                if (month < Integer.valueOf(time)) {
                    amount = amount.add(receivableIncomeEO.getWeeklyArrival());
                }
            } else if (timeType == 3) { // 按照当月计算
                int month = Integer.valueOf(receivableIncomeEO.getAccountTime().substring(5, 6));
                if (month == Integer.valueOf(time)) {
                    amount = amount.add(receivableIncomeEO.getWeeklyArrival());
                }
            }
        }
        return amount.divide(TEN_THOUSAND, 4, RoundingMode.HALF_UP).doubleValue();
    }

    public Double sumIncomeEOAmount(List<ReceivableIncomeEO> receivableIncomeEOList) {
        BigDecimal amount = ZERO;
        for (ReceivableIncomeEO receivableIncomeEO : receivableIncomeEOList) {
            if (null == receivableIncomeEO.getAccountTime() || null == receivableIncomeEO.getWeeklyArrival()) {
                continue;
            }
            amount = amount.add(receivableIncomeEO.getWeeklyArrival());
        }
        return amount.divide(TEN_THOUSAND).doubleValue();
    }

    public Double sumReceivableAmount(List<ReceivableIncomeEO> receivableIncomeEOList) {
        BigDecimal amount = ZERO;
        for (ReceivableIncomeEO receivableIncomeEO : receivableIncomeEOList) {
            if (null == receivableIncomeEO.getAccountTime() || null == receivableIncomeEO.getWeeklyArrival()) {
                continue;
            }
            amount = amount.add(receivableIncomeEO.getAmountReceivable());
        }
        return amount.divide(TEN_THOUSAND).doubleValue();
    }

    public Double sumHistoryIncomeEOAmountByMonth(List<ReceivableIncomeFiledEO> receivableIncomeFiledEOList, int month) {
        BigDecimal amount = ZERO;
        for (ReceivableIncomeFiledEO receivableIncomeFiledEO : receivableIncomeFiledEOList) {
            if (StringUtils.isNotEmpty(receivableIncomeFiledEO.getMonth()) && month == Integer.valueOf(receivableIncomeFiledEO.getMonth())) {
                amount = amount.add(receivableIncomeFiledEO.getIncome());
            }
        }
        return amount.divide(TEN_THOUSAND).doubleValue();
    }

    /**
     * @Description: 根据公司名称查询所在地
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 14:32
     **/
    public String getCompanyAreaByCompanyName(List<EnterpriseEO> enterpriseEOList, String companyName) {
        for (EnterpriseEO enterpriseEO : enterpriseEOList) {
            if (StringUtils.equals(enterpriseEO.getEnterpriseName(), companyName)) {
                return enterpriseEO.getEnterpriseProvince();
            }
        }
        return "其他";
    }

    /**
     * @Description: ReceivableIncomeEO
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, List<ReceivableIncomeEO>> getReceivableIncomeEOAreaMap(List<ReceivableIncomeEO> receivableIncomeEOList,
            List<EnterpriseEO> enterpriseEOList,
            List<ProvinceAreaEO> provinceAreaEOList) {

        Map<String, List<ReceivableIncomeEO>> areaReceivableIncomeEOMap = new LinkedHashMap<>();

        for (ReceivableIncomeEO receivableIncomeEO : receivableIncomeEOList) {
            if (StringUtils.isEmpty(receivableIncomeEO.getProject())) {
                continue;
            }
            String province = getCompanyAreaByCompanyName(enterpriseEOList, receivableIncomeEO.getCorpname());
            String area = getProvinceBelong(provinceAreaEOList, province);
            List<ReceivableIncomeEO> tempList = areaReceivableIncomeEOMap.get(area);
            if (null == tempList) {
                tempList = new ArrayList<>();
            }
            tempList.add(receivableIncomeEO);
            areaReceivableIncomeEOMap.put(area, tempList);
        }
        return areaReceivableIncomeEOMap;
    }

    /**
     * @Description: ReceivableIncomeEO
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, List<ReceivableIncomeEO>> getProvinceReceivableIncomeEOMap(List<ReceivableIncomeEO> receivableIncomeEOList,
            List<EnterpriseEO> enterpriseEOList) {

        Map<String, List<ReceivableIncomeEO>> provinceReceivableIncomeEOMap = new LinkedHashMap<>();

        for (ReceivableIncomeEO receivableIncomeEO : receivableIncomeEOList) {
            if (StringUtils.isEmpty(receivableIncomeEO.getProject())) {
                continue;
            }
            String province = getCompanyAreaByCompanyName(enterpriseEOList, receivableIncomeEO.getCorpname());
            List<ReceivableIncomeEO> tempList = provinceReceivableIncomeEOMap.get(province);
            if (null == tempList) {
                tempList = new ArrayList<>();
            }
            tempList.add(receivableIncomeEO);
            provinceReceivableIncomeEOMap.put(province, tempList);
        }
        return provinceReceivableIncomeEOMap;
    }

    /**
     * @Description: ReceivableIncomeEO
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, List<ReceivableIncomeEO>> getOrgReceivableIncomeEOMap(List<ReceivableIncomeEO> receivableIncomeEOList,
            List<OrgEO> orgEOList) {
        Map<String, List<ReceivableIncomeEO>> orgNameReceivableIncomeEOMap = new LinkedHashMap<>();

        for (ReceivableIncomeEO receivableIncomeEO : receivableIncomeEOList) {
            if (StringUtils.isEmpty(receivableIncomeEO.getProject())) {
                continue;
            }
            String orgName = getOrgEOBelong(orgEOList, receivableIncomeEO.getDepartmentId()).getName();
            List<ReceivableIncomeEO> tempList = orgNameReceivableIncomeEOMap.get(orgName);
            if (null == tempList) {
                tempList = new ArrayList<>();
            }
            tempList.add(receivableIncomeEO);
            orgNameReceivableIncomeEOMap.put(orgName, tempList);
        }
        return orgNameReceivableIncomeEOMap;
    }

    /**
     * @Description: ReceivableIncomeEO
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
//    public Map<String, List<ReceivableIncomeFiledEO>> getOrgReceivableIncomeFiledEOMap(List<ReceivableIncomeFiledEO> receivableIncomeFiledEOList,
//            List<OrgEO> orgEOList) {
//        Map<String, List<ReceivableIncomeFiledEO>> orgNameReceivableIncomeEOMap = new LinkedHashMap<>();
//
//        for (ReceivableIncomeFiledEO ReceivableIncomeFiledEO : receivableIncomeFiledEOList) {
//            if (StringUtils.isEmpty(ReceivableIncomeFiledEO.getArea())) {
//                continue;
//            }
//            String orgName = getOrgEOBelong(orgEOList, receivableIncomeEO.getDepartmentId()).getName();
//            List<ReceivableIncomeEO> tempList = orgNameReceivableIncomeEOMap.get(orgName);
//            if (null == tempList) {
//                tempList = new ArrayList<>();
//            }
//            tempList.add(receivableIncomeEO);
//            orgNameReceivableIncomeEOMap.put(orgName, tempList);
//        }
//        return orgNameReceivableIncomeEOMap;
//    }
    public BigDecimal sumProjectContractAmount(List<Project> projectList){
        BigDecimal amount = ZERO;
        for (Project project : projectList){
            if (StringUtils.isNotEmpty( project.getContractAmountStr())) {
                amount = amount.add(new BigDecimal(project.getContractAmountStr()));
            }else {
                amount = amount.add(new BigDecimal(project.getContractAmount()));
            }
        }
        return amount.divide(TEN_THOUSAND);
    }
    /*
    * 计算某两个月份之间的合同和 闭区间
    *
    * */
    public BigDecimal sumProjectContractAmountBetweenMonth(List<Project> projectList,Integer startMonth, Integer endMonth){
        BigDecimal amount = ZERO;
        for (Project project : projectList){
            if(project.getProjectMonth() >= startMonth&& project.getProjectMonth() <= endMonth){
                if (StringUtils.isNotEmpty( project.getContractAmountStr())) {
                    amount = amount.add(new BigDecimal(project.getContractAmountStr()));
                }else {
                    amount = amount.add(new BigDecimal(project.getContractAmount()));
                }
            }
        }
        return amount.divide(TEN_THOUSAND);
    }

    public BigDecimal sumProjectContractAmountByMonth(List<Project> projectList, int month) {
        List<Project> checkProjectList = new ArrayList<>();
        BigDecimal amount = ZERO;
        for (Project project : projectList) {
            if (project.getProjectMonth() != month) {
                continue;
            }
            checkProjectList.add(project);
            if (StringUtils.isNotEmpty(project.getContractAmountStr())) {
                amount = amount.add(new BigDecimal(project.getContractAmountStr()));
            } else {
                amount = amount.add(new BigDecimal(project.getContractAmount()));
            }
        }
        for (Project project : projectList) {
            System.out.println(project.getContractNo()+"---"+project.getBudgetDomainId());
        }
        return amount.divide(TEN_THOUSAND);
    }

    /**
     * @Description: 对project分类
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, List<Project>> getProjectAreaMap(List<Project> projectList, List<ProvinceAreaEO> provinceAreaEOList) {
        Map<String, List<Project>> areaProjectMap = new LinkedHashMap<>();

        for (Project project : projectList) {
            if (StringUtils.isEmpty(project.getProvince())) {
                continue;
            }
            String area = getProvinceBelong(provinceAreaEOList, project.getProvince());
            List<Project> tempList = areaProjectMap.get(area);
            if (null == tempList) {
                tempList = new ArrayList<>();
            }
            tempList.add(project);
            areaProjectMap.put(area, tempList);

        }
        return areaProjectMap;
    }

    /**
     * @Description: 对project分类
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, List<Project>> getProvinceProjectMap(List<Project> projectList) {
        Map<String, List<Project>> areaProjectMap = new LinkedHashMap<>();

        for (Project project : projectList) {
            if (StringUtils.isEmpty(project.getProvince())) {
                continue;
            }
            String province = project.getProvince();
            List<Project> tempList = areaProjectMap.get(province);
            if (null == tempList) {
                tempList = new ArrayList<>();
            }
            tempList.add(project);
            areaProjectMap.put(province, tempList);

        }
        return areaProjectMap;
    }


    /**
     * @Description: 对project分类
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 17:03
     **/
    public Map<String, List<Project>> getProjectOrgMap(List<Project> projectList, List<OrgEO> orgEOList) {
        Map<String, List<Project>> orgProjectMap = new LinkedHashMap<>();

        for (Project project : projectList) {
            if (StringUtils.isEmpty(project.getDeptId())) {
                continue;
            }
            String bigOrgNameAbb = getOrgEOBelong(orgEOList, project.getDeptId()).getName();
            List<Project> tempList = orgProjectMap.get(bigOrgNameAbb);
            if (null == tempList) {
                tempList = new ArrayList<>();
            }
            tempList.add(project);
            orgProjectMap.put(bigOrgNameAbb, tempList);

        }
        return orgProjectMap;
    }

    /**
     * @Description: 对project分类
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 17:03
     **/
    public Map<String, List<Project>> getProjectOrgMap(List<Project> projectList) {
        Map<String, List<Project>> orgIdProjectMap = new LinkedHashMap<>();
        for (Project project : projectList) {
            if (StringUtils.isEmpty(project.getDeptId())) {
                continue;
            }
            List<Project> tempList = orgIdProjectMap.get(project.getDeptId());
            if (null == tempList) {
                tempList = new ArrayList<>();
            }
            tempList.add(project);
            orgIdProjectMap.put(project.getDeptId(), tempList);
        }
        return orgIdProjectMap;
    }

    /**
     * @Description: 对开票数据按区域分类
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, List<ProjectContractInvoiceEO>> getContractInvoiceEOAreaMap(List<ProjectContractInvoiceEO> contractInvoiceEOList,
            List<ProvinceAreaEO> provinceAreaEOList) {
        Map<String, List<ProjectContractInvoiceEO>> contractInvoiceEOAreaMap = new LinkedHashMap<>();
        Map<String, String> projectIdAndProvinceMap = new LinkedHashMap<>();

        Set<String> projectIdSet = getProjectIdSet(contractInvoiceEOList);
        if (CollectionUtils.isEmpty(projectIdSet)) {
            return contractInvoiceEOAreaMap;
        }

        List<Project> projectList = projectRepository.findByIdIn(projectIdSet);
        for (Project project : projectList) {
            projectIdAndProvinceMap.put(project.getId(), project.getProvince());
        }

        for (ProjectContractInvoiceEO contractInvoiceEO : contractInvoiceEOList) {
            if (StringUtils.isEmpty(projectIdAndProvinceMap.get(contractInvoiceEO.getProjectId()))) {
                continue;
            }
            String area = getProvinceBelong(provinceAreaEOList, projectIdAndProvinceMap.get(contractInvoiceEO.getProjectId()));
            List<ProjectContractInvoiceEO> tempList = contractInvoiceEOAreaMap.get(area);
            if (null == tempList) {
                tempList = new ArrayList<>();
            }
            tempList.add(contractInvoiceEO);
            contractInvoiceEOAreaMap.put(area, tempList);
        }
        return contractInvoiceEOAreaMap;
    }

    /**
     * @Description: 对开票数据按区域分类 新版的 省份已经存在 ext1  或者 ext3 中
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, List<ProjectContractInvoiceEO>> getContractInvoiceEOAreaMapNew(List<ProjectContractInvoiceEO> contractInvoiceEOList,
                                                                                   List<ProvinceAreaEO> provinceAreaEOList) {
        Map<String, List<ProjectContractInvoiceEO>> contractInvoiceEOAreaMap = new LinkedHashMap<>();

        for (ProjectContractInvoiceEO contractInvoiceEO : contractInvoiceEOList) {
            if (StringUtils.isEmpty(contractInvoiceEO.getExt01())||StringUtils.isEmpty(contractInvoiceEO.getExt03())) {
                continue;
            }
            String area =null;
            if (!StringUtils.isEmpty(contractInvoiceEO.getExt01())) {
                area = getProvinceBelong(provinceAreaEOList, contractInvoiceEO.getExt01());
            }else {
                area = getProvinceBelong(provinceAreaEOList, contractInvoiceEO.getExt03());
            }
            List<ProjectContractInvoiceEO> tempList = contractInvoiceEOAreaMap.get(area);
            if (CollectionUtils.isEmpty(tempList)) {
                tempList = new ArrayList<>();
            }
            tempList.add(contractInvoiceEO);
            contractInvoiceEOAreaMap.put(area, tempList);
        }
        return contractInvoiceEOAreaMap;
    }

    /**
     * @Description: 对开票数据按区域分类
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, List<ProjectContractInvoiceEO>> getProvinceContractInvoiceEOMap(List<ProjectContractInvoiceEO> contractInvoiceEOList) {
        Map<String, List<ProjectContractInvoiceEO>> provinceContractInvoiceEOAreaMap = new LinkedHashMap<>();
        Map<String, String> projectIdAndProvinceMap = new LinkedHashMap<>();

        Set<String> projectIdSet = getProjectIdSet(contractInvoiceEOList);
        if (CollectionUtils.isEmpty(projectIdSet)) {
            return provinceContractInvoiceEOAreaMap;
        }

        List<Project> projectList = projectRepository.findByIdIn(projectIdSet);
        for (Project project : projectList) {
            projectIdAndProvinceMap.put(project.getId(), project.getProvince());
        }

        for (ProjectContractInvoiceEO contractInvoiceEO : contractInvoiceEOList) {
            if (StringUtils.isEmpty(projectIdAndProvinceMap.get(contractInvoiceEO.getProjectId()))) {
                continue;
            }
            String province = projectIdAndProvinceMap.get(contractInvoiceEO.getProjectId());
            List<ProjectContractInvoiceEO> tempList = provinceContractInvoiceEOAreaMap.get(province);
            if (null == tempList) {
                tempList = new ArrayList<>();
            }
            tempList.add(contractInvoiceEO);
            provinceContractInvoiceEOAreaMap.put(province, tempList);
        }
        return provinceContractInvoiceEOAreaMap;
    }

    public Set<String> getProjectIdSet(List<ProjectContractInvoiceEO> contractInvoiceEOList) {
        Set<String> projectIdSet = new LinkedHashSet<>();
        for (ProjectContractInvoiceEO contractInvoiceEO : contractInvoiceEOList) {
            if (StringUtils.isNotEmpty(contractInvoiceEO.getProjectId())) {
                projectIdSet.add(contractInvoiceEO.getProjectId());
            }
        }
        return projectIdSet;
    }

    /**
     * @Description: 对开票数据按部门分类
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, List<ProjectContractInvoiceEO>> getOrgIdContractInvoiceEOMap(List<ProjectContractInvoiceEO> contractInvoiceEOList,
            Set<String> allOrgIdSet) {
        Map<String, List<ProjectContractInvoiceEO>> orgIdContractInvoiceEOMap = new LinkedHashMap<>();
        Map<String, String> projectIdAndOrgIdMap = new LinkedHashMap<>();

        Set<String> projectIdSet = getProjectIdSet(contractInvoiceEOList);
        if (CollectionUtils.isEmpty(projectIdSet)) {
            return orgIdContractInvoiceEOMap;
        }
        List<Project> projectList = projectRepository.findByIdIn(projectIdSet);
        for (Project project : projectList) {
            projectIdAndOrgIdMap.put(project.getId(), project.getDeptId());
            allOrgIdSet.add(project.getDeptId());
        }
        for (ProjectContractInvoiceEO contractInvoiceEO : contractInvoiceEOList) {
            if (StringUtils.isEmpty(projectIdAndOrgIdMap.get(contractInvoiceEO.getProjectId()))) {
                continue;
            }
            String orgId = projectIdAndOrgIdMap.get(contractInvoiceEO.getProjectId());

            List<ProjectContractInvoiceEO> tempList = orgIdContractInvoiceEOMap.get(orgId);
            if (null == tempList) {
                tempList = new ArrayList<>();
            }
            tempList.add(contractInvoiceEO);
            orgIdContractInvoiceEOMap.put(orgId, tempList);
        }
        return orgIdContractInvoiceEOMap;
    }

    /**
     * @Description: 对开票数据按部门分类 根据business dept
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, List<ProjectContractInvoiceEO>> getOrgIdContractInvoiceEOMapNew(List<ProjectContractInvoiceEO> contractInvoiceEOList) {
        Map<String, List<ProjectContractInvoiceEO>> orgIdContractInvoiceEOMap = new LinkedHashMap<>();
        for (ProjectContractInvoiceEO contractInvoiceEO : contractInvoiceEOList) {
            if (StringUtils.isEmpty(contractInvoiceEO.getBusinessDeptId())) {
                continue;
            }
            List<ProjectContractInvoiceEO> tempList = orgIdContractInvoiceEOMap.get(contractInvoiceEO.getBusinessDeptId());
            if (null == tempList) {
                tempList = new ArrayList<>();
            }
            tempList.add(contractInvoiceEO);
            orgIdContractInvoiceEOMap.put(contractInvoiceEO.getBusinessDeptId(), tempList);
        }
        return orgIdContractInvoiceEOMap;
    }

    /**
     * @Description: 对预算数据按小部门分类
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, BigDecimal> getSmallOrgIdBudgetManagementInfoEOMap(List<BudgetManagementInfoEO> budgetManagementInfoEOList) {
        Map<String, BigDecimal> orgIdBudgetAmountMap = new LinkedHashMap<>();

        for (BudgetManagementInfoEO budgetManagementInfoEO : budgetManagementInfoEOList) {
            orgIdBudgetAmountMap.put(budgetManagementInfoEO.getDepartmentId(), budgetManagementInfoEO.getOuputTarget());
        }
        return orgIdBudgetAmountMap;
    }

    /**
     * @Description: 对预算数据按大部门分类
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, BigDecimal> getBigOrgIdBudgetManagementInfoEOEOMap(List<BudgetManagementInfoEO> budgetManagementInfoEOList) {
        Map<String, BigDecimal> orgIdBudgetAmountMap = new LinkedHashMap<>();

        for (BudgetManagementInfoEO budgetManagementInfoEO : budgetManagementInfoEOList) {
            BigDecimal budgetAmount = ZERO;
            if (null != orgIdBudgetAmountMap.get(budgetManagementInfoEO.getHeadquartersId())) {
                budgetAmount = budgetManagementInfoEO.getOuputTarget().add(orgIdBudgetAmountMap.get(budgetManagementInfoEO.getHeadquartersId()));
            } else {
                budgetAmount = budgetManagementInfoEO.getOuputTarget();
            }
            orgIdBudgetAmountMap.put(budgetManagementInfoEO.getHeadquartersId(), budgetAmount);
        }
        return orgIdBudgetAmountMap;
    }

    /**
     * @Description: 对开票数据分类
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, List<ProjectContractInvoiceEO>> getOrgContractInvoiceEOMap(List<ProjectContractInvoiceEO> contractInvoiceEOList,
                                                                                  List<OrgEO> orgEOList) {

        Map<String, List<ProjectContractInvoiceEO>> orgNameContractInvoiceEOMap = new LinkedHashMap<>();
        Map<String, String> projectIdAndOrgNameMap = new LinkedHashMap<>();

        Set<String> projectIdSet = new LinkedHashSet<>();
        for (ProjectContractInvoiceEO contractInvoiceEO : contractInvoiceEOList) {
            if (StringUtils.isNotEmpty(contractInvoiceEO.getProjectId())) {
                projectIdSet.add(contractInvoiceEO.getProjectId());
            }
        }
        if (CollectionUtils.isEmpty(projectIdSet)) {
            return orgNameContractInvoiceEOMap;
        }

        List<Project> projectList = projectRepository.findByIdIn(projectIdSet);
        for (Project project : projectList) {
            projectIdAndOrgNameMap.put(project.getId(), getOrgEOBelong(orgEOList, project.getDeptId()).getName());
        }

        for (ProjectContractInvoiceEO contractInvoiceEO : contractInvoiceEOList) {
            if (StringUtils.isEmpty(projectIdAndOrgNameMap.get(contractInvoiceEO.getProjectId()))) {
                continue;
            }
            String area = projectIdAndOrgNameMap.get(contractInvoiceEO.getProjectId());
            List<ProjectContractInvoiceEO> tempList = orgNameContractInvoiceEOMap.get(area);
            if (null == tempList) {
                tempList = new ArrayList<>();
            }
            tempList.add(contractInvoiceEO);
            orgNameContractInvoiceEOMap.put(area, tempList);
        }
        return orgNameContractInvoiceEOMap;
    }


    /**
     * @Description: 对开票数据分类 利用开票底表的business dept
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:48
     **/
    public Map<String, List<ProjectContractInvoiceEO>> getOrgContractInvoiceEOMapNew(List<ProjectContractInvoiceEO> contractInvoiceEOList,
                                                                                  List<OrgEO> orgEOList) {
        Map<String, List<ProjectContractInvoiceEO>> orgNameContractInvoiceEOMap = new LinkedHashMap<>();
        for (ProjectContractInvoiceEO contractInvoiceEO : contractInvoiceEOList) {
            if (StringUtils.isEmpty(contractInvoiceEO.getBusinessDeptId())) {
                continue;
            }
            String orgEOName = getOrgEOName(orgEOList,contractInvoiceEO.getBusinessDeptId());
            if (StringUtils.isEmpty(orgEOName)) {
                continue;
            }
            List<ProjectContractInvoiceEO> tempList = orgNameContractInvoiceEOMap.get(orgEOName);
            if (CollectionUtils.isEmpty(tempList)) {
                tempList = new ArrayList<>();
            }
            tempList.add(contractInvoiceEO);
            orgNameContractInvoiceEOMap.put(orgEOName, tempList);
        }
        return orgNameContractInvoiceEOMap;
    }

    public Double culRate(Double currentAmount, Double lastAmount) {
        if (null == currentAmount) {
            currentAmount = 0.0D;
        }
        if (null == lastAmount) {
            lastAmount = 0.0D;
        }
        Double monthAmountRate = 0.0D;
        //今年 - 去年
        BigDecimal difference = BigDecimal.valueOf(currentAmount - lastAmount);
        //去年
        BigDecimal divide = BigDecimal.valueOf(lastAmount);
        if (BigDecimal.ZERO.compareTo(divide) == 0 && BigDecimal.ZERO.compareTo(BigDecimal.valueOf(currentAmount)) != 0) {
            monthAmountRate = 100.0D;
        } else if (BigDecimal.ZERO.compareTo(divide) != 0) {
            monthAmountRate = difference.multiply(BigDecimal.valueOf(100)).divide(divide, 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return monthAmountRate;
    }

    public Double culRate(BigDecimal currentAmount, BigDecimal lastAmount) {
        if (null == currentAmount) {
            currentAmount = ZERO;
        }
        if (null == lastAmount) {
            lastAmount = ZERO;
        }
        BigDecimal monthAmountRate = ZERO;
        BigDecimal difference = currentAmount.subtract(lastAmount);
        BigDecimal divide = lastAmount;
        if (BigDecimal.ZERO.compareTo(divide) == 0 && BigDecimal.ZERO.compareTo(currentAmount) != 0) {
            monthAmountRate = BigDecimal.valueOf(100.0D);
        } else if (BigDecimal.ZERO.compareTo(divide) != 0) {
            monthAmountRate = difference.multiply(BigDecimal.valueOf(100)).divide(divide, 1, BigDecimal.ROUND_HALF_UP);
        }
        return monthAmountRate.doubleValue();
    }

    public double culFinishRate(Double act, Double target) {
        if (null == act) {
            act = 0D;
        }
        if (null == target) {
            target = 0.0D;
        }
        double finishRate = 0.0D;
        BigDecimal acture = BigDecimal.valueOf(act);
        BigDecimal divide = BigDecimal.valueOf(target);

        if (BigDecimal.ZERO.compareTo(divide) != 0) {
            finishRate = acture.multiply(BigDecimal.valueOf(100)).divide(divide, 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return finishRate;
    }

    public double culFinishRate(BigDecimal act, BigDecimal target) {
        if (null == act) {
            act = ZERO;
        }
        if (null == target) {
            target = ZERO;
        }
        double finishRate = 0.0D;
//        BigDecimal acture = BigDecimal.valueOf(act);
//        BigDecimal divide = BigDecimal.valueOf(target);

        if (BigDecimal.ZERO.compareTo(target) != 0) {
            finishRate = act.multiply(BigDecimal.valueOf(100)).divide(target, 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return finishRate;
    }

    /**
     * @Description: 数据资源中心经营数据看板-2 -- 地图部分
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 8:21
     **/
    public AreaDashBoardVO getMapData(int type) {
        if (type == 0) {
            return getContractAmountAreaDashBoardVO();
        } else if (type == 1) {
            return getContractInvoiceAreaDashBoardVO();
        } else if (type == 2) {
            return getContractIncomeAreaDashBoardVO();
        }
        return new AreaDashBoardVO();
    }

    public AreaDashBoardVO getContractAmountAreaDashBoardVO() {
        List<ProvinceAreaEO> provinceAreaEOList = provinceAreaEODao.queryAllByList();

        AreaDashBoardVO areaDashBoardVO = new AreaDashBoardVO();
        List<Map<String, Object>> provinceDataMapList = areaDashBoardVO.getMapData();
        Set<String> areaSet = areaDashBoardVO.getAreaSet();
        areaSet = getAreaSet(provinceAreaEOList);

        List<Double> currentTimeDataList = areaDashBoardVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = areaDashBoardVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = areaDashBoardVO.getRateList();

        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        areaDashBoardVO.setCurrentYear(currentYear);
        areaDashBoardVO.setLastYear(lastYear);

        List<Project> currentYearProjectList = projectRepository.findByProjectYearAndProjectMonthAndDelFlagNot(currentYear, currentMonth, true);
        List<Project> lastYearProjectList = projectRepository.findByProjectYearAndProjectMonthAndDelFlagNot(lastYear, currentMonth, true);

        Map<String, List<Project>> currentAreaProjectMap = getProjectAreaMap(currentYearProjectList, provinceAreaEOList);
        Map<String, List<Project>> lastAreaProjectMap = getProjectAreaMap(lastYearProjectList, provinceAreaEOList);

        Map<String, List<Project>> currentProvinceProjectMap = getProvinceProjectMap(currentYearProjectList);
        Set<String> provinceSet = getProvinceSet(provinceAreaEOList);




        for (String area : areaSet) {
            BigDecimal currentAmount = BigDecimal.ZERO;
            BigDecimal lastAmount = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(currentAreaProjectMap.get(area))) {
                currentAmount = sumProjectContractAmount(currentAreaProjectMap.get(area));
            }
            if (CollectionUtils.isNotEmpty(lastAreaProjectMap.get(area))) {
                lastAmount = sumProjectContractAmount(lastAreaProjectMap.get(area));
            }
            currentTimeDataList.add(getScale2(currentAmount));
            lastTimeDataList.add(getScale2(lastAmount));
            Double rate = culRate(currentAmount, lastAmount);
            rateList.add(rate);
        }
        for (String province : provinceSet) {
            BigDecimal currentAmount =BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(currentProvinceProjectMap.get(province))) {
                currentAmount = sumProjectContractAmount(currentProvinceProjectMap.get(province));
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", province);
            map.put("value", getScale2(currentAmount));
            provinceDataMapList.add(map);

        }


        areaDashBoardVO.setAreaSet(areaSet);
        return areaDashBoardVO;

    }

    public AreaDashBoardVO getContractInvoiceAreaDashBoardVO() {
        List<ProvinceAreaEO> provinceAreaEOList = provinceAreaEODao.queryAllByList();

        AreaDashBoardVO areaDashBoardVO = new AreaDashBoardVO();
        List<Map<String, Object>> provinceDataMapList = areaDashBoardVO.getMapData();
        Set<String> areaSet = areaDashBoardVO.getAreaSet();
        areaSet = getAreaSet(provinceAreaEOList);

        List<Double> currentTimeDataList = areaDashBoardVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = areaDashBoardVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = areaDashBoardVO.getRateList();

        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        areaDashBoardVO.setCurrentYear(currentYear);
        areaDashBoardVO.setLastYear(lastYear);

        List<ProjectContractInvoiceEO> currentYearProjectContractInvoiceEOList = projectContractInvoiceEODao
                .selectByYearAndMonth(currentYear, currentMonth);
        List<ProjectContractInvoiceEO> lastYearProjectContractInvoiceEOList = projectContractInvoiceEODao
                .selectByYearAndMonth(lastYear, currentMonth);

        Map<String, List<ProjectContractInvoiceEO>> currentContractInvoiceEOAreaMap = getContractInvoiceEOAreaMap(
                currentYearProjectContractInvoiceEOList,
                provinceAreaEOList);
        Map<String, List<ProjectContractInvoiceEO>> lastContractInvoiceEOAreaMap = getContractInvoiceEOAreaMap(lastYearProjectContractInvoiceEOList,
                provinceAreaEOList);

        Map<String, List<ProjectContractInvoiceEO>> provinceContractInvoiceEOMap = getProvinceContractInvoiceEOMap(
                currentYearProjectContractInvoiceEOList);
        Set<String> provinceSet = getProvinceSet(provinceAreaEOList);

        for (String province : provinceSet) {
            BigDecimal currentAmount = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(provinceContractInvoiceEOMap.get(province))) {
                currentAmount = sumProjectContractInvoiceEOAmount(provinceContractInvoiceEOMap.get(province));
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", province);
            map.put("value", getScale2(currentAmount));
            provinceDataMapList.add(map);

        }


        for (String area : areaSet) {
            BigDecimal currentAmount = BigDecimal.ZERO;
            BigDecimal lastAmount = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(currentContractInvoiceEOAreaMap.get(area))) {
                currentAmount = sumProjectContractInvoiceEOAmount(currentContractInvoiceEOAreaMap.get(area));
            }
            if (CollectionUtils.isNotEmpty(lastContractInvoiceEOAreaMap.get(area))) {
                lastAmount = sumProjectContractInvoiceEOAmount(lastContractInvoiceEOAreaMap.get(area));
            }
            currentTimeDataList.add(getScale2(currentAmount));
            lastTimeDataList.add(getScale2(lastAmount));
            Double rate = culRate(currentAmount, lastAmount);
            rateList.add(rate);
        }
        areaDashBoardVO.setAreaSet(areaSet);
        return areaDashBoardVO;
    }

    public AreaDashBoardVO getContractInvoiceAreaDashBoardVONew() {
        List<ProvinceAreaEO> provinceAreaEOList = provinceAreaEODao.queryAllByList();

        AreaDashBoardVO areaDashBoardVO = new AreaDashBoardVO();
        List<Map<String, Object>> provinceDataMapList = areaDashBoardVO.getMapData();
        Set<String> areaSet = areaDashBoardVO.getAreaSet();
        areaSet = getAreaSet(provinceAreaEOList);

        List<Double> currentTimeDataList = areaDashBoardVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = areaDashBoardVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = areaDashBoardVO.getRateList();

        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        areaDashBoardVO.setCurrentYear(currentYear);
        areaDashBoardVO.setLastYear(lastYear);

        List<ProjectContractInvoiceEO> currentYearProjectContractInvoiceEOList = projectContractInvoiceEODao
                .selectByYearAndMonth(currentYear, currentMonth);
        List<ProjectContractInvoiceEO> lastYearProjectContractInvoiceEOList = projectContractInvoiceEODao
                .selectByYearAndMonth(lastYear, currentMonth);

        Map<String, List<ProjectContractInvoiceEO>> currentContractInvoiceEOAreaMap = getContractInvoiceEOAreaMapNew(
                currentYearProjectContractInvoiceEOList,
                provinceAreaEOList);
        Map<String, List<ProjectContractInvoiceEO>> lastContractInvoiceEOAreaMap = getContractInvoiceEOAreaMapNew(
                lastYearProjectContractInvoiceEOList,
                provinceAreaEOList);

        Map<String, List<ProjectContractInvoiceEO>> provinceContractInvoiceEOMap = getProvinceContractInvoiceEOMap(
                currentYearProjectContractInvoiceEOList);
        Set<String> provinceSet = getProvinceSet(provinceAreaEOList);

        for (String province : provinceSet) {
            BigDecimal currentAmount = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(provinceContractInvoiceEOMap.get(province))) {
                currentAmount = sumProjectContractInvoiceEOAmount(provinceContractInvoiceEOMap.get(province));
            }
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", province);
            map.put("value", getScale2(currentAmount));
            provinceDataMapList.add(map);

        }


        for (String area : areaSet) {
            BigDecimal currentAmount = BigDecimal.ZERO;
            BigDecimal lastAmount = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(currentContractInvoiceEOAreaMap.get(area))) {
                currentAmount = sumProjectContractInvoiceEOAmount(currentContractInvoiceEOAreaMap.get(area));
            }
            if (CollectionUtils.isNotEmpty(lastContractInvoiceEOAreaMap.get(area))) {
                lastAmount = sumProjectContractInvoiceEOAmount(lastContractInvoiceEOAreaMap.get(area));
            }
            currentTimeDataList.add(getScale2(currentAmount));
            lastTimeDataList.add(getScale2(lastAmount));
            Double rate = culRate(currentAmount, lastAmount);
            rateList.add(rate);
        }
        areaDashBoardVO.setAreaSet(areaSet);
        return areaDashBoardVO;
    }

    public Map<String, Double> getAreaContractInvoiceMap(List<ProvinceAreaEO> provinceAreaEOList, Set<String> areaSet) {
        Map<String, Double> areaContractInvoiceMap = new LinkedHashMap<>();
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        List<ProjectContractInvoiceEO> currentYearProjectContractInvoiceEOList = projectContractInvoiceEODao
                .selectByYear(currentYear);
        Map<String, List<ProjectContractInvoiceEO>> currentContractInvoiceEOAreaMap = getContractInvoiceEOAreaMap(
                currentYearProjectContractInvoiceEOList,
                provinceAreaEOList);
        for (String area : areaSet) {
            BigDecimal currentAmount = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(currentContractInvoiceEOAreaMap.get(area))) {
                currentAmount = sumProjectContractInvoiceEOAmount(currentContractInvoiceEOAreaMap.get(area));
            }
            areaContractInvoiceMap.put(area, getScale2(currentAmount));
        }
        return areaContractInvoiceMap;
    }

    public Map<String, Double> getProvinceContractInvoiceMap(List<ProvinceAreaEO> provinceAreaEOList) {
        Map<String, Double> provinceContractInvoiceMap = new LinkedHashMap<>();
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        List<ProjectContractInvoiceEO> currentYearProjectContractInvoiceEOList = projectContractInvoiceEODao
                .selectByYearAndMonth(currentYear, currentMonth);
        Map<String, List<ProjectContractInvoiceEO>> provinceContractInvoiceEOMap = getProvinceContractInvoiceEOMap(
                currentYearProjectContractInvoiceEOList);

        Set<String> provinceSet = getProvinceSet(provinceAreaEOList);

        for (String province : provinceSet) {
            BigDecimal currentAmount = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(provinceContractInvoiceEOMap.get(province))) {
                currentAmount = sumProjectContractInvoiceEOAmount(provinceContractInvoiceEOMap.get(province));
            }
            provinceContractInvoiceMap.put(province, getScale2(currentAmount));

        }
        return provinceContractInvoiceMap;
    }

    public AreaDashBoardVO getContractIncomeAreaDashBoardVO() {
        List<ProvinceAreaEO> provinceAreaEOList = provinceAreaEODao.queryAllByList();
        AreaDashBoardVO areaDashBoardVO = new AreaDashBoardVO();
        List<Map<String, Object>> provinceDataMapList = areaDashBoardVO.getMapData();
        Set<String> areaSet = areaDashBoardVO.getAreaSet();
        areaSet = getAreaSet(provinceAreaEOList);

        List<Double> currentTimeDataList = areaDashBoardVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = areaDashBoardVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = areaDashBoardVO.getRateList();

        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();
        areaDashBoardVO.setCurrentYear(currentYear);
        areaDashBoardVO.setLastYear(lastYear);


        List<ReceivableIncomeFiledEO> currentYearReceivableIncomeFiledEOList = receivableIncomeFiledEODao.listByYearAndMonthEqualGroupArea(currentYear,currentMonth);
        Map<String,BigDecimal> currentAreaIncomeAmountMap =  getAreaIncomeAmountMap(currentYearReceivableIncomeFiledEOList) ;

        List<ReceivableIncomeFiledEO> lastYearReceivableIncomeFiledEOList = receivableIncomeFiledEODao.listByYearAndMonthEqualGroupArea(lastYear,currentMonth);
        Map<String,BigDecimal> lastAreaIncomeAmountMap =  getAreaIncomeAmountMap(lastYearReceivableIncomeFiledEOList) ;

        Set<String> provinceSet = getProvinceSet(provinceAreaEOList);
        //累计进账额 = 本年累计开票额+历史年份到账额-所有应收账款余额
        //月度进账额 = 本年累计进账额-本年历史月份归档进账
        //TODO 由于应收归档粒度为大区，省份的没法算
        for (String province : provinceSet) {
            Double currentMonthIncomeAmount = 0.0D;
            Double currentInvoiceAmount = 0.0D;
            Double allReceivableAmount = 0.0D; //所有应收
            Double formerYearIncomeAmount = 0.0D; //历史进账
            Double allIncome = 0.0D;

            Map<String, Object> map = new LinkedHashMap<>();
            map.put("name", province);
            map.put("value", getScale2(0.0D));
            provinceDataMapList.add(map);

        }

        for (String area : areaSet) {
            BigDecimal currentMonthIncomeAmount = ZERO;
            BigDecimal lastMonthIncomeAmount = ZERO;

            //本年累计开票
            if (null != currentAreaIncomeAmountMap.get(area)) {
                currentMonthIncomeAmount = currentAreaIncomeAmountMap.get(area);
            } else {
                currentMonthIncomeAmount = ZERO;
            }
            if (null != lastAreaIncomeAmountMap.get(area)) {
                lastMonthIncomeAmount = lastAreaIncomeAmountMap.get(area);
            } else {
                lastMonthIncomeAmount = ZERO;
            }
            currentTimeDataList.add(DoubleUtils.geDoubletDivideTenThousandScale2(currentMonthIncomeAmount));
            lastTimeDataList.add(DoubleUtils.geDoubletDivideTenThousandScale2(lastMonthIncomeAmount));
            Double rate = culRate(currentMonthIncomeAmount, lastMonthIncomeAmount);
            rateList.add(rate);
        }
        areaDashBoardVO.setAreaSet(areaSet);
        return areaDashBoardVO;
    }

    /**
     * @Description: TODO
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 16:45
     **/
    public OrgEODashBoardVO getOrgChartDataVO(int type) {
        if (type == 0) {
            return getContractAmountOrgChartDataVO();
        } else if (type == 1) {
            return getContractInvoiceOrgChartDataVO();
        } else if (type == 2) {
            return getContractIncomeOrgChartDataVO();
        }

        return new OrgEODashBoardVO();
    }

    public Map<String,BigDecimal> getAreaIncomeAmountMap(List<ReceivableIncomeFiledEO> receivableIncomeFiledEOList) {
        Map<String,BigDecimal> map = new LinkedHashMap<>();
        for (ReceivableIncomeFiledEO receivableIncomeFiledEO : receivableIncomeFiledEOList){
            map.put(receivableIncomeFiledEO.getArea(),receivableIncomeFiledEO.getIncome());
        }

        return map;
    }
    private BigDecimal culThisMonthAmount(int day, int days,BigDecimal amount){
        if (null == amount || amount.compareTo(BigDecimal.ZERO)==0){
            return BigDecimal.ZERO;
        }
        BigDecimal decimalDay = BigDecimal.valueOf(day);
        BigDecimal decimalDays = BigDecimal.valueOf(days);
        return amount.multiply(decimalDay).divide(decimalDays,2,BigDecimal.ROUND_HALF_UP);
    }

    private BigDecimal doAdd(BigDecimal a,BigDecimal b){
        if (null == a ){
            a = BigDecimal.ZERO;
        }
        if (null == b ){
            b = BigDecimal.ZERO;
        }
        return a.add(b);
    }

    /**
     * @Description: 本部年度数据统计，展板2的柱状图
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 16:48
     **/
    public OrgEODashBoardVO getContractAmountOrgChartDataVO() {
        List<OrgEO> orgEOList = orgEODao.queryOrgAllByRemarkAsc();

        OrgEODashBoardVO orgEODashBoardVO = new OrgEODashBoardVO();
        Map<String, Double> areaDataMap = orgEODashBoardVO.getMapData();
        Set<String> orgNameSet = orgEODashBoardVO.getOrgNameSet();
        orgNameSet = getBigOrgNameSet(orgEOList);
        Set<String> actureOrgNameSet = new LinkedHashSet<>();

        List<Double> currentTimeDataList = orgEODashBoardVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = orgEODashBoardVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = orgEODashBoardVO.getRateList();

        DateTime dateTime = new DateTime();
        int currentYear = dateTime.getYear();
        int lastYear = dateTime.minusYears(1).getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);


        orgEODashBoardVO.setCurrentYear(currentYear);
        orgEODashBoardVO.setLastYear(lastYear);

        List<Project> currentYearProjectList = projectRepository.findByProjectYearAndDelFlagNot(currentYear, true);
        List<Project> lastYearProjectList = projectRepository.findByProjectYear(lastYear);

        Map<String, List<Project>> currentOrgProjectMap = getProjectOrgMap(currentYearProjectList, orgEOList);
        Map<String, List<Project>> lastOrgProjectMap = getProjectOrgMap(lastYearProjectList, orgEOList);

        for (String orgName : orgNameSet) {
            BigDecimal currentAmount = BigDecimal.ZERO;
            BigDecimal lastFormerAmount = BigDecimal.ZERO;
            BigDecimal lastThisAmount = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(currentOrgProjectMap.get(orgName))) {
                currentAmount = sumProjectContractAmount(currentOrgProjectMap.get(orgName));
                areaDataMap.put(orgName, getScale2(currentAmount));
            }
            if (CollectionUtils.isNotEmpty(lastOrgProjectMap.get(orgName))) {
                lastFormerAmount = sumProjectContractAmountBetweenMonth(lastOrgProjectMap.get(orgName),1,month-1);
                lastThisAmount = sumProjectContractAmountBetweenMonth(lastOrgProjectMap.get(orgName),month,month);
            }

            //
            lastThisAmount = culThisMonthAmount(day,days,lastThisAmount);
            lastFormerAmount = doAdd(lastFormerAmount,lastThisAmount);

            if (currentAmount.compareTo(BigDecimal.ZERO) > 0 || lastFormerAmount.compareTo(BigDecimal.ZERO) > 0) {
                actureOrgNameSet.add(orgName);
                currentTimeDataList.add(getScale2(currentAmount));
                lastTimeDataList.add(getScale2(lastFormerAmount));
                Double rate = culRate(currentAmount, lastFormerAmount);
                rateList.add(rate);
            }
        }
        orgEODashBoardVO.setOrgNameSet(actureOrgNameSet);
        return orgEODashBoardVO;
    }

    /**
     * @Description: 本部年度数据统计，展板2的柱状图
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 16:48
     **/
    public OrgEODashBoardVO getContractInvoiceOrgChartDataVO() {
        List<OrgEO> orgEOList = orgEODao.queryOrgAllByRemarkAsc();

        OrgEODashBoardVO orgEODashBoardVO = new OrgEODashBoardVO();
        Map<String, Double> areaDataMap = orgEODashBoardVO.getMapData();
        Set<String> orgNameSet = orgEODashBoardVO.getOrgNameSet();
        orgNameSet = getBigOrgNameSet(orgEOList);
        Set<String> actureOrgNameSet = new LinkedHashSet<>();

        List<Double> currentTimeDataList = orgEODashBoardVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = orgEODashBoardVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = orgEODashBoardVO.getRateList();

        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        orgEODashBoardVO.setCurrentYear(currentYear);
        orgEODashBoardVO.setLastYear(lastYear);

        List<ProjectContractInvoiceEO> currentYearProjectContractInvoiceEOList = projectContractInvoiceEODao.selectByYear(currentYear);
        List<ProjectContractInvoiceEO> lastYearProjectContractInvoiceEOList = projectContractInvoiceEODao.selectByYear(lastYear);

        Map<String, List<ProjectContractInvoiceEO>> currentContractInvoiceEOAreaMap
                = getOrgContractInvoiceEOMap(currentYearProjectContractInvoiceEOList, orgEOList);
        Map<String, List<ProjectContractInvoiceEO>> lastContractInvoiceEOAreaMap
                = getOrgContractInvoiceEOMap(lastYearProjectContractInvoiceEOList, orgEOList);

        for (String orgName : orgNameSet) {
            BigDecimal currentAmount =BigDecimal.ZERO;
            BigDecimal lastAmount = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(currentContractInvoiceEOAreaMap.get(orgName))) {
                currentAmount = sumProjectContractInvoiceEOAmount(currentContractInvoiceEOAreaMap.get(orgName));
                areaDataMap.put(orgName, getScale2(currentAmount));
            }
            if (CollectionUtils.isNotEmpty(lastContractInvoiceEOAreaMap.get(orgName))) {
                lastAmount = sumProjectContractInvoiceEOAmount(lastContractInvoiceEOAreaMap.get(orgName));
            }
            if (currentAmount.compareTo(BigDecimal.ZERO) > 0 || lastAmount.compareTo(BigDecimal.ZERO) > 0) {
                actureOrgNameSet.add(orgName);
                currentTimeDataList.add(getScale2(currentAmount));
                lastTimeDataList.add(getScale2(lastAmount));
                Double rate = culRate(currentAmount, lastAmount);
                rateList.add(rate);
            }
        }
        orgEODashBoardVO.setOrgNameSet(actureOrgNameSet);
        return orgEODashBoardVO;
    }

    /**
     * @Description: 本部年度数据统计，展板2的柱状图
     * 注：新改动，因为开票底表有变化 这里可以直接用底表的bussinessdept 字段确定属于哪个本部
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 14:02
     **/
    public OrgEODashBoardVO getContractInvoiceOrgChartDataVONew() {
        List<OrgEO> orgEOList = orgEODao.queryOrgAllByRemarkAsc();

        OrgEODashBoardVO orgEODashBoardVO = new OrgEODashBoardVO();
        Map<String, Double> areaDataMap = orgEODashBoardVO.getMapData();
        Set<String> orgNameSet = orgEODashBoardVO.getOrgNameSet();
        orgNameSet = getBigOrgNameSet(orgEOList);
        Set<String> actureOrgNameSet = new LinkedHashSet<>();

        List<Double> currentTimeDataList = orgEODashBoardVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = orgEODashBoardVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = orgEODashBoardVO.getRateList();

        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        orgEODashBoardVO.setCurrentYear(currentYear);
        orgEODashBoardVO.setLastYear(lastYear);

        List<ProjectContractInvoiceEO> currentYearProjectContractInvoiceEOList = projectContractInvoiceEODao.selectByYear(currentYear);
        List<ProjectContractInvoiceEO> lastYearProjectContractInvoiceEOList = projectContractInvoiceEODao.selectByYear(lastYear);

        Map<String, List<ProjectContractInvoiceEO>> currentContractInvoiceEOOrgMap
                = getOrgContractInvoiceEOMapNew(currentYearProjectContractInvoiceEOList, orgEOList);
        Map<String, List<ProjectContractInvoiceEO>> lastContractInvoiceEOOrgMap
                = getOrgContractInvoiceEOMapNew(lastYearProjectContractInvoiceEOList, orgEOList);

        for (String orgName : orgNameSet) {
            BigDecimal currentAmount =BigDecimal.ZERO;
            BigDecimal lastAmount = BigDecimal.ZERO;
            if (CollectionUtils.isNotEmpty(currentContractInvoiceEOOrgMap.get(orgName))) {
                currentAmount = sumProjectContractInvoiceEOAmount(currentContractInvoiceEOOrgMap.get(orgName));
                areaDataMap.put(orgName, getScale2(currentAmount));
            }
            if (CollectionUtils.isNotEmpty(lastContractInvoiceEOOrgMap.get(orgName))) {
                lastAmount = sumProjectContractInvoiceEOAmount(lastContractInvoiceEOOrgMap.get(orgName));
            }
            if (currentAmount.compareTo(BigDecimal.ZERO) > 0 || lastAmount.compareTo(BigDecimal.ZERO) > 0) {
                actureOrgNameSet.add(orgName);
                currentTimeDataList.add(getScale2(currentAmount));
                lastTimeDataList.add(getScale2(lastAmount));
                Double rate = culRate(currentAmount, lastAmount);
                rateList.add(rate);
            }
        }
        orgEODashBoardVO.setOrgNameSet(actureOrgNameSet);
        return orgEODashBoardVO;
    }

    /**
     * @Description: 本部年度数据统计，展板2的柱状图
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 16:48
     **/
    public OrgEODashBoardVO getContractIncomeOrgChartDataVO() {
        List<OrgEO> orgEOList = orgEODao.queryOrgAllByRemarkAsc();

        OrgEODashBoardVO orgEODashBoardVO = new OrgEODashBoardVO();
        Map<String, Double> areaDataMap = orgEODashBoardVO.getMapData();
        Set<String> orgNameSet = orgEODashBoardVO.getOrgNameSet();
        orgNameSet = getBigOrgNameSet(orgEOList);
        Set<String> actureOrgNameSet = new LinkedHashSet<>();

        List<Double> currentTimeDataList = orgEODashBoardVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = orgEODashBoardVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = orgEODashBoardVO.getRateList();

        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        orgEODashBoardVO.setCurrentYear(currentYear);
        orgEODashBoardVO.setLastYear(lastYear);

        List<ReceivableIncomeEO> currentYearProjectContractIncomeEOList = receivableIncomeEODao.selectByYear(currentYear);
//        List<ReceivableIncomeFiledEO> receivableIncomeFiledEOList = receivableIncomeFiledEODao.listByYearEqual(lastYear);
        List<ReceivableIncomeEO> lastYearProjectContractIncomeEOList = receivableIncomeEODao.selectByYear(lastYear);
        Map<String, List<ReceivableIncomeEO>> currentContractIncomeEOOrgMap
                = getOrgReceivableIncomeEOMap(currentYearProjectContractIncomeEOList, orgEOList);

        Map<String, List<ReceivableIncomeEO>> lastContractInvoiceEOOrgMap
                = getOrgReceivableIncomeEOMap(lastYearProjectContractIncomeEOList, orgEOList);

        for (String orgName : orgNameSet) {
            Double currentAmount = 0.0D;
            Double lastAmount = 0.0D;
            if (CollectionUtils.isNotEmpty(currentContractIncomeEOOrgMap.get(orgName))) {
                currentAmount = sumIncomeEOAmount(currentContractIncomeEOOrgMap.get(orgName));
                areaDataMap.put(orgName, currentAmount);
            }
            if (CollectionUtils.isNotEmpty(lastContractInvoiceEOOrgMap.get(orgName))) {
                lastAmount = sumIncomeEOAmount(lastContractInvoiceEOOrgMap.get(orgName));
            }
            if (currentAmount > 0 || lastAmount > 0) {
                actureOrgNameSet.add(orgName);
                currentTimeDataList.add(getScale2(currentAmount));
                lastTimeDataList.add(getScale2(lastAmount));
                Double rate = culRate(currentAmount, lastAmount);
                rateList.add(rate);
            }
        }
        orgEODashBoardVO.setOrgNameSet(actureOrgNameSet);
        return orgEODashBoardVO;
    }

    /**
     * @Description: 展板2 各部门数据
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 18:05
     **/
    public List<OrgContractInvoiceVO> getChartDataList() {
        DateTime dateTime = new DateTime();
        final int currentYear = dateTime.getYear();
        final int lastYear = dateTime.minusYears(1).getYear();
        final int currentMonth = dateTime.getMonthOfYear();

        List<OrgEO> orgEOList = orgEODao.queryOrgAllByRemarkAsc();
//        Map<String, String> orgIdOrgEOMap = getOrgIdOrgNameMap(orgEOList);
        Map<String, String> orgIdOrgAbbMap = getOrgIdOrgAbbMap(orgEOList);
        Set<String> allOrgIdSet = new LinkedHashSet<>();
//        List<OrgEO> bigOrgEOList = getBigOrgEOList(orgEOList);
        //当月和去年同月的开票Map
        List<ProjectContractInvoiceEO> currentMonthProjectContractInvoiceEOList = projectContractInvoiceEODao
                .selectByYearAndMonth(currentYear, currentMonth);
        List<ProjectContractInvoiceEO> lastMonthProjectContractInvoiceEOList = projectContractInvoiceEODao
                .selectByYearAndMonth(lastYear, currentMonth);

        //当年开票Map
        List<ProjectContractInvoiceEO> currentYearProjectContractInvoiceEOList = projectContractInvoiceEODao.selectByYear(currentYear);

        //当月和去年同月的开票Map
        Map<String, List<ProjectContractInvoiceEO>> currentMonthOrgIdContractInvoiceEOMap = getOrgIdContractInvoiceEOMap(
                currentMonthProjectContractInvoiceEOList,
                allOrgIdSet);
        Map<String, List<ProjectContractInvoiceEO>> lastMonthOrgIdContractInvoiceEOMap = getOrgIdContractInvoiceEOMap(
                lastMonthProjectContractInvoiceEOList,
                allOrgIdSet);

        //当年开票Map
        Map<String, List<ProjectContractInvoiceEO>> currentYearOrgIdContractInvoiceEOMap = getOrgIdContractInvoiceEOMap(
                currentYearProjectContractInvoiceEOList,
                allOrgIdSet);
        List<BudgetManagementInfoEO> budgetManagementInfoEOList = budgetManagementInfoEODao.selectByYear(String.valueOf(currentYear));
        Map<String, List<String>> bigOrgIdSmallOrgIdMap = getBigOrgIdSmallOrgIdMap(allOrgIdSet, orgEOList);
        Map<String, BigDecimal> bigOrgIdBudgetMap = getBigOrgIdBudgetManagementInfoEOEOMap(budgetManagementInfoEOList);
        Map<String, BigDecimal> smallOrgIdBudgetMap = getSmallOrgIdBudgetManagementInfoEOMap(budgetManagementInfoEOList);
        List<OrgContractInvoiceVO> orgContractInvoiceVOList = new ArrayList<>();

        for (OrgEO orgEO : orgEOList) {
            //for (Map.Entry<String, List<String>> entry : bigOrgIdSmallOrgIdMap.entrySet()) {
                String bigOrgId = orgEO.getId();
                if (!bigOrgIdSmallOrgIdMap.containsKey(bigOrgId)){
                    continue;
                }
                BigDecimal currentMonthBigOrgAmount = ZERO; //当月开票
                BigDecimal lastMonthBigOrgAmount = ZERO;//往月开票
                BigDecimal currentBigOrgYearAmount = ZERO; // 当年总额
                BigDecimal bigOrgBudgetTarget = ZERO;
                if (bigOrgIdBudgetMap.get(bigOrgId) != null) {
                    bigOrgBudgetTarget = bigOrgIdBudgetMap.get(bigOrgId);
                    bigOrgBudgetTarget = bigOrgBudgetTarget.divide(TEN_THOUSAND);
                }

                for (String smallOrgId : bigOrgIdSmallOrgIdMap.get(bigOrgId)) { //for list 循环
                    BigDecimal currentYearAmount = ZERO;
                    BigDecimal currentMonthAmount = ZERO;
                    BigDecimal lastMonthAmount = ZERO;
                    BigDecimal smallOrgBudgetTarget = ZERO;
                    if (null != smallOrgIdBudgetMap.get(smallOrgId)) {
                        smallOrgBudgetTarget = smallOrgIdBudgetMap.get(smallOrgId);
                        smallOrgBudgetTarget = smallOrgBudgetTarget.divide(TEN_THOUSAND);
                    }
                    if (CollectionUtils.isNotEmpty(currentMonthOrgIdContractInvoiceEOMap.get(smallOrgId))) {
                        currentMonthAmount = sumProjectContractInvoiceEOAmountBigDecimal(currentMonthOrgIdContractInvoiceEOMap.get(smallOrgId));
                        currentMonthBigOrgAmount = currentMonthBigOrgAmount.add(currentMonthAmount);
                    }
                    if (CollectionUtils.isNotEmpty(lastMonthOrgIdContractInvoiceEOMap.get(smallOrgId))) {
                        lastMonthAmount = sumProjectContractInvoiceEOAmountBigDecimal(lastMonthOrgIdContractInvoiceEOMap.get(smallOrgId));
                        lastMonthBigOrgAmount = lastMonthBigOrgAmount.add(lastMonthAmount);
                    }
                    if (CollectionUtils.isNotEmpty(currentYearOrgIdContractInvoiceEOMap.get(smallOrgId))) {
                        currentYearAmount = sumProjectContractInvoiceEOAmountBigDecimal(currentYearOrgIdContractInvoiceEOMap.get(smallOrgId));
                        currentBigOrgYearAmount = currentBigOrgYearAmount.add(currentYearAmount);
                    }
                    double finishRate = culFinishRate(currentYearAmount.doubleValue(), smallOrgBudgetTarget.doubleValue());

                    Double monthIncreaseRate = culRate(currentMonthAmount, lastMonthAmount);
//                    String orgName = orgIdOrgEOMap.get(smallOrgId);
                    String orgAbb = orgIdOrgAbbMap.get(smallOrgId);
                    orgContractInvoiceVOList.add(OrgContractInvoiceVO.builder()
                            .orgId(smallOrgId) //组织机构ID
                            .orgName(orgAbb)//部门名称
                            .monthContractAmount(getScale2(currentMonthAmount))//月开票额
                            .monthIncreaseRate(monthIncreaseRate) //月增长
                            .allContractAmount(getScale2(currentYearAmount))//累计（全年）开票额
                            .finishedRate(finishRate)//完成率
                            .build()
                    );
                }
                double bigOrgFinishRate = culFinishRate(currentBigOrgYearAmount, bigOrgBudgetTarget);
                Double bigOrgMonthIncreaseRate = culRate(currentMonthBigOrgAmount, lastMonthBigOrgAmount);
//                String bigOrgName = orgIdOrgEOMap.get(bigOrgId);
                String bigOrgAbb = orgIdOrgAbbMap.get(bigOrgId);
                orgContractInvoiceVOList.add(OrgContractInvoiceVO.builder()
                        .orgId(bigOrgId) //组织机构ID
                        .orgName(bigOrgAbb)//部门名称
                        .isBigOrg(true)
                        .monthContractAmount(getScale2(currentMonthBigOrgAmount))//月开票额
                        .monthIncreaseRate(bigOrgMonthIncreaseRate) //月增长
                        .allContractAmount(getScale2(currentBigOrgYearAmount))//累计（全年）开票额
                        .finishedRate(bigOrgFinishRate)//完成率
                        .build()
                );
//            }
        }
        return orgContractInvoiceVOList;
    }

    /**
     * @Description: 展板2 各部门数据
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 18:05
     **/
    public List<OrgContractInvoiceVO> getChartDataListNew() {
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer lastYear = dateTime.minusYears(1).getYear();
        Integer currentMonth = dateTime.getMonthOfYear();

        List<OrgEO> orgEOList = orgEODao.queryOrgAllByRemarkAsc();
        Map<String, String> orgIdOrgEOMap = getOrgIdOrgNameMap(orgEOList);
        Set<String> allOrgIdSet = new LinkedHashSet<>();
//        List<OrgEO> bigOrgEOList = getBigOrgEOList(orgEOList);
        //当月和去年同月的开票Map
        List<ProjectContractInvoiceEO> currentMonthProjectContractInvoiceEOList = projectContractInvoiceEODao
                .sumByYearAndMonthGroupByBusinessDeptId(currentYear, currentMonth);
        List<ProjectContractInvoiceEO> lastMonthProjectContractInvoiceEOList = projectContractInvoiceEODao
                .sumByYearAndMonthGroupByBusinessDeptId(lastYear, currentMonth);

        //当年开票Map
        List<ProjectContractInvoiceEO> currentYearProjectContractInvoiceEOList = projectContractInvoiceEODao.sumByYearGroupByBusinessDeptId(currentYear);

        //当月和去年同月的开票Map
        Map<String, List<ProjectContractInvoiceEO>> currentMonthOrgIdContractInvoiceEOMap = getOrgIdContractInvoiceEOMapNew(currentMonthProjectContractInvoiceEOList);
        Map<String, List<ProjectContractInvoiceEO>> lastMonthOrgIdContractInvoiceEOMap = getOrgIdContractInvoiceEOMapNew(lastMonthProjectContractInvoiceEOList);

        //当年开票Map
        Map<String, List<ProjectContractInvoiceEO>> currentYearOrgIdContractInvoiceEOMap = getOrgIdContractInvoiceEOMapNew(currentYearProjectContractInvoiceEOList);

        List<BudgetManagementInfoEO> budgetManagementInfoEOList = budgetManagementInfoEODao.selectByYear(String.valueOf(currentYear));

        Map<String, List<String>> bigOrgIdSmallOrgIdMap = getBigOrgIdSmallOrgIdMap(allOrgIdSet, orgEOList);
        Map<String, BigDecimal> bigOrgIdBudgetMap = getBigOrgIdBudgetManagementInfoEOEOMap(budgetManagementInfoEOList);
        Map<String, BigDecimal> smallOrgIdBudgetMap = getSmallOrgIdBudgetManagementInfoEOMap(budgetManagementInfoEOList);
        List<OrgContractInvoiceVO> orgContractInvoiceVOList = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : bigOrgIdSmallOrgIdMap.entrySet()) {
            String bigOrgId = entry.getKey();
            BigDecimal currentMonthBigOrgAmount = ZERO; //当月开票
            BigDecimal lastMonthBigOrgAmount = ZERO;//往月开票
            BigDecimal currentBigOrgYearAmount = ZERO; // 当年总额
            BigDecimal bigOrgBudgetTarget = ZERO;
            if (bigOrgIdBudgetMap.get(bigOrgId) != null) {
                bigOrgBudgetTarget = bigOrgIdBudgetMap.get(bigOrgId);
                bigOrgBudgetTarget = bigOrgBudgetTarget.divide(TEN_THOUSAND);
            }

            for (String smallOrgId : entry.getValue()) { //for list 循环
                BigDecimal currentYearAmount = ZERO;
                BigDecimal currentMonthAmount = ZERO;
                BigDecimal lastMonthAmount = ZERO;
                BigDecimal smallOrgBudgetTarget = ZERO;
                if (null != smallOrgIdBudgetMap.get(smallOrgId)) {
                    smallOrgBudgetTarget = smallOrgIdBudgetMap.get(smallOrgId);
                    smallOrgBudgetTarget = smallOrgBudgetTarget.divide(TEN_THOUSAND);
                }
                if (CollectionUtils.isNotEmpty(currentMonthOrgIdContractInvoiceEOMap.get(smallOrgId))) {
                    currentMonthAmount = sumProjectContractInvoiceEOAmountBigDecimal(currentMonthOrgIdContractInvoiceEOMap.get(smallOrgId));
                    currentMonthBigOrgAmount = currentMonthBigOrgAmount.add(currentMonthAmount);
                }
                if (CollectionUtils.isNotEmpty(lastMonthOrgIdContractInvoiceEOMap.get(smallOrgId))) {
                    lastMonthAmount = sumProjectContractInvoiceEOAmountBigDecimal(lastMonthOrgIdContractInvoiceEOMap.get(smallOrgId));
                    lastMonthBigOrgAmount = lastMonthBigOrgAmount.add(lastMonthAmount);
                }
                if (CollectionUtils.isNotEmpty(currentYearOrgIdContractInvoiceEOMap.get(smallOrgId))) {
                    currentYearAmount = sumProjectContractInvoiceEOAmountBigDecimal(currentYearOrgIdContractInvoiceEOMap.get(smallOrgId));
                    currentBigOrgYearAmount = currentBigOrgYearAmount.add(currentYearAmount);
                }
                double finishRate = culFinishRate(currentYearAmount.doubleValue(), smallOrgBudgetTarget.doubleValue());

                Double monthIncreaseRate = culRate(currentMonthAmount, lastMonthAmount);
                String orgName = orgIdOrgEOMap.get(smallOrgId);
                orgContractInvoiceVOList.add(OrgContractInvoiceVO.builder()
                        .orgId(smallOrgId) //组织机构ID
                        .orgName(orgName)//部门名称
                        .monthContractAmount(getScale2(currentMonthAmount))//月开票额
                        .monthIncreaseRate(monthIncreaseRate) //月增长
                        .allContractAmount(getScale2(currentYearAmount))//累计（全年）开票额
                        .finishedRate(finishRate)//完成率
                        .build()
                );
            }
            double bigOrgFinishRate = culFinishRate(currentBigOrgYearAmount, bigOrgBudgetTarget);
            Double bigOrgMonthIncreaseRate = culRate(currentMonthBigOrgAmount, lastMonthBigOrgAmount);
            String bigOrgName = orgIdOrgEOMap.get(bigOrgId);
            orgContractInvoiceVOList.add(OrgContractInvoiceVO.builder()
                    .orgId(bigOrgId) //组织机构ID
                    .orgName(bigOrgName)//部门名称
                    .isBigOrg(true)
                    .monthContractAmount(getScale2(currentMonthBigOrgAmount))//月开票额
                    .monthIncreaseRate(bigOrgMonthIncreaseRate) //月增长
                    .allContractAmount(getScale2(currentBigOrgYearAmount))//累计（全年）开票额
                    .finishedRate(bigOrgFinishRate)//完成率
                    .build()
            );
        }
        return orgContractInvoiceVOList;
    }



    /**
     * @Description: 返回project中出现的所有部门id的集合
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 9:46
     **/
    public Set<String> getOrgIdSet(List<Project> projectList) {
        Set<String> orgIdSet = new LinkedHashSet<>();
        for (Project project : projectList) {
            if (StringUtils.isEmpty(project.getDeptId())) {
                continue;
            }
            orgIdSet.add(project.getDeptId());
        }
        return orgIdSet;
    }

    /**
     * @Description: 对组织机构进行map Map key=本部id  value = List = 本部下小部门id
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 9:51
     **/
    public Map<String, List<String>> getBigOrgIdSmallOrgIdMap(Set<String> orgIdSet, List<OrgEO> allOrgEOList) {
        Map<String, List<String>> bigOrgIdAndSmallOrgIdListMap = new LinkedHashMap<>();
        for (String smallOrgId : orgIdSet) {
            OrgEO parentOrgEO = getOrgEOBelong(allOrgEOList, smallOrgId);
            if (null != parentOrgEO && StringUtils.isNotEmpty(parentOrgEO.getId())) {
                List<String> smallOrgIdList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(bigOrgIdAndSmallOrgIdListMap.get(parentOrgEO.getId()))) {
                    smallOrgIdList = bigOrgIdAndSmallOrgIdListMap.get(parentOrgEO.getId());
                }
                smallOrgIdList.add(smallOrgId);
                bigOrgIdAndSmallOrgIdListMap.put(parentOrgEO.getId(), smallOrgIdList);
            }
        }

        return bigOrgIdAndSmallOrgIdListMap;
    }

    /**
     * @Description: 根据省份返回大区的名字
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:05
     **/
    private String getProvinceBelong(List<ProvinceAreaEO> provinceAreaEOList, String province) {
        for (ProvinceAreaEO provinceAreaEO : provinceAreaEOList) {
            if (StringUtils.equals(province, provinceAreaEO.getProvince())) {
                return provinceAreaEO.getArea();
            }
        }
        return "其他区";
    }

    /**
     * @Description: 返回所有大区的名字的Set
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 10:05
     **/
    private Set<String> getAreaSet(List<ProvinceAreaEO> provinceAreaEOList) {
        Set<String> areaSet = new LinkedHashSet<>();
        for (ProvinceAreaEO provinceAreaEO : provinceAreaEOList) {
            areaSet.add(provinceAreaEO.getArea());
        }
        return areaSet;
    }

    private Set<String> getProvinceSet(List<ProvinceAreaEO> provinceAreaEOList) {
        Set<String> provinceSet = new LinkedHashSet<>();
        for (ProvinceAreaEO provinceAreaEO : provinceAreaEOList) {
            provinceSet.add(provinceAreaEO.getProvince());
        }
        return provinceSet;
    }

    /**
     * @Description: 返回所有本部的名字的Set
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 16:52
     **/
    private Set<String> getBigOrgNameSet(List<OrgEO> orgEOList) {
        Set<String> orgNameSet = new LinkedHashSet<>();

        for (OrgEO orgEO : orgEOList) {
            String parentIds = orgEO.getParentIds();
            if (StringUtils.isEmpty(parentIds)) {
                continue;
            }
            String[] parentIdArr = StringUtils.split(parentIds, ',');
            Set<String> parentIdSet = removeArrBlankElement(parentIdArr);
            if (parentIdSet.size() == 3) {
                orgNameSet.add(orgEO.getOrgAbb()); //改成名称的缩写
            }
        }
        return orgNameSet;
    }

    /**
     * @Description: 数据资源中心经营数据看板-2 -- 按部门返回开票
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 13:36
     **/
    private OrgEO getOrgEOBelong(List<OrgEO> orgEOList, String orgId) {
        OrgEO myOrgEO = null;
        for (OrgEO orgEO : orgEOList) {
            if (StringUtils.equals(orgId, orgEO.getId())) {
                myOrgEO = orgEO;
                break;
            }
        }
        for (OrgEO orgEO : orgEOList) {
            String parentIds = orgEO.getParentIds();
            if (StringUtils.isEmpty(parentIds)) {
                continue;
            }
            if (null == myOrgEO) {
                break;
            }
            String[] parentIdArr = StringUtils.split(parentIds, ',');
            Set<String> parentIdSet = removeArrBlankElement(parentIdArr);
            if (parentIdSet.size() == 3 && StringUtils.equals(orgEO.getId(), myOrgEO.getParentId())) {
                return orgEO;
            }
        }
        OrgEO orgEO = new OrgEO();
        orgEO.setName("其他");
        orgEO.setOrgAbb("其他");
        return orgEO;
    }

    /**
     * @Description: 部门id与部门名称的Map
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 13:36
     **/
    private Map<String, String> getOrgIdOrgNameMap(List<OrgEO> orgEOList) {
        Map<String, String> orgIdOrgEOMap = new LinkedHashMap<>();

        for (OrgEO orgEO : orgEOList) {
            orgIdOrgEOMap.put(orgEO.getId(), orgEO.getName());
        }
        return orgIdOrgEOMap;
    }

    /**
     * @Description: 部门id与部门名称简写的Map
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 13:36
     **/
    private Map<String, String> getOrgIdOrgAbbMap(List<OrgEO> orgEOList) {
        Map<String, String> orgIdOrgEOMap = new LinkedHashMap<>();
        for (OrgEO orgEO : orgEOList) {
            orgIdOrgEOMap.put(orgEO.getId(), orgEO.getOrgAbb());
        }
        return orgIdOrgEOMap;
    }


    /**
     * @Description: 找到所有的本部实体List
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 13:36
     **/
    private List<OrgEO> getBigOrgEOList(List<OrgEO> orgEOList) {
        List<OrgEO> bigOrgEOList = new ArrayList<>();

        for (OrgEO orgEO : orgEOList) {
            String parentIds = orgEO.getParentIds();
            if (StringUtils.isEmpty(parentIds)) {
                continue;
            }
            String[] parentIdArr = StringUtils.split(parentIds, ',');
            Set<String> parentIdSet = removeArrBlankElement(parentIdArr);
            if (parentIdSet.size() == 3) {
                bigOrgEOList.add(orgEO);
            }
        }
        return bigOrgEOList;
    }

    /**
     * @Description: TODO
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 9:29
     **/
    private String getOrgEOName(List<OrgEO> orgEOList, String orgId) {
        for (OrgEO orgEO : orgEOList) {
            if (StringUtils.equals(orgId, orgEO.getId())) {
                return orgEO.getName();
            }
        }

        return null;
    }

    public Set<String> removeArrBlankElement(String[] arr) { // 去掉数组里面的空串
        Set<String> set = new LinkedHashSet<>();
        if (CollectionUtils.isNotEmpty(arr)) {
            for (String element : arr) {
                if (StringUtils.isNotEmpty(element)) {
                    set.add(element);
                }
            }
        } else {
            return set;
        }
        return set;
    }

    public Double getScale2(Double amount) {
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public Double getScale2(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
