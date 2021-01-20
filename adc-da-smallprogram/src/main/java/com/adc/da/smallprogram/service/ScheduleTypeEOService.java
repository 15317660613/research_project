package com.adc.da.smallprogram.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.ScheduleTypeEODao;
import com.adc.da.smallprogram.entity.ScheduleTypeEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_TYPE TsScheduleTypeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-03-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("tsScheduleTypeEOService")
@Transactional(rollbackFor = Throwable.class)
public class ScheduleTypeEOService extends BaseService<ScheduleTypeEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleTypeEOService.class);

    @Autowired
    private ScheduleTypeEODao dao;

    public ScheduleTypeEODao getDao() {
        return dao;
    }

}
