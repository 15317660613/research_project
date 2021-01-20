package com.adc.da.event.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.event.dao.EventFileEODao;
import com.adc.da.event.entity.EventFileEO;


/**
 *
 * <br>
 * <b>功能：</b>WR_EVENT_FILE EventFileEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-12 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("eventFileEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EventFileEOService extends BaseService<EventFileEO, Void> {

    private static final Logger logger = LoggerFactory.getLogger(EventFileEOService.class);

    @Autowired
    private EventFileEODao dao;

    public EventFileEODao getDao() {
        return dao;
    }

    public String selectByEventId(String eventId){
       return dao.selectByEventId(eventId);
    }

    public String selectByFileId(String fileId){
        return dao.selectByFileId(fileId);
    }

}
