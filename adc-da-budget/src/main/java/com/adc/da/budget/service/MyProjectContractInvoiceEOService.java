package com.adc.da.budget.service;

import com.adc.da.base.service.BaseService;

import com.adc.da.budget.dao.MyProjectContractInvoiceEODao;
import com.adc.da.budget.entity.ProjectContractInvoiceEO;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>PF_PROJECT_CONTRACT_INVOICE ProjectContractInvoiceEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("myProjectContractInvoiceEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class MyProjectContractInvoiceEOService extends BaseService<ProjectContractInvoiceEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(MyProjectContractInvoiceEOService.class);

    @Autowired
    private MyProjectContractInvoiceEODao dao;

    public MyProjectContractInvoiceEODao getDao() {
        return dao;
    }

   public List<ProjectContractInvoiceEO> queryByContractId( String contractId){
        return dao.queryByContractId(contractId) ;
    }

    public List<ProjectContractInvoiceEO> queryByProjectId( String projectId){
        return dao.queryByProjectId(projectId) ;
    }

}
