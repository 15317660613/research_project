package com.adc.da.finance.page;

import com.adc.da.base.page.BasePage;

/**
 * <b>功能：</b>F_COST_MANAGEMENT CostManagementEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class CostManagementEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String year;
    private String yearOperator = "=";
    private String month;
    private String monthOperator = "=";
    private String day;
    private String dayOperator = "=";
    private String subjectName;
    private String subjectNameOperator = "=";
    private String orgName;
    private String orgNameOperator = "=";
    private String certificationNumber;
    private String certificationNumberOperator = "=";
    private String abstractInfo;
    private String abstractInfoOperator = "=";

    private String otherSubjectName;

    private String otherSubjectNameOperator = "=";
    private String amount;
    private String amountOperator = "=";
    private String businessName;
    private String businessNameOperator = "=";
    private String distributionUserId;
    private String distributionUserIdOperator = "=";
    private String distributionUserName;
    private String distributionUserNameOperator = "=";
    private String distributionTime;
    private String distributionTime1;
    private String distributionTime2;
    private String distributionTimeOperator = "=";
    private String deadlineTime;
    private String deadlineTime1;
    private String deadlineTime2;
    private String deadlineTimeOperator = "=";
    private String claimTime;
    private String claimTime1;
    private String claimTime2;
    private String claimTimeOperator = "=";
    private String claimUserName;
    private String claimUserNameOperator = "=";
    private String claimUserId;
    private String claimUserIdOperator = "=";
    private String status;
    private String statusOperator = "=";
    private String extInfo;
    private String extInfoOperator = "=";
    private String extInfo1;
    private String extInfo1Operator = "=";
    private String extInfo2;
    private String extInfo2Operator = "=";
    private String extInfo3;
    private String extInfo3Operator = "=";
    private String extInfo4;
    private String extInfo4Operator = "=";
    private String extInfo5;
    private String extInfo5Operator = "=";

    private String delFlag;

    private String delFlagOperator = "=";

    private String orgId;

    private String orgIdOperator = "=";

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

    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYearOperator() {
        return this.yearOperator;
    }

    public void setYearOperator(String yearOperator) {
        this.yearOperator = yearOperator;
    }

    public String getMonth() {
        return this.month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getMonthOperator() {
        return this.monthOperator;
    }

    public void setMonthOperator(String monthOperator) {
        this.monthOperator = monthOperator;
    }

    public String getDay() {
        return this.day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDayOperator() {
        return this.dayOperator;
    }

    public void setDayOperator(String dayOperator) {
        this.dayOperator = dayOperator;
    }

    public String getSubjectName() {
        return this.subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectNameOperator() {
        return this.subjectNameOperator;
    }

    public void setSubjectNameOperator(String subjectNameOperator) {
        this.subjectNameOperator = subjectNameOperator;
    }

    public String getOrgName() {
        return this.orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgNameOperator() {
        return this.orgNameOperator;
    }

    public void setOrgNameOperator(String orgNameOperator) {
        this.orgNameOperator = orgNameOperator;
    }

    public String getCertificationNumber() {
        return this.certificationNumber;
    }

    public void setCertificationNumber(String certificationNumber) {
        this.certificationNumber = certificationNumber;
    }

    public String getCertificationNumberOperator() {
        return this.certificationNumberOperator;
    }

    public void setCertificationNumberOperator(String certificationNumberOperator) {
        this.certificationNumberOperator = certificationNumberOperator;
    }

    public String getAbstractInfo() {
        return this.abstractInfo;
    }

    public void setAbstractInfo(String abstractInfo) {
        this.abstractInfo = abstractInfo;
    }

    public String getAbstractInfoOperator() {
        return this.abstractInfoOperator;
    }

    public void setAbstractInfoOperator(String abstractInfoOperator) {
        this.abstractInfoOperator = abstractInfoOperator;
    }

    public String getOtherSubjectName() {
        return this.otherSubjectName;
    }

    public void setOtherSubjectName(String otherSubject) {
        this.otherSubjectName = otherSubject;
    }

    public String getOtherSubjectNameOperator() {
        return this.otherSubjectNameOperator;
    }

    public void setOtherSubjectNameOperator(String otherSubjectOperator) {
        this.otherSubjectNameOperator = otherSubjectOperator;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountOperator() {
        return this.amountOperator;
    }

    public void setAmountOperator(String amountOperator) {
        this.amountOperator = amountOperator;
    }

    public String getBusinessName() {
        return this.businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessNameOperator() {
        return this.businessNameOperator;
    }

    public void setBusinessNameOperator(String businessNameOperator) {
        this.businessNameOperator = businessNameOperator;
    }

    public String getDistributionUserId() {
        return this.distributionUserId;
    }

    public void setDistributionUserId(String distributionUserId) {
        this.distributionUserId = distributionUserId;
    }

    public String getDistributionUserIdOperator() {
        return this.distributionUserIdOperator;
    }

    public void setDistributionUserIdOperator(String distributionUserIdOperator) {
        this.distributionUserIdOperator = distributionUserIdOperator;
    }

    public String getDistributionUserName() {
        return this.distributionUserName;
    }

    public void setDistributionUserName(String distributionUserName) {
        this.distributionUserName = distributionUserName;
    }

    public String getDistributionUserNameOperator() {
        return this.distributionUserNameOperator;
    }

    public void setDistributionUserNameOperator(String distributionUserNameOperator) {
        this.distributionUserNameOperator = distributionUserNameOperator;
    }

    public String getDistributionTime() {
        return this.distributionTime;
    }

    public void setDistributionTime(String distributionTime) {
        this.distributionTime = distributionTime;
    }

    public String getDistributionTime1() {
        return this.distributionTime1;
    }

    public void setDistributionTime1(String distributionTime1) {
        this.distributionTime1 = distributionTime1;
    }

    public String getDistributionTime2() {
        return this.distributionTime2;
    }

    public void setDistributionTime2(String distributionTime2) {
        this.distributionTime2 = distributionTime2;
    }

    public String getDistributionTimeOperator() {
        return this.distributionTimeOperator;
    }

    public void setDistributionTimeOperator(String distributionTimeOperator) {
        this.distributionTimeOperator = distributionTimeOperator;
    }

    public String getDeadlineTime() {
        return this.deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }

    public String getDeadlineTime1() {
        return this.deadlineTime1;
    }

    public void setDeadlineTime1(String deadlineTime1) {
        this.deadlineTime1 = deadlineTime1;
    }

    public String getDeadlineTime2() {
        return this.deadlineTime2;
    }

    public void setDeadlineTime2(String deadlineTime2) {
        this.deadlineTime2 = deadlineTime2;
    }

    public String getDeadlineTimeOperator() {
        return this.deadlineTimeOperator;
    }

    public void setDeadlineTimeOperator(String deadlineTimeOperator) {
        this.deadlineTimeOperator = deadlineTimeOperator;
    }

    public String getClaimTime() {
        return this.claimTime;
    }

    public void setClaimTime(String claimTime) {
        this.claimTime = claimTime;
    }

    public String getClaimTime1() {
        return this.claimTime1;
    }

    public void setClaimTime1(String claimTime1) {
        this.claimTime1 = claimTime1;
    }

    public String getClaimTime2() {
        return this.claimTime2;
    }

    public void setClaimTime2(String claimTime2) {
        this.claimTime2 = claimTime2;
    }

    public String getClaimTimeOperator() {
        return this.claimTimeOperator;
    }

    public void setClaimTimeOperator(String claimTimeOperator) {
        this.claimTimeOperator = claimTimeOperator;
    }

    public String getClaimUserName() {
        return this.claimUserName;
    }

    public void setClaimUserName(String claimUserName) {
        this.claimUserName = claimUserName;
    }

    public String getClaimUserNameOperator() {
        return this.claimUserNameOperator;
    }

    public void setClaimUserNameOperator(String claimUserNameOperator) {
        this.claimUserNameOperator = claimUserNameOperator;
    }

    public String getClaimUserId() {
        return this.claimUserId;
    }

    public void setClaimUserId(String claimUserId) {
        this.claimUserId = claimUserId;
    }

    public String getClaimUserIdOperator() {
        return this.claimUserIdOperator;
    }

    public void setClaimUserIdOperator(String claimUserIdOperator) {
        this.claimUserIdOperator = claimUserIdOperator;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusOperator() {
        return this.statusOperator;
    }

    public void setStatusOperator(String statusOperator) {
        this.statusOperator = statusOperator;
    }

    public String getExtInfo() {
        return this.extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public String getExtInfoOperator() {
        return this.extInfoOperator;
    }

    public void setExtInfoOperator(String extInfoOperator) {
        this.extInfoOperator = extInfoOperator;
    }

    public String getExtInfo1() {
        return this.extInfo1;
    }

    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    public String getExtInfo1Operator() {
        return this.extInfo1Operator;
    }

    public void setExtInfo1Operator(String extInfo1Operator) {
        this.extInfo1Operator = extInfo1Operator;
    }

    public String getExtInfo2() {
        return this.extInfo2;
    }

    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

    public String getExtInfo2Operator() {
        return this.extInfo2Operator;
    }

    public void setExtInfo2Operator(String extInfo2Operator) {
        this.extInfo2Operator = extInfo2Operator;
    }

    public String getExtInfo3() {
        return this.extInfo3;
    }

    public void setExtInfo3(String extInfo3) {
        this.extInfo3 = extInfo3;
    }

    public String getExtInfo3Operator() {
        return this.extInfo3Operator;
    }

    public void setExtInfo3Operator(String extInfo3Operator) {
        this.extInfo3Operator = extInfo3Operator;
    }

    public String getExtInfo4() {
        return this.extInfo4;
    }

    public void setExtInfo4(String extInfo4) {
        this.extInfo4 = extInfo4;
    }

    public String getExtInfo4Operator() {
        return this.extInfo4Operator;
    }

    public void setExtInfo4Operator(String extInfo4Operator) {
        this.extInfo4Operator = extInfo4Operator;
    }

    public String getExtInfo5() {
        return this.extInfo5;
    }

    public void setExtInfo5(String extInfo5) {
        this.extInfo5 = extInfo5;
    }

    public String getExtInfo5Operator() {
        return this.extInfo5Operator;
    }

    public void setExtInfo5Operator(String extInfo5Operator) {
        this.extInfo5Operator = extInfo5Operator;
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

    public String getOrgId() {
        return this.orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgIdOperator() {
        return this.orgIdOperator;
    }

    public void setOrgIdOperator(String orgIdOperator) {
        this.orgIdOperator = orgIdOperator;
    }

}
