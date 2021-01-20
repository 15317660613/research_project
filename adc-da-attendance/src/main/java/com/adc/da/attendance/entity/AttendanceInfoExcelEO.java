package com.adc.da.attendance.entity;

import lombok.Data;

import java.util.Map;

@Data
public class AttendanceInfoExcelEO  {
    //工号
    private String workId;
    //姓名
    private String humanName;
    //打卡日期 第一个string是日期
    private Map<String,TimeExcptionDTO> attendanceTime;;

}
