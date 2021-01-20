package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>BUISNESS_EDUCATION BuisnessEducationEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuisnessEducationEO extends BaseEntity {

    private String trainingprogrmId;
    private Integer trainingprogrmHours;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date trainingprogrmTime;
    private String trainingprogrmName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;
    private String creationname;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>trainingprogrmId -> trainingprogrm_id</li>
     * <li>trainingprogrmHours -> trainingprogrm_hours</li>
     * <li>trainingprogrmTime -> trainingprogrm_time</li>
     * <li>trainingprogrmName -> trainingprogrm_name</li>
     * <li>creationtime -> creationtime</li>
     * <li>creationname -> creationname</li>
     * <li>updatetime -> updatetime</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "trainingprogrmId": return "trainingprogrm_id";
            case "trainingprogrmHours": return "trainingprogrm_hours";
            case "trainingprogrmTime": return "trainingprogrm_time";
            case "trainingprogrmName": return "trainingprogrm_name";
            case "creationtime": return "creationtime";
            case "creationname": return "creationname";
            case "updatetime": return "updatetime";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>trainingprogrm_id -> trainingprogrmId</li>
     * <li>trainingprogrm_hours -> trainingprogrmHours</li>
     * <li>trainingprogrm_time -> trainingprogrmTime</li>
     * <li>trainingprogrm_name -> trainingprogrmName</li>
     * <li>creationtime -> creationtime</li>
     * <li>creationname -> creationname</li>
     * <li>updatetime -> updatetime</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "trainingprogrm_id": return "trainingprogrmId";
            case "trainingprogrm_hours": return "trainingprogrmHours";
            case "trainingprogrm_time": return "trainingprogrmTime";
            case "trainingprogrm_name": return "trainingprogrmName";
            case "creationtime": return "creationtime";
            case "creationname": return "creationname";
            case "updatetime": return "updatetime";
            default: return null;
        }
    }
    
    /**  **/
    public String getTrainingprogrmId() {
        return this.trainingprogrmId;
    }

    /**  **/
    public void setTrainingprogrmId(String trainingprogrmId) {
        this.trainingprogrmId = trainingprogrmId;
    }

    /**  **/
    public Integer getTrainingprogrmHours() {
        return this.trainingprogrmHours;
    }

    /**  **/
    public void setTrainingprogrmHours(Integer trainingprogrmHours) {
        this.trainingprogrmHours = trainingprogrmHours;
    }

    /**  **/
    public Date getTrainingprogrmTime() {
        return this.trainingprogrmTime;
    }

    /**  **/
    public void setTrainingprogrmTime(Date trainingprogrmTime) {
        this.trainingprogrmTime = trainingprogrmTime;
    }

    /**  **/
    public String getTrainingprogrmName() {
        return this.trainingprogrmName;
    }

    /**  **/
    public void setTrainingprogrmName(String trainingprogrmName) {
        this.trainingprogrmName = trainingprogrmName;
    }

    /**  **/
    public Date getCreationtime() {
        return this.creationtime;
    }

    /**  **/
    public void setCreationtime(Date creationtime) {
        this.creationtime = creationtime;
    }

    /**  **/
    public String getCreationname() {
        return this.creationname;
    }

    /**  **/
    public void setCreationname(String creationname) {
        this.creationname = creationname;
    }

    /**  **/
    public Date getUpdatetime() {
        return this.updatetime;
    }

    /**  **/
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

}
