package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>BUISNESS_TRAVEL BuisnessTravelEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuisnessTravelEO extends BaseEntity {

    private String travelId;
    private String travelProjectMeetings;
    private String travelCause;
    private String personnalId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date evectiontime;
    private String remarke;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creattime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date update;
    private String admin;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>travelId -> travel_id</li>
     * <li>travelProjectMeetings -> travel_project_meetings</li>
     * <li>travelCause -> travel_cause</li>
     * <li>personnalId -> personnal_id</li>
     * <li>evectiontime -> evectiontime</li>
     * <li>remarke -> remarke</li>
     * <li>creattime -> creattime</li>
     * <li>update -> update</li>
     * <li>admin -> admin</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "travelId": return "travel_id";
            case "travelProjectMeetings": return "travel_project_meetings";
            case "travelCause": return "travel_cause";
            case "personnalId": return "personnal_id";
            case "evectiontime": return "evectiontime";
            case "remarke": return "remarke";
            case "creattime": return "creattime";
            case "update": return "update";
            case "admin": return "admin";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>travel_id -> travelId</li>
     * <li>travel_project_meetings -> travelProjectMeetings</li>
     * <li>travel_cause -> travelCause</li>
     * <li>personnal_id -> personnalId</li>
     * <li>evectiontime -> evectiontime</li>
     * <li>remarke -> remarke</li>
     * <li>creattime -> creattime</li>
     * <li>update -> update</li>
     * <li>admin -> admin</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "travel_id": return "travelId";
            case "travel_project_meetings": return "travelProjectMeetings";
            case "travel_cause": return "travelCause";
            case "personnal_id": return "personnalId";
            case "evectiontime": return "evectiontime";
            case "remarke": return "remarke";
            case "creattime": return "creattime";
            case "update": return "update";
            case "admin": return "admin";
            default: return null;
        }
    }
    
    /**  **/
    public String getTravelId() {
        return this.travelId;
    }

    /**  **/
    public void setTravelId(String travelId) {
        this.travelId = travelId;
    }

    /**  **/
    public String getTravelProjectMeetings() {
        return this.travelProjectMeetings;
    }

    /**  **/
    public void setTravelProjectMeetings(String travelProjectMeetings) {
        this.travelProjectMeetings = travelProjectMeetings;
    }

    /**  **/
    public String getTravelCause() {
        return this.travelCause;
    }

    /**  **/
    public void setTravelCause(String travelCause) {
        this.travelCause = travelCause;
    }

    /**  **/
    public String getPersonnalId() {
        return this.personnalId;
    }

    /**  **/
    public void setPersonnalId(String personnalId) {
        this.personnalId = personnalId;
    }

    /**  **/
    public Date getEvectiontime() {
        return this.evectiontime;
    }

    /**  **/
    public void setEvectiontime(Date evectiontime) {
        this.evectiontime = evectiontime;
    }

    /**  **/
    public String getRemarke() {
        return this.remarke;
    }

    /**  **/
    public void setRemarke(String remarke) {
        this.remarke = remarke;
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
    public Date getUpdate() {
        return this.update;
    }

    /**  **/
    public void setUpdate(Date update) {
        this.update = update;
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
