package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.dao.SummaryEODao;
import com.adc.da.research.entity.SummaryEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_SUMMARY SummaryEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("summaryEOService")
@Transactional(value = "transactionManager",
    readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class SummaryEOService extends BaseService<SummaryEO, String> {

    @Autowired
    private SummaryEODao dao;

    public SummaryEODao getDao() {
        return dao;
    }

}
