package com.adc.da.budget.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.ContractPartnerEODao;
import com.adc.da.budget.entity.ContractPartnerEO;


/**
 *
 * <br>
 * <b>功能：</b>DB_CONTRACT_PARTNER ContractPartnerEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("contractPartnerEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ContractPartnerEOService extends BaseService<ContractPartnerEO, Void> {

    private static final Logger logger = LoggerFactory.getLogger(ContractPartnerEOService.class);

    @Autowired
    private ContractPartnerEODao dao;

    public ContractPartnerEODao getDao() {
        return dao;
    }

}
