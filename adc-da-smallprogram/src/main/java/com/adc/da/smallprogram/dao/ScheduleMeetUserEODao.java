package com.adc.da.smallprogram.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.smallprogram.entity.ScheduleMeetUserEO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_MEET_USER ScheduleMeetUserEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ScheduleMeetUserEODao extends BaseDao<ScheduleMeetUserEO> {
    int insertList(List<ScheduleMeetUserEO> list);

    void deleteByMeetId(String id);
    List<ScheduleMeetUserEO> selectByMeetId(String meetId);
    void updateStatusByMeetId(String meetId);

    int updateByMeetIdAndUserIdSelective(ScheduleMeetUserEO scheduleMeetUserEO);

    ScheduleMeetUserEO selectByMeetIdAndUserId(@Param("meetId") String meetId , @Param("userId") String userId);


}
