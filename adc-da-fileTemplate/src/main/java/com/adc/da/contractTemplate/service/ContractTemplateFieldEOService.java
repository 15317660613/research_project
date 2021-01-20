package com.adc.da.contractTemplate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.contractTemplate.dao.ContractTemplateFieldEODao;
import com.adc.da.contractTemplate.entity.ContractTemplateFieldEO;


/**
 *
 * <br>
 * <b>功能：</b>CONTRACT_TEMPLATE_FIELD ContractTemplateFieldEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("contractTemplateFieldEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ContractTemplateFieldEOService extends BaseService<ContractTemplateFieldEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ContractTemplateFieldEOService.class);

    @Autowired
    private ContractTemplateFieldEODao dao;

    public ContractTemplateFieldEODao getDao() {
        return dao;
    }

}
