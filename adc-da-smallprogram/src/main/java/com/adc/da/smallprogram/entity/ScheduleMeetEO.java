package com.adc.da.smallprogram.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>TS_SCHEDULE_MEET ScheduleMeetEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScheduleMeetEO extends BaseEntity {

    private String address;
    private String id;
    private String title;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date deadTime;
    private String createUserId;
    private String createUserName;
    private String detail;
    private Integer delFlag;
    //0 党会  1 工作会
    private Integer meetType;

    private Integer status;
    private String extInfo;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>address -> address</li>
     * <li>id -> id_</li>
     * <li>title -> title</li>
     * <li>createTime -> create_time</li>
     * <li>updateTime -> update_time</li>
     * <li>deadTime -> dead_time</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>detail -> detail</li>
     * <li>delFlag -> del_flag</li>
     * <li>meetType -> meet_type</li>
     * <li>status -> status</li>
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
            case "address": return "address";
            case "id": return "id_";
            case "title": return "title";
            case "createTime": return "create_time";
            case "updateTime": return "update_time";
            case "deadTime": return "dead_time";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "detail": return "detail";
            case "delFlag": return "del_flag";
            case "meetType": return "meet_type";
            case "status": return "status";
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
     * <li>address -> address</li>
     * <li>id_ -> id</li>
     * <li>title -> title</li>
     * <li>create_time -> createTime</li>
     * <li>update_time -> updateTime</li>
     * <li>dead_time -> deadTime</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>detail -> detail</li>
     * <li>del_flag -> delFlag</li>
     * <li>meet_type -> meetType</li>
     * <li>status -> status</li>
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
            case "address": return "address";
            case "id_": return "id";
            case "title": return "title";
            case "create_time": return "createTime";
            case "update_time": return "updateTime";
            case "dead_time": return "deadTime";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "detail": return "detail";
            case "del_flag": return "delFlag";
            case "meet_type": return "meetType";
            case "status": return "status";
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
    public String getAddress() {
        return this.address;
    }

    /**  **/
    public void setAddress(String address) {
        this.address = address;
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
    public String getTitle() {
        return this.title;
    }

    /**  **/
    public void setTitle(String title) {
        this.title = title;
    }

    /**  **/
    public Date getCreateTime() {
        return this.createTime;
    }

    /**  **/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**  **/
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**  **/
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**  **/
    public Date getDeadTime() {
        return this.deadTime;
    }

    /**  **/
    public void setDeadTime(Date deadTime) {
        this.deadTime = deadTime;
    }

    /**  **/
    public String getCreateUserId() {
        return this.createUserId;
    }

    /**  **/
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**  **/
    public String getCreateUserName() {
        return this.createUserName;
    }

    /**  **/
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    /**  **/
    public String getDetail() {
        return this.detail;
    }

    /**  **/
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**  **/
    public Integer getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /**  **/
    public Integer getMeetType() {
        return this.meetType;
    }

    /**  **/
    public void setMeetType(Integer meetType) {
        this.meetType = meetType;
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
