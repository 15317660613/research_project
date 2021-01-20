package com.adc.da.smallprogram.dao;


import com.adc.da.base.dao.BaseDao;
import com.adc.da.smallprogram.entity.ScheduleHourEO;
import com.adc.da.smallprogram.vo.ScheduleHourVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_HOUR TsScheduleHourEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-03-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ScheduleHourEODao extends BaseDao<ScheduleHourEO> {

   void deleteScheduleById(@Param("scheduleId") String scheduleId);

//   List<ScheduleHourVO> queryListByUserIdTimeBetween(@Param("userId") String userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

   List<ScheduleHourEO> queryListByUserIdTimeBetween(@Param("userId") String userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
   void resetUpdateFlagByIds(@Param("list") List<String> list);

   List<ScheduleHourEO> selectByUserIdAndScheduleDate(@Param("userId") String userId,@Param("scheduleDate") Date scheduleDate);
}
