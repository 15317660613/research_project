package com.adc.da.research.project.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>RS_PROJECT_DATA ProjectDataEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ProjectDataEODto implements IExcelDataModel, IExcelModel {


    private String id;
    @Excel(name = "*项目编号",orderNum = "1",width = 50)
    private String projectCode;
    @Excel(name = "*项目名称",orderNum = "2",width = 50)
    private String projectName;

    private String projectTypeId;

    @Excel(name = "*项目来源",orderNum = "3",width = 50)
    private String projectSource;
    @Excel(name = "*类型",orderNum = "4",width = 50)
    private String projectClass;

    private String projectLeader;

    @Excel(name = "*技术负责人",orderNum = "5",width = 50)
    private String technicalDirector;

    private String subjectDirector;

    @Excel(name = "*承担方式",orderNum = "6",width = 50)
    private String undertakingId;
    @Excel(name = "*申报单位",orderNum = "7",width = 50)
    private String reportingUnitId;
    @Excel(name = "*牵头单位",orderNum = "8",width = 50)
    private String leadUnit;
    @Excel(name = "*参与单位",orderNum = "9",width = 50)
    private String participateUnit;

    private String deptId;
    private Double totalFunding;
    @Excel(name = "*预期经费（万元）",orderNum = "10",width = 50)
    private Double expectedExpenditure;
    @Excel(name = "*国拨经费（万元）",orderNum = "11",width = 50)
    private Double stateFunding;
    @Excel(name = "*申请中心预算（万元）",orderNum = "12",width = 50)
    private Double centerBudgetApply;
    @Excel(name = "*部门自筹（万元）",orderNum = "13",width = 50)
    private Double selfFunded;
    @Excel(name = "*其他经费（万元）",orderNum = "14",width = 50)
    private Double otherFunded;
    @Excel(name = "*状态",orderNum = "15",width = 50)
    private String projectStatus;
    @Excel(name = "研究开始时间", orderNum = "16",format="yyyy-MM-dd")
    private Date startTime;
    @Excel(name = "研究结束时间", orderNum = "17",format="yyyy-MM-dd")
    private Date endTime;
    @Excel(name = "申报申请时间", orderNum = "18",format="yyyy-MM-dd")
    private Date applicationTime;
    @Excel(name = "立项时间", orderNum = "19",format="yyyy-MM-dd")
    private Date projectTime;
    @Excel(name = "变更时间", orderNum = "20",format="yyyy-MM-dd")
    private Date changeTime;

    private String projectApplicantId;
    private String projectApplicantName;
    @Excel(name = "项目概述",orderNum = "21",width = 50)
    private String projectOverview;
    @Excel(name = "科研指标",orderNum = "22",width = 50)
    private String researchIndicators;
    @Excel(name = "备注",orderNum = "22",width = 50)
    private String remark;
    private Integer delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    @Excel(name = "阶段",orderNum = "23",width = 50)
    private String stageName;
    @Excel(name = "研究目标",orderNum = "24",width = 50)
    private String researchTarget;
    @Excel(name = "主要研究内容",orderNum = "25",width = 50)
    private String researchContent;
    private int rowNum;

    private String errorMsg;
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
            case "projectSource": return "project_source";
            case "projectClass": return "project_class";
            case "projectLeader": return "project_leader";
            case "technicalDirector": return "technical_director";
            case "subjectDirector": return "subject_director";
            case "undertakingId": return "undertaking_id";
            case "reportingUnitId": return "reporting_unit_id";
            case "leadUnit": return "lead_unit";
            case "participateUnit": return "participate_unit";
            case "deptId": return "dept_id";
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



}
