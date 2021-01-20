package com.adc.da.event.service;

import com.adc.da.event.dao.EventEODao;
import com.adc.da.event.entity.EventEO;
import com.adc.da.event.page.MyEventEOPage;
import com.adc.da.event.page.MyEventReceiveEOPage;
import com.adc.da.event.page.SearchPage;
import com.adc.da.event.vo.EventProcessVO;
import com.adc.da.event.vo.EventReceiveProcessVO;
import com.adc.da.event.vo.EventReceiveVO;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.event.dao.EventReceiveEODao;
import com.adc.da.event.entity.EventReceiveEO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>WR_EVENT_RECEIVE EventReceiveEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-12 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("eventReceiveEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EventReceiveEOService extends BaseService<EventReceiveEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(EventReceiveEOService.class);
    @Autowired
    private EventReceiveEODao dao;

    @Autowired
    private EventEODao eventEODao;

    public EventReceiveEODao getDao() {
        return dao;
    }

    public List<EventReceiveEO> selectByEventId(String eventId){

        return dao.selectByEventId(eventId);
    }

    public List<EventProcessVO> getEventState(String eventId){
        List<EventProcessVO> eventProcessVOList = new ArrayList<>();
        EventEO eventEO = eventEODao.selectByPrimaryKey(eventId);
        List<EventReceiveEO> eventReceiveEOList =  dao.selectByEventId(eventId);
        if(CollectionUtils.isNotEmpty(eventReceiveEOList)){
            for(EventReceiveEO eventReceiveEO:eventReceiveEOList){
                if(StringUtils.equals(eventReceiveEO.getReceiveUserId(),eventEO.getCreateUserId())){
                    EventProcessVO eventProcessVO = new EventProcessVO();
                    List<EventReceiveProcessVO> eventReceiveProcessVOList = new ArrayList<>();
                    EventReceiveProcessVO eventReceiveProcessVO = new EventReceiveProcessVO();
                    eventReceiveProcessVO.setAssignee(eventReceiveEO.getReceiveUserId());
                    eventReceiveProcessVO.setAssigneeRealName(eventReceiveEO.getReceiveUserName());
                    eventReceiveProcessVO.setDealTime(DateUtils.dateToString(eventReceiveEO.getReceiveTime(),DateUtils.YYYY_MM_DD_HH_MM_SS_EN));
                    eventReceiveProcessVO.setStatus(eventReceiveEO.getState()<1?0:1);
                    eventReceiveProcessVOList.add(eventReceiveProcessVO);
                    eventProcessVO.setStatus(eventReceiveProcessVO.getStatus());
                    eventProcessVO.setEventReceiveProcessVOList(eventReceiveProcessVOList);
                    eventProcessVOList.add(eventProcessVO);
                    eventReceiveEOList.remove(eventReceiveEO);
                    break;
                }
            }
            int tepStatus = 3;
            for(int i=0;i<eventReceiveEOList.size();i++){
                EventReceiveEO eventReceiveEO = eventReceiveEOList.get(i);
                EventProcessVO eventProcessVO = new EventProcessVO();
                List<EventReceiveProcessVO> eventReceiveProcessVOList = new ArrayList<>();
                EventReceiveProcessVO eventReceiveProcessVO = new EventReceiveProcessVO();
                eventReceiveProcessVO.setAssignee(eventReceiveEO.getReceiveUserId());
                eventReceiveProcessVO.setAssigneeRealName(eventReceiveEO.getReceiveUserName());
                eventReceiveProcessVO.setDealTime(DateUtils.dateToString(eventReceiveEO.getReceiveTime(),DateUtils.YYYY_MM_DD_EN));
                eventReceiveProcessVO.setStatus(eventReceiveEO.getState()<3?0:1);
                eventReceiveProcessVOList.add(eventReceiveProcessVO);
                if (eventReceiveProcessVO.getStatus() < tepStatus) {
                    tepStatus = 0;
                }
                for (int j = eventReceiveEOList.size() - 1; j > i; j--) {
                    EventReceiveEO nextEventReceiveEO = eventReceiveEOList.get(j);
                    long diffTime =
                        nextEventReceiveEO.getReceiveTime().getTime() - eventReceiveEO.getReceiveTime().getTime();
                    if (Math.abs(diffTime) <= 1000){
                        EventReceiveProcessVO nextEventReceiveProcessVO = new EventReceiveProcessVO();
                        nextEventReceiveProcessVO.setAssignee(nextEventReceiveEO.getReceiveUserId());
                        nextEventReceiveProcessVO.setAssigneeRealName(nextEventReceiveEO.getReceiveUserName());
                        nextEventReceiveProcessVO.setDealTime(DateUtils.dateToString(nextEventReceiveEO.getReceiveTime(),DateUtils.YYYY_MM_DD_EN));
                        nextEventReceiveProcessVO.setStatus(nextEventReceiveEO.getState()<3?0:1);
                        if (nextEventReceiveProcessVO.getStatus() < tepStatus) {
                            tepStatus = nextEventReceiveProcessVO.getStatus();
                        }
                        eventReceiveProcessVOList.add(nextEventReceiveProcessVO);
                        eventReceiveEOList.remove(j);
                    }
                }
                eventProcessVO.setStatus(tepStatus<3?0:1);
                tepStatus = 3;
                eventProcessVO.setEventReceiveProcessVOList(eventReceiveProcessVOList);
                eventProcessVOList.add(eventProcessVO);
            }
        }
        return eventProcessVOList;
    }





    public List<EventReceiveEO> selectByReceiveUserId(String userId){
        return dao.selectByReceiveUserId(userId);
    }

    public List<EventReceiveEO> queryByMyPage(MyEventReceiveEOPage page){
        Integer rowCount = dao.queryByMyCount(page);
        page.getPager().setRowCount(rowCount);
        return dao.queryByMyPage(page);
    }

    public int selectNumOfState(EventReceiveEO eventReceiveEO){
       return dao.selectNumOfState(eventReceiveEO);
    }

    public void delByPrimaryKey(String id){
        dao.delByPrimaryKey(id);
    }

    public List<EventReceiveEO> querySearchPage(SearchPage page){
        Integer rowCount = this.queryBySearchPageCount(page);
        page.getPager().setRowCount(rowCount);
        return dao.queryBySearchPage(page);
    }

    public Integer queryBySearchPageCount(SearchPage page){
        return dao.queryBySearchPageCount(page);
    }

   public void updateByReceiveUserId( Integer state ,  String eventId, String receiveUserId){
        dao.updateByReceiveUserId(state,eventId,receiveUserId);
   }
    public void delByEventId(@Param("value")String id){
        dao.delByEventId(id);
    }



}
