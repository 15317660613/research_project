package com.adc.da.finance.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.adc.da.base.service.BaseService;
import com.adc.da.finance.dao.*;
import com.adc.da.finance.dto.ProfitManagementDto;
import com.adc.da.finance.entity.*;
import com.adc.da.finance.page.BusinessGonfigEOPage;
import com.adc.da.finance.page.ProfitManagementEOPage;
import com.adc.da.finance.util.DoubleUtils;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 *
 * <br>
 * <b>功能：</b>F_PROFIT_MANAGEMENT ProfitManagementEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("profitManagementEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProfitManagementEOService extends BaseService<ProfitManagementEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProfitManagementEOService.class);

    @Autowired
    private ProfitManagementEODao dao;
    @Autowired
    private UserEODao userEODao;
    @Autowired
    private RevenueManagementEODao revenueManagementEODao;
    @Autowired
    private CostManagementEODao costManagementEODao;
    @Autowired
    private SalaryManagementEODao salaryManagementEODao;

    @Autowired
    private ReceivablesManagementEODao receivablesManagementEODao;
    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private BusinessGonfigEODao businessGonfigEODao;

    public ProfitManagementEODao getDao() {
        return dao;
    }


    public List<ProfitManagementEO> queryByPage(ProfitManagementEOPage profitManagementEOPage) throws Exception{
        //查询计算余额
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        //默认是查询正常的数据
        if (StringUtils.isEmpty(profitManagementEOPage.getDelFlag())){
            profitManagementEOPage.setDelFlag("0");
        }
        //根据业务名称模糊查询
        if (StringUtils.isNotEmpty(profitManagementEOPage.getBusinessGonfigName())){
            profitManagementEOPage.setBusinessGonfigName("%"+profitManagementEOPage.getBusinessGonfigName()+"%");
            profitManagementEOPage.setBusinessGonfigNameOperator("LIKE");
        }

        int num = dao.queryByCount(profitManagementEOPage);
        profitManagementEOPage.getPager().setRowCount(num);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ORDER BY ABS(PM.PM_YEAR) DESC,ABS(PM.PM_MONTH) DESC,NLSSORT(BG.BG_NAME,'NLS_SORT = SCHINESE_PINYIN_M') ASC");
        profitManagementEOPage.setSql_filter(stringBuilder.toString());
        return dao.queryByPage(profitManagementEOPage);
    }

    /**
     * 修改利润管理
     * @param eo
     * @return
     * @throws Exception
     */
    public ProfitManagementEO update(ProfitManagementEO eo) throws Exception{
        if (StringUtils.isEmpty(UserUtils.getUserId())){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        eo.setUpdateTime(new Date());
        eo.setDelFlag("0");
        dao.updateByPrimaryKeySelective(eo);
        return eo;
    }
    /**
     * 导出
     * @param exportParams
     * @param eoPage
     * @return
     * @throws Exception
     */
    public Workbook exportProfitManagement(ExportParams exportParams,ProfitManagementEOPage eoPage) throws Exception{
        eoPage.setPageSize(15000);
        List<ProfitManagementEO> result = this.queryByPage(eoPage);
        List<ProfitManagementDto> datas = new ArrayList<>();
        if (result.size()>0){
            datas = beanMapper.mapList(result,ProfitManagementDto.class);
        }
        return ExcelExportUtil.exportExcel(exportParams,ProfitManagementDto.class,datas);
    }

    /***
     * 新增数据
     * @param profitManagementEO
     * @return
     * @throws Exception
     */
    public ProfitManagementEO create(ProfitManagementEO profitManagementEO) throws Exception {
        Date date = new Date();
        profitManagementEO.setId(UUID.randomUUID10());
        profitManagementEO.setCreateTime(date);
        profitManagementEO.setUpdateTime(date);
        profitManagementEO.setDelFlag("0");
        dao.insertSelective(profitManagementEO);
        return profitManagementEO;
    }


    public void updateProfit() {
        logger.debug("------------------------------------------");
        logger.debug("------------------------------------------");
        logger.debug("------------定时任务启动---------------------");
        logger.debug("------------------------------------------");
        logger.debug("------------------------------------------");
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        List<ProfitManagementEO> profitManagementEOList = new ArrayList<>();

        List<BusinessGonfigEO> businessGonfigEOList = businessGonfigEODao.queryByList(new BusinessGonfigEOPage());
        Map<String, String> businessIdNameMap = getBusinessIdNameMap(businessGonfigEOList);
        Set<String> businessIdSet = businessIdNameMap.keySet();
        Map<String, Double> businessIdCostMap = getBusinessIdCostMap(year, month);
        Map<String, Double> businessIdSalaryMap = getBusinessIdSalaryMap(year, month);
        Map<String, Double> businessIdReceiveMap = getBusinessIdReceiveMap(year, month);
        for (String businessId : businessIdSet) {
            Double currentCostAmount = businessIdCostMap.get(businessId);
            if (currentCostAmount == null) {
                currentCostAmount = 0.0D;
            }
            Double currentSalaryAmount = businessIdSalaryMap.get(businessId);
            if (currentSalaryAmount == null) {
                currentSalaryAmount = 0.0D;
            }
            //当月总收入
            Double allCurrentReceiveAmount = businessIdReceiveMap.get(businessId);
            if (allCurrentReceiveAmount == null) {
                allCurrentReceiveAmount = 0.0D;
            }
            //当月总成本
            double allCurrentCostAmount = currentCostAmount + currentSalaryAmount;

            //当月总利润 6.	利润金额=收入金额-成本金额
            double allCurrentProfitAmount = allCurrentReceiveAmount - allCurrentCostAmount;
            //7.	利润率=利润金额÷收入金额
            double rate = culRate(allCurrentProfitAmount, allCurrentReceiveAmount);
            profitManagementEOList.add(
                    ProfitManagementEO.builder()
                            .id(UUID.randomUUID10())
                            .businessGonfigId(businessId)
                            .businessGonfigName(businessIdNameMap.get(businessId))
                            .costMoney(getScale2(allCurrentCostAmount))
                            .createTime(dateTime.toDate())
                            .incomeMoney(getScale2(allCurrentReceiveAmount))
                            .pmMonth(String.valueOf(month))
                            .pmYear(String.valueOf(year))
                            .profitMoney(getScale2(allCurrentProfitAmount))
                            .profitMargin(getScale2(rate).toString())
                            .build()
            );
            dao.deleteByBusinessIdAndYearAndMonth(businessId, String.valueOf(year), String.valueOf(month));
        }
        dao.insertList(profitManagementEOList);
    }

    public double culRate(Double act, Double target) {
        double rate = 0.0D;
        BigDecimal acture = BigDecimal.valueOf(act);
        BigDecimal divide = BigDecimal.valueOf(target);

        if (BigDecimal.ZERO.compareTo(divide) != 0) {
            rate = acture.multiply(BigDecimal.valueOf(100)).divide(divide, 1, BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        return rate;
    }

    public BigDecimal getScale2(Double amount) {
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP);
    }


    public Map<String, Double> getBusinessIdCostMap(Integer year, Integer month) {
        Map<String, Double> businessIdCostMap = new HashMap<>();
        List<CostManagementEO> costManagementEOList = costManagementEODao
                .selectSumCostByYearAndMonthGroupByBusinessId(String.valueOf(year), String.valueOf(month));
        if (CollectionUtils.isEmpty(costManagementEOList)) {
            return businessIdCostMap;
        }
        for (CostManagementEO costManagementEO : costManagementEOList) {
            businessIdCostMap.put(costManagementEO.getBusinessId(), costManagementEO.getAmount());
        }
        return businessIdCostMap;
    }

    public Map<String, Double> getBusinessIdSalaryMap(Integer year, Integer month) {
        Map<String, Double> businessIdSalaryMap = new HashMap<>();
        List<SalaryManagementEO> costManagementEOList = salaryManagementEODao
                .selectSumSalaryByYearAndMonthGroupByBusinessId(String.valueOf(year), String.valueOf(month));
        if (CollectionUtils.isEmpty(costManagementEOList)) {
            return businessIdSalaryMap;
        }
        for (SalaryManagementEO salaryManagementEO : costManagementEOList) {
            businessIdSalaryMap.put(salaryManagementEO.getBusinessId(), salaryManagementEO.getAmount());
        }
        return businessIdSalaryMap;
    }

    public Map<String, Double> getBusinessIdReceiveMap(Integer year, Integer month) {
        Map<String, Double> businessIdReceiveMap = new HashMap<>();
        List<ReceivablesManagementEO> costManagementEOList = receivablesManagementEODao
                .selectSumReceiveByYearAndMonthGroupByBusinessId(String.valueOf(year), String.valueOf(month));
        if (CollectionUtils.isEmpty(costManagementEOList)) {
            return businessIdReceiveMap;
        }
        for (ReceivablesManagementEO receivablesManagementEO : costManagementEOList) {
            businessIdReceiveMap.put(receivablesManagementEO.getBusinessGonfigId(), receivablesManagementEO.getRemMoney().doubleValue());
        }
        return businessIdReceiveMap;
    }

    public Map<String, Double> getBusinessIdCostMap(Integer year, Integer month, String businessId) {
        Map<String, Double> businessIdCostMap = new HashMap<>();
        List<CostManagementEO> costManagementEOList = costManagementEODao
                .selectSumCostByYearAndMonthAndBusinessIdGroupByBusinessId(String.valueOf(year),
                        String.valueOf(month), businessId);
        if (CollectionUtils.isEmpty(costManagementEOList)) {
            return businessIdCostMap;
        }
        for (CostManagementEO costManagementEO : costManagementEOList) {
            businessIdCostMap.put(costManagementEO.getBusinessId(), costManagementEO.getAmount());
        }
        return businessIdCostMap;
    }

    public Map<String, Double> getBusinessIdSalaryMap(Integer year, Integer month, String businessId) {
        Map<String, Double> businessIdSalaryMap = new HashMap<>();
        List<SalaryManagementEO> costManagementEOList = salaryManagementEODao
                .selectSumSalaryByYearAndMonthAndBusinessIdGroupByBusinessId(String.valueOf(year),
                        String.valueOf(month), businessId);
        if (CollectionUtils.isEmpty(costManagementEOList)) {
            return businessIdSalaryMap;
        }
        for (SalaryManagementEO salaryManagementEO : costManagementEOList) {
            businessIdSalaryMap.put(salaryManagementEO.getBusinessId(), salaryManagementEO.getAmount());
        }
        return businessIdSalaryMap;
    }

    public Map<String, String> getBusinessIdNameMap(List<BusinessGonfigEO> businessGonfigEOList) {
        Map<String, String> businessIdNameMap = new HashMap<>();
        for (BusinessGonfigEO businessGonfigEO : businessGonfigEOList) {
            businessIdNameMap.put(businessGonfigEO.getId(), businessGonfigEO.getBgName());
        }
        return businessIdNameMap;
    }

    /**
     * 更新成本金额方法
     * @param businessGonfigId
     * @param year
     * @param month
     * @throws Exception
     */
    public void updateProfitByBusinessGonfigIdAndYearAndMonth(String businessGonfigId,String year,String month) throws Exception{
        //根据业务id 年月查询当月成本数据
        CostManagementEO costManagementEO = costManagementEODao.statisticsAmountByBusinessId(businessGonfigId, year, month);
        BigDecimal costManagementMoney = new BigDecimal(0);
         if (StringUtils.isNotEmpty(costManagementEO)){
            costManagementMoney = BigDecimal.valueOf(costManagementEO.getAmount());
        }
        //根据业务id 年月查询当月薪酬数据
        SalaryManagementEO salaryManagementEO = salaryManagementEODao.statisticsAmountByBusinessId(businessGonfigId, year, month);
        BigDecimal salaryMoney = new BigDecimal(0);
        if (StringUtils.isNotEmpty(salaryManagementEO)){
            salaryMoney = BigDecimal.valueOf(salaryManagementEO.getAmount());
        }
        //成本金额=当月成本数据+当月薪酬数据
        BigDecimal costMoney =  costManagementMoney.add(salaryMoney);
        //收入金额
        RevenueManagementEO revenueManagementEO = revenueManagementEODao.statisticsMoney(businessGonfigId, year, month);
        BigDecimal incomeMoney = new BigDecimal(0);
        if (StringUtils.isNotEmpty(revenueManagementEO)){
            incomeMoney = revenueManagementEO.getRmMoney();
        }
        //利润金额=收入金额-成本金额
        BigDecimal profitMoney = incomeMoney.subtract(costMoney);
        //利润率=利润金额÷收入金额
        String profitMargin = "无法计算";
        if (!BigDecimal.ZERO.equals(incomeMoney)){
            profitMargin = profitMoney.divide(incomeMoney,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString()+"%";
        }
        //根据业务id年月查询利润信息
        ProfitManagementEO profitManagementEO = dao.queryByBusinessGonfigIdAndYearAndMonth(businessGonfigId,year,month);
        if (StringUtils.isNotEmpty(profitManagementEO)){
            profitManagementEO.setCostMoney(DoubleUtils.getScale2BybigDecimal(costMoney));
            profitManagementEO.setIncomeMoney(DoubleUtils.getScale2BybigDecimal(incomeMoney));
            profitManagementEO.setProfitMoney(DoubleUtils.getScale2BybigDecimal(profitMoney));
            profitManagementEO.setProfitMargin(profitMargin);
            profitManagementEO.setUpdateTime(new Date());
            dao.updateByPrimaryKeySelective(profitManagementEO);
        }else{
            profitManagementEO = new ProfitManagementEO();
            profitManagementEO.setId(UUID.randomUUID10());
            profitManagementEO.setDelFlag("0");
            profitManagementEO.setCreateTime(new Date());
            profitManagementEO.setUpdateTime(new Date());
            profitManagementEO.setCostMoney(DoubleUtils.getScale2BybigDecimal(costMoney));
            profitManagementEO.setIncomeMoney(DoubleUtils.getScale2BybigDecimal(incomeMoney));
            profitManagementEO.setProfitMoney(DoubleUtils.getScale2BybigDecimal(profitMoney));
            profitManagementEO.setProfitMargin(profitMargin);
            profitManagementEO.setBusinessGonfigId(businessGonfigId);
            profitManagementEO.setPmYear(year);
            profitManagementEO.setPmMonth(month);
            dao.insertSelective(profitManagementEO);
        }
    }


    //每年每月生成一条业务数据
    public void createProfit()throws Exception{
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH)+1);
        BusinessGonfigEOPage eoPage = new BusinessGonfigEOPage();
        eoPage.setDelFlag("0");
        eoPage.setBgType("0");//只统计经营类
        eoPage.setBgStatus("5E8YLRRFEL");//进行中的
        List<BusinessGonfigEO> businessGonfigEOList =businessGonfigEODao.queryByList(eoPage);
        if (businessGonfigEOList.size()>0){
            for (BusinessGonfigEO businessGonfigEO : businessGonfigEOList){
                String businessGonfigId = businessGonfigEO.getId();
                this.updateProfitByBusinessGonfigIdAndYearAndMonth(businessGonfigId,year,month);
            }
        }
    }


}
