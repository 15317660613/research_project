package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>BUISNESS_PERSION_INFO BuisnessPersionInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuisnessPersionInfoEO extends BaseEntity {

    private String personnelId;
    private String personnelName;
    private String personnelNative;
    private String personnelHrsf;
    private String personnelUschool;
    private Integer personnelUschooltype;
    private String personnelMschool;
    private Integer personnelMschooltype;
    private Integer personnelPlandscape;
    private String personnelRtime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date personnelStarttime;
    private Integer personnelType;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date personnelEndtime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creationtime;
    private String creationname;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatetime;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>personnelId -> personnel_id</li>
     * <li>personnelName -> personnel_name</li>
     * <li>personnelNative -> personnel_native</li>
     * <li>personnelHrsf -> personnel_hrsf</li>
     * <li>personnelUschool -> personnel_uschool</li>
     * <li>personnelUschooltype -> personnel_uschooltype</li>
     * <li>personnelMschool -> personnel_mschool</li>
     * <li>personnelMschooltype -> personnel_mschooltype</li>
     * <li>personnelPlandscape -> personnel_plandscape</li>
     * <li>personnelRtime -> personnel_rtime</li>
     * <li>personnelStarttime -> personnel_starttime</li>
     * <li>personnelType -> personnel_type</li>
     * <li>personnelEndtime -> personnel_endtime</li>
     * <li>creationtime -> creationtime</li>
     * <li>creationname -> creationname</li>
     * <li>updatetime -> updatetime</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "personnelId": return "personnel_id";
            case "personnelName": return "personnel_name";
            case "personnelNative": return "personnel_native";
            case "personnelHrsf": return "personnel_hrsf";
            case "personnelUschool": return "personnel_uschool";
            case "personnelUschooltype": return "personnel_uschooltype";
            case "personnelMschool": return "personnel_mschool";
            case "personnelMschooltype": return "personnel_mschooltype";
            case "personnelPlandscape": return "personnel_plandscape";
            case "personnelRtime": return "personnel_rtime";
            case "personnelStarttime": return "personnel_starttime";
            case "personnelType": return "personnel_type";
            case "personnelEndtime": return "personnel_endtime";
            case "creationtime": return "creationtime";
            case "creationname": return "creationname";
            case "updatetime": return "updatetime";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>personnel_id -> personnelId</li>
     * <li>personnel_name -> personnelName</li>
     * <li>personnel_native -> personnelNative</li>
     * <li>personnel_hrsf -> personnelHrsf</li>
     * <li>personnel_uschool -> personnelUschool</li>
     * <li>personnel_uschooltype -> personnelUschooltype</li>
     * <li>personnel_mschool -> personnelMschool</li>
     * <li>personnel_mschooltype -> personnelMschooltype</li>
     * <li>personnel_plandscape -> personnelPlandscape</li>
     * <li>personnel_rtime -> personnelRtime</li>
     * <li>personnel_starttime -> personnelStarttime</li>
     * <li>personnel_type -> personnelType</li>
     * <li>personnel_endtime -> personnelEndtime</li>
     * <li>creationtime -> creationtime</li>
     * <li>creationname -> creationname</li>
     * <li>updatetime -> updatetime</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "personnel_id": return "personnelId";
            case "personnel_name": return "personnelName";
            case "personnel_native": return "personnelNative";
            case "personnel_hrsf": return "personnelHrsf";
            case "personnel_uschool": return "personnelUschool";
            case "personnel_uschooltype": return "personnelUschooltype";
            case "personnel_mschool": return "personnelMschool";
            case "personnel_mschooltype": return "personnelMschooltype";
            case "personnel_plandscape": return "personnelPlandscape";
            case "personnel_rtime": return "personnelRtime";
            case "personnel_starttime": return "personnelStarttime";
            case "personnel_type": return "personnelType";
            case "personnel_endtime": return "personnelEndtime";
            case "creationtime": return "creationtime";
            case "creationname": return "creationname";
            case "updatetime": return "updatetime";
            default: return null;
        }
    }
    
    /**  **/
    public String getPersonnelId() {
        return this.personnelId;
    }

    /**  **/
    public void setPersonnelId(String personnelId) {
        this.personnelId = personnelId;
    }

    /**  **/
    public String getPersonnelName() {
        return this.personnelName;
    }

    /**  **/
    public void setPersonnelName(String personnelName) {
        this.personnelName = personnelName;
    }

    /**  **/
    public String getPersonnelNative() {
        return this.personnelNative;
    }

    /**  **/
    public void setPersonnelNative(String personnelNative) {
        this.personnelNative = personnelNative;
    }

    /**  **/
    public String getPersonnelHrsf() {
        return this.personnelHrsf;
    }

    /**  **/
    public void setPersonnelHrsf(String personnelHrsf) {
        this.personnelHrsf = personnelHrsf;
    }

    /**  **/
    public String getPersonnelUschool() {
        return this.personnelUschool;
    }

    /**  **/
    public void setPersonnelUschool(String personnelUschool) {
        this.personnelUschool = personnelUschool;
    }

    /**  **/
    public Integer getPersonnelUschooltype() {
        return this.personnelUschooltype;
    }

    /**  **/
    public void setPersonnelUschooltype(Integer personnelUschooltype) {
        this.personnelUschooltype = personnelUschooltype;
    }

    /**  **/
    public String getPersonnelMschool() {
        return this.personnelMschool;
    }

    /**  **/
    public void setPersonnelMschool(String personnelMschool) {
        this.personnelMschool = personnelMschool;
    }

    /**  **/
    public Integer getPersonnelMschooltype() {
        return this.personnelMschooltype;
    }

    /**  **/
    public void setPersonnelMschooltype(Integer personnelMschooltype) {
        this.personnelMschooltype = personnelMschooltype;
    }

    /**  **/
    public Integer getPersonnelPlandscape() {
        return this.personnelPlandscape;
    }

    /**  **/
    public void setPersonnelPlandscape(Integer personnelPlandscape) {
        this.personnelPlandscape = personnelPlandscape;
    }

    /**  **/
    public String getPersonnelRtime() {
        return this.personnelRtime;
    }

    /**  **/
    public void setPersonnelRtime(String personnelRtime) {
        this.personnelRtime = personnelRtime;
    }

    /**  **/
    public Date getPersonnelStarttime() {
        return this.personnelStarttime;
    }

    /**  **/
    public void setPersonnelStarttime(Date personnelStarttime) {
        this.personnelStarttime = personnelStarttime;
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
    public Date getPersonnelEndtime() {
        return this.personnelEndtime;
    }

    /**  **/
    public void setPersonnelEndtime(Date personnelEndtime) {
        this.personnelEndtime = personnelEndtime;
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
