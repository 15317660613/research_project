package com.adc.da.smallprogram.vo;

import com.adc.da.smallprogram.entity.ScheduleDetailEO;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ScheduleHourVO implements Serializable , Comparable<ScheduleHourVO>{
    /**
     * id
     */
    private String id;
    /**
     * 日程内容
     */
    private String scheduleContent;
    /**
     * 日程事件
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date scheduleDate;
    /**
     * 日程精确时间  0上午 1下午
     */
    private String scheduleHour;
    /**
     *
     */
    private String delFlag;
    /**
     *
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     *
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 日程类型
     */
    private String scheduleTypeId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 新增描述字段
     * SCHEDULE_DESC
     */
    private String scheduleDesc;

    private  List<ScheduleDetailEO> scheduleDetailEOList  = new ArrayList<>();

    private int updateFlag;

    private String  updateUserName;

    private String  updateUserId;



    @Override
    public int compareTo(ScheduleHourVO o) {
        return this.scheduleDate.compareTo(o.getScheduleDate());
    }
}
