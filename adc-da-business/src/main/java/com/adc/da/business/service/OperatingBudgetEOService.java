package com.adc.da.business.service;

import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.sys.dao.MyOrgEODao;
import com.adc.da.util.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.business.dao.OperatingBudgetEODao;
import com.adc.da.business.entity.OperatingBudgetEO;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <br>
 * <b>功能：</b>TS_OPERATING_BUDGET OperatingBudgetEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-05-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("operatingBudgetEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class OperatingBudgetEOService extends BaseService<OperatingBudgetEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(OperatingBudgetEOService.class);

    @Autowired
    private OperatingBudgetEODao dao;

    @Autowired
    private MyOrgEODao myOrgEODao;

    public OperatingBudgetEODao getDao() {
        return dao;
    }

    public List<OperatingBudgetEO> excelImport(InputStream is, ImportParams params) throws Exception {
        List<OperatingBudgetEO> datas = ExcelImportUtil.importExcel(is, OperatingBudgetEO.class, params);
        List<OperatingBudgetEO> returnDatas = new ArrayList<OperatingBudgetEO>();
        //导入
        for (OperatingBudgetEO operatingBudgetEO : datas) {
            operatingBudgetEO.setId(UUID.randomUUID().toString());
            if (null != operatingBudgetEO.getDeptName() && !"".equals(operatingBudgetEO.getDeptName())) {
                operatingBudgetEO.setDeptId(myOrgEODao.getOrgIdByOrgName(operatingBudgetEO.getDeptName()));
            }
            returnDatas.add(operatingBudgetEO);
            dao.insertSelective(operatingBudgetEO);
        }
        return returnDatas;
    }

    public  List<OperatingBudgetEO> queryYearData(String year ,String startMonth , String endMonth) {
        return   this.dao.queryYearData(year ,startMonth, endMonth);
    }

    public List<String> queryAllYear(){
        return  this.dao.queryAllYear();
    }

    public Float queryForwardYearAR(String year){
        return  this.dao.queryForwardYearAR(year);
    }

    public Float queryThisYearAR(String year){
        return  this.dao.queryThisYearAR(year);
    }

    public Float queryContractAmountByYearAndMonths(List<String> deptIds,String year ,String startMonth , String endMonth){

        return this.dao.queryContractAmountByYearAndMonths(deptIds , year ,startMonth, endMonth);

    }

    public Float queryInvoiceAmountByYearAndMonths(List<String> deptIds, String year, String startMonth,
        String endMonth) {

        return this.dao.queryInvoiceAmountByYearAndMonths(deptIds, year, startMonth, endMonth);

    }


    public List<String> queryBusinessNamesByYearAndMonths(String year, String startMonth, String endMonth) {
        return this.dao.queryBusinessNamesByYearAndMonths(year, startMonth, endMonth);
    }


    public String[] getBusinessYearData(String bizName, String year) {
        String [] array = new String[12];
        for (int i = 0 ; i < 12 ; i++){
            array[i]="0.00" ;
        }
        List<OperatingBudgetEO> operatingBudgetEOList = this.dao.selectEveryMonthInvoiceDataByBudgetName(bizName, year );
        if (CollectionUtils.isNotEmpty(operatingBudgetEOList)){
             for(OperatingBudgetEO operatingBudgetEO:operatingBudgetEOList){
                 int pos = operatingBudgetEO.getMonth().intValue()-1;
                 array[pos] = String.valueOf(operatingBudgetEO.getInvoiceAmount());
             }
        }
        return array;
    }
}
