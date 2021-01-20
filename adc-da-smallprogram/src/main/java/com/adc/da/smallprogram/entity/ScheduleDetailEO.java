package com.adc.da.smallprogram.entity;

import com.adc.da.base.entity.BaseEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * <b>功能：</b>TS_SCHEDULE_DETAIL ScheduleDetailEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-19 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScheduleDetailEO extends BaseEntity implements Serializable,Comparable<ScheduleDetailEO> {

    private String id;
    private String parentId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date beginTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private String timeLimit;
    private String scheduleDetail;
    private Integer orderNumber;
    private String createUserId;
    private Integer detailType;
    private Integer delFlag;
    private Integer status;
    private String extInfo1;
    private String extInfo2; // 此字段已经用于形成的详细地点
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;


    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>parentId -> parent_id</li>
     * <li>beginTime -> begin_time</li>
     * <li>endTime -> end_time</li>
     * <li>timeLimit -> time_limit</li>
     * <li>scheduleDetail -> schedule_detail</li>
     * <li>orderNumber -> order_number</li>
     * <li>createUserId -> create_user_id</li>
     * <li>detailType -> detail_type</li>
     * <li>delFlag -> del_flag</li>
     * <li>status -> status</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>extInfo5 -> ext_info5</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) return null;
        switch (fieldName) {
            case "id": return "id";
            case "parentId": return "parent_id";
            case "beginTime": return "begin_time";
            case "endTime": return "end_time";
            case "timeLimit": return "time_limit";
            case "scheduleDetail": return "schedule_detail";
            case "orderNumber": return "order_number";
            case "createUserId": return "create_user_id";
            case "detailType": return "detail_type";
            case "delFlag": return "del_flag";
            case "status": return "status";
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
     * <li>id -> id</li>
     * <li>parent_id -> parentId</li>
     * <li>begin_time -> beginTime</li>
     * <li>end_time -> endTime</li>
     * <li>time_limit -> timeLimit</li>
     * <li>schedule_detail -> scheduleDetail</li>
     * <li>order_number -> orderNumber</li>
     * <li>create_user_id -> createUserId</li>
     * <li>detail_type -> detailType</li>
     * <li>del_flag -> delFlag</li>
     * <li>status -> status</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>ext_info5 -> extInfo5</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) return null;
        switch (columnName) {
            case "id": return "id";
            case "parent_id": return "parentId";
            case "begin_time": return "beginTime";
            case "end_time": return "endTime";
            case "time_limit": return "timeLimit";
            case "schedule_detail": return "scheduleDetail";
            case "order_number": return "orderNumber";
            case "create_user_id": return "createUserId";
            case "detail_type": return "detailType";
            case "del_flag": return "delFlag";
            case "status": return "status";
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
    public String getParentId() {
        return this.parentId;
    }

    /**  **/
    public void setParentId(String parentId) {
        this.parentId = parentId;
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
    public String getTimeLimit() {
        return this.timeLimit;
    }

    /**  **/
    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    /**  **/
    public String getScheduleDetail() {
        return this.scheduleDetail;
    }

    /**  **/
    public void setScheduleDetail(String scheduleDetail) {
        this.scheduleDetail = scheduleDetail;
    }

    /**  **/
    public Integer getOrderNumber() {
        return this.orderNumber;
    }

    /**  **/
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
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
    public Integer getDetailType() {
        return this.detailType;
    }

    /**  **/
    public void setDetailType(Integer detailType) {
        this.detailType = detailType;
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
    public Integer getStatus() {
        return this.status;
    }

    /**  **/
    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public int compareTo(ScheduleDetailEO o) {
        if (null == o.getBeginTime() ){
            return 0;
        }
        if (null == this.getBeginTime() ){
            return 0;
        }
        return  this.getBeginTime().compareTo(o.getBeginTime()) ;
    }
}
