package com.adc.da.smallprogram.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.smallprogram.entity.ScheduleMeetEO;
import com.adc.da.smallprogram.page.MeetPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_MEET ScheduleMeetEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ScheduleMeetEODao extends BaseDao<ScheduleMeetEO> {

     List<ScheduleMeetEO> selectByReceiveUserId(@Param("receiveUserId") String receiveUserId);

     List<ScheduleMeetEO> queryByPageWithReceiveUserId(MeetPage page);

     Integer queryCountByPageWithReceiveUserId(MeetPage page);

     ScheduleMeetEO selectById(String id);


     Integer queryCountByPageAll(MeetPage page);

     List<ScheduleMeetEO>  queryByPageAll(MeetPage page);

     Integer queryCountByPageAdmin(MeetPage page);

     List<ScheduleMeetEO>  queryByPageAdmin(MeetPage page);

}
