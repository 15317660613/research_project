package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>BUISNESS_PERSION_EDUCATE BuisnessPersionEducateEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuisnessPersionEducateEO extends BaseEntity {

    private String id;
    private String applypersonnelId;
    private String trainingprogrmId;
    private Integer personnelType;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;
    private String creationname;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatetime;
    private String actualpersonnelId;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>applypersonnelId -> applypersonnel_id</li>
     * <li>trainingprogrmId -> trainingprogrm_id</li>
     * <li>personnelType -> personnel_type</li>
     * <li>creationtime -> creationtime</li>
     * <li>creationname -> creationname</li>
     * <li>updatetime -> updatetime</li>
     * <li>actualpersonnelId -> actualpersonnel_id</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "applypersonnelId": return "applypersonnel_id";
            case "trainingprogrmId": return "trainingprogrm_id";
            case "personnelType": return "personnel_type";
            case "creationtime": return "creationtime";
            case "creationname": return "creationname";
            case "updatetime": return "updatetime";
            case "actualpersonnelId": return "actualpersonnel_id";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>applypersonnel_id -> applypersonnelId</li>
     * <li>trainingprogrm_id -> trainingprogrmId</li>
     * <li>personnel_type -> personnelType</li>
     * <li>creationtime -> creationtime</li>
     * <li>creationname -> creationname</li>
     * <li>updatetime -> updatetime</li>
     * <li>actualpersonnel_id -> actualpersonnelId</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "applypersonnel_id": return "applypersonnelId";
            case "trainingprogrm_id": return "trainingprogrmId";
            case "personnel_type": return "personnelType";
            case "creationtime": return "creationtime";
            case "creationname": return "creationname";
            case "updatetime": return "updatetime";
            case "actualpersonnel_id": return "actualpersonnelId";
            default: return null;
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
    public String getApplypersonnelId() {
        return this.applypersonnelId;
    }

    /**  **/
    public void setApplypersonnelId(String applypersonnelId) {
        this.applypersonnelId = applypersonnelId;
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
    public Integer getPersonnelType() {
        return this.personnelType;
    }

    /**  **/
    public void setPersonnelType(Integer personnelType) {
        this.personnelType = personnelType;
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

    /**  **/
    public String getActualpersonnelId() {
        return this.actualpersonnelId;
    }

    /**  **/
    public void setActualpersonnelId(String actualpersonnelId) {
        this.actualpersonnelId = actualpersonnelId;
    }

}
