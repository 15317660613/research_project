package com.adc.da.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.crm.dao.BTravelProjectVisitRecordEODao;
import com.adc.da.crm.entity.BTravelProjectVisitRecordEO;


/**
 *
 * <br>
 * <b>功能：</b>B_TRAVEL_PROJECT_VISIT_RECORD BTravelProjectVisitRecordEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("bTravelProjectVisitRecordEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BTravelProjectVisitRecordEOService extends BaseService<BTravelProjectVisitRecordEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(BTravelProjectVisitRecordEOService.class);

    @Autowired
    private BTravelProjectVisitRecordEODao dao;

    public BTravelProjectVisitRecordEODao getDao() {
        return dao;
    }

}
