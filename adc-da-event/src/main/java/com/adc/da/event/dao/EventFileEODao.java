package com.adc.da.event.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.event.entity.EventFileEO;
import org.apache.ibatis.annotations.Param;

/**
 *
 * <br>
 * <b>功能：</b>WR_EVENT_FILE EventFileEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-12 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface EventFileEODao extends BaseDao<EventFileEO> {
   String selectByEventId(@Param("value") String eventId);

   String selectByFileId(@Param("value") String fileId);
}
