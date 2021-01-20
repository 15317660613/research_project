package com.adc.da.smallprogram.dao;

import com.adc.da.smallprogram.vo.ScheduleHourReqVO;
import com.adc.da.smallprogram.vo.ScheduleHourResVO;

import java.util.List;

public interface ScheduleHourDao {
    List<ScheduleHourResVO> queryByList(ScheduleHourReqVO scheduleHourReqVO);

}
