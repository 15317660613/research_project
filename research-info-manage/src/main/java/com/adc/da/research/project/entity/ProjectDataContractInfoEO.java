package com.adc.da.research.project.entity;

import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>RS_PROJECT_DATA ProjectDataEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectDataContractInfoEO extends RContractInfoEO {

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

    private Double centerBudgetData;//数据中心预算
    private String reasonsRejection;//驳回原因
    private String judgeMethod;//评分方式
    private String subjectDirectorCode;//课题负责人工号
    private String technicalDirectorCode;//技术负责人工号
    private String projectApplicantCode;//经办人工号

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>researchTarget -> research_target</li>
     * <li>researchContent -> research_content</li>
     * <li>id -> id</li>
     * <li>projectCode -> project_code</li>
     * <li>projectName -> project_name</li>
     * <li>projectTypeId -> project_type_id</li>
     * <li>projectSource -> project_source</li>
     * <li>projectClass -> project_class</li>
     * <li>projectLeader -> project_leader</li>
     * <li>technicalDirector -> technical_director</li>
     * <li>subjectDirector -> subject_director</li>
     * <li>undertakingId -> undertaking_id</li>
     * <li>reportingUnitId -> reporting_unit_id</li>
     * <li>leadUnit -> lead_unit</li>
     * <li>participateUnit -> participate_unit</li>
     * <li>deptId -> dept_id</li>
     * <li>totalFunding -> total_funding</li>
     * <li>expectedExpenditure -> expected_expenditure</li>
     * <li>stateFunding -> state_funding</li>
     * <li>centerBudgetApply -> center_budget_apply</li>
     * <li>selfFunded -> self_funded</li>
     * <li>otherFunded -> other_funded</li>
     * <li>projectStatus -> project_status</li>
     * <li>startTime -> start_time</li>
     * <li>endTime -> end_time</li>
     * <li>applicationTime -> application_time</li>
     * <li>projectTime -> project_time</li>
     * <li>changeTime -> change_time</li>
     * <li>projectApplicantId -> project_applicant_id</li>
     * <li>projectApplicantName -> project_applicant_name</li>
     * <li>projectOverview -> project_overview</li>
     * <li>researchIndicators -> research_indicators</li>
     * <li>remark -> remark</li>
     * <li>delFlag -> del_flag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "researchTarget": return "research_target";
            case "researchContent": return "research_content";
            case "id": return "id";
            case "projectCode": return "project_code";
            case "projectName": return "project_name";
            case "projectTypeId": return "project_type_id";
            case "projectTypeName": return "project_type_name";
            case "projectSource": return "project_source";
            case "projectClass": return "project_class";
            case "projectLeader": return "project_leader";
            case "technicalDirector": return "technical_director";
            case "subjectDirector": return "subject_director";
            case "undertakingId": return "undertaking_id";
            case "undertakingName": return "undertaking_name";
            case "reportingUnitId": return "reporting_unit_id";
            case "reportingUnitName": return "reporting_unit_name";
            case "leadUnit": return "lead_unit";
            case "participateUnit": return "participate_unit";
            case "deptId": return "dept_id";
            case "deptName": return "dept_name";
            case "totalFunding": return "total_funding";
            case "expectedExpenditure": return "expected_expenditure";
            case "stateFunding": return "state_funding";
            case "centerBudgetApply": return "center_budget_apply";
            case "selfFunded": return "self_funded";
            case "otherFunded": return "other_funded";
            case "projectStatus": return "project_status";
            case "startTime": return "start_time";
            case "endTime": return "end_time";
            case "applicationTime": return "application_time";
            case "projectTime": return "project_time";
            case "changeTime": return "change_time";
            case "projectApplicantId": return "project_applicant_id";
            case "projectApplicantName": return "project_applicant_name";
            case "projectOverview": return "project_overview";
            case "researchIndicators": return "research_indicators";
            case "remark": return "remark";
            case "delFlag": return "del_flag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>research_target -> researchTarget</li>
     * <li>research_content -> researchContent</li>
     * <li>id -> id</li>
     * <li>project_code -> projectCode</li>
     * <li>project_name -> projectName</li>
     * <li>project_type_id -> projectTypeId</li>
     * <li>project_source -> projectSource</li>
     * <li>project_class -> projectClass</li>
     * <li>project_leader -> projectLeader</li>
     * <li>technical_director -> technicalDirector</li>
     * <li>subject_director -> subjectDirector</li>
     * <li>undertaking_id -> undertakingId</li>
     * <li>reporting_unit_id -> reportingUnitId</li>
     * <li>lead_unit -> leadUnit</li>
     * <li>participate_unit -> participateUnit</li>
     * <li>dept_id -> deptId</li>
     * <li>total_funding -> totalFunding</li>
     * <li>expected_expenditure -> expectedExpenditure</li>
     * <li>state_funding -> stateFunding</li>
     * <li>center_budget_apply -> centerBudgetApply</li>
     * <li>self_funded -> selfFunded</li>
     * <li>other_funded -> otherFunded</li>
     * <li>project_status -> projectStatus</li>
     * <li>start_time -> startTime</li>
     * <li>end_time -> endTime</li>
     * <li>application_time -> applicationTime</li>
     * <li>project_time -> projectTime</li>
     * <li>change_time -> changeTime</li>
     * <li>project_applicant_id -> projectApplicantId</li>
     * <li>project_applicant_name -> projectApplicantName</li>
     * <li>project_overview -> projectOverview</li>
     * <li>research_indicators -> researchIndicators</li>
     * <li>remark -> remark</li>
     * <li>del_flag -> delFlag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "research_target": return "researchTarget";
            case "research_content": return "researchContent";
            case "id": return "id";
            case "project_code": return "projectCode";
            case "project_name": return "projectName";
            case "project_type_id": return "projectTypeId";
            case "project_type_name": return "projectTypeName";
            case "project_source": return "projectSource";
            case "project_class": return "projectClass";
            case "project_leader": return "projectLeader";
            case "technical_director": return "technicalDirector";
            case "subject_director": return "subjectDirector";
            case "undertaking_id": return "undertakingId";
            case "undertaking_name": return "undertakingName";
            case "reporting_unit_id": return "reportingUnitId";
            case "reporting_unit_name": return "reportingUnitName";
            case "lead_unit": return "leadUnit";
            case "participate_unit": return "participateUnit";
            case "dept_id": return "deptId";
            case "dept_name": return "deptName";
            case "total_funding": return "totalFunding";
            case "expected_expenditure": return "expectedExpenditure";
            case "state_funding": return "stateFunding";
            case "center_budget_apply": return "centerBudgetApply";
            case "self_funded": return "selfFunded";
            case "other_funded": return "otherFunded";
            case "project_status": return "projectStatus";
            case "start_time": return "startTime";
            case "end_time": return "endTime";
            case "application_time": return "applicationTime";
            case "project_time": return "projectTime";
            case "change_time": return "changeTime";
            case "project_applicant_id": return "projectApplicantId";
            case "project_applicant_name": return "projectApplicantName";
            case "project_overview": return "projectOverview";
            case "research_indicators": return "researchIndicators";
            case "remark": return "remark";
            case "del_flag": return "delFlag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            default: return null;
        }
    }

    public String getUndertakingName() {
        return undertakingName;
    }

    public void setUndertakingName(String undertakingName) {
        this.undertakingName = undertakingName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getReportingUnitName() {
        return reportingUnitName;
    }

    public void setReportingUnitName(String reportingUnitName) {
        this.reportingUnitName = reportingUnitName;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    /**  **/
    public String getResearchTarget() {
        return this.researchTarget;
    }

    /**  **/
    public void setResearchTarget(String researchTarget) {
        this.researchTarget = researchTarget;
    }

    /**  **/
    public String getResearchContent() {
        return this.researchContent;
    }

    /**  **/
    public void setResearchContent(String researchContent) {
        this.researchContent = researchContent;
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
    public String getProjectCode() {
        return this.projectCode;
    }

    /**  **/
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    /**  **/
    public String getProjectName() {
        return this.projectName;
    }

    /**  **/
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**  **/
    public String getProjectTypeId() {
        return this.projectTypeId;
    }

    /**  **/
    public void setProjectTypeId(String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    /**  **/
    public String getProjectSource() {
        return this.projectSource;
    }

    /**  **/
    public void setProjectSource(String projectSource) {
        this.projectSource = projectSource;
    }

    /**  **/
    public String getProjectClass() {
        return this.projectClass;
    }

    /**  **/
    public void setProjectClass(String projectClass) {
        this.projectClass = projectClass;
    }

    /**  **/
    public String getProjectLeader() {
        return this.projectLeader;
    }

    /**  **/
    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    /**  **/
    public String getTechnicalDirector() {
        return this.technicalDirector;
    }

    /**  **/
    public void setTechnicalDirector(String technicalDirector) {
        this.technicalDirector = technicalDirector;
    }

    /**  **/
    public String getSubjectDirector() {
        return this.subjectDirector;
    }

    /**  **/
    public void setSubjectDirector(String subjectDirector) {
        this.subjectDirector = subjectDirector;
    }

    /**  **/
    public String getUndertakingId() {
        return this.undertakingId;
    }

    /**  **/
    public void setUndertakingId(String undertakingId) {
        this.undertakingId = undertakingId;
    }

    /**  **/
    public String getReportingUnitId() {
        return this.reportingUnitId;
    }

    /**  **/
    public void setReportingUnitId(String reportingUnitId) {
        this.reportingUnitId = reportingUnitId;
    }

    /**  **/
    public String getLeadUnit() {
        return this.leadUnit;
    }

    /**  **/
    public void setLeadUnit(String leadUnit) {
        this.leadUnit = leadUnit;
    }

    /**  **/
    public String getParticipateUnit() {
        return this.participateUnit;
    }

    /**  **/
    public void setParticipateUnit(String participateUnit) {
        this.participateUnit = participateUnit;
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
    public Double getTotalFunding() {
        return this.totalFunding;
    }

    /**  **/
    public void setTotalFunding(Double totalFunding) {
        this.totalFunding = totalFunding;
    }

    /**  **/
    public Double getExpectedExpenditure() {
        return this.expectedExpenditure;
    }

    /**  **/
    public void setExpectedExpenditure(Double expectedExpenditure) {
        this.expectedExpenditure = expectedExpenditure;
    }

    /**  **/
    public Double getStateFunding() {
        return this.stateFunding;
    }

    /**  **/
    public void setStateFunding(Double stateFunding) {
        this.stateFunding = stateFunding;
    }

    /**  **/
    public Double getCenterBudgetApply() {
        return this.centerBudgetApply;
    }

    /**  **/
    public void setCenterBudgetApply(Double centerBudgetApply) {
        this.centerBudgetApply = centerBudgetApply;
    }

    /**  **/
    public Double getSelfFunded() {
        return this.selfFunded;
    }

    /**  **/
    public void setSelfFunded(Double selfFunded) {
        this.selfFunded = selfFunded;
    }

    /**  **/
    public Double getOtherFunded() {
        return this.otherFunded;
    }

    /**  **/
    public void setOtherFunded(Double otherFunded) {
        this.otherFunded = otherFunded;
    }

    /**  **/
    public String getProjectStatus() {
        return this.projectStatus;
    }

    /**  **/
    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    /**  **/
    public Date getStartTime() {
        return this.startTime;
    }

    /**  **/
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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
    public Date getApplicationTime() {
        return this.applicationTime;
    }

    /**  **/
    public void setApplicationTime(Date applicationTime) {
        this.applicationTime = applicationTime;
    }

    /**  **/
    public Date getProjectTime() {
        return this.projectTime;
    }

    /**  **/
    public void setProjectTime(Date projectTime) {
        this.projectTime = projectTime;
    }

    /**  **/
    public Date getChangeTime() {
        return this.changeTime;
    }

    /**  **/
    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    /**  **/
    public String getProjectApplicantId() {
        return this.projectApplicantId;
    }

    /**  **/
    public void setProjectApplicantId(String projectApplicantId) {
        this.projectApplicantId = projectApplicantId;
    }

    /**  **/
    public String getProjectApplicantName() {
        return this.projectApplicantName;
    }

    /**  **/
    public void setProjectApplicantName(String projectApplicantName) {
        this.projectApplicantName = projectApplicantName;
    }

    /**  **/
    public String getProjectOverview() {
        return this.projectOverview;
    }

    /**  **/
    public void setProjectOverview(String projectOverview) {
        this.projectOverview = projectOverview;
    }

    /**  **/
    public String getResearchIndicators() {
        return this.researchIndicators;
    }

    /**  **/
    public void setResearchIndicators(String researchIndicators) {
        this.researchIndicators = researchIndicators;
    }

    /**  **/
    public String getRemark() {
        return this.remark;
    }

    /**  **/
    public void setRemark(String remark) {
        this.remark = remark;
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
    public String getExt1() {
        return this.ext1;
    }

    /**  **/
    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    /**  **/
    public String getExt2() {
        return this.ext2;
    }

    /**  **/
    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    /**  **/
    public String getExt3() {
        return this.ext3;
    }

    /**  **/
    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    /**  **/
    public String getExt4() {
        return this.ext4;
    }

    /**  **/
    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    /**  **/
    public String getExt5() {
        return this.ext5;
    }

    /**  **/
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

    public Double getCenterBudgetData() {
        return centerBudgetData;
    }

    public void setCenterBudgetData(Double centerBudgetData) {
        this.centerBudgetData = centerBudgetData;
    }

    public String getReasonsRejection() {
        return reasonsRejection;
    }

    public void setReasonsRejection(String reasonsRejection) {
        this.reasonsRejection = reasonsRejection;
    }

    public String getJudgeMethod() {
        return judgeMethod;
    }

    public void setJudgeMethod(String judgeMethod) {
        this.judgeMethod = judgeMethod;
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

    public List<AnnexFileEO> getAnnexFileEOList() {
        return annexFileEOList;
    }

    public void setAnnexFileEOList(List<AnnexFileEO> annexFileEOList) {
        this.annexFileEOList = annexFileEOList;
    }


}
