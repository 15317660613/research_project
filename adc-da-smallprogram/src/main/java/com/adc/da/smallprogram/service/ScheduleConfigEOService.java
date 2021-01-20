package com.adc.da.smallprogram.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.ScheduleConfigEODao;
import com.adc.da.smallprogram.entity.ScheduleConfigEO;


/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_CONFIG ScheduleConfigEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-05-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scheduleConfigEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScheduleConfigEOService extends BaseService<ScheduleConfigEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleConfigEOService.class);

    @Autowired
    private ScheduleConfigEODao dao;

    public ScheduleConfigEODao getDao() {
        return dao;
    }

}
