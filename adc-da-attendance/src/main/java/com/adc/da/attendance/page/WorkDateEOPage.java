package com.adc.da.attendance.page;

import com.adc.da.base.page.BasePage;

import java.util.Date;

/**
 * <b>功能：</b>WORK_DATE WorkDateEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-01-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class WorkDateEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String dept;
    private String deptOperator = "=";
    private String dateTime;
    private String dateTime1;
    private String dateTime2;
    private String dateTimeOperator = "=";
    private String week;
    private String weekOperator = "=";
    private String isWorkDate;
    private String isWorkDateOperator = "=";
    private String isPublicHoliday;
    private String isPublicHolidayOperator = "=";
    private String holidayName;
    private String holidayNameOperator = "=";

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOperator() {
        return this.idOperator;
    }

    public void setIdOperator(String idOperator) {
        this.idOperator = idOperator;
    }

    public String getDept() {
        return this.dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDeptOperator() {
        return this.deptOperator;
    }

    public void setDeptOperator(String deptOperator) {
        this.deptOperator = deptOperator;
    }

    public String getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDateTime1() {
        return this.dateTime1;
    }

    public void setDateTime1(String dateTime1) {
        this.dateTime1 = dateTime1;
    }

    public String getDateTime2() {
        return this.dateTime2;
    }

    public void setDateTime2(String dateTime2) {
        this.dateTime2 = dateTime2;
    }

    public String getDateTimeOperator() {
        return this.dateTimeOperator;
    }

    public void setDateTimeOperator(String dateTimeOperator) {
        this.dateTimeOperator = dateTimeOperator;
    }

    public String getWeek() {
        return this.week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWeekOperator() {
        return this.weekOperator;
    }

    public void setWeekOperator(String weekOperator) {
        this.weekOperator = weekOperator;
    }

    public String getIsWorkDate() {
        return this.isWorkDate;
    }

    public void setIsWorkDate(String isWorkDate) {
        this.isWorkDate = isWorkDate;
    }

    public String getIsWorkDateOperator() {
        return this.isWorkDateOperator;
    }

    public void setIsWorkDateOperator(String isWorkDateOperator) {
        this.isWorkDateOperator = isWorkDateOperator;
    }

    public String getIsPublicHoliday() {
        return this.isPublicHoliday;
    }

    public void setIsPublicHoliday(String isPublicHoliday) {
        this.isPublicHoliday = isPublicHoliday;
    }

    public String getIsPublicHolidayOperator() {
        return this.isPublicHolidayOperator;
    }

    public void setIsPublicHolidayOperator(String isPublicHolidayOperator) {
        this.isPublicHolidayOperator = isPublicHolidayOperator;
    }

    public String getHolidayName() {
        return this.holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public String getHolidayNameOperator() {
        return this.holidayNameOperator;
    }

    public void setHolidayNameOperator(String holidayNameOperator) {
        this.holidayNameOperator = holidayNameOperator;
    }

}
