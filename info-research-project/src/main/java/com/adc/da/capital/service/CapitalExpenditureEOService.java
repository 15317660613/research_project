package com.adc.da.capital.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.capital.dao.CapitalExpenditureEODao;
import com.adc.da.capital.entity.CapitalExpenditureEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_CAPITAL_EXPENDITURE CapitalExpenditureEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("capitalExpenditureEOService")
@Transactional(value = "transactionManager",
    readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CapitalExpenditureEOService extends BaseService<CapitalExpenditureEO, String> {

    @Autowired
    private CapitalExpenditureEODao dao;

    public CapitalExpenditureEODao getDao() {
        return dao;
    }

    /**
     * 批量新增
     * @param list
     * @return
     */
    public int insertSelectiveAll(List<CapitalExpenditureEO> list) {
        return dao.insertSelectiveAll(list);
    }

}
