package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import java.util.Date;

/**
 * <b>功能：</b>TS_PROJECT_HISTORY TsProjectHistoryEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectHistoryEO extends BaseEntity {

    /**
     * 部门ID
     */
    private String deptId;
    /**
     * 项目组成员ID
     */
    private String projectMemberIds;
    /**
     * 项目组成员姓名
     */
    private String projectMemberNames;
    /**
     * 业务ID
     */
    private String budgetId;
    /**
     * 业务类型ID
     */
    private String businessId;
    /**
     * 业务方
     */
    private String projectOwner;
    /**
     * 项目开始时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date projectStartTime;
    /**
     * 项目完成状态
     */
    private String finishedStatus;
    /**
     * 人力投入（人/天）
     */
    private Integer personInput;
    /**
     * 删除标记
     */
    private Integer delFlag;
    /**
     * 合同编号
     */
    private String contractNo;
    /**
     * 操作日期
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operateDate;
    /**
     * 操作时间
     */
    private String operateTime;
    /**
     * 操作人
     */
    private String operateUser;
    /**
     * 操作类型
     */
    private String operateType;
    /**
     * 数据创建人
     */
    private String createUserId;
    /**
     * 主键
     */
    private String id;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目描述
     */
    private String projectDescription;
    /**
     * 项目负责人ID
     */
    private String projectLeaderId;
    /**
     * 项目负责人姓名
     */
    private String projectLeaderName;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>deptId -> dept_id</li>
     * <li>projectMemberIds -> project_member_ids</li>
     * <li>projectMemberNames -> project_member_names</li>
     * <li>budgetId -> budget_id</li>
     * <li>businessId -> business_id</li>
     * <li>projectOwner -> project_owner</li>
     * <li>projectStartTime -> project_start_time</li>
     * <li>finishedStatus -> finished_status</li>
     * <li>personInput -> person_input</li>
     * <li>delFlag -> del_flag</li>
     * <li>contractNo -> contract_no</li>
     * <li>operateDate -> operate_date</li>
     * <li>operateTime -> operate_time</li>
     * <li>operateUser -> operate_user</li>
     * <li>operateType -> operate_type</li>
     * <li>createUserId -> create_user_id</li>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>projectName -> project_name</li>
     * <li>projectDescription -> project_description</li>
     * <li>projectLeaderId -> project_leader_id</li>
     * <li>projectLeaderName -> project_leader_name</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "deptId": return "dept_id";
            case "projectMemberIds": return "project_member_ids";
            case "projectMemberNames": return "project_member_names";
            case "budgetId": return "budget_id";
            case "businessId": return "business_id";
            case "projectOwner": return "project_owner";
            case "projectStartTime": return "project_start_time";
            case "finishedStatus": return "finished_status";
            case "personInput": return "person_input";
            case "delFlag": return "del_flag";
            case "contractNo": return "contract_no";
            case "operateDate": return "operate_date";
            case "operateTime": return "operate_time";
            case "operateUser": return "operate_user";
            case "operateType": return "operate_type";
            case "createUserId": return "create_user_id";
            case "id": return "id";
            case "projectId": return "project_id";
            case "projectName": return "project_name";
            case "projectDescription": return "project_description";
            case "projectLeaderId": return "project_leader_id";
            case "projectLeaderName": return "project_leader_name";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>dept_id -> deptId</li>
     * <li>project_member_ids -> projectMemberIds</li>
     * <li>project_member_names -> projectMemberNames</li>
     * <li>budget_id -> budgetId</li>
     * <li>business_id -> businessId</li>
     * <li>project_owner -> projectOwner</li>
     * <li>project_start_time -> projectStartTime</li>
     * <li>finished_status -> finishedStatus</li>
     * <li>person_input -> personInput</li>
     * <li>del_flag -> delFlag</li>
     * <li>contract_no -> contractNo</li>
     * <li>operate_date -> operateDate</li>
     * <li>operate_time -> operateTime</li>
     * <li>operate_user -> operateUser</li>
     * <li>operate_type -> operateType</li>
     * <li>create_user_id -> createUserId</li>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>project_name -> projectName</li>
     * <li>project_description -> projectDescription</li>
     * <li>project_leader_id -> projectLeaderId</li>
     * <li>project_leader_name -> projectLeaderName</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "dept_id": return "deptId";
            case "project_member_ids": return "projectMemberIds";
            case "project_member_names": return "projectMemberNames";
            case "budget_id": return "budgetId";
            case "business_id": return "businessId";
            case "project_owner": return "projectOwner";
            case "project_start_time": return "projectStartTime";
            case "finished_status": return "finishedStatus";
            case "person_input": return "personInput";
            case "del_flag": return "delFlag";
            case "contract_no": return "contractNo";
            case "operate_date": return "operateDate";
            case "operate_time": return "operateTime";
            case "operate_user": return "operateUser";
            case "operate_type": return "operateType";
            case "create_user_id": return "createUserId";
            case "id": return "id";
            case "project_id": return "projectId";
            case "project_name": return "projectName";
            case "project_description": return "projectDescription";
            case "project_leader_id": return "projectLeaderId";
            case "project_leader_name": return "projectLeaderName";
            default: return null;
        }
    }
    
    /**
     * get 部门ID
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * set 部门ID
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    /**
     * get 项目组成员ID
     */
    public String getProjectMemberIds() {
        return projectMemberIds;
    }

    /**
     * set 项目组成员ID
     */
    public void setProjectMemberIds(String projectMemberIds) {
        this.projectMemberIds = projectMemberIds;
    }
    /**
     * get 项目组成员姓名
     */
    public String getProjectMemberNames() {
        return projectMemberNames;
    }

    /**
     * set 项目组成员姓名
     */
    public void setProjectMemberNames(String projectMemberNames) {
        this.projectMemberNames = projectMemberNames;
    }
    /**
     * get 业务ID
     */
    public String getBudgetId() {
        return budgetId;
    }

    /**
     * set 业务ID
     */
    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }
    /**
     * get 业务类型ID
     */
    public String getBusinessId() {
        return businessId;
    }

    /**
     * set 业务类型ID
     */
    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
    /**
     * get 业务方
     */
    public String getProjectOwner() {
        return projectOwner;
    }

    /**
     * set 业务方
     */
    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
    }
    /**
     * get 项目开始时间
     */
    public Date getProjectStartTime() {
        return projectStartTime;
    }

    /**
     * set 项目开始时间
     */
    public void setProjectStartTime(Date projectStartTime) {
        this.projectStartTime = projectStartTime;
    }
    /**
     * get 项目完成状态
     */
    public String getFinishedStatus() {
        return finishedStatus;
    }

    /**
     * set 项目完成状态
     */
    public void setFinishedStatus(String finishedStatus) {
        this.finishedStatus = finishedStatus;
    }
    /**
     * get 人力投入（人/天）
     */
    public Integer getPersonInput() {
        return personInput;
    }

    /**
     * set 人力投入（人/天）
     */
    public void setPersonInput(Integer personInput) {
        this.personInput = personInput;
    }
    /**
     * get 删除标记
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * set 删除标记
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
    /**
     * get 合同编号
     */
    public String getContractNo() {
        return contractNo;
    }

    /**
     * set 合同编号
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    /**
     * get 操作日期
     */
    public Date getOperateDate() {
        return operateDate;
    }

    /**
     * set 操作日期
     */
    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }
    /**
     * get 操作时间
     */
    public String getOperateTime() {
        return operateTime;
    }

    /**
     * set 操作时间
     */
    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }
    /**
     * get 操作人
     */
    public String getOperateUser() {
        return operateUser;
    }

    /**
     * set 操作人
     */
    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }
    /**
     * get 操作类型
     */
    public String getOperateType() {
        return operateType;
    }

    /**
     * set 操作类型
     */
    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }
    /**
     * get 数据创建人
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * set 数据创建人
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
    /**
     * get 主键
     */
    public String getId() {
        return id;
    }

    /**
     * set 主键
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * get 项目ID
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * set 项目ID
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    /**
     * get 项目名称
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * set 项目名称
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    /**
     * get 项目描述
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    /**
     * set 项目描述
     */
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
    /**
     * get 项目负责人ID
     */
    public String getProjectLeaderId() {
        return projectLeaderId;
    }

    /**
     * set 项目负责人ID
     */
    public void setProjectLeaderId(String projectLeaderId) {
        this.projectLeaderId = projectLeaderId;
    }
    /**
     * get 项目负责人姓名
     */
    public String getProjectLeaderName() {
        return projectLeaderName;
    }

    /**
     * set 项目负责人姓名
     */
    public void setProjectLeaderName(String projectLeaderName) {
        this.projectLeaderName = projectLeaderName;
    }
}
