package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.dao.KpiDeliverableEODao;
import com.adc.da.research.entity.KpiDeliverableEO;
import com.adc.da.util.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_KPI_DELIVERABLE KpiDeliverableEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-23 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("kpiDeliverableEOService")
@Transactional(value = "transactionManager",
    readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class KpiDeliverableEOService extends BaseService<KpiDeliverableEO, String> {

    @Autowired
    private KpiDeliverableEODao dao;

    public KpiDeliverableEODao getDao() {
        return dao;
    }

    @Override
    public int insertSelective(KpiDeliverableEO t) {
        t.setId(UUID.randomUUID10());
        return this.getDao().insertSelective(t);
    }
}
