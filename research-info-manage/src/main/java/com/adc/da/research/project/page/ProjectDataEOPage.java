package com.adc.da.research.project.page;

import com.adc.da.base.page.BasePage;

/**
 * <b>功能：</b>RS_PROJECT_DATA ProjectDataEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectDataEOPage extends BasePage {

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
    private String ids;
    private String file;
    private String expertUserId;

    //到账率
    private String arrivalRateBefore;
    private String arrivalRateEnd;
    //国拨执行到账率
    private String stateExecutionsBefore;
    private String stateExecutionsEnd;
    //国拨本季度到账率
    private String stateQuarterExecutionsBefore;
    private String stateQuarterExecutionsEnd;
    //本季度自筹经费
    private String selfFundedQuarterBefore;
    private String selfFundedQuarterEnd;
    //自筹经费执行率
    private String selfExecutionsBefore;
    private String selfExecutionsEnd;
    //本季度自筹经费执行率
    private String selfExecutionsQuarterBefore;
    private String selfExecutionsQuarterEnd;


    //国拨本季度经费
    private String stateFundQuarterBefore;
    private String stateFundQuarterEnd;
    //本年度自筹经费
    private String selfFundedYearBefore;
    private String selfFundedYearEnd;
    //本年度自筹经费执行率
    private String selfExecutionsYearBefore;
    private String selfExecutionsYearEnd;
    //国拨本年度执行率
    private String stateYearExecutionsBefore;
    private String stateYearExecutionsEnd;
    //国拨本年度经费
    private String stateFundYeahBefore;
    private String stateFundYeahEnd;


    private String financialDocument;//财务建档
    private String financialDocumentOperator = "=";
    private String propertyChanges;//变更属性
    private String propertyChangesOperator = "=";
    private String changeStatus;//变更状态
    private String changeStatusOperator = "=";
    private String fundTemplateId;//经费模板id
    private String fundTemplateIdOperator = "=";
    private String contractTemplate;//合同模板
    private String contractTemplateOperator = "=";

    //经费用（中汽中心当年预算）
    private String budgetsumBefore;
    private String budgetsumEnd;

    //项目开始与结束时间
    private String projectTimeBegin;
    private String projectTimeEnd;


    public String getProjectTimeBegin() {
        return projectTimeBegin;
    }

    public void setProjectTimeBegin(String projectTimeBegin) {
        this.projectTimeBegin = projectTimeBegin;
    }

    public String getProjectTimeEnd() {
        return projectTimeEnd;
    }

    public void setProjectTimeEnd(String projectTimeEnd) {
        this.projectTimeEnd = projectTimeEnd;
    }

    public String getBudgetsumBefore() {
        return budgetsumBefore;
    }

    public void setBudgetsumBefore(String budgetsumBefore) {
        this.budgetsumBefore = budgetsumBefore;
    }

    public String getBudgetsumEnd() {
        return budgetsumEnd;
    }

    public void setBudgetsumEnd(String budgetsumEnd) {
        this.budgetsumEnd = budgetsumEnd;
    }

    public String getStateFundQuarterBefore() {
        return stateFundQuarterBefore;
    }

    public void setStateFundQuarterBefore(String stateFundQuarterBefore) {
        this.stateFundQuarterBefore = stateFundQuarterBefore;
    }

    public String getStateFundQuarterEnd() {
        return stateFundQuarterEnd;
    }

    public void setStateFundQuarterEnd(String stateFundQuarterEnd) {
        this.stateFundQuarterEnd = stateFundQuarterEnd;
    }

    public String getSelfFundedYearBefore() {
        return selfFundedYearBefore;
    }

    public void setSelfFundedYearBefore(String selfFundedYearBefore) {
        this.selfFundedYearBefore = selfFundedYearBefore;
    }

    public String getSelfFundedYearEnd() {
        return selfFundedYearEnd;
    }

    public void setSelfFundedYearEnd(String selfFundedYearEnd) {
        this.selfFundedYearEnd = selfFundedYearEnd;
    }

    public String getSelfExecutionsYearBefore() {
        return selfExecutionsYearBefore;
    }

    public void setSelfExecutionsYearBefore(String selfExecutionsYearBefore) {
        this.selfExecutionsYearBefore = selfExecutionsYearBefore;
    }

    public String getSelfExecutionsYearEnd() {
        return selfExecutionsYearEnd;
    }

    public void setSelfExecutionsYearEnd(String selfExecutionsYearEnd) {
        this.selfExecutionsYearEnd = selfExecutionsYearEnd;
    }

    public String getStateYearExecutionsBefore() {
        return stateYearExecutionsBefore;
    }

    public void setStateYearExecutionsBefore(String stateYearExecutionsBefore) {
        this.stateYearExecutionsBefore = stateYearExecutionsBefore;
    }

    public String getStateYearExecutionsEnd() {
        return stateYearExecutionsEnd;
    }

    public void setStateYearExecutionsEnd(String stateYearExecutionsEnd) {
        this.stateYearExecutionsEnd = stateYearExecutionsEnd;
    }

    public String getStateFundYeahBefore() {
        return stateFundYeahBefore;
    }

    public void setStateFundYeahBefore(String stateFundYeahBefore) {
        this.stateFundYeahBefore = stateFundYeahBefore;
    }

    public String getStateFundYeahEnd() {
        return stateFundYeahEnd;
    }

    public void setStateFundYeahEnd(String stateFundYeahEnd) {
        this.stateFundYeahEnd = stateFundYeahEnd;
    }

    public String getSelfExecutionsBefore() {
        return selfExecutionsBefore;
    }

    public void setSelfExecutionsBefore(String selfExecutionsBefore) {
        this.selfExecutionsBefore = selfExecutionsBefore;
    }

    public String getSelfExecutionsEnd() {
        return selfExecutionsEnd;
    }

    public void setSelfExecutionsEnd(String selfExecutionsEnd) {
        this.selfExecutionsEnd = selfExecutionsEnd;
    }

    public String getSelfExecutionsQuarterBefore() {
        return selfExecutionsQuarterBefore;
    }

    public void setSelfExecutionsQuarterBefore(String selfExecutionsQuarterBefore) {
        this.selfExecutionsQuarterBefore = selfExecutionsQuarterBefore;
    }

    public String getSelfExecutionsQuarterEnd() {
        return selfExecutionsQuarterEnd;
    }

    public void setSelfExecutionsQuarterEnd(String selfExecutionsQuarterEnd) {
        this.selfExecutionsQuarterEnd = selfExecutionsQuarterEnd;
    }

    public String getSelfFundedQuarterBefore() {
        return selfFundedQuarterBefore;
    }

    public void setSelfFundedQuarterBefore(String selfFundedQuarterBefore) {
        this.selfFundedQuarterBefore = selfFundedQuarterBefore;
    }

    public String getSelfFundedQuarterEnd() {
        return selfFundedQuarterEnd;
    }

    public void setSelfFundedQuarterEnd(String selfFundedQuarterEnd) {
        this.selfFundedQuarterEnd = selfFundedQuarterEnd;
    }

    public String getStateQuarterExecutionsBefore() {
        return stateQuarterExecutionsBefore;
    }

    public void setStateQuarterExecutionsBefore(String stateQuarterExecutionsBefore) {
        this.stateQuarterExecutionsBefore = stateQuarterExecutionsBefore;
    }

    public String getStateQuarterExecutionsEnd() {
        return stateQuarterExecutionsEnd;
    }

    public void setStateQuarterExecutionsEnd(String stateQuarterExecutionsEnd) {
        this.stateQuarterExecutionsEnd = stateQuarterExecutionsEnd;
    }

    public String getArrivalRateBefore() {
        return arrivalRateBefore;
    }

    public void setArrivalRateBefore(String arrivalRateBefore) {
        this.arrivalRateBefore = arrivalRateBefore;
    }

    public String getArrivalRateEnd() {
        return arrivalRateEnd;
    }

    public void setArrivalRateEnd(String arrivalRateEnd) {
        this.arrivalRateEnd = arrivalRateEnd;
    }

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

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getExpertUserId() {
        return expertUserId;
    }

    public void setExpertUserId(String expertUserId) {
        this.expertUserId = expertUserId;
    }

    public String getStateExecutionsBefore() {
        return stateExecutionsBefore;
    }

    public void setStateExecutionsBefore(String stateExecutionsBefore) {
        this.stateExecutionsBefore = stateExecutionsBefore;
    }

    public String getStateExecutionsEnd() {
        return stateExecutionsEnd;
    }

    public void setStateExecutionsEnd(String stateExecutionsEnd) {
        this.stateExecutionsEnd = stateExecutionsEnd;
    }

    public String getFinancialDocument() {
        return financialDocument;
    }

    public void setFinancialDocument(String financialDocument) {
        this.financialDocument = financialDocument;
    }

    public String getFinancialDocumentOperator() {
        return financialDocumentOperator;
    }

    public void setFinancialDocumentOperator(String financialDocumentOperator) {
        this.financialDocumentOperator = financialDocumentOperator;
    }

    public String getPropertyChanges() {
        return propertyChanges;
    }

    public void setPropertyChanges(String propertyChanges) {
        this.propertyChanges = propertyChanges;
    }

    public String getPropertyChangesOperator() {
        return propertyChangesOperator;
    }

    public void setPropertyChangesOperator(String propertyChangesOperator) {
        this.propertyChangesOperator = propertyChangesOperator;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }

    public String getChangeStatusOperator() {
        return changeStatusOperator;
    }

    public void setChangeStatusOperator(String changeStatusOperator) {
        this.changeStatusOperator = changeStatusOperator;
    }

    public String getFundTemplateId() {
        return fundTemplateId;
    }

    public void setFundTemplateId(String fundTemplateId) {
        this.fundTemplateId = fundTemplateId;
    }

    public String getFundTemplateIdOperator() {
        return fundTemplateIdOperator;
    }

    public void setFundTemplateIdOperator(String fundTemplateIdOperator) {
        this.fundTemplateIdOperator = fundTemplateIdOperator;
    }

    public String getContractTemplate() {
        return contractTemplate;
    }

    public void setContractTemplate(String contractTemplate) {
        this.contractTemplate = contractTemplate;
    }

    public String getContractTemplateOperator() {
        return contractTemplateOperator;
    }

    public void setContractTemplateOperator(String contractTemplateOperator) {
        this.contractTemplateOperator = contractTemplateOperator;
    }
}
