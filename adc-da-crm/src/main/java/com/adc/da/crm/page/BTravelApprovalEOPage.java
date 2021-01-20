package com.adc.da.crm.page;

import com.adc.da.base.page.BasePage;

import java.sql.Clob;
import java.util.Date;

/**
 * <b>功能：</b>B_TRAVEL_APPROVAL BTravelApprovalEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BTravelApprovalEOPage extends BasePage {

    private String travelStartAndFinishTime;
    private String travelStartAndFinishTimeOperator = "=";
    private String id;
    private String idOperator = "=";
    private String orderNumber;
    private String orderNumberOperator = "=";
    private String travelUserId;
    private String travelUserIdOperator = "=";
    private String deptId;
    private String deptIdOperator = "=";
    private String leader;
    private String leaderOperator = "=";
    private String job;
    private String jobOperator = "=";
    private String address;
    private String addressOperator = "=";
    private String type;
    private String typeOperator = "=";
    private String travelStartTime;
    private String travelStartTime1;
    private String travelStartTime2;
    private String travelStartTimeOperator = "=";
    private String travelEndTime;
    private String travelEndTime1;
    private String travelEndTime2;
    private String travelEndTimeOperator = "=";
    private String reason;
    private String reasonOperator = "=";
    private String ifIssue;
    private String ifIssueOperator = "=";
    private String issueName;
    private String issueNameOperator = "=";
    private String vehicle;
    private String vehicleOperator = "=";
    private String vehicleReason;
    private String vehicleReasonOperator = "=";
    private String ifReserveFromCtrip;
    private String ifReserveFromCtripOperator = "=";
    private String otherReserveReason;
    private String otherReserveReasonOperator = "=";
    private String customerRecordId;
    private String customerRecordIdOperator = "=";
    private String deptHeadOpinion;
    private String deptHeadOpinionOperator = "=";
    private String dataCenterLeaderOpinion;
    private String dataCenterLeaderOpinionOperator = "=";
    private String delFlag;
    private String delFlagOperator = "=";
    private String createdTime;
    private String createdTime1;
    private String createdTime2;
    private String createdTimeOperator = "=";
    private String createdUser;
    private String createdUserOperator = "=";
    private String modifiedTime;
    private String modifiedTime1;
    private String modifiedTime2;
    private String modifiedTimeOperator = "=";
    private String modifiedUser;
    private String modifiedUserOperator = "=";
    private String checkStatus;
    private String checkStatusOperator = "=";

    public String getTravelStartAndFinishTime() {
        return this.travelStartAndFinishTime;
    }

    public void setTravelStartAndFinishTime(String travelStartAndFinishTime) {
        this.travelStartAndFinishTime = travelStartAndFinishTime;
    }

    public String getTravelStartAndFinishTimeOperator() {
        return this.travelStartAndFinishTimeOperator;
    }

    public void setTravelStartAndFinishTimeOperator(String travelStartAndFinishTimeOperator) {
        this.travelStartAndFinishTimeOperator = travelStartAndFinishTimeOperator;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOperator() {
        return this.idOperator;
    }

    public void setIdOperator(String idOperator) {
        this.idOperator = idOperator;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderNumberOperator() {
        return this.orderNumberOperator;
    }

    public void setOrderNumberOperator(String orderNumberOperator) {
        this.orderNumberOperator = orderNumberOperator;
    }

    public String getTravelUserId() {
        return this.travelUserId;
    }

    public void setTravelUserId(String travelUserId) {
        this.travelUserId = travelUserId;
    }

    public String getTravelUserIdOperator() {
        return this.travelUserIdOperator;
    }

    public void setTravelUserIdOperator(String travelUserIdOperator) {
        this.travelUserIdOperator = travelUserIdOperator;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptIdOperator() {
        return this.deptIdOperator;
    }

    public void setDeptIdOperator(String deptIdOperator) {
        this.deptIdOperator = deptIdOperator;
    }

    public String getLeader() {
        return this.leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLeaderOperator() {
        return this.leaderOperator;
    }

    public void setLeaderOperator(String leaderOperator) {
        this.leaderOperator = leaderOperator;
    }

    public String getJob() {
        return this.job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobOperator() {
        return this.jobOperator;
    }

    public void setJobOperator(String jobOperator) {
        this.jobOperator = jobOperator;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressOperator() {
        return this.addressOperator;
    }

    public void setAddressOperator(String addressOperator) {
        this.addressOperator = addressOperator;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeOperator() {
        return this.typeOperator;
    }

    public void setTypeOperator(String typeOperator) {
        this.typeOperator = typeOperator;
    }

    public String getTravelStartTime() {
        return this.travelStartTime;
    }

    public void setTravelStartTime(String travelStartTime) {
        this.travelStartTime = travelStartTime;
    }

    public String getTravelStartTime1() {
        return this.travelStartTime1;
    }

    public void setTravelStartTime1(String travelStartTime1) {
        this.travelStartTime1 = travelStartTime1;
    }

    public String getTravelStartTime2() {
        return this.travelStartTime2;
    }

    public void setTravelStartTime2(String travelStartTime2) {
        this.travelStartTime2 = travelStartTime2;
    }

    public String getTravelStartTimeOperator() {
        return this.travelStartTimeOperator;
    }

    public void setTravelStartTimeOperator(String travelStartTimeOperator) {
        this.travelStartTimeOperator = travelStartTimeOperator;
    }

    public String getTravelEndTime() {
        return this.travelEndTime;
    }

    public void setTravelEndTime(String travelEndTime) {
        this.travelEndTime = travelEndTime;
    }

    public String getTravelEndTime1() {
        return this.travelEndTime1;
    }

    public void setTravelEndTime1(String travelEndTime1) {
        this.travelEndTime1 = travelEndTime1;
    }

    public String getTravelEndTime2() {
        return this.travelEndTime2;
    }

    public void setTravelEndTime2(String travelEndTime2) {
        this.travelEndTime2 = travelEndTime2;
    }

    public String getTravelEndTimeOperator() {
        return this.travelEndTimeOperator;
    }

    public void setTravelEndTimeOperator(String travelEndTimeOperator) {
        this.travelEndTimeOperator = travelEndTimeOperator;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReasonOperator() {
        return this.reasonOperator;
    }

    public void setReasonOperator(String reasonOperator) {
        this.reasonOperator = reasonOperator;
    }

    public String getIfIssue() {
        return this.ifIssue;
    }

    public void setIfIssue(String ifIssue) {
        this.ifIssue = ifIssue;
    }

    public String getIfIssueOperator() {
        return this.ifIssueOperator;
    }

    public void setIfIssueOperator(String ifIssueOperator) {
        this.ifIssueOperator = ifIssueOperator;
    }

    public String getIssueName() {
        return this.issueName;
    }

    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }

    public String getIssueNameOperator() {
        return this.issueNameOperator;
    }

    public void setIssueNameOperator(String issueNameOperator) {
        this.issueNameOperator = issueNameOperator;
    }

    public String getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getVehicleOperator() {
        return this.vehicleOperator;
    }

    public void setVehicleOperator(String vehicleOperator) {
        this.vehicleOperator = vehicleOperator;
    }

    public String getVehicleReason() {
        return this.vehicleReason;
    }

    public void setVehicleReason(String vehicleReason) {
        this.vehicleReason = vehicleReason;
    }

    public String getVehicleReasonOperator() {
        return this.vehicleReasonOperator;
    }

    public void setVehicleReasonOperator(String vehicleReasonOperator) {
        this.vehicleReasonOperator = vehicleReasonOperator;
    }

    public String getIfReserveFromCtrip() {
        return this.ifReserveFromCtrip;
    }

    public void setIfReserveFromCtrip(String ifReserveFromCtrip) {
        this.ifReserveFromCtrip = ifReserveFromCtrip;
    }

    public String getIfReserveFromCtripOperator() {
        return this.ifReserveFromCtripOperator;
    }

    public void setIfReserveFromCtripOperator(String ifReserveFromCtripOperator) {
        this.ifReserveFromCtripOperator = ifReserveFromCtripOperator;
    }

    public String getOtherReserveReason() {
        return this.otherReserveReason;
    }

    public void setOtherReserveReason(String otherReserveReason) {
        this.otherReserveReason = otherReserveReason;
    }

    public String getOtherReserveReasonOperator() {
        return this.otherReserveReasonOperator;
    }

    public void setOtherReserveReasonOperator(String otherReserveReasonOperator) {
        this.otherReserveReasonOperator = otherReserveReasonOperator;
    }

    public String getCustomerRecordId() {
        return this.customerRecordId;
    }

    public void setCustomerRecordId(String customerRecordId) {
        this.customerRecordId = customerRecordId;
    }

    public String getCustomerRecordIdOperator() {
        return this.customerRecordIdOperator;
    }

    public void setCustomerRecordIdOperator(String customerRecordIdOperator) {
        this.customerRecordIdOperator = customerRecordIdOperator;
    }

    public String getDeptHeadOpinion() {
        return this.deptHeadOpinion;
    }

    public void setDeptHeadOpinion(String deptHeadOpinion) {
        this.deptHeadOpinion = deptHeadOpinion;
    }

    public String getDeptHeadOpinionOperator() {
        return this.deptHeadOpinionOperator;
    }

    public void setDeptHeadOpinionOperator(String deptHeadOpinionOperator) {
        this.deptHeadOpinionOperator = deptHeadOpinionOperator;
    }

    public String getDataCenterLeaderOpinion() {
        return this.dataCenterLeaderOpinion;
    }

    public void setDataCenterLeaderOpinion(String dataCenterLeaderOpinion) {
        this.dataCenterLeaderOpinion = dataCenterLeaderOpinion;
    }

    public String getDataCenterLeaderOpinionOperator() {
        return this.dataCenterLeaderOpinionOperator;
    }

    public void setDataCenterLeaderOpinionOperator(String dataCenterLeaderOpinionOperator) {
        this.dataCenterLeaderOpinionOperator = dataCenterLeaderOpinionOperator;
    }

    public String getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlagOperator() {
        return this.delFlagOperator;
    }

    public void setDelFlagOperator(String delFlagOperator) {
        this.delFlagOperator = delFlagOperator;
    }

    public String getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedTime1() {
        return this.createdTime1;
    }

    public void setCreatedTime1(String createdTime1) {
        this.createdTime1 = createdTime1;
    }

    public String getCreatedTime2() {
        return this.createdTime2;
    }

    public void setCreatedTime2(String createdTime2) {
        this.createdTime2 = createdTime2;
    }

    public String getCreatedTimeOperator() {
        return this.createdTimeOperator;
    }

    public void setCreatedTimeOperator(String createdTimeOperator) {
        this.createdTimeOperator = createdTimeOperator;
    }

    public String getCreatedUser() {
        return this.createdUser;
    }

    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    public String getCreatedUserOperator() {
        return this.createdUserOperator;
    }

    public void setCreatedUserOperator(String createdUserOperator) {
        this.createdUserOperator = createdUserOperator;
    }

    public String getModifiedTime() {
        return this.modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getModifiedTime1() {
        return this.modifiedTime1;
    }

    public void setModifiedTime1(String modifiedTime1) {
        this.modifiedTime1 = modifiedTime1;
    }

    public String getModifiedTime2() {
        return this.modifiedTime2;
    }

    public void setModifiedTime2(String modifiedTime2) {
        this.modifiedTime2 = modifiedTime2;
    }

    public String getModifiedTimeOperator() {
        return this.modifiedTimeOperator;
    }

    public void setModifiedTimeOperator(String modifiedTimeOperator) {
        this.modifiedTimeOperator = modifiedTimeOperator;
    }

    public String getModifiedUser() {
        return this.modifiedUser;
    }

    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public String getModifiedUserOperator() {
        return this.modifiedUserOperator;
    }

    public void setModifiedUserOperator(String modifiedUserOperator) {
        this.modifiedUserOperator = modifiedUserOperator;
    }

    public String getCheckStatus() {
        return this.checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckStatusOperator() {
        return this.checkStatusOperator;
    }

    public void setCheckStatusOperator(String checkStatusOperator) {
        this.checkStatusOperator = checkStatusOperator;
    }

}
