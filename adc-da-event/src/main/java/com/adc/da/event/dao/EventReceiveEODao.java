package com.adc.da.event.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.event.entity.EventEO;
import com.adc.da.event.entity.EventReceiveEO;
import com.adc.da.event.page.MyEventEOPage;
import com.adc.da.event.page.MyEventReceiveEOPage;
import com.adc.da.event.page.SearchPage;
import com.adc.da.event.vo.EventReceiveVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>WR_EVENT_RECEIVE EventReceiveEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-12 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface EventReceiveEODao extends BaseDao<EventReceiveEO> {
    List<EventReceiveEO> selectByEventId(@Param("value") String eventId);
    List<EventReceiveEO> selectByReceiveUserId(@Param("value") String userId);

    List<EventReceiveEO> queryByMyPage(MyEventReceiveEOPage page);

    int queryByMyCount(BasePage var1);

    int selectNumOfState(EventReceiveEO eventReceiveEO);

    void delByPrimaryKey(@Param("value")String id);

    List<EventReceiveEO> queryBySearchPage(SearchPage page);

    Integer queryBySearchPageCount(SearchPage page);

    void updateByReceiveUserId(@Param("state") Integer state , @Param("eventId") String eventId, @Param("receiveUserId") String receiveUserId);

    void delByEventId(@Param("value")String id);

}



