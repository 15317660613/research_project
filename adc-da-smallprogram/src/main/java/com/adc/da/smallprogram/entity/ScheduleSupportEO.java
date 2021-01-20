package com.adc.da.smallprogram.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>TS_SCHEDULE_SUPPORT ScheduleSupportEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScheduleSupportEO extends BaseEntity {

    private Integer delFlag;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date finishedTime;
    private String id;
    private String title;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private String address;
    private Integer status;
    private String createUserId;
    private String detail;
    private String receiveUserId;
    private String receiveUserName;
    private Integer collected;
    private String createUserName;
    private Integer top;
    private String extInfo;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;
    private String dateSection;
    private Integer type;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>delFlag -> del_flag</li>
     * <li>receiveTime -> receive_time</li>
     * <li>finishedTime -> finished_time</li>
     * <li>id -> id_</li>
     * <li>title -> title</li>
     * <li>beginTime -> begin_time</li>
     * <li>endTime -> end_time</li>
     * <li>address -> address</li>
     * <li>status -> status</li>
     * <li>createUserId -> create_user_id</li>
     * <li>detail -> detail</li>
     * <li>receiveUserId -> receive_user_id</li>
     * <li>receiveUserName -> receive_user_name</li>
     * <li>collected -> collected</li>
     * <li>createUserName -> create_user_name</li>
     * <li>top -> top</li>
     * <li>extInfo -> ext_info</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>extInfo5 -> ext_info5</li>
     * <li>dateSection -> date_section</li>
     * <li>type -> type_</li>
     * <li>createTime -> create_time</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) return null;
        switch (fieldName) {
            case "delFlag": return "del_flag";
            case "receiveTime": return "receive_time";
            case "finishedTime": return "finished_time";
            case "id": return "id_";
            case "title": return "title";
            case "beginTime": return "begin_time";
            case "endTime": return "end_time";
            case "address": return "address";
            case "status": return "status";
            case "createUserId": return "create_user_id";
            case "detail": return "detail";
            case "receiveUserId": return "receive_user_id";
            case "receiveUserName": return "receive_user_name";
            case "collected": return "collected";
            case "createUserName": return "create_user_name";
            case "top": return "top";
            case "extInfo": return "ext_info";
            case "extInfo1": return "ext_info1";
            case "extInfo2": return "ext_info2";
            case "extInfo3": return "ext_info3";
            case "extInfo4": return "ext_info4";
            case "extInfo5": return "ext_info5";
            case "dateSection": return "date_section";
            case "type": return "type_";
            case "createTime": return "create_time";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>del_flag -> delFlag</li>
     * <li>receive_time -> receiveTime</li>
     * <li>finished_time -> finishedTime</li>
     * <li>id_ -> id</li>
     * <li>title -> title</li>
     * <li>begin_time -> beginTime</li>
     * <li>end_time -> endTime</li>
     * <li>address -> address</li>
     * <li>status -> status</li>
     * <li>create_user_id -> createUserId</li>
     * <li>detail -> detail</li>
     * <li>receive_user_id -> receiveUserId</li>
     * <li>receive_user_name -> receiveUserName</li>
     * <li>collected -> collected</li>
     * <li>create_user_name -> createUserName</li>
     * <li>top -> top</li>
     * <li>ext_info -> extInfo</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>ext_info5 -> extInfo5</li>
     * <li>date_section -> dateSection</li>
     * <li>type_ -> type</li>
     * <li>create_time -> createTime</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) return null;
        switch (columnName) {
            case "del_flag": return "delFlag";
            case "receive_time": return "receiveTime";
            case "finished_time": return "finishedTime";
            case "id_": return "id";
            case "title": return "title";
            case "begin_time": return "beginTime";
            case "end_time": return "endTime";
            case "address": return "address";
            case "status": return "status";
            case "create_user_id": return "createUserId";
            case "detail": return "detail";
            case "receive_user_id": return "receiveUserId";
            case "receive_user_name": return "receiveUserName";
            case "collected": return "collected";
            case "create_user_name": return "createUserName";
            case "top": return "top";
            case "ext_info": return "extInfo";
            case "ext_info1": return "extInfo1";
            case "ext_info2": return "extInfo2";
            case "ext_info3": return "extInfo3";
            case "ext_info4": return "extInfo4";
            case "ext_info5": return "extInfo5";
            case "date_section": return "dateSection";
            case "type_": return "type";
            case "create_time": return "createTime";
            default: return null;
        }
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
    public Date getReceiveTime() {
        return this.receiveTime;
    }

    /**  **/
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
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
    public Date getBeginTime() {
        return this.beginTime;
    }

    /**  **/
    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    /**  **/
    public Date getEndTime() {
        return this.endTime;
    }

    /**  **/
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
    public Integer getStatus() {
        return this.status;
    }

    /**  **/
    public void setStatus(Integer status) {
        this.status = status;
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
    public String getDetail() {
        return this.detail;
    }

    /**  **/
    public void setDetail(String detail) {
        this.detail = detail;
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
    public Integer getCollected() {
        return this.collected;
    }

    /**  **/
    public void setCollected(Integer collected) {
        this.collected = collected;
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
    public Integer getTop() {
        return this.top;
    }

    /**  **/
    public void setTop(Integer top) {
        this.top = top;
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

    /**  **/
    public String getDateSection() {
        return this.dateSection;
    }

    /**  **/
    public void setDateSection(String dateSection) {
        this.dateSection = dateSection;
    }

    /**  **/
    public Integer getType() {
        return this.type;
    }

    /**  **/
    public void setType(Integer type) {
        this.type = type;
    }

    /**  **/
    public Date getCreateTime() {
        return this.createTime;
    }

    /**  **/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
