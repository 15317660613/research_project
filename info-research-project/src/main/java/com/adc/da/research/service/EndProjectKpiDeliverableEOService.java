package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.dao.EndProjectKpiDeliverableEODao;
import com.adc.da.research.entity.EndProjectKpiDeliverableEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 * <b>功能：</b>RS_END_PROJECT_KPI_DELIVERABLE EndProjectKpiDeliverableEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("endProjectKpiDeliverableEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EndProjectKpiDeliverableEOService extends BaseService<EndProjectKpiDeliverableEO, String> {

    @Autowired
    private EndProjectKpiDeliverableEODao dao;

    public EndProjectKpiDeliverableEODao getDao() {
        return dao;
    }

}
