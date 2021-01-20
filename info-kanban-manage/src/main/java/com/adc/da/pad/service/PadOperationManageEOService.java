package com.adc.da.pad.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.adc.da.login.util.UserUtils;
import com.adc.da.pad.handler.PadOperationManageEOHandler;
import com.adc.da.pad.page.PadOperationManageEOPage;
import com.adc.da.pad.vo.DashBoardBarVO;
import com.adc.da.pad.vo.PadDashBoardHeaderVO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.DoubleUtils;
import com.adc.da.util.exception.AdcDaBaseException;
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

import com.adc.da.base.service.BaseService;
import com.adc.da.pad.dao.PadOperationManageEODao;
import com.adc.da.pad.entity.PadOperationManageEO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.Collator;
import java.util.*;


/**
 *
 * <br>
 * <b>功能：</b>PAD_OPERATION_MANAGE PadOperationManageEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("padOperationManageEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class PadOperationManageEOService extends BaseService<PadOperationManageEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(PadOperationManageEOService.class);

    @Autowired
    private PadOperationManageEODao dao;
    @Autowired
    private OrgEODao orgEODao;

    public PadOperationManageEODao getDao() {
        return dao;
    }


    public void deleteLogicInBatch(List<String> list){
        dao.deleteLogicInBatch(list);
    }
    /**
     * 导入
     */

    public PadDashBoardHeaderVO getHeaderVO(){
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        PadOperationManageEO yearResult = dao.sumByYear(year);
        PadOperationManageEO monthResult = dao.sumByMonthAndYear(month,year);
        PadDashBoardHeaderVO padDashBoardHeaderVO =new PadDashBoardHeaderVO();

        if (null != yearResult) {
            padDashBoardHeaderVO.setYearContractAmount(DoubleUtils.getDoubletDivideTenThousandInt(yearResult.getContractAmount()));
            padDashBoardHeaderVO.setYearInvoiceAmount(DoubleUtils.getDoubletDivideTenThousandInt(yearResult.getInvoiceAmount()));
            padDashBoardHeaderVO.setYearIncomeAmount(DoubleUtils.getDoubletDivideTenThousandInt(yearResult.getIncomeAmount()));
        }
        if (null != monthResult) {
            padDashBoardHeaderVO.setMonthContractAmount(DoubleUtils.getDoubletDivideTenThousandInt(monthResult.getContractAmount()));
            padDashBoardHeaderVO.setMonthInvoiceAmount(DoubleUtils.getDoubletDivideTenThousandInt(monthResult.getInvoiceAmount()));
            padDashBoardHeaderVO.setMonthIncomeAmount(DoubleUtils.getDoubletDivideTenThousandInt(monthResult.getIncomeAmount()));
        }

        return padDashBoardHeaderVO;
    }


    public DashBoardBarVO DashBoardBarVO(int type){
        DashBoardBarVO dashBoardBarVO = new DashBoardBarVO();
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        dashBoardBarVO.setCurrentYear(year);
        dashBoardBarVO.setLastYear(year-1);

        List<String> xIndexList = dashBoardBarVO.getXIndexList();
        List<Double> currentTimeDataList =dashBoardBarVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = dashBoardBarVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = dashBoardBarVO.getRateList(); //同比变化率
        List<PadOperationManageEO> thisYearPadOperationManageEOList = dao.sumGroupByMonthInYear(year);
        List<PadOperationManageEO> lastYearPadOperationManageEOList = dao.sumGroupByMonthInYear(year-1);
        for (int i = 1 ;  i <=12 ; i ++){
            BigDecimal thisAmount =BigDecimal.ZERO;
            BigDecimal lastAmount =BigDecimal.ZERO;
            for (PadOperationManageEO padOperationManageEO : thisYearPadOperationManageEOList){
                if (null!= padOperationManageEO.getMonth() && i == padOperationManageEO.getMonth()){
                    if (type == 1) {
                        thisAmount = padOperationManageEO.getContractAmount();
                        break;
                    } else if (type == 2){
                        thisAmount = padOperationManageEO.getInvoiceAmount();
                        break;
                    } else if (type == 3){
                        thisAmount = padOperationManageEO.getIncomeAmount();
                        break;
                    }
                }
            }
            for (PadOperationManageEO padOperationManageEO : lastYearPadOperationManageEOList){
                if (null!= padOperationManageEO.getMonth() && i == padOperationManageEO.getMonth()){
                    if (type == 1) {
                        lastAmount = padOperationManageEO.getContractAmount();
                        break;
                    } else if (type == 2){
                        lastAmount = padOperationManageEO.getInvoiceAmount();
                        break;
                    } else if (type == 3){
                        lastAmount = padOperationManageEO.getIncomeAmount();
                        break;
                    }
                }
            }
            currentTimeDataList.add(DoubleUtils.getDoubletDivideTenThousandInt(thisAmount));
            lastTimeDataList.add(DoubleUtils.getDoubletDivideTenThousandInt(lastAmount));
            double rate = 0.0d;
            if (i <= month) {
                 rate = culIncreaseRate(thisAmount, lastAmount);
            }
            rateList.add(rate);
            xIndexList.add(String.valueOf(i));
        }

        return dashBoardBarVO;
    }

    public DashBoardBarVO orgDashBoardBarVO(int type){
        DashBoardBarVO dashBoardBarVO = new DashBoardBarVO();
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        dashBoardBarVO.setCurrentYear(year);
        dashBoardBarVO.setLastYear(year-1);

        List<String> xIndexList = dashBoardBarVO.getXIndexList();
        List<Double> currentTimeDataList =dashBoardBarVO.getCurrentTimeDataList(); //当前时间数值
        List<Double> lastTimeDataList = dashBoardBarVO.getLastTimeDataList(); //同期时间数值
        List<Double> rateList = dashBoardBarVO.getRateList(); //同比变化率
        List<PadOperationManageEO> thisYearPadOperationManageEOList = dao.sumGroupByOrgInYear(year);
        //往年各部门整年整月的数据
        List<PadOperationManageEO> lastYearFormerPadOperationManageEOList = dao.sumGroupByOrgInYearAndMonthLte(year-1,month-1);
        //往年各部分当月的加权数据
        List<PadOperationManageEO> lastYearMonthPadOperationManageEOList = dao.sumGroupByOrgInYearAndMonthEq(year-1,month);
        for (PadOperationManageEO padOperationManageEO : lastYearMonthPadOperationManageEOList){
            BigDecimal contractAmount = culThisMonthAmount(day,days,padOperationManageEO.getContractAmount());
            BigDecimal invoiceAmount =culThisMonthAmount(day,days,padOperationManageEO.getInvoiceAmount());
            BigDecimal incomeAmount =culThisMonthAmount(day,days,padOperationManageEO.getIncomeAmount());
            for (PadOperationManageEO operationManageEO : lastYearFormerPadOperationManageEOList){
                if (StringUtils.equals(padOperationManageEO.getBigOrgName(),operationManageEO.getBigOrgName())){
                    operationManageEO.setContractAmount(doAdd(operationManageEO.getContractAmount(),contractAmount));
                    operationManageEO.setInvoiceAmount(doAdd(operationManageEO.getInvoiceAmount(),invoiceAmount));
                    operationManageEO.setIncomeAmount(doAdd(operationManageEO.getIncomeAmount(),incomeAmount));
                }
            }
        }
        List<String> orgNameList = dao.selectBigOrgNames();

        for (String orgName : orgNameList){
            BigDecimal thisAmount =BigDecimal.ZERO;
            BigDecimal lastAmount =BigDecimal.ZERO;
            for (PadOperationManageEO padOperationManageEO : thisYearPadOperationManageEOList){
                if (StringUtils.equals(orgName,padOperationManageEO.getBigOrgName())){
                    if (type == 1) {
                        thisAmount = padOperationManageEO.getContractAmount();
                        break;
                    } else if (type == 2){
                        thisAmount = padOperationManageEO.getInvoiceAmount();
                        break;
                    } else if (type == 3){
                        thisAmount = padOperationManageEO.getIncomeAmount();
                        break;
                    }
                }
            }
            for (PadOperationManageEO padOperationManageEO : lastYearFormerPadOperationManageEOList){
                if (StringUtils.equals(orgName,padOperationManageEO.getBigOrgName())){
                    if (type == 1) {
                        lastAmount = padOperationManageEO.getContractAmount();
                        break;
                    } else if (type == 2){
                        lastAmount = padOperationManageEO.getInvoiceAmount();
                        break;
                    } else if (type == 3){
                        lastAmount = padOperationManageEO.getIncomeAmount();
                        break;
                    }
                }
            }
            currentTimeDataList.add(DoubleUtils.getDoubletDivideTenThousandInt(thisAmount));
            lastTimeDataList.add(DoubleUtils.getDoubletDivideTenThousandInt(lastAmount));
            double rate =culIncreaseRate(thisAmount,lastAmount);
            rateList.add(rate);
            xIndexList.add(orgName);
        }

        return dashBoardBarVO;
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


    public Double culRate(BigDecimal currentAmount, BigDecimal lastAmount) {
        if (null == currentAmount) {
            currentAmount = BigDecimal.ZERO;
        }
        if (null == lastAmount) {
            lastAmount = BigDecimal.ZERO;;
        }
        BigDecimal rate = BigDecimal.ZERO;;
        BigDecimal difference = currentAmount;
        BigDecimal divide = lastAmount;
        if (BigDecimal.ZERO.compareTo(divide) == 0 && BigDecimal.ZERO.compareTo(currentAmount) != 0) {
            rate = BigDecimal.valueOf(100.0D);
        } else if (BigDecimal.ZERO.compareTo(divide) != 0) {
            difference = DoubleUtils.getBigDecimaltDivideTenThousandInt(difference);
            divide = DoubleUtils.getBigDecimaltDivideTenThousandInt(divide);
            rate = difference.multiply(BigDecimal.valueOf(100)).divide(divide, 0, BigDecimal.ROUND_HALF_UP);
        }
        return rate.doubleValue();
    }
    public Double culIncreaseRate(BigDecimal currentAmount, BigDecimal lastAmount) {
        if (null == currentAmount) {
            currentAmount = BigDecimal.ZERO;
        }
        if (null == lastAmount) {
            lastAmount = BigDecimal.ZERO;
        }
        BigDecimal AmountRate = BigDecimal.ZERO;
        BigDecimal difference = currentAmount.subtract(lastAmount);
        BigDecimal divide = lastAmount;
        if (BigDecimal.ZERO.compareTo(divide) == 0 && BigDecimal.ZERO.compareTo(currentAmount) != 0) {
            AmountRate = BigDecimal.valueOf(100.0D);
        } else if (BigDecimal.ZERO.compareTo(divide) != 0) {
            AmountRate = difference.multiply(BigDecimal.valueOf(100)).divide(divide, 0, BigDecimal.ROUND_HALF_UP);
        }
        return AmountRate.doubleValue();
    }

    public void excelImport(InputStream is) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        Map<String, String> orgNameIdMap = getOrgNameIdMap(orgEOList);

        ImportParams params = new ImportParams();
        params.setNeedVerfiy(true);
        params.setVerifyHandler(new PadOperationManageEOHandler());
        ExcelImportResult<PadOperationManageEO> result = ExcelImportUtil.importExcelMore(is, PadOperationManageEO.class, params);
        // 如果校验不通过，返回错误信息
        if (result.isVerfiyFail()) {
            List<Integer> rowIndexList = new ArrayList<>();
            List<PadOperationManageEO> errors = result.getFailList();
            StringBuilder sb = new StringBuilder();
            for (PadOperationManageEO error : errors) {
                rowIndexList.add(error.getRowNum() + 1);
            }
            sb.append("第 ");
            sb.append(StringUtils.join(rowIndexList, '、'));
            sb.append(" 行数据不合法，请检查！");
            logger.error("excel校验不通过！" + sb.toString());
            throw new AdcDaBaseException(sb.toString());
        }
        List<PadOperationManageEO> padOperationManageEOList = result.getList();
        if (CollectionUtils.isEmpty(padOperationManageEOList)){
            throw new AdcDaBaseException("导入数据集为空，请检查！");
        }
        //导入
        for (PadOperationManageEO padOperationManageEO : padOperationManageEOList) {
            padOperationManageEO.setId(UUID.randomUUID10());
            String orgId = orgNameIdMap.get(padOperationManageEO.getBigOrgName());
            if (null == orgId) {
                throw new AdcDaBaseException("组织机构 " + padOperationManageEO.getBigOrgName() + " 不存在,请检查！");
            }
            padOperationManageEO.setBigOrgId(orgId);
            padOperationManageEO.setUpdateTime(new Date());
            padOperationManageEO.setUpdateUserId(userEO.getUsid());
            padOperationManageEO.setUpdateUserName(userEO.getUsname());
        }
        dao.insertList(padOperationManageEOList);
    }

    /**
     * 导出
     *
     * @param padOperationManageEOPage
     * @return
     */
    public Workbook excelExport(PadOperationManageEOPage padOperationManageEOPage) {
        List<PadOperationManageEO> costManagementEOList = dao.queryByList(padOperationManageEOPage);
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);
        return ExcelExportUtil.exportExcel(params, PadOperationManageEO.class, costManagementEOList);
    }

    public Map<String, String> getOrgNameIdMap(List<OrgEO> orgEOList) {
        Map<String, String> nameIdMap = new HashMap<>();
        for (OrgEO orgEO : orgEOList) {
            nameIdMap.put(orgEO.getName(), orgEO.getId());
        }
        return nameIdMap;
    }

}
