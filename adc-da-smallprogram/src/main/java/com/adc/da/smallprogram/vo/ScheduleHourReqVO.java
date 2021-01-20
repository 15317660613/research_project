package com.adc.da.smallprogram.vo;

import java.util.Date;

public class ScheduleHourReqVO {
    /**
     * id
     */
    private String id;
    /**
     * 日程事件
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date scheduleDate;
    /**
     * 日程时间截止 开始
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date scheduleDateStart;
    /**
     * 日程时间截止 结束
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date scheduleDateEnd;
    /**
     * 日程精确时间  0上午 1下午
     */
    private String scheduleHour;

    /**
     * 日程类型
     */
    private String scheduleTypeId;
    /**
     * 用户id
     */
    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Date getScheduleDateStart() {
        return scheduleDateStart;
    }

    public void setScheduleDateStart(Date scheduleDateStart) {
        this.scheduleDateStart = scheduleDateStart;
    }

    public Date getScheduleDateEnd() {
        return scheduleDateEnd;
    }

    public void setScheduleDateEnd(Date scheduleDateEnd) {
        this.scheduleDateEnd = scheduleDateEnd;
    }

    public String getScheduleHour() {
        return scheduleHour;
    }

    public void setScheduleHour(String scheduleHour) {
        this.scheduleHour = scheduleHour;
    }

    public String getScheduleTypeId() {
        return scheduleTypeId;
    }

    public void setScheduleTypeId(String scheduleTypeId) {
        this.scheduleTypeId = scheduleTypeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
