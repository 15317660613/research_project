package com.adc.da.industymeeting.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.service.ProjectService;
import com.adc.da.industymeeting.dao.ReceivableIncomeEODao;
import com.adc.da.industymeeting.dao.ReceivableIncomeFiledEODao;
import com.adc.da.industymeeting.entity.ReceivableIncomeEO;
import com.adc.da.industymeeting.entity.ReceivableIncomeFiledEO;
import com.adc.da.sys.dao.DicTypeEODao;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.util.DoubleUtils;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.math.BigInteger.ZERO;


/**
 *
 * <br>
 * <b>功能：</b>RECEIVABLE_INCOME ReceivableIncomeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("receivableIncomeEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ReceivableIncomeEOService extends BaseService<ReceivableIncomeEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ReceivableIncomeEOService.class);

    @Autowired
    private ReceivableIncomeEODao dao;

    @Autowired
    private ReceivableIncomeFiledEODao receivableIncomeFiledEODao;

    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private BudgetEODao budgetEODao;

    @Autowired
    private DicTypeEODao dicTypeEODao;

    @Autowired
    private ProjectService projectService;

    public ReceivableIncomeEODao getDao() {
        return dao;
    }





    public ReceivableIncomeEO save(ReceivableIncomeEO eo, String userId) {
        eo.setId(UUID.randomUUID10());
        eo.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
        eo.setCreateUserId(userId);
        eo.setCreateTime(new Date());


        eo.setUpdateUserId(userId);
        eo.setUpdateTime(new Date());
        dao.insertSelective(eo);
        return eo;
    }

    public List<ReceivableIncomeEO> batchSave(List<ReceivableIncomeEO> receivableIncomeEOS, String userId) {
        // 导入数据前是否需要逻辑删除全部？
//        empty();
        for (ReceivableIncomeEO eo : receivableIncomeEOS) {
            eo.setId(UUID.randomUUID10());
            eo.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
            eo.setDepartmentId(orgEODao.getOrgIdByOrgName(eo.getDepartment()));
            eo.setHeadquartersId(orgEODao.getOrgIdByOrgName(eo.getHeadquarters()));
//            List<BudgetEO> budgetEOs = budgetEODao.findAllBudgetNameLikeAndByType(eo.getProject(),"0");
//            if (CollectionUtils.isNotEmpty(budgetEOs)) {
//                eo.setProject(budgetEOs.get(0).getId());
//            }
            eo.setCompany(dicTypeEODao.getDicTypeIdByDicIdAndDicTypeName("ECGGB823JL", eo.getCompany()));
            eo.setCreateUserId(userId);
            eo.setCreateTime(new Date());
            eo.setUpdateUserId(userId);
            eo.setUpdateTime(new Date());
            dao.insertSelective(eo);
        }
        return receivableIncomeEOS;
    }

    /**
     * 批量逻辑删除
     */
    public void deleteLogicInBatch(List<String> ids) {
        dao.deleteLogicInBatch(ids);
    }

    /**
     * 逻辑清空
     */
    public void empty() {
        dao.empty();
    }

    /**
     * 企业应收账款top 10
     */
    public List<ReceivableIncomeEO> accountReceivableByEnterprise (Integer topNum) throws Exception{
        List<ReceivableIncomeEO> list = dao.accountReceivableByEnterprise(topNum);
        List<ReceivableIncomeEO> result = new ArrayList<>();
        if (StringUtils.isNotEmpty(list)){
            for (ReceivableIncomeEO receivableIncomeEO : list){
                BigDecimal amountReceivableTemp = receivableIncomeEO.getAmountReceivable();
                BigDecimal temp = DoubleUtils.getDivideTenThousandScale2(amountReceivableTemp);
                String tempStr = DoubleUtils.formatToNumber(temp);

                receivableIncomeEO.setAmountReceivable(new BigDecimal(tempStr));
                receivableIncomeEO.setAmountReceivableStr(tempStr);
                result.add(receivableIncomeEO);
            }
        }
        return result;
    }

    /**
     *部门应收账款占比TOP10
     */
    public List<ReceivableIncomeEO> accountReceivableByDepart (Integer topNum) throws Exception{
        List<ReceivableIncomeEO> list = dao.accountReceivableByDepart(topNum);
        List<ReceivableIncomeEO> result = new ArrayList<>();
        if (StringUtils.isNotEmpty(list)){
            for (ReceivableIncomeEO receivableIncomeEO : list){
                BigDecimal amountReceivableTemp = receivableIncomeEO.getAmountReceivable();
                BigDecimal temp = DoubleUtils.getDivideTenThousandScale2(amountReceivableTemp);
                receivableIncomeEO.setAmountReceivable(temp);
                BigDecimal totalamountReceivableTemp = BigDecimal.valueOf(receivableIncomeEO.getTotalAmountReceivable());
                BigDecimal tempTotal = DoubleUtils.getDivideTenThousandScale2(totalamountReceivableTemp);

                receivableIncomeEO.setTotalAmountReceivable(tempTotal.doubleValue());
                result.add(receivableIncomeEO);
            }
        }
        return result;
    }

    public Map<String,BigDecimal> getCompanyIncomeAmountMap(List<ReceivableIncomeFiledEO> receivableIncomeFiledEOList) {
        Map<String,BigDecimal> map = new HashMap<>();
        for (ReceivableIncomeFiledEO receivableIncomeFiledEO : receivableIncomeFiledEOList){
            map.put(receivableIncomeFiledEO.getCompany(),receivableIncomeFiledEO.getIncome());
        }

        return map;
    }

    /**
     * 各公司经营情况统计 （按照当前年份）
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> getPartBOperationStatistics() throws Exception{
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer currentMonth = dateTime.getMonthOfYear();

        List<Map<String, Object>> list = projectService.getPartBOperationStatistic();
        List<ReceivableIncomeFiledEO>  receivableIncomeFiledEOList = receivableIncomeFiledEODao.listByYearAndMonthEqualGroupCompanyFullName(currentYear,currentMonth);
        Map<String,BigDecimal> companyIncomeAmountMap =  getCompanyIncomeAmountMap(receivableIncomeFiledEOList);

        List<Map<String,Object>> result = new ArrayList<>();

        for (Map<String,Object> l : list){
            String partyB = l.get("partyB").toString();
            BigDecimal currentMonthIncomeAmount = BigDecimal.valueOf(0);
            BigDecimal invoiceAmount = (BigDecimal)l.get("invoiceAmount");//开票额
            BigDecimal contractAmount = (BigDecimal)l.get("contractAmount");//合同额

            //本年累计开票
            if (null != companyIncomeAmountMap.get(partyB)) {
                currentMonthIncomeAmount = companyIncomeAmountMap.get(partyB);
            }

            l.put("projectOwner", partyB);
            l.put("accountAmount", String.valueOf(DoubleUtils.getDivideTenThousandScale2(currentMonthIncomeAmount)));//进账额
            l.put("contractAmount",String.valueOf(DoubleUtils.getDivideTenThousandScale2(contractAmount)));//合同额
            l.put("invoiceAmount",String.valueOf(DoubleUtils.getDivideTenThousandScale2(invoiceAmount)));//开票额

            result.add(l);
        }
        return result;
    }

    public List<Map<String,Object>> getPadPartBOperationStatistics() throws Exception{
        DateTime dateTime = new DateTime();
        Integer currentYear = dateTime.getYear();
        Integer currentMonth = dateTime.getMonthOfYear();

        List<Map<String, Object>> list = projectService.getPartBOperationStatistic();
        List<ReceivableIncomeFiledEO>  receivableIncomeFiledEOList = receivableIncomeFiledEODao.listByYearAndMonthEqualGroupCompanyFullName(currentYear,currentMonth);
        Map<String,BigDecimal> companyIncomeAmountMap =  getCompanyIncomeAmountMap(receivableIncomeFiledEOList);

        List<Map<String,Object>> result = new ArrayList<>();

        for (Map<String,Object> l : list){
            String partyB = l.get("partyB").toString();
            BigDecimal currentMonthIncomeAmount = BigDecimal.valueOf(0);
            BigDecimal invoiceAmount = (BigDecimal)l.get("invoiceAmount");//开票额
            BigDecimal contractAmount = (BigDecimal)l.get("contractAmount");//合同额

            //本年累计开票
            if (null != companyIncomeAmountMap.get(partyB)) {
                currentMonthIncomeAmount = companyIncomeAmountMap.get(partyB);
            }

            l.put("projectOwner", partyB);
            l.put("accountAmount", String.valueOf(DoubleUtils.getDoubletDivideTenThousandInt(currentMonthIncomeAmount)));//进账额
            l.put("contractAmount",String.valueOf(DoubleUtils.getDoubletDivideTenThousandInt(contractAmount)));//合同额
            l.put("invoiceAmount",String.valueOf(DoubleUtils.getDoubletDivideTenThousandInt(invoiceAmount)));//开票额

            result.add(l);
        }
        return result;
    }


}
