package com.adc.da.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.crm.dao.BTravelCustomerVisitRecordEODao;
import com.adc.da.crm.entity.BTravelCustomerVisitRecordEO;


/**
 *
 * <br>
 * <b>功能：</b>B_TRAVEL_CUSTOMER_VISIT_RECORD BTravelCustomerVisitRecordEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("bTravelCustomerVisitRecordEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BTravelCustomerVisitRecordEOService extends BaseService<BTravelCustomerVisitRecordEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(BTravelCustomerVisitRecordEOService.class);

    @Autowired
    private BTravelCustomerVisitRecordEODao dao;

    public BTravelCustomerVisitRecordEODao getDao() {
        return dao;
    }

}
