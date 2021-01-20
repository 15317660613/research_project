package com.adc.da.attendance.dao;

import com.adc.da.attendance.entity.AttendanceEO;
import com.adc.da.base.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>ST_ATTENDANCE AttendanceEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-12-13 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface AttendanceEODao extends BaseDao<AttendanceEO> {

    /**
     * @param list
     * @return
     */
    Integer insertSelectiveAll(@Param("list") List<AttendanceEO> list);

    /**
     * @param beginDate
     * @param endDate
     * @return
     */
    Integer deleteByTimeArea(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

}
