package com.adc.da.smallprogram.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.ScheduleDetailEODao;
import com.adc.da.smallprogram.entity.ScheduleDetailEO;

import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_DETAIL ScheduleDetailEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-19 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scheduleDetailEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScheduleDetailEOService extends BaseService<ScheduleDetailEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleDetailEOService.class);

    @Autowired
    private ScheduleDetailEODao dao;

    public ScheduleDetailEODao getDao() {
        return dao;
    }

    public void logicDelete(String id) {
        dao.logicDelete(id);
    }

    public void logicDeleteInBatch(List<String> ids) {
        dao.logicDeleteInBatch(ids);
    }

    List<ScheduleDetailEO> selectByParentId(String id, int detailType) {
        return dao.selectByParentId(id, detailType);
    }

    List<ScheduleDetailEO> selectByParentId(String id, boolean filter) {
        return dao.selectByParentIdNew(id, filter);
    }


}
