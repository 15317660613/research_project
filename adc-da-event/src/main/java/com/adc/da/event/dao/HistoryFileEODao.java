package com.adc.da.event.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.event.entity.EventEO;
import com.adc.da.event.entity.HistoryFileEO;
import com.adc.da.event.page.MyEventEOPage;
import com.adc.da.event.page.MyHistoryFileEOPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>WR_HISTORY_FILE HistoryFileEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface HistoryFileEODao extends BaseDao<HistoryFileEO> {
    List<HistoryFileEO> queryByMyPage(MyHistoryFileEOPage page);
}
