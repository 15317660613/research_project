package com.adc.da.attendance.dao;

import com.adc.da.attendance.entity.AttendanceInfo;
import com.adc.da.attendance.entity.AttendanceInfoEO;
import com.adc.da.attendance.entity.ExportDateExcleDTO;
import com.adc.da.attendance.entity.ExportExcleDTO;
import com.adc.da.base.dao.BaseDao;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>ATTENDANCE_INFO AttendanceInfoEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-01-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface MAttendanceInfoEODao  {

    int insertList(List<AttendanceInfoEO>  attendanceInfo);

    List<AttendanceInfoEO> selectAllList( ExportDateExcleDTO exportDateExcleDTO);

    int updateByDate( ExportDateExcleDTO exportDateExcleDTO);

    int deleteByDate( ExportDateExcleDTO exportDateExcleDTO);

    int existsAttendanceInfo(AttendanceInfoEO attendanceInfoEO);
}
