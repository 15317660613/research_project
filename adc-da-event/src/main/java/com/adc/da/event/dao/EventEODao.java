package com.adc.da.event.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.event.entity.EventEO;
import com.adc.da.event.page.MyEventEOPage;
import com.adc.da.event.page.SearchPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>WR_EVENT EventEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-12 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface EventEODao extends BaseDao<EventEO> {
    List<EventEO> selectByCreateUserId(@Param("value") String createUserId);

    List<EventEO> queryByMyPage(MyEventEOPage page);

    void delByPrimaryKey(@Param("value")String eventId);

    void setSendFlagById(@Param("value")String eventId);

    List<EventEO> queryBySearchPage(SearchPage page);

    List<EventEO> queryToDoBySearchPage(SearchPage page);

    List<EventEO> queryDoneBySearchPage(SearchPage page);

    Integer queryBySearchPageCount(SearchPage page);

    Integer selectCountByEventName(@Param("eventName")String eventName,@Param("queryFlag")String queryFlag);
}
