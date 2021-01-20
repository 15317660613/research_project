package com.adc.da.research.project.vo;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.research.project.entity.AnnexFileEO;
import com.adc.da.research.project.entity.JudgeInfoEO;

import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>RS_PROJECT_DATA ProjectDataEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectDataVO extends BaseEntity {

    private String researchTarget;//研究目标
    private String researchContent;//主要研究内容
    private String id;
    private String projectCode;//项目编号
    private String projectName;//项目名称
    private String projectTypeId;//项目类别ID
    private String projectTypeName;//项目类别名称
    private String projectSource;//项目来源
    private String projectClass;//类型
    private String projectLeader;//项目负责人(不使用)
    private String technicalDirector;//技术负责人
    private String subjectDirector;//课题负责人
    private String undertakingId;//承担方式ID
    private String undertakingName;//承担方式名称
    private String reportingUnitId;//申报单位id
    private String reportingUnitName;//申报单位名称
    private String leadUnit;//牵头单位
    private String participateUnit;//参与单位
    private String deptId;//申报部门
    private String deptName;//申报部门名称
    private Double totalFunding;//经费总额
    private Double expectedExpenditure;//预期经费
    private Double stateFunding;//国拨经费
    private Double centerBudgetApply;//申请中心预算
    private Double selfFunded;//部门自筹
    private Double otherFunded;//其他
    private String projectStatus;//状态-->审批中11、审批通过10、审批未通过12、 待审批13||待填报10、撤回待填报100、驳回待填报101、待评审23、评审中21、申报中20、准备签约26
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;//研究开始时间
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endTime;//研究结束时间
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date applicationTime;//申报申请时间
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date projectTime;//立项时间
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date changeTime;//变更时间
    private String projectApplicantId;//(经办人)项目申请人ID
    private String projectApplicantName;//(经办人)项目申请人姓名
    private String projectOverview;//项目概述
    private String researchIndicators;//科研指标
    private String remark;//备注
    private Integer delFlag = 0;//删除标记
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private String stageName;//流程阶段名称
    private String stageId;//流程阶段id
    private String implementationId;//流程执行过程id
    private List<JudgeInfoEO> judgeInfoEOs;
    private List<AnnexFileEO> annexFileEOList;

    private String subjectDirectorCode;//课题负责人工号
    private String technicalDirectorCode;//技术负责人工号
    private String projectApplicantCode;//经办人工号

    private String token;//用户签名，用户验证用户有效性

    public String getResearchTarget() {
        return researchTarget;
    }

    public void setResearchTarget(String researchTarget) {
        this.researchTarget = researchTarget;
    }

    public String getResearchContent() {
        return researchContent;
    }

    public void setResearchContent(String researchContent) {
        this.researchContent = researchContent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getProjectSource() {
        return projectSource;
    }

    public void setProjectSource(String projectSource) {
        this.projectSource = projectSource;
    }

    public String getProjectClass() {
        return projectClass;
    }

    public void setProjectClass(String projectClass) {
        this.projectClass = projectClass;
    }

    public String getProjectLeader() {
        return projectLeader;
    }

    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    public String getTechnicalDirector() {
        return technicalDirector;
    }

    public void setTechnicalDirector(String technicalDirector) {
        this.technicalDirector = technicalDirector;
    }

    public String getSubjectDirector() {
        return subjectDirector;
    }

    public void setSubjectDirector(String subjectDirector) {
        this.subjectDirector = subjectDirector;
    }

    public String getUndertakingId() {
        return undertakingId;
    }

    public void setUndertakingId(String undertakingId) {
        this.undertakingId = undertakingId;
    }

    public String getUndertakingName() {
        return undertakingName;
    }

    public void setUndertakingName(String undertakingName) {
        this.undertakingName = undertakingName;
    }

    public String getReportingUnitId() {
        return reportingUnitId;
    }

    public void setReportingUnitId(String reportingUnitId) {
        this.reportingUnitId = reportingUnitId;
    }

    public String getReportingUnitName() {
        return reportingUnitName;
    }

    public void setReportingUnitName(String reportingUnitName) {
        this.reportingUnitName = reportingUnitName;
    }

    public String getLeadUnit() {
        return leadUnit;
    }

    public void setLeadUnit(String leadUnit) {
        this.leadUnit = leadUnit;
    }

    public String getParticipateUnit() {
        return participateUnit;
    }

    public void setParticipateUnit(String participateUnit) {
        this.participateUnit = participateUnit;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Double getTotalFunding() {
        return totalFunding;
    }

    public void setTotalFunding(Double totalFunding) {
        this.totalFunding = totalFunding;
    }

    public Double getExpectedExpenditure() {
        return expectedExpenditure;
    }

    public void setExpectedExpenditure(Double expectedExpenditure) {
        this.expectedExpenditure = expectedExpenditure;
    }

    public Double getStateFunding() {
        return stateFunding;
    }

    public void setStateFunding(Double stateFunding) {
        this.stateFunding = stateFunding;
    }

    public Double getCenterBudgetApply() {
        return centerBudgetApply;
    }

    public void setCenterBudgetApply(Double centerBudgetApply) {
        this.centerBudgetApply = centerBudgetApply;
    }

    public Double getSelfFunded() {
        return selfFunded;
    }

    public void setSelfFunded(Double selfFunded) {
        this.selfFunded = selfFunded;
    }

    public Double getOtherFunded() {
        return otherFunded;
    }

    public void setOtherFunded(Double otherFunded) {
        this.otherFunded = otherFunded;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    public Date getProjectTime() {
        return projectTime;
    }

    public void setProjectTime(Date projectTime) {
        this.projectTime = projectTime;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public String getProjectApplicantId() {
        return projectApplicantId;
    }

    public void setProjectApplicantId(String projectApplicantId) {
        this.projectApplicantId = projectApplicantId;
    }

    public String getProjectApplicantName() {
        return projectApplicantName;
    }

    public void setProjectApplicantName(String projectApplicantName) {
        this.projectApplicantName = projectApplicantName;
    }

    public String getProjectOverview() {
        return projectOverview;
    }

    public void setProjectOverview(String projectOverview) {
        this.projectOverview = projectOverview;
    }

    public String getResearchIndicators() {
        return researchIndicators;
    }

    public void setResearchIndicators(String researchIndicators) {
        this.researchIndicators = researchIndicators;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    public String getExt5() {
        return ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getImplementationId() {
        return implementationId;
    }

    public void setImplementationId(String implementationId) {
        this.implementationId = implementationId;
    }

    public List<JudgeInfoEO> getJudgeInfoEOs() {
        return judgeInfoEOs;
    }

    public void setJudgeInfoEOs(List<JudgeInfoEO> judgeInfoEOs) {
        this.judgeInfoEOs = judgeInfoEOs;
    }

    public List<AnnexFileEO> getAnnexFileEOList() {
        return annexFileEOList;
    }

    public void setAnnexFileEOList(List<AnnexFileEO> annexFileEOList) {
        this.annexFileEOList = annexFileEOList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSubjectDirectorCode() {
        return subjectDirectorCode;
    }

    public void setSubjectDirectorCode(String subjectDirectorCode) {
        this.subjectDirectorCode = subjectDirectorCode;
    }

    public String getTechnicalDirectorCode() {
        return technicalDirectorCode;
    }

    public void setTechnicalDirectorCode(String technicalDirectorCode) {
        this.technicalDirectorCode = technicalDirectorCode;
    }

    public String getProjectApplicantCode() {
        return projectApplicantCode;
    }

    public void setProjectApplicantCode(String projectApplicantCode) {
        this.projectApplicantCode = projectApplicantCode;
    }
}
