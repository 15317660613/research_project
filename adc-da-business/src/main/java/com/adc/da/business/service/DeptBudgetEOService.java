package com.adc.da.business.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.business.dao.DeptBudgetEODao;
import com.adc.da.business.entity.DeptBudgetEO;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.sys.dao.MyOrgEODao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <br>
 * <b>功能：</b>TS_DEPT_BUDGET DeptBudgetEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-05-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("deptBudgetEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DeptBudgetEOService extends BaseService<DeptBudgetEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(DeptBudgetEOService.class);

    @Autowired
    private DeptBudgetEODao dao;

    @Autowired
    private MyOrgEODao myOrgEODao;

    public DeptBudgetEODao getDao() {
        return dao;
    }

    public List<DeptBudgetEO> excelImport(InputStream is, ImportParams params) throws Exception {
        List<DeptBudgetEO> datas = ExcelImportUtil.importExcel(is, DeptBudgetEO.class, params);
        List<DeptBudgetEO> returnDatas = new ArrayList<DeptBudgetEO>();
        //导入
        for (DeptBudgetEO deptBudgetEO : datas) {
            deptBudgetEO.setId(UUID.randomUUID().toString());
            if (null != deptBudgetEO.getDeptName() && !"".equals(deptBudgetEO.getDeptName())) {
                deptBudgetEO.setDeptId(myOrgEODao.getOrgIdByOrgName(deptBudgetEO.getDeptName()));
            }
            returnDatas.add(deptBudgetEO);
            dao.insertSelective(deptBudgetEO);
        }
        return returnDatas;
    }

    public Float queryBigDeptProfit(List<String> deptIds, String thisYear) {
        return this.dao.queryBigDeptProfit(deptIds, thisYear);
    }

    public Float queryCenterProfit(String thisYear) {
        return this.dao.queryCenterProfit(thisYear);
    }

    public Float queryCenterContractAmountByYear(String thisYear) {
        return this.dao.queryCenterContractAmountByYear(thisYear);
    }

    public Float queryCenterInvoiceAmountByYear(String thisYear) {
        return this.dao.queryCenterInvoiceAmountByYear(thisYear);
    }

    public Float queryBigDeptContractAmountByYear(List<String> deptIds, String thisYear) {
        return this.dao.queryBigDeptContractAmountByYear(deptIds, thisYear);
    }

    public Float queryBigDeptInvoiceAmountByYear(List<String> deptIds, String thisYear) {
        return this.dao.queryBigDeptInvoiceAmountByYear(deptIds, thisYear);
    }

    public List<Map<String, Object>> queryOfficeCostByYear(String thisYear) {
        return this.dao.queryOfficeCostByYear(thisYear);
    }

    public List<Map<String, Object>> queryConsumableCostByYear(String thisYear) {
        return this.dao.queryConsumableCostByYear(thisYear);
    }

    public List<Map<String, Object>> queryTravelCostByYear(String thisYear) {
        return this.dao.queryTravelCostByYear(thisYear);
    }

}
