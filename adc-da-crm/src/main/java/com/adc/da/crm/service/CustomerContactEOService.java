package com.adc.da.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.crm.dao.CustomerContactEODao;
import com.adc.da.crm.entity.CustomerContactEO;


/**
 *
 * <br>
 * <b>功能：</b>CUSTOMER_CONTACT CustomerContactEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("customerContactEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CustomerContactEOService extends BaseService<CustomerContactEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerContactEOService.class);

    @Autowired
    private CustomerContactEODao dao;

    public CustomerContactEODao getDao() {
        return dao;
    }

}
