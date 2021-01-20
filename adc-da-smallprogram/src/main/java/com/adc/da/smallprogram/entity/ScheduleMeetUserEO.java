package com.adc.da.smallprogram.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>TS_SCHEDULE_MEET_USER ScheduleMeetUserEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScheduleMeetUserEO extends BaseEntity {

    private String id;
    private String receiveUserId;
    private String receiveUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;
    private Integer top;
    private Integer collected;
    //高层领导 status 1000
    private Integer status;
    private String meetId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date finishedTime;
    private String extInfo;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id_</li>
     * <li>receiveUserId -> receive_user_id</li>
     * <li>receiveUserName -> receive_user_name</li>
     * <li>receiveTime -> receive_time</li>
     * <li>top -> top</li>
     * <li>collected -> collected</li>
     * <li>status -> status</li>
     * <li>meetId -> meet_id</li>
     * <li>finishedTime -> finished_time</li>
     * <li>extInfo -> ext_info</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>extInfo5 -> ext_info5</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id_";
            case "receiveUserId": return "receive_user_id";
            case "receiveUserName": return "receive_user_name";
            case "receiveTime": return "receive_time";
            case "top": return "top";
            case "collected": return "collected";
            case "status": return "status";
            case "meetId": return "meet_id";
            case "finishedTime": return "finished_time";
            case "extInfo": return "ext_info";
            case "extInfo1": return "ext_info1";
            case "extInfo2": return "ext_info2";
            case "extInfo3": return "ext_info3";
            case "extInfo4": return "ext_info4";
            case "extInfo5": return "ext_info5";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id_ -> id</li>
     * <li>receive_user_id -> receiveUserId</li>
     * <li>receive_user_name -> receiveUserName</li>
     * <li>receive_time -> receiveTime</li>
     * <li>top -> top</li>
     * <li>collected -> collected</li>
     * <li>status -> status</li>
     * <li>meet_id -> meetId</li>
     * <li>finished_time -> finishedTime</li>
     * <li>ext_info -> extInfo</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>ext_info5 -> extInfo5</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id_": return "id";
            case "receive_user_id": return "receiveUserId";
            case "receive_user_name": return "receiveUserName";
            case "receive_time": return "receiveTime";
            case "top": return "top";
            case "collected": return "collected";
            case "status": return "status";
            case "meet_id": return "meetId";
            case "finished_time": return "finishedTime";
            case "ext_info": return "extInfo";
            case "ext_info1": return "extInfo1";
            case "ext_info2": return "extInfo2";
            case "ext_info3": return "extInfo3";
            case "ext_info4": return "extInfo4";
            case "ext_info5": return "extInfo5";
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
    public String getReceiveUserId() {
        return this.receiveUserId;
    }

    /**  **/
    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    /**  **/
    public String getReceiveUserName() {
        return this.receiveUserName;
    }

    /**  **/
    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    /**  **/
    public Date getReceiveTime() {
        return this.receiveTime;
    }

    /**  **/
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    /**  **/
    public Integer getTop() {
        return this.top;
    }

    /**  **/
    public void setTop(Integer top) {
        this.top = top;
    }

    /**  **/
    public Integer getCollected() {
        return this.collected;
    }

    /**  **/
    public void setCollected(Integer collected) {
        this.collected = collected;
    }

    /**  **/
    public Integer getStatus() {
        return this.status;
    }

    /**  **/
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**  **/
    public String getMeetId() {
        return this.meetId;
    }

    /**  **/
    public void setMeetId(String meetId) {
        this.meetId = meetId;
    }

    /**  **/
    public Date getFinishedTime() {
        return this.finishedTime;
    }

    /**  **/
    public void setFinishedTime(Date finishedTime) {
        this.finishedTime = finishedTime;
    }

    /**  **/
    public String getExtInfo() {
        return this.extInfo;
    }

    /**  **/
    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    /**  **/
    public String getExtInfo1() {
        return this.extInfo1;
    }

    /**  **/
    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    /**  **/
    public String getExtInfo2() {
        return this.extInfo2;
    }

    /**  **/
    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

    /**  **/
    public String getExtInfo3() {
        return this.extInfo3;
    }

    /**  **/
    public void setExtInfo3(String extInfo3) {
        this.extInfo3 = extInfo3;
    }

    /**  **/
    public String getExtInfo4() {
        return this.extInfo4;
    }

    /**  **/
    public void setExtInfo4(String extInfo4) {
        this.extInfo4 = extInfo4;
    }

    /**  **/
    public String getExtInfo5() {
        return this.extInfo5;
    }

    /**  **/
    public void setExtInfo5(String extInfo5) {
        this.extInfo5 = extInfo5;
    }

}
