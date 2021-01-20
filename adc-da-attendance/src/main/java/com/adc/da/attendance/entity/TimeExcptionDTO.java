package com.adc.da.attendance.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TimeExcptionDTO {
    //打卡记录
    private String time;
    //如果是1则为异常数据 如果是0则为正常数据
    private String isException;
}
