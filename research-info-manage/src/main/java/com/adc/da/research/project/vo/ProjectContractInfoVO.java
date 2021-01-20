package com.adc.da.research.project.vo;

import com.adc.da.base.page.BasePage;

/**
 * <b>功能：科研项目合同信息查询参数
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectContractInfoVO extends BasePage {

    private String researchTarget;
    private String researchTargetOperator = "=";
    private String researchContent;
    private String researchContentOperator = "=";
    private String id;
    private String idOperator = "=";
    private String projectCode;
    private String projectCodeOperator = "=";
    private String projectName;
    private String projectNameOperator = "=";
    private String projectTypeId;
    private String projectTypeIdOperator = "=";
    private String projectSource;
    private String projectSourceOperator = "=";
    private String projectClass;
    private String projectClassOperator = "=";
    private String projectLeader;
    private String projectLeaderOperator = "=";
    private String technicalDirector;
    private String technicalDirectorOperator = "=";
    private String subjectDirector;
    private String subjectDirectorOperator = "=";
    private String undertakingId;
    private String undertakingIdOperator = "=";
    private String reportingUnitId;
    private String reportingUnitName;
    private String reportingUnitIdOperator = "=";
    private String leadUnit;
    private String leadUnitOperator = "=";
    private String participateUnit;
    private String participateUnitOperator = "=";
    private String deptId;
    private String deptIdOperator = "=";
    private String totalFunding;
    private Integer totalFundingStart; //用于经费区间查询
    private Integer totalFundingEnd;
    private String loginUserId; //不是管理员 将对该值赋值进行分页查询
    private String totalFundingOperator = "=";
    private String expectedExpenditure;
    private String expectedExpenditureBefore;
    private String expectedExpenditureEnd;
    private String expectedExpenditureOperator = "=";
    private String stateFunding;
    private String stateFundingBefore;
    private String stateFundingEnd;
    private String stateFundingOperator = "=";
    private String centerBudgetApply;
    private String centerBudgetApplyBefore;
    private String centerBudgetApplyEnd;
    private String centerBudgetApplyOperator = "=";
    private String selfFunded;
    private String selfFundedBefore;
    private String selfFundedEnd;
    private String selfFundedOperator = "=";
    private String otherFunded;
    private String otherFundedBefore;
    private String otherFundedEnd;
    private String otherFundedOperator = "=";
    private String projectStatus;
    private String projectStatusOperator = "=";
    private String startTime;
    private String startTime1;
    private String startTime2;
    private String startTimeOperator = "=";
    private String endTime;
    private String endTime1;
    private String endTime2;
    private String endTimeOperator = "=";
    private String applicationTime;
    private String applicationTime1;
    private String applicationTime2;
    private String applicationTimeOperator = "=";
    private String projectTime;
    private String projectTime1;
    private String projectTime2;
    private String projectTimeOperator = "=";
    private String changeTime;
    private String changeTime1;
    private String changeTime2;
    private String changeTimeOperator = "=";
    private String projectApplicantId;
    private String projectApplicantIdOperator = "=";
    private String projectApplicantName;
    private String projectApplicantNameOperator = "=";
    private String projectOverview;
    private String projectOverviewOperator = "=";
    private String researchIndicators;
    private String researchIndicatorsOperator = "=";
    private String remark;
    private String remarkOperator = "=";
    private String delFlag;
    private String delFlagOperator = "=";
    private String ext1;
    private String ext1Operator = "=";
    private String ext2;
    private String ext2Operator = "=";
    private String ext3;
    private String ext3Operator = "=";
    private String ext4;
    private String ext4Operator = "=";
    private String ext5;
    private String ext5Operator = "=";
    private String stageName;
    //项目ID
    private String partyaName;
    private String partyaNameOperator = "=";
    private String partyaUser;
    private String partyaUserOperator = "=";
    private String partyaTel;
    private String partyaTelOperator = "=";
    private String partyaEmail;
    private String partyaEmailOperator = "=";
    private String partybName;
    private String partybNameOperator = "=";
    private String partybUser;
    private String partybUserOperator = "=";
    private String partybDept;
    private String partybDeptOperator = "=";
    private String partybTel;
    private String partybTelOperator = "=";
    private String partybMobile;
    private String partybMobileOperator = "=";
    private String partybEmail;
    private String partybEmailOperator = "=";
    private String partybFax;
    private String partybFaxOperator = "=";
    private String partycName;
    private String partycNameOperator = "=";
    private String partycUser;
    private String partycUserOperator = "=";
    private String researchContact;
    private String researchContactOperator = "=";
    private String partycTel;
    private String partycTelOperator = "=";
    private String partycEmail;
    private String partycEmailOperator = "=";
    private String partycFax;
    private String partycFaxOperator = "=";

    public String getReportingUnitName() {
        return reportingUnitName;
    }

    public void setReportingUnitName(String reportingUnitName) {
        this.reportingUnitName = reportingUnitName;
    }

    public Integer getTotalFundingStart() {
        return totalFundingStart;
    }

    public void setTotalFundingStart(Integer totalFundingStart) {
        this.totalFundingStart = totalFundingStart;
    }

    public Integer getTotalFundingEnd() {
        return totalFundingEnd;
    }

    public void setTotalFundingEnd(Integer totalFundingEnd) {
        this.totalFundingEnd = totalFundingEnd;
    }

    public String getLoginUserId() {
        return loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getResearchTarget() {
        return this.researchTarget;
    }

    public void setResearchTarget(String researchTarget) {
        this.researchTarget = researchTarget;
    }

    public String getResearchTargetOperator() {
        return this.researchTargetOperator;
    }

    public void setResearchTargetOperator(String researchTargetOperator) {
        this.researchTargetOperator = researchTargetOperator;
    }

    public String getResearchContent() {
        return this.researchContent;
    }

    public void setResearchContent(String researchContent) {
        this.researchContent = researchContent;
    }

    public String getResearchContentOperator() {
        return this.researchContentOperator;
    }

    public void setResearchContentOperator(String researchContentOperator) {
        this.researchContentOperator = researchContentOperator;
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

    public String getProjectCode() {
        return this.projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectCodeOperator() {
        return this.projectCodeOperator;
    }

    public void setProjectCodeOperator(String projectCodeOperator) {
        this.projectCodeOperator = projectCodeOperator;
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

    public String getProjectTypeId() {
        return this.projectTypeId;
    }

    public void setProjectTypeId(String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public String getProjectTypeIdOperator() {
        return this.projectTypeIdOperator;
    }

    public void setProjectTypeIdOperator(String projectTypeIdOperator) {
        this.projectTypeIdOperator = projectTypeIdOperator;
    }

    public String getProjectSource() {
        return this.projectSource;
    }

    public void setProjectSource(String projectSource) {
        this.projectSource = projectSource;
    }

    public String getProjectSourceOperator() {
        return this.projectSourceOperator;
    }

    public void setProjectSourceOperator(String projectSourceOperator) {
        this.projectSourceOperator = projectSourceOperator;
    }

    public String getProjectClass() {
        return this.projectClass;
    }

    public void setProjectClass(String projectClass) {
        this.projectClass = projectClass;
    }

    public String getProjectClassOperator() {
        return this.projectClassOperator;
    }

    public void setProjectClassOperator(String projectClassOperator) {
        this.projectClassOperator = projectClassOperator;
    }

    public String getProjectLeader() {
        return this.projectLeader;
    }

    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    public String getProjectLeaderOperator() {
        return this.projectLeaderOperator;
    }

    public void setProjectLeaderOperator(String projectLeaderOperator) {
        this.projectLeaderOperator = projectLeaderOperator;
    }

    public String getTechnicalDirector() {
        return this.technicalDirector;
    }

    public void setTechnicalDirector(String technicalDirector) {
        this.technicalDirector = technicalDirector;
    }

    public String getTechnicalDirectorOperator() {
        return this.technicalDirectorOperator;
    }

    public void setTechnicalDirectorOperator(String technicalDirectorOperator) {
        this.technicalDirectorOperator = technicalDirectorOperator;
    }

    public String getSubjectDirector() {
        return this.subjectDirector;
    }

    public void setSubjectDirector(String subjectDirector) {
        this.subjectDirector = subjectDirector;
    }

    public String getSubjectDirectorOperator() {
        return this.subjectDirectorOperator;
    }

    public void setSubjectDirectorOperator(String subjectDirectorOperator) {
        this.subjectDirectorOperator = subjectDirectorOperator;
    }

    public String getUndertakingId() {
        return this.undertakingId;
    }

    public void setUndertakingId(String undertakingId) {
        this.undertakingId = undertakingId;
    }

    public String getUndertakingIdOperator() {
        return this.undertakingIdOperator;
    }

    public void setUndertakingIdOperator(String undertakingIdOperator) {
        this.undertakingIdOperator = undertakingIdOperator;
    }

    public String getReportingUnitId() {
        return this.reportingUnitId;
    }

    public void setReportingUnitId(String reportingUnitId) {
        this.reportingUnitId = reportingUnitId;
    }

    public String getReportingUnitIdOperator() {
        return this.reportingUnitIdOperator;
    }

    public void setReportingUnitIdOperator(String reportingUnitIdOperator) {
        this.reportingUnitIdOperator = reportingUnitIdOperator;
    }

    public String getLeadUnit() {
        return this.leadUnit;
    }

    public void setLeadUnit(String leadUnit) {
        this.leadUnit = leadUnit;
    }

    public String getLeadUnitOperator() {
        return this.leadUnitOperator;
    }

    public void setLeadUnitOperator(String leadUnitOperator) {
        this.leadUnitOperator = leadUnitOperator;
    }

    public String getParticipateUnit() {
        return this.participateUnit;
    }

    public void setParticipateUnit(String participateUnit) {
        this.participateUnit = participateUnit;
    }

    public String getParticipateUnitOperator() {
        return this.participateUnitOperator;
    }

    public void setParticipateUnitOperator(String participateUnitOperator) {
        this.participateUnitOperator = participateUnitOperator;
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

    public String getTotalFunding() {
        return this.totalFunding;
    }

    public void setTotalFunding(String totalFunding) {
        this.totalFunding = totalFunding;
    }

    public String getTotalFundingOperator() {
        return this.totalFundingOperator;
    }

    public void setTotalFundingOperator(String totalFundingOperator) {
        this.totalFundingOperator = totalFundingOperator;
    }

    public String getExpectedExpenditure() {
        return this.expectedExpenditure;
    }

    public void setExpectedExpenditure(String expectedExpenditure) {
        this.expectedExpenditure = expectedExpenditure;
    }

    public String getExpectedExpenditureOperator() {
        return this.expectedExpenditureOperator;
    }

    public void setExpectedExpenditureOperator(String expectedExpenditureOperator) {
        this.expectedExpenditureOperator = expectedExpenditureOperator;
    }

    public String getStateFunding() {
        return this.stateFunding;
    }

    public void setStateFunding(String stateFunding) {
        this.stateFunding = stateFunding;
    }

    public String getStateFundingOperator() {
        return this.stateFundingOperator;
    }

    public void setStateFundingOperator(String stateFundingOperator) {
        this.stateFundingOperator = stateFundingOperator;
    }

    public String getCenterBudgetApply() {
        return this.centerBudgetApply;
    }

    public void setCenterBudgetApply(String centerBudgetApply) {
        this.centerBudgetApply = centerBudgetApply;
    }

    public String getCenterBudgetApplyOperator() {
        return this.centerBudgetApplyOperator;
    }

    public void setCenterBudgetApplyOperator(String centerBudgetApplyOperator) {
        this.centerBudgetApplyOperator = centerBudgetApplyOperator;
    }

    public String getSelfFunded() {
        return this.selfFunded;
    }

    public void setSelfFunded(String selfFunded) {
        this.selfFunded = selfFunded;
    }

    public String getSelfFundedOperator() {
        return this.selfFundedOperator;
    }

    public void setSelfFundedOperator(String selfFundedOperator) {
        this.selfFundedOperator = selfFundedOperator;
    }

    public String getOtherFunded() {
        return this.otherFunded;
    }

    public void setOtherFunded(String otherFunded) {
        this.otherFunded = otherFunded;
    }

    public String getOtherFundedOperator() {
        return this.otherFundedOperator;
    }

    public void setOtherFundedOperator(String otherFundedOperator) {
        this.otherFundedOperator = otherFundedOperator;
    }

    public String getProjectStatus() {
        return this.projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getProjectStatusOperator() {
        return this.projectStatusOperator;
    }

    public void setProjectStatusOperator(String projectStatusOperator) {
        this.projectStatusOperator = projectStatusOperator;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime1() {
        return this.startTime1;
    }

    public void setStartTime1(String startTime1) {
        this.startTime1 = startTime1;
    }

    public String getStartTime2() {
        return this.startTime2;
    }

    public void setStartTime2(String startTime2) {
        this.startTime2 = startTime2;
    }

    public String getStartTimeOperator() {
        return this.startTimeOperator;
    }

    public void setStartTimeOperator(String startTimeOperator) {
        this.startTimeOperator = startTimeOperator;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEndTime1() {
        return this.endTime1;
    }

    public void setEndTime1(String endTime1) {
        this.endTime1 = endTime1;
    }

    public String getEndTime2() {
        return this.endTime2;
    }

    public void setEndTime2(String endTime2) {
        this.endTime2 = endTime2;
    }

    public String getEndTimeOperator() {
        return this.endTimeOperator;
    }

    public void setEndTimeOperator(String endTimeOperator) {
        this.endTimeOperator = endTimeOperator;
    }

    public String getApplicationTime() {
        return this.applicationTime;
    }

    public void setApplicationTime(String applicationTime) {
        this.applicationTime = applicationTime;
    }

    public String getApplicationTime1() {
        return applicationTime1;
    }

    public void setApplicationTime1(String applicationTime1) {
        this.applicationTime1 = applicationTime1;
    }

    public String getApplicationTime2() {
        return applicationTime2;
    }

    public void setApplicationTime2(String applicationTime2) {
        this.applicationTime2 = applicationTime2;
    }

    public String getApplicationTimeOperator() {
        return this.applicationTimeOperator;
    }

    public void setApplicationTimeOperator(String applicationTimeOperator) {
        this.applicationTimeOperator = applicationTimeOperator;
    }

    public String getProjectTime() {
        return this.projectTime;
    }

    public void setProjectTime(String projectTime) {
        this.projectTime = projectTime;
    }

    public String getProjectTime1() {
        return this.projectTime1;
    }

    public void setProjectTime1(String projectTime1) {
        this.projectTime1 = projectTime1;
    }

    public String getProjectTime2() {
        return this.projectTime2;
    }

    public void setProjectTime2(String projectTime2) {
        this.projectTime2 = projectTime2;
    }

    public String getProjectTimeOperator() {
        return this.projectTimeOperator;
    }

    public void setProjectTimeOperator(String projectTimeOperator) {
        this.projectTimeOperator = projectTimeOperator;
    }

    public String getChangeTime() {
        return this.changeTime;
    }

    public void setChangeTime(String changeTime) {
        this.changeTime = changeTime;
    }

    public String getChangeTime1() {
        return this.changeTime1;
    }

    public void setChangeTime1(String changeTime1) {
        this.changeTime1 = changeTime1;
    }

    public String getChangeTime2() {
        return this.changeTime2;
    }

    public void setChangeTime2(String changeTime2) {
        this.changeTime2 = changeTime2;
    }

    public String getChangeTimeOperator() {
        return this.changeTimeOperator;
    }

    public void setChangeTimeOperator(String changeTimeOperator) {
        this.changeTimeOperator = changeTimeOperator;
    }

    public String getProjectApplicantId() {
        return this.projectApplicantId;
    }

    public void setProjectApplicantId(String projectApplicantId) {
        this.projectApplicantId = projectApplicantId;
    }

    public String getProjectApplicantIdOperator() {
        return this.projectApplicantIdOperator;
    }

    public void setProjectApplicantIdOperator(String projectApplicantIdOperator) {
        this.projectApplicantIdOperator = projectApplicantIdOperator;
    }

    public String getProjectApplicantName() {
        return this.projectApplicantName;
    }

    public void setProjectApplicantName(String projectApplicantName) {
        this.projectApplicantName = projectApplicantName;
    }

    public String getProjectApplicantNameOperator() {
        return this.projectApplicantNameOperator;
    }

    public void setProjectApplicantNameOperator(String projectApplicantNameOperator) {
        this.projectApplicantNameOperator = projectApplicantNameOperator;
    }

    public String getProjectOverview() {
        return this.projectOverview;
    }

    public void setProjectOverview(String projectOverview) {
        this.projectOverview = projectOverview;
    }

    public String getProjectOverviewOperator() {
        return this.projectOverviewOperator;
    }

    public void setProjectOverviewOperator(String projectOverviewOperator) {
        this.projectOverviewOperator = projectOverviewOperator;
    }

    public String getResearchIndicators() {
        return this.researchIndicators;
    }

    public void setResearchIndicators(String researchIndicators) {
        this.researchIndicators = researchIndicators;
    }

    public String getResearchIndicatorsOperator() {
        return this.researchIndicatorsOperator;
    }

    public void setResearchIndicatorsOperator(String researchIndicatorsOperator) {
        this.researchIndicatorsOperator = researchIndicatorsOperator;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkOperator() {
        return this.remarkOperator;
    }

    public void setRemarkOperator(String remarkOperator) {
        this.remarkOperator = remarkOperator;
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

    public String getExt1() {
        return this.ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt1Operator() {
        return this.ext1Operator;
    }

    public void setExt1Operator(String ext1Operator) {
        this.ext1Operator = ext1Operator;
    }

    public String getExt2() {
        return this.ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt2Operator() {
        return this.ext2Operator;
    }

    public void setExt2Operator(String ext2Operator) {
        this.ext2Operator = ext2Operator;
    }

    public String getExt3() {
        return this.ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getExt3Operator() {
        return this.ext3Operator;
    }

    public void setExt3Operator(String ext3Operator) {
        this.ext3Operator = ext3Operator;
    }

    public String getExt4() {
        return this.ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    public String getExt4Operator() {
        return this.ext4Operator;
    }

    public void setExt4Operator(String ext4Operator) {
        this.ext4Operator = ext4Operator;
    }

    public String getExt5() {
        return this.ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    public String getExt5Operator() {
        return this.ext5Operator;
    }

    public void setExt5Operator(String ext5Operator) {
        this.ext5Operator = ext5Operator;
    }

    public String getExpectedExpenditureBefore() {
        return expectedExpenditureBefore;
    }

    public void setExpectedExpenditureBefore(String expectedExpenditureBefore) {
        this.expectedExpenditureBefore = expectedExpenditureBefore;
    }

    public String getExpectedExpenditureEnd() {
        return expectedExpenditureEnd;
    }

    public void setExpectedExpenditureEnd(String expectedExpenditureEnd) {
        this.expectedExpenditureEnd = expectedExpenditureEnd;
    }

    public String getStateFundingBefore() {
        return stateFundingBefore;
    }

    public void setStateFundingBefore(String stateFundingBefore) {
        this.stateFundingBefore = stateFundingBefore;
    }

    public String getStateFundingEnd() {
        return stateFundingEnd;
    }

    public void setStateFundingEnd(String stateFundingEnd) {
        this.stateFundingEnd = stateFundingEnd;
    }

    public String getCenterBudgetApplyBefore() {
        return centerBudgetApplyBefore;
    }

    public void setCenterBudgetApplyBefore(String centerBudgetApplyBefore) {
        this.centerBudgetApplyBefore = centerBudgetApplyBefore;
    }

    public String getCenterBudgetApplyEnd() {
        return centerBudgetApplyEnd;
    }

    public void setCenterBudgetApplyEnd(String centerBudgetApplyEnd) {
        this.centerBudgetApplyEnd = centerBudgetApplyEnd;
    }

    public String getSelfFundedBefore() {
        return selfFundedBefore;
    }

    public void setSelfFundedBefore(String selfFundedBefore) {
        this.selfFundedBefore = selfFundedBefore;
    }

    public String getSelfFundedEnd() {
        return selfFundedEnd;
    }

    public void setSelfFundedEnd(String selfFundedEnd) {
        this.selfFundedEnd = selfFundedEnd;
    }

    public String getOtherFundedBefore() {
        return otherFundedBefore;
    }

    public void setOtherFundedBefore(String otherFundedBefore) {
        this.otherFundedBefore = otherFundedBefore;
    }

    public String getOtherFundedEnd() {
        return otherFundedEnd;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public void setOtherFundedEnd(String otherFundedEnd) {
        this.otherFundedEnd = otherFundedEnd;
    }


    public String getPartyaName() {
        return partyaName;
    }

    public void setPartyaName(String partyaName) {
        this.partyaName = partyaName;
    }

    public String getPartyaNameOperator() {
        return partyaNameOperator;
    }

    public void setPartyaNameOperator(String partyaNameOperator) {
        this.partyaNameOperator = partyaNameOperator;
    }

    public String getPartyaUser() {
        return partyaUser;
    }

    public void setPartyaUser(String partyaUser) {
        this.partyaUser = partyaUser;
    }

    public String getPartyaUserOperator() {
        return partyaUserOperator;
    }

    public void setPartyaUserOperator(String partyaUserOperator) {
        this.partyaUserOperator = partyaUserOperator;
    }

    public String getPartyaTel() {
        return partyaTel;
    }

    public void setPartyaTel(String partyaTel) {
        this.partyaTel = partyaTel;
    }

    public String getPartyaTelOperator() {
        return partyaTelOperator;
    }

    public void setPartyaTelOperator(String partyaTelOperator) {
        this.partyaTelOperator = partyaTelOperator;
    }

    public String getPartyaEmail() {
        return partyaEmail;
    }

    public void setPartyaEmail(String partyaEmail) {
        this.partyaEmail = partyaEmail;
    }

    public String getPartyaEmailOperator() {
        return partyaEmailOperator;
    }

    public void setPartyaEmailOperator(String partyaEmailOperator) {
        this.partyaEmailOperator = partyaEmailOperator;
    }

    public String getPartybName() {
        return partybName;
    }

    public void setPartybName(String partybName) {
        this.partybName = partybName;
    }

    public String getPartybNameOperator() {
        return partybNameOperator;
    }

    public void setPartybNameOperator(String partybNameOperator) {
        this.partybNameOperator = partybNameOperator;
    }

    public String getPartybUser() {
        return partybUser;
    }

    public void setPartybUser(String partybUser) {
        this.partybUser = partybUser;
    }

    public String getPartybUserOperator() {
        return partybUserOperator;
    }

    public void setPartybUserOperator(String partybUserOperator) {
        this.partybUserOperator = partybUserOperator;
    }

    public String getPartybDept() {
        return partybDept;
    }

    public void setPartybDept(String partybDept) {
        this.partybDept = partybDept;
    }

    public String getPartybDeptOperator() {
        return partybDeptOperator;
    }

    public void setPartybDeptOperator(String partybDeptOperator) {
        this.partybDeptOperator = partybDeptOperator;
    }

    public String getPartybTel() {
        return partybTel;
    }

    public void setPartybTel(String partybTel) {
        this.partybTel = partybTel;
    }

    public String getPartybTelOperator() {
        return partybTelOperator;
    }

    public void setPartybTelOperator(String partybTelOperator) {
        this.partybTelOperator = partybTelOperator;
    }

    public String getPartybMobile() {
        return partybMobile;
    }

    public void setPartybMobile(String partybMobile) {
        this.partybMobile = partybMobile;
    }

    public String getPartybMobileOperator() {
        return partybMobileOperator;
    }

    public void setPartybMobileOperator(String partybMobileOperator) {
        this.partybMobileOperator = partybMobileOperator;
    }

    public String getPartybEmail() {
        return partybEmail;
    }

    public void setPartybEmail(String partybEmail) {
        this.partybEmail = partybEmail;
    }

    public String getPartybEmailOperator() {
        return partybEmailOperator;
    }

    public void setPartybEmailOperator(String partybEmailOperator) {
        this.partybEmailOperator = partybEmailOperator;
    }

    public String getPartybFax() {
        return partybFax;
    }

    public void setPartybFax(String partybFax) {
        this.partybFax = partybFax;
    }

    public String getPartybFaxOperator() {
        return partybFaxOperator;
    }

    public void setPartybFaxOperator(String partybFaxOperator) {
        this.partybFaxOperator = partybFaxOperator;
    }

    public String getPartycName() {
        return partycName;
    }

    public void setPartycName(String partycName) {
        this.partycName = partycName;
    }

    public String getPartycNameOperator() {
        return partycNameOperator;
    }

    public void setPartycNameOperator(String partycNameOperator) {
        this.partycNameOperator = partycNameOperator;
    }

    public String getPartycUser() {
        return partycUser;
    }

    public void setPartycUser(String partycUser) {
        this.partycUser = partycUser;
    }

    public String getPartycUserOperator() {
        return partycUserOperator;
    }

    public void setPartycUserOperator(String partycUserOperator) {
        this.partycUserOperator = partycUserOperator;
    }

    public String getResearchContact() {
        return researchContact;
    }

    public void setResearchContact(String researchContact) {
        this.researchContact = researchContact;
    }

    public String getResearchContactOperator() {
        return researchContactOperator;
    }

    public void setResearchContactOperator(String researchContactOperator) {
        this.researchContactOperator = researchContactOperator;
    }

    public String getPartycTel() {
        return partycTel;
    }

    public void setPartycTel(String partycTel) {
        this.partycTel = partycTel;
    }

    public String getPartycTelOperator() {
        return partycTelOperator;
    }

    public void setPartycTelOperator(String partycTelOperator) {
        this.partycTelOperator = partycTelOperator;
    }

    public String getPartycEmail() {
        return partycEmail;
    }

    public void setPartycEmail(String partycEmail) {
        this.partycEmail = partycEmail;
    }

    public String getPartycEmailOperator() {
        return partycEmailOperator;
    }

    public void setPartycEmailOperator(String partycEmailOperator) {
        this.partycEmailOperator = partycEmailOperator;
    }

    public String getPartycFax() {
        return partycFax;
    }

    public void setPartycFax(String partycFax) {
        this.partycFax = partycFax;
    }

    public String getPartycFaxOperator() {
        return partycFaxOperator;
    }

    public void setPartycFaxOperator(String partycFaxOperator) {
        this.partycFaxOperator = partycFaxOperator;
    }
}
