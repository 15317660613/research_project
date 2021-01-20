package com.adc.da.smallprogram.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>TS_SCHEDULE_HOUR TsScheduleHourEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-03-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScheduleHourEO extends BaseEntity {

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

    private int updateFlag;

    private String  updateUserName;

    private String  updateUserId;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>scheduleContent -> schedule_content</li>
     * <li>scheduleDate -> schedule_date</li>
     * <li>scheduleHour -> schedule_hour</li>
     * <li>delFlag -> del_flag</li>
     * <li>createTime -> create_time</li>
     * <li>updateTime -> update_time</li>
     * <li>scheduleTypeId -> schedule_type_id</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) return null;
        switch (fieldName) {
            case "id": return "id";
            case "scheduleContent": return "schedule_content";
            case "scheduleDate": return "schedule_date";
            case "scheduleHour": return "schedule_hour";
            case "delFlag": return "del_flag";
            case "createTime": return "create_time";
            case "updateTime": return "update_time";
            case "scheduleTypeId": return "schedule_type_id";
            case "userId" : return "user_id";
            case "scheduleDesc" : return "schedule_desc";
            case "updateFlag" : return "update_flag";
            case "updateUserId" : return "update_user_id";
            case "updateUserName" : return "update_user_name";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>schedule_content -> scheduleContent</li>
     * <li>schedule_date -> scheduleDate</li>
     * <li>schedule_hour -> scheduleHour</li>
     * <li>del_flag -> delFlag</li>
     * <li>create_time -> createTime</li>
     * <li>update_time -> updateTime</li>
     * <li>schedule_type_id -> scheduleTypeId</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) return null;
        switch (columnName) {
            case "id": return "id";
            case "schedule_content": return "scheduleContent";
            case "schedule_date": return "scheduleDate";
            case "schedule_hour": return "scheduleHour";
            case "del_flag": return "delFlag";
            case "create_time": return "createTime";
            case "update_time": return "updateTime";
            case "schedule_type_id": return "scheduleTypeId";
            case "user_id" : return "userId";
            case "schedule_desc" : return "scheduleDesc";
            case "update_flag" : return "updateFlag";
            case "update_user_id" : return "updateUserId";
            case "update_user_name" : return "updateUserName";
            default: return null;
        }
    }

    /**
     * get
     */
    public String getId() {
        return id;
    }

    /**
     * set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * get
     */
    public String getScheduleContent() {
        return scheduleContent;
    }

    /**
     * set
     */
    public void setScheduleContent(String scheduleContent) {
        this.scheduleContent = scheduleContent;
    }
    /**
     * get
     */
    public Date getScheduleDate() {
        return scheduleDate;
    }

    /**
     * set
     */
    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
    /**
     * get
     */
    public String getScheduleHour() {
        return scheduleHour;
    }

    /**
     * set
     */
    public void setScheduleHour(String scheduleHour) {
        this.scheduleHour = scheduleHour;
    }
    /**
     * get
     */
    public String getDelFlag() {
        return delFlag;
    }

    /**
     * set
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    /**
     * get
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     * get
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * set
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     * get
     */
    public String getScheduleTypeId() {
        return scheduleTypeId;
    }

    /**
     * set
     */
    public void setScheduleTypeId(String scheduleTypeId) {
        this.scheduleTypeId = scheduleTypeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getScheduleDesc() {
        return scheduleDesc;
    }

    public void setScheduleDesc(String scheduleDesc) {
        this.scheduleDesc = scheduleDesc;
    }

    public int getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(int updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }
}
