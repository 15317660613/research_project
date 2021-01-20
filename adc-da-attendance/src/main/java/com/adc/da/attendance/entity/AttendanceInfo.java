package com.adc.da.attendance.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

@Data
@ToString
public class AttendanceInfo {

    //部门
    private String dept;
    //工号
    private String workId;
    //员工姓名
    private String humanName;
    //考勤信息
    private Map<Date,String> attendanceTimeInfo;
    //是否是异常数据 0 为 正常数据 1 为异常数据数据
    private Map<Date,String> isExcption;


}
