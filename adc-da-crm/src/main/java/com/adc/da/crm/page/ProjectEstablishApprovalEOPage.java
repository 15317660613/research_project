package com.adc.da.crm.page;

import com.adc.da.base.page.BasePage;

import java.sql.Clob;
import java.util.Date;

/**
 * <b>功能：</b>PROJECT_ESTABLISH_APPROVAL ProjectEstablishApprovalEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectEstablishApprovalEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String projecrManagerId;
    private String projecrManagerIdOperator = "=";
    private String deptId;
    private String deptIdOperator = "=";
    private String establishDate;
    private String establishDate1;
    private String establishDate2;
    private String establishDateOperator = "=";
    private String projectId;
    private String projectIdOperator = "=";
    private String projectNumber;
    private String projectNumberOperator = "=";
    private String projectName;
    private String projectNameOperator = "=";
    private String discription;
    private String discriptionOperator = "=";
    private String establishFile;
    private String establishFileOperator = "=";
    private String establishExplanation;
    private String establishExplanationOperator = "=";
    private String belongingPlatform;
    private String belongingPlatformOperator = "=";
    private String belongingPlate;
    private String belongingPlateOperator = "=";
    private String belongingCentralDept;
    private String belongingCentralDeptOperator = "=";
    private String belongingDept;
    private String belongingDeptOperator = "=";
    private String proPeriod;
    private String proPeriodOperator = "=";
    private String proType;
    private String proTypeOperator = "=";
    private String mobile;
    private String mobileOperator = "=";
    private String phone;
    private String phoneOperator = "=";
    private String email;
    private String emailOperator = "=";
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

    public String getEstablishDate() {
        return this.establishDate;
    }

    public void setEstablishDate(String establishDate) {
        this.establishDate = establishDate;
    }

    public String getEstablishDate1() {
        return this.establishDate1;
    }

    public void setEstablishDate1(String establishDate1) {
        this.establishDate1 = establishDate1;
    }

    public String getEstablishDate2() {
        return this.establishDate2;
    }

    public void setEstablishDate2(String establishDate2) {
        this.establishDate2 = establishDate2;
    }

    public String getEstablishDateOperator() {
        return this.establishDateOperator;
    }

    public void setEstablishDateOperator(String establishDateOperator) {
        this.establishDateOperator = establishDateOperator;
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

    public String getEstablishFile() {
        return this.establishFile;
    }

    public void setEstablishFile(String establishFile) {
        this.establishFile = establishFile;
    }

    public String getEstablishFileOperator() {
        return this.establishFileOperator;
    }

    public void setEstablishFileOperator(String establishFileOperator) {
        this.establishFileOperator = establishFileOperator;
    }

    public String getEstablishExplanation() {
        return this.establishExplanation;
    }

    public void setEstablishExplanation(String establishExplanation) {
        this.establishExplanation = establishExplanation;
    }

    public String getEstablishExplanationOperator() {
        return this.establishExplanationOperator;
    }

    public void setEstablishExplanationOperator(String establishExplanationOperator) {
        this.establishExplanationOperator = establishExplanationOperator;
    }

    public String getBelongingPlatform() {
        return this.belongingPlatform;
    }

    public void setBelongingPlatform(String belongingPlatform) {
        this.belongingPlatform = belongingPlatform;
    }

    public String getBelongingPlatformOperator() {
        return this.belongingPlatformOperator;
    }

    public void setBelongingPlatformOperator(String belongingPlatformOperator) {
        this.belongingPlatformOperator = belongingPlatformOperator;
    }

    public String getBelongingPlate() {
        return this.belongingPlate;
    }

    public void setBelongingPlate(String belongingPlate) {
        this.belongingPlate = belongingPlate;
    }

    public String getBelongingPlateOperator() {
        return this.belongingPlateOperator;
    }

    public void setBelongingPlateOperator(String belongingPlateOperator) {
        this.belongingPlateOperator = belongingPlateOperator;
    }

    public String getBelongingCentralDept() {
        return this.belongingCentralDept;
    }

    public void setBelongingCentralDept(String belongingCentralDept) {
        this.belongingCentralDept = belongingCentralDept;
    }

    public String getBelongingCentralDeptOperator() {
        return this.belongingCentralDeptOperator;
    }

    public void setBelongingCentralDeptOperator(String belongingCentralDeptOperator) {
        this.belongingCentralDeptOperator = belongingCentralDeptOperator;
    }

    public String getBelongingDept() {
        return this.belongingDept;
    }

    public void setBelongingDept(String belongingDept) {
        this.belongingDept = belongingDept;
    }

    public String getBelongingDeptOperator() {
        return this.belongingDeptOperator;
    }

    public void setBelongingDeptOperator(String belongingDeptOperator) {
        this.belongingDeptOperator = belongingDeptOperator;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobileOperator() {
        return this.mobileOperator;
    }

    public void setMobileOperator(String mobileOperator) {
        this.mobileOperator = mobileOperator;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneOperator() {
        return this.phoneOperator;
    }

    public void setPhoneOperator(String phoneOperator) {
        this.phoneOperator = phoneOperator;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailOperator() {
        return this.emailOperator;
    }

    public void setEmailOperator(String emailOperator) {
        this.emailOperator = emailOperator;
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
