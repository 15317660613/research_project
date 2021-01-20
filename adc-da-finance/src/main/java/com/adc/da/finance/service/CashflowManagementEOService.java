package com.adc.da.finance.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.adc.da.base.page.BasePage;
import com.adc.da.finance.dao.*;
import com.adc.da.finance.dto.CashflowManagementDto;
import com.adc.da.finance.entity.*;
import com.adc.da.finance.page.BusinessGonfigEOPage;
import com.adc.da.finance.page.CashflowManagementEOPage;
import com.adc.da.finance.util.DoubleUtils;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>F_CASHFLOW_MANAGEMENT CashflowManagementEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("cashflowManagementEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CashflowManagementEOService extends BaseService<CashflowManagementEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CashflowManagementEOService.class);

    @Autowired
    private CashflowManagementEODao dao;
    @Autowired
    private UserEODao userEODao;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private ReceivablesManagementEODao receivablesManagementEODao;
    @Autowired
    private CostManagementEODao costManagementEODao;
    @Autowired
    private SalaryManagementEODao salaryManagementEODao;
    @Autowired
    private BusinessGonfigEODao businessGonfigEODao;

    public CashflowManagementEODao getDao() {
        return dao;
    }


    public List<CashflowManagementEO> queryByPage(CashflowManagementEOPage cashflowManagementEOPage) throws Exception {
        //查询计算余额
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        //默认是查询正常的数据
        if (StringUtils.isEmpty(cashflowManagementEOPage.getDelFlag())){
            cashflowManagementEOPage.setDelFlag("0");
        }
        //根据业务名称模糊查询
        if (StringUtils.isNotEmpty(cashflowManagementEOPage.getBusinessGonfigName())){
            cashflowManagementEOPage.setBusinessGonfigName("%"+cashflowManagementEOPage.getBusinessGonfigName()+"%");
            cashflowManagementEOPage.setBusinessGonfigNameOperator("LIKE");
        }
        int num = dao.queryByCount(cashflowManagementEOPage);
        cashflowManagementEOPage.getPager().setRowCount(num);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ORDER BY ABS(CM.CM_YEAR) DESC,ABS(CM.CM_MONTH) DESC,NLSSORT(BG.BG_NAME,'NLS_SORT = SCHINESE_PINYIN_M') ASC");
        cashflowManagementEOPage.setSql_filter(stringBuilder.toString());
        return  dao.queryByPage(cashflowManagementEOPage);
    }
    /**
     * 修改现金流量管理
     * @param eo
     * @return
     * @throws Exception
     */
    public CashflowManagementEO update(CashflowManagementEO eo) throws Exception{
        if (StringUtils.isEmpty(UserUtils.getUserId())){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        eo.setUpdateTime(new Date());
        eo.setDelFlag("0");
        dao.updateByPrimaryKeySelective(eo);
        return eo;
    }

    /**
     * 导出现金流量管理
     * @param exportParams
     * @param eoPage
     * @return
     * @throws Exception
     */
    public Workbook exportCashflowManagement(ExportParams exportParams,CashflowManagementEOPage eoPage) throws Exception{
        eoPage.setPageSize(15000);
        List<CashflowManagementEO> result = this.queryByPage(eoPage);
        List<CashflowManagementDto> datas = new ArrayList<>();
        if (result.size()>0){
            datas = beanMapper.mapList(result,CashflowManagementDto.class);
        }
        return ExcelExportUtil.exportExcel(exportParams,CashflowManagementDto.class,datas);
    }

    /**
     * 更新现金流量金额
     * @param businessGonfigId
     * @param year
     * @param month
     * @throws Exception
     */
    public void updateCashflowByBusinessGonfigIdAndYearAndMonth(String businessGonfigId,String year,String month) throws Exception{
        //① 现金流入=应收账款数据的余额之和
        ReceivablesManagementEO receivablesManagementEO = receivablesManagementEODao.statisticsMoney(businessGonfigId, year, month);
        BigDecimal flowInMoney = new BigDecimal(0);
         if (StringUtils.isNotEmpty(receivablesManagementEO)){
            flowInMoney = receivablesManagementEO.getRemMoney();
        }
        // ② 现金流出 = 成本数据 + 薪酬数据
        //成本数据
        CostManagementEO costManagementEO = costManagementEODao.statisticsAmountByBusinessId(businessGonfigId, year, month);
        BigDecimal costAmountMoney = new BigDecimal(0);
        if (StringUtils.isNotEmpty(costManagementEO)){
            costAmountMoney = BigDecimal.valueOf(costManagementEO.getAmount());
        }
        //薪酬数据
        SalaryManagementEO salaryManagementEO = salaryManagementEODao.statisticsAmountByBusinessId(businessGonfigId, year, month);
        BigDecimal saAmount = new BigDecimal(0);
        if (StringUtils.isNotEmpty(salaryManagementEO)){
            saAmount = BigDecimal.valueOf(salaryManagementEO.getAmount());
        }
        BigDecimal flowOutMoney =costAmountMoney.add(saAmount);
        // ③ 现金余额=现金流入-现金流出
        BigDecimal surplusMoney = flowInMoney.subtract(flowOutMoney);
        //根据年月日查询
        CashflowManagementEO eo = dao.queryByBusinessGonfigIdAndYearAndMonth(businessGonfigId,year,month);
        if (StringUtils.isNotEmpty(eo)){
            eo.setFlowInMoney(DoubleUtils.getScale2BybigDecimal(flowInMoney));
            eo.setFlowOutMoney(DoubleUtils.getScale2BybigDecimal(flowOutMoney));
            eo.setSurplusMoney(DoubleUtils.getScale2BybigDecimal(surplusMoney));
            eo.setUpdateTime(new Date());
            dao.updateByPrimaryKeySelective(eo);
        }else{
            eo = new CashflowManagementEO();
            eo.setId(UUID.randomUUID10());
            eo.setCreateTime(new Date());
            eo.setUpdateTime(new Date());
            eo.setDelFlag("0");
            eo.setFlowInMoney(DoubleUtils.getScale2BybigDecimal(flowInMoney));
            eo.setFlowOutMoney(DoubleUtils.getScale2BybigDecimal(flowOutMoney));
            eo.setSurplusMoney(DoubleUtils.getScale2BybigDecimal(surplusMoney));
            eo.setBusinessGonfigId(businessGonfigId);
            eo.setCmYear(year);
            eo.setCmMonth(month);
            dao.insertSelective(eo);
        }
    }


    //每年每月生成一条业务数据
    public void createCashflow()throws Exception{
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
                this.updateCashflowByBusinessGonfigIdAndYearAndMonth(businessGonfigId,year,month);
            }
        }
    }
}
