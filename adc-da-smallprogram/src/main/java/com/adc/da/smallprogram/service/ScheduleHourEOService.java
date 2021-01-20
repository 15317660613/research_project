package com.adc.da.smallprogram.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.ScheduleHourEODao;
import com.adc.da.smallprogram.entity.ScheduleHourEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_HOUR TsScheduleHourEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-03-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("tsScheduleHourEOService")
@Transactional(rollbackFor = Throwable.class)
public class ScheduleHourEOService extends BaseService<ScheduleHourEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleHourEOService.class);

    @Autowired
    private ScheduleHourEODao dao;

    public ScheduleHourEODao getDao() {
        return dao;
    }

}
