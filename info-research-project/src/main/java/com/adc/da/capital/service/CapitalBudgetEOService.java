package com.adc.da.capital.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.capital.dao.CapitalBudgetEODao;
import com.adc.da.capital.entity.CapitalBudgetEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 * <b>功能：</b>RS_CAPITAL_BUDGET CapitalBudgetEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("capitalBudgetEOService")
@Transactional(value = "transactionManager",
    readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CapitalBudgetEOService extends BaseService<CapitalBudgetEO, String> {

    @Autowired
    private CapitalBudgetEODao dao;

    public CapitalBudgetEODao getDao() {
        return dao;
    }

}
