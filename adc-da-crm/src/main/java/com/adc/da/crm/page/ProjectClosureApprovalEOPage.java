package com.adc.da.crm.page;

import com.adc.da.base.page.BasePage;

import java.sql.Clob;
import java.util.Date;

/**
 * <b>功能：</b>PROJECT_CLOSURE_APPROVAL ProjectClosureApprovalEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectClosureApprovalEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String projecrManagerId;
    private String projecrManagerIdOperator = "=";
    private String deptId;
    private String deptIdOperator = "=";
    private String endDate;
    private String endDate1;
    private String endDate2;
    private String endDateOperator = "=";
    private String projectId;
    private String projectIdOperator = "=";
    private String projectNumber;
    private String projectNumberOperator = "=";
    private String projectName;
    private String projectNameOperator = "=";
    private String discription;
    private String discriptionOperator = "=";
    private String achievementFile;
    private String achievementFileOperator = "=";
    private String proPeriod;
    private String proPeriodOperator = "=";
    private String deptHeadOpinion;
    private String deptHeadOpinionOperator = "=";
    private String leaderOpinion;
    private String leaderOpinionOperator = "=";
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

    public String getProjecrManagerId() {
        return this.projecrManagerId;
    }

    public void setProjecrManagerId(String projecrManagerId) {
        this.projecrManagerId = projecrManagerId;
    }

    public String getProjecrManagerIdOperator() {
        return this.projecrManagerIdOperator;
    }

    public void setProjecrManagerIdOperator(String projecrManagerIdOperator) {
        this.projecrManagerIdOperator = projecrManagerIdOperator;
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

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate1() {
        return this.endDate1;
    }

    public void setEndDate1(String endDate1) {
        this.endDate1 = endDate1;
    }

    public String getEndDate2() {
        return this.endDate2;
    }

    public void setEndDate2(String endDate2) {
        this.endDate2 = endDate2;
    }

    public String getEndDateOperator() {
        return this.endDateOperator;
    }

    public void setEndDateOperator(String endDateOperator) {
        this.endDateOperator = endDateOperator;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectIdOperator() {
        return this.projectIdOperator;
    }

    public void setProjectIdOperator(String projectIdOperator) {
        this.projectIdOperator = projectIdOperator;
    }

    public String getProjectNumber() {
        return this.projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectNumberOperator() {
        return this.projectNumberOperator;
    }

    public void setProjectNumberOperator(String projectNumberOperator) {
        this.projectNumberOperator = projectNumberOperator;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectNameOperator() {
        return this.projectNameOperator;
    }

    public void setProjectNameOperator(String projectNameOperator) {
        this.projectNameOperator = projectNameOperator;
    }

    public String getDiscription() {
        return this.discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getDiscriptionOperator() {
        return this.discriptionOperator;
    }

    public void setDiscriptionOperator(String discriptionOperator) {
        this.discriptionOperator = discriptionOperator;
    }

    public String getAchievementFile() {
        return this.achievementFile;
    }

    public void setAchievementFile(String achievementFile) {
        this.achievementFile = achievementFile;
    }

    public String getAchievementFileOperator() {
        return this.achievementFileOperator;
    }

    public void setAchievementFileOperator(String achievementFileOperator) {
        this.achievementFileOperator = achievementFileOperator;
    }

    public String getProPeriod() {
        return this.proPeriod;
    }

    public void setProPeriod(String proPeriod) {
        this.proPeriod = proPeriod;
    }

    public String getProPeriodOperator() {
        return this.proPeriodOperator;
    }

    public void setProPeriodOperator(String proPeriodOperator) {
        this.proPeriodOperator = proPeriodOperator;
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

    public String getLeaderOpinion() {
        return this.leaderOpinion;
    }

    public void setLeaderOpinion(String leaderOpinion) {
        this.leaderOpinion = leaderOpinion;
    }

    public String getLeaderOpinionOperator() {
        return this.leaderOpinionOperator;
    }

    public void setLeaderOpinionOperator(String leaderOpinionOperator) {
        this.leaderOpinionOperator = leaderOpinionOperator;
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
