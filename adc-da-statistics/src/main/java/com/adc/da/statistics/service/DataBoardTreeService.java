package com.adc.da.statistics.service;

import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.entity.OrgWithLevelEO;
import com.adc.da.statistics.dao.ContractAmountEODao;
import com.adc.da.statistics.entity.DataBoardTreeEO;
import com.adc.da.statistics.util.DataBoardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adc.da.statistics.util.DataBoardUtils.INT_BEGIN;
import static com.adc.da.statistics.util.DataBoardUtils.INT_END;

/**
 * <br>
 * <b>功能：</b>ST_BUSINESS_WORKTIME BusinessWorktimeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("dataBoardTreeService")
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DataBoardTreeService {

    @Autowired
    private ContractAmountEODao contractAmountEODao;

    /**
     * equal 1
     */
    private static final int BILLING_TREE = 0b001;

    /**
     * equal 2
     */
    private static final int CONTRACT_TREE = 0b010;

    /**
     * equal 4
     */
    private static final int EXPENDITURE_TREE = 0b100;

    /**
     * 获取根据开票排序的业务树
     *
     * @param year
     * @param pageSize
     * @param type
     * @return
     */
    public Map<String, List<DataBoardTreeEO>> getBudgetTree(int year, Integer pageSize, Integer type) {
        Date[] date = DataBoardUtils.initDate(year);
        Map<String, List<DataBoardTreeEO>> resultMap = new HashMap<>();




        /*
         *  101  & 001 = 1  true
         *  BILLING_TREE= 1
         */
        if (BILLING_TREE == (type & BILLING_TREE)) {
            List<DataBoardTreeEO> billing = contractAmountEODao
                .getBudgetTree(pageSize, date[INT_BEGIN], date[INT_END], BILLING_TREE);
            resultMap.put("billing", billing);
        }


        /*
         * 101 & 010 = 0    false
         *  CONTRACT_TREE = 2
         */
        if (CONTRACT_TREE == (type & CONTRACT_TREE)) {
            List<DataBoardTreeEO> contractAmount = contractAmountEODao
                .getBudgetTree(pageSize, date[INT_BEGIN], date[INT_END], CONTRACT_TREE);
            resultMap.put("contractAmount", contractAmount);
        }

        /*
         * 101  & 100 = 100  true
         * EXPENDITURE_TREE = 4
         */
        if (EXPENDITURE_TREE == (type & EXPENDITURE_TREE)) {
            List<DataBoardTreeEO> deptExpenditure = contractAmountEODao
                .getBudgetTree(pageSize, date[INT_BEGIN], null, EXPENDITURE_TREE);
            resultMap.put("deptExpenditure", deptExpenditure);
        }

        return resultMap;
    }

    @Autowired
    private OrgListDao orgListDao;

    public List<OrgWithLevelEO> getDeptTree() {
        return orgListDao.getOrgAndSubOrgIdsWithLevel(null);
    }
}
