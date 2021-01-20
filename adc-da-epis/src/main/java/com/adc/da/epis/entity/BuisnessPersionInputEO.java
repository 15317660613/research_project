package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>BUISNESS_PERSION_INPUT BuisnessPersionInputEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuisnessPersionInputEO extends BaseEntity {

    private String humaninputId;
    private String projectId;
    private Integer year;
    private Integer week;
    private Float inputNumber;
    private Float adminInputNumber;
    private Float updateInput;
    private Float opationsInputNumber;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creattime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatetime;
    private String admin;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>humaninputId -> humaninput_id</li>
     * <li>projectId -> project_id</li>
     * <li>year -> year</li>
     * <li>week -> week</li>
     * <li>inputNumber -> input_number</li>
     * <li>adminInputNumber -> admin_input_number</li>
     * <li>updateInput -> update_input</li>
     * <li>opationsInputNumber -> opations_input_number</li>
     * <li>creattime -> creattime</li>
     * <li>updatetime -> updatetime</li>
     * <li>admin -> admin</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "humaninputId": return "humaninput_id";
            case "projectId": return "project_id";
            case "year": return "year";
            case "week": return "week";
            case "inputNumber": return "input_number";
            case "adminInputNumber": return "admin_input_number";
            case "updateInput": return "update_input";
            case "opationsInputNumber": return "opations_input_number";
            case "creattime": return "creattime";
            case "updatetime": return "updatetime";
            case "admin": return "admin";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>humaninput_id -> humaninputId</li>
     * <li>project_id -> projectId</li>
     * <li>year -> year</li>
     * <li>week -> week</li>
     * <li>input_number -> inputNumber</li>
     * <li>admin_input_number -> adminInputNumber</li>
     * <li>update_input -> updateInput</li>
     * <li>opations_input_number -> opationsInputNumber</li>
     * <li>creattime -> creattime</li>
     * <li>updatetime -> updatetime</li>
     * <li>admin -> admin</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "humaninput_id": return "humaninputId";
            case "project_id": return "projectId";
            case "year": return "year";
            case "week": return "week";
            case "input_number": return "inputNumber";
            case "admin_input_number": return "adminInputNumber";
            case "update_input": return "updateInput";
            case "opations_input_number": return "opationsInputNumber";
            case "creattime": return "creattime";
            case "updatetime": return "updatetime";
            case "admin": return "admin";
            default: return null;
        }
    }
    
    /**  **/
    public String getHumaninputId() {
        return this.humaninputId;
    }

    /**  **/
    public void setHumaninputId(String humaninputId) {
        this.humaninputId = humaninputId;
    }

    /**  **/
    public String getProjectId() {
        return this.projectId;
    }

    /**  **/
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**  **/
    public Integer getYear() {
        return this.year;
    }

    /**  **/
    public void setYear(Integer year) {
        this.year = year;
    }

    /**  **/
    public Integer getWeek() {
        return this.week;
    }

    /**  **/
    public void setWeek(Integer week) {
        this.week = week;
    }

    /**  **/
    public Float getInputNumber() {
        return this.inputNumber;
    }

    /**  **/
    public void setInputNumber(Float inputNumber) {
        this.inputNumber = inputNumber;
    }

    /**  **/
    public Float getAdminInputNumber() {
        return this.adminInputNumber;
    }

    /**  **/
    public void setAdminInputNumber(Float adminInputNumber) {
        this.adminInputNumber = adminInputNumber;
    }

    /**  **/
    public Float getUpdateInput() {
        return this.updateInput;
    }

    /**  **/
    public void setUpdateInput(Float updateInput) {
        this.updateInput = updateInput;
    }

    /**  **/
    public Float getOpationsInputNumber() {
        return this.opationsInputNumber;
    }

    /**  **/
    public void setOpationsInputNumber(Float opationsInputNumber) {
        this.opationsInputNumber = opationsInputNumber;
    }

    /**  **/
    public Date getCreattime() {
        return this.creattime;
    }

    /**  **/
    public void setCreattime(Date creattime) {
        this.creattime = creattime;
    }

    /**  **/
    public Date getUpdatetime() {
        return this.updatetime;
    }

    /**  **/
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**  **/
    public String getAdmin() {
        return this.admin;
    }

    /**  **/
    public void setAdmin(String admin) {
        this.admin = admin;
    }

}
