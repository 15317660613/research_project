package com.adc.da.event.service;

import com.adc.da.event.page.MyEventEOPage;
import com.adc.da.event.page.SearchPage;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.event.dao.EventEODao;
import com.adc.da.event.entity.EventEO;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>WR_EVENT EventEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-12 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("eventEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EventEOService extends BaseService<EventEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(EventEOService.class);
    @Autowired
    private EventEODao dao;

    public EventEODao getDao() {
        return dao;
    }

    public List<EventEO> selectByCreateUserId(String createUserId){
        return dao.selectByCreateUserId(createUserId);
    }

    public List<EventEO> queryByMyPage(MyEventEOPage page){
        return dao.queryByMyPage(page);
    }
    public void delByPrimaryKey(String eventId){
        dao.delByPrimaryKey(eventId);
    }

    public void setSendFlagById(String eventId){
        dao.setSendFlagById(eventId);
    }


    public List<EventEO> querySearchPage(SearchPage page){
        Integer rowCount = this.queryBySearchPageCount(page);
        page.getPager().setRowCount(rowCount);
        return dao.queryBySearchPage(page);
    }

    public Integer queryBySearchPageCount(SearchPage page){
        return dao.queryBySearchPageCount(page);
    }


    public List<EventEO> queryToDoBySearchPage(SearchPage page){
        return dao.queryToDoBySearchPage(page);
    }

    public List<EventEO> queryDoneBySearchPage(SearchPage page){
        return dao.queryDoneBySearchPage(page);
    }


    public Integer selectCountByEventName(String eventName,String queryFlag){
        return dao.selectCountByEventName(eventName,queryFlag);
    }
}
