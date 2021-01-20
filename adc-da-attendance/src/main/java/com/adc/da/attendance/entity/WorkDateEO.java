package com.adc.da.attendance.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>WORK_DATE WorkDateEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-01-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class WorkDateEO extends BaseEntity {

    private String id;

    private Integer dept;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dateTime;

    private String week;

    private Integer isWorkDate;

    private Integer isPublicHoliday;

    private String holidayName;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>dept -> dept</li>
     * <li>dateTime -> date_time</li>
     * <li>week -> week</li>
     * <li>isWorkDate -> is_work_date</li>
     * <li>isPublicHoliday -> is_public_holiday</li>
     * <li>holidayName -> holiday_name</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {
            return null;
        }
        switch (fieldName) {
            case "id":
                return "id";
            case "dept":
                return "dept";
            case "dateTime":
                return "date_time";
            case "week":
                return "week";
            case "isWorkDate":
                return "is_work_date";
            case "isPublicHoliday":
                return "is_public_holiday";
            case "holidayName":
                return "holiday_name";
            default:
                return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>dept -> dept</li>
     * <li>date_time -> dateTime</li>
     * <li>week -> week</li>
     * <li>is_work_date -> isWorkDate</li>
     * <li>is_public_holiday -> isPublicHoliday</li>
     * <li>holiday_name -> holidayName</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {
            return null;
        }
        switch (columnName) {
            case "id":
                return "id";
            case "dept":
                return "dept";
            case "date_time":
                return "dateTime";
            case "week":
                return "week";
            case "is_work_date":
                return "isWorkDate";
            case "is_public_holiday":
                return "isPublicHoliday";
            case "holiday_name":
                return "holidayName";
            default:
                return null;
        }
    }

    /**  **/
    public String getId() {
        return this.id;
    }

    /**  **/
    public void setId(String id) {
        this.id = id;
    }

    /**  **/
    public Integer getDept() {
        return this.dept;
    }

    /**  **/
    public void setDept(Integer dept) {
        this.dept = dept;
    }

    /**  **/
    public Date getDateTime() {
        return this.dateTime;
    }

    /**  **/
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    /**  **/
    public String getWeek() {
        return this.week;
    }

    /**  **/
    public void setWeek(String week) {
        this.week = week;
    }

    /**  **/
    public Integer getIsWorkDate() {
        return this.isWorkDate;
    }

    /**  **/
    public void setIsWorkDate(Integer isWorkDate) {
        this.isWorkDate = isWorkDate;
    }

    /**  **/
    public Integer getIsPublicHoliday() {
        return this.isPublicHoliday;
    }

    /**  **/
    public void setIsPublicHoliday(Integer isPublicHoliday) {
        this.isPublicHoliday = isPublicHoliday;
    }

    /**  **/
    public String getHolidayName() {
        return this.holidayName;
    }

    /**  **/
    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

}
