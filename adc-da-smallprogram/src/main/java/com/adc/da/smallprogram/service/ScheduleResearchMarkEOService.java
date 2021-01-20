package com.adc.da.smallprogram.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.ScheduleResearchMarkEODao;
import com.adc.da.smallprogram.entity.ScheduleResearchMarkEO;


/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_RESEARCH_MARK ScheduleResearchMarkEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-05-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scheduleResearchMarkEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScheduleResearchMarkEOService extends BaseService<ScheduleResearchMarkEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleResearchMarkEOService.class);

    @Autowired
    private ScheduleResearchMarkEODao dao;

    public ScheduleResearchMarkEODao getDao() {
        return dao;
    }

    public ScheduleResearchMarkEO updateTopOrCollect(ScheduleResearchMarkEO researchMarkEO){
        if(null!=researchMarkEO.getTop()&&researchMarkEO.getTop() == 1){
            dao.resetTop(researchMarkEO);
        }
        dao.updateTopOrCollect(researchMarkEO);
        return researchMarkEO;
    }
}
