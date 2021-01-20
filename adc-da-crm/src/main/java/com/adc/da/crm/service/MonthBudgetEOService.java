package com.adc.da.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.crm.dao.MonthBudgetEODao;
import com.adc.da.crm.entity.MonthBudgetEO;


/**
 *
 * <br>
 * <b>功能：</b>MONTH_BUDGET MonthBudgetEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("monthBudgetEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class MonthBudgetEOService extends BaseService<MonthBudgetEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(MonthBudgetEOService.class);

    @Autowired
    private MonthBudgetEODao dao;

    public MonthBudgetEODao getDao() {
        return dao;
    }

}
