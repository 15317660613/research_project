package com.adc.da.crm.vo;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import com.adc.da.crm.annotation.MatchFieldCollection;
import com.adc.da.crm.entity.BTravelCustomerVisitRecordEO;
import com.adc.da.crm.entity.BTravelProjectVisitRecordEO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>B_TRAVEL_APPROVAL BTravelApprovalEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BTravelApprovalVO extends BaseEntity {


    private String id;
    private String orderNumber;
    @MatchField("出差人")
    private String travelUserId;
    @MatchField("填报部门")
    private String deptId;
    @MatchField("中心领导")
    private String leader;
    @MatchField("职务")
    private String job;
    @MatchField("出差地点")
    private String address;
    @MatchField("出差类型")
    private String type;
    @MatchField("出差日期起")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date travelStartTime;
    @MatchField("出差日期止")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date travelEndTime;
    @MatchField("出差起止日期")
    private String travelStartAndFinishTime;
    @MatchField("出差事由")
    private String reason;
    @MatchField("是否因课题出差")
    private String ifIssue;
    @MatchField("课题名称")
    private String issueName;
    @MatchField("乘坐交通工具")
    private String vehicle;
    @MatchField("原因")
    private String vehicleReason;
    @MatchField("是否于携程订票")
    private String ifReserveFromCtrip;
    @MatchField("其他订票方式原因")
    private String otherReserveReason;
    private String customerRecordId;
    @MatchField("科室负责人")
    private String deptHeadOpinion;
    @MatchField("数据中心领导")
    private String dataCenterLeaderOpinion;
    private String delFlag;
    @MatchField("填报日期")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private String createdUser;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;
    private String modifiedUser;
    private String checkStatus;

    //客户拜访记录
    @MatchFieldCollection
    private List<BTravelCustomerVisitRecordEO> bTravelCustomerVisitRecordEOList = new ArrayList<>();
    //项目拜访情况
    @MatchFieldCollection
    private List<BTravelProjectVisitRecordEO> bTravelProjectVisitRecordEOList = new ArrayList<>();

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>travelStartAndFinishTime -> travel_start_and_finish_time</li>
     * <li>id -> id</li>
     * <li>orderNumber -> order_number</li>
     * <li>travelUserId -> travel_user_id</li>
     * <li>deptId -> dept_id</li>
     * <li>leader -> leader</li>
     * <li>job -> job</li>
     * <li>address -> address</li>
     * <li>type -> type</li>
     * <li>travelStartTime -> travel_start_time</li>
     * <li>travelEndTime -> travel_end_time</li>
     * <li>reason -> reason</li>
     * <li>ifIssue -> if_issue</li>
     * <li>issueName -> issue_name</li>
     * <li>vehicle -> vehicle</li>
     * <li>vehicleReason -> vehicle_reason</li>
     * <li>ifReserveFromCtrip -> if_reserve_from_ctrip</li>
     * <li>otherReserveReason -> other_reserve_reason</li>
     * <li>customerRecordId -> customer_record_id</li>
     * <li>deptHeadOpinion -> dept_head_opinion</li>
     * <li>dataCenterLeaderOpinion -> data_center_leader_opinion</li>
     * <li>delFlag -> del_flag</li>
     * <li>createdTime -> created_time</li>
     * <li>createdUser -> created_user</li>
     * <li>modifiedTime -> modified_time</li>
     * <li>modifiedUser -> modified_user</li>
     * <li>checkStatus -> check_status</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "travelStartAndFinishTime": return "travel_start_and_finish_time";
            case "id": return "id";
            case "orderNumber": return "order_number";
            case "travelUserId": return "travel_user_id";
            case "deptId": return "dept_id";
            case "leader": return "leader";
            case "job": return "job";
            case "address": return "address";
            case "type": return "type";
            case "travelStartTime": return "travel_start_time";
            case "travelEndTime": return "travel_end_time";
            case "reason": return "reason";
            case "ifIssue": return "if_issue";
            case "issueName": return "issue_name";
            case "vehicle": return "vehicle";
            case "vehicleReason": return "vehicle_reason";
            case "ifReserveFromCtrip": return "if_reserve_from_ctrip";
            case "otherReserveReason": return "other_reserve_reason";
            case "customerRecordId": return "customer_record_id";
            case "deptHeadOpinion": return "dept_head_opinion";
            case "dataCenterLeaderOpinion": return "data_center_leader_opinion";
            case "delFlag": return "del_flag";
            case "createdTime": return "created_time";
            case "createdUser": return "created_user";
            case "modifiedTime": return "modified_time";
            case "modifiedUser": return "modified_user";
            case "checkStatus": return "check_status";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>travel_start_and_finish_time -> travelStartAndFinishTime</li>
     * <li>id -> id</li>
     * <li>order_number -> orderNumber</li>
     * <li>travel_user_id -> travelUserId</li>
     * <li>dept_id -> deptId</li>
     * <li>leader -> leader</li>
     * <li>job -> job</li>
     * <li>address -> address</li>
     * <li>type -> type</li>
     * <li>travel_start_time -> travelStartTime</li>
     * <li>travel_end_time -> travelEndTime</li>
     * <li>reason -> reason</li>
     * <li>if_issue -> ifIssue</li>
     * <li>issue_name -> issueName</li>
     * <li>vehicle -> vehicle</li>
     * <li>vehicle_reason -> vehicleReason</li>
     * <li>if_reserve_from_ctrip -> ifReserveFromCtrip</li>
     * <li>other_reserve_reason -> otherReserveReason</li>
     * <li>customer_record_id -> customerRecordId</li>
     * <li>dept_head_opinion -> deptHeadOpinion</li>
     * <li>data_center_leader_opinion -> dataCenterLeaderOpinion</li>
     * <li>del_flag -> delFlag</li>
     * <li>created_time -> createdTime</li>
     * <li>created_user -> createdUser</li>
     * <li>modified_time -> modifiedTime</li>
     * <li>modified_user -> modifiedUser</li>
     * <li>check_status -> checkStatus</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "travel_start_and_finish_time": return "travelStartAndFinishTime";
            case "id": return "id";
            case "order_number": return "orderNumber";
            case "travel_user_id": return "travelUserId";
            case "dept_id": return "deptId";
            case "leader": return "leader";
            case "job": return "job";
            case "address": return "address";
            case "type": return "type";
            case "travel_start_time": return "travelStartTime";
            case "travel_end_time": return "travelEndTime";
            case "reason": return "reason";
            case "if_issue": return "ifIssue";
            case "issue_name": return "issueName";
            case "vehicle": return "vehicle";
            case "vehicle_reason": return "vehicleReason";
            case "if_reserve_from_ctrip": return "ifReserveFromCtrip";
            case "other_reserve_reason": return "otherReserveReason";
            case "customer_record_id": return "customerRecordId";
            case "dept_head_opinion": return "deptHeadOpinion";
            case "data_center_leader_opinion": return "dataCenterLeaderOpinion";
            case "del_flag": return "delFlag";
            case "created_time": return "createdTime";
            case "created_user": return "createdUser";
            case "modified_time": return "modifiedTime";
            case "modified_user": return "modifiedUser";
            case "check_status": return "checkStatus";
            default: return null;
        }
    }
    
    /**  **/
    public String getTravelStartAndFinishTime() {
        return this.travelStartAndFinishTime;
    }

    /**  **/
    public void setTravelStartAndFinishTime(String travelStartAndFinishTime) {
        this.travelStartAndFinishTime = travelStartAndFinishTime;
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
    public String getOrderNumber() {
        return this.orderNumber;
    }

    /**  **/
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**  **/
    public String getTravelUserId() {
        return this.travelUserId;
    }

    /**  **/
    public void setTravelUserId(String travelUserId) {
        this.travelUserId = travelUserId;
    }

    /**  **/
    public String getDeptId() {
        return this.deptId;
    }

    /**  **/
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**  **/
    public String getLeader() {
        return this.leader;
    }

    /**  **/
    public void setLeader(String leader) {
        this.leader = leader;
    }

    /**  **/
    public String getJob() {
        return this.job;
    }

    /**  **/
    public void setJob(String job) {
        this.job = job;
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
    public String getType() {
        return this.type;
    }

    /**  **/
    public void setType(String type) {
        this.type = type;
    }

    /**  **/
    public Date getTravelStartTime() {
        return this.travelStartTime;
    }

    /**  **/
    public void setTravelStartTime(Date travelStartTime) {
        this.travelStartTime = travelStartTime;
    }

    /**  **/
    public Date getTravelEndTime() {
        return this.travelEndTime;
    }

    /**  **/
    public void setTravelEndTime(Date travelEndTime) {
        this.travelEndTime = travelEndTime;
    }

    /**  **/
    public String getReason() {
        return this.reason;
    }

    /**  **/
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**  **/
    public String getIfIssue() {
        return this.ifIssue;
    }

    /**  **/
    public void setIfIssue(String ifIssue) {
        this.ifIssue = ifIssue;
    }

    /**  **/
    public String getIssueName() {
        return this.issueName;
    }

    /**  **/
    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    /**  **/
    public String getVehicle() {
        return this.vehicle;
    }

    /**  **/
    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    /**  **/
    public String getVehicleReason() {
        return this.vehicleReason;
    }

    /**  **/
    public void setVehicleReason(String vehicleReason) {
        this.vehicleReason = vehicleReason;
    }

    /**  **/
    public String getIfReserveFromCtrip() {
        return this.ifReserveFromCtrip;
    }

    /**  **/
    public void setIfReserveFromCtrip(String ifReserveFromCtrip) {
        this.ifReserveFromCtrip = ifReserveFromCtrip;
    }

    /**  **/
    public String getOtherReserveReason() {
        return this.otherReserveReason;
    }

    /**  **/
    public void setOtherReserveReason(String otherReserveReason) {
        this.otherReserveReason = otherReserveReason;
    }

    /**  **/
    public String getCustomerRecordId() {
        return this.customerRecordId;
    }

    /**  **/
    public void setCustomerRecordId(String customerRecordId) {
        this.customerRecordId = customerRecordId;
    }

    /**  **/
    public String getDeptHeadOpinion() {
        return this.deptHeadOpinion;
    }

    /**  **/
    public void setDeptHeadOpinion(String deptHeadOpinion) {
        this.deptHeadOpinion = deptHeadOpinion;
    }

    /**  **/
    public String getDataCenterLeaderOpinion() {
        return this.dataCenterLeaderOpinion;
    }

    /**  **/
    public void setDataCenterLeaderOpinion(String dataCenterLeaderOpinion) {
        this.dataCenterLeaderOpinion = dataCenterLeaderOpinion;
    }

    /**  **/
    public String getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    /**  **/
    public Date getCreatedTime() {
        return this.createdTime;
    }

    /**  **/
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**  **/
    public String getCreatedUser() {
        return this.createdUser;
    }

    /**  **/
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    /**  **/
    public Date getModifiedTime() {
        return this.modifiedTime;
    }

    /**  **/
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    /**  **/
    public String getModifiedUser() {
        return this.modifiedUser;
    }

    /**  **/
    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    /**  **/
    public String getCheckStatus() {
        return this.checkStatus;
    }

    /**  **/
    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public List<BTravelCustomerVisitRecordEO> getbTravelCustomerVisitRecordEOList() {
        return bTravelCustomerVisitRecordEOList;
    }

    public void setbTravelCustomerVisitRecordEOList(List<BTravelCustomerVisitRecordEO> bTravelCustomerVisitRecordEOList) {
        this.bTravelCustomerVisitRecordEOList = bTravelCustomerVisitRecordEOList;
    }

    public List<BTravelProjectVisitRecordEO> getbTravelProjectVisitRecordEOList() {
        return bTravelProjectVisitRecordEOList;
    }

    public void setbTravelProjectVisitRecordEOList(List<BTravelProjectVisitRecordEO> bTravelProjectVisitRecordEOList) {
        this.bTravelProjectVisitRecordEOList = bTravelProjectVisitRecordEOList;
    }
}
