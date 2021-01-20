package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import java.util.Date;

/**
 * <b>功能：</b>TS_TASK_HISTORY TsTaskHistoryEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class TaskHistoryEO extends BaseEntity {

    /**
     * 主键
     */
    private String id;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 业务ID
     */
    private String budgetId;
    /**
     * 部门ID
     */
    private String deptId;
    /**
     * 参与成员ID
     */
    private String memberIds;
    /**
     * 参与成员姓名
     */
    private String memberNames;
    /**
     * 任务计划开始时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;
    /**
     * 任务计划结束时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;
    /**
     * 优先级
     */
    private String priority;
    /**
     * 删除标记
     */
    private Integer delFlag;
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
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>taskId -> task_id</li>
     * <li>taskName -> task_name</li>
     * <li>projectId -> project_id</li>
     * <li>budgetId -> budget_id</li>
     * <li>deptId -> dept_id</li>
     * <li>memberIds -> member_ids</li>
     * <li>memberNames -> member_names</li>
     * <li>planStartTime -> plan_start_time</li>
     * <li>planEndTime -> plan_end_time</li>
     * <li>priority -> priority</li>
     * <li>delFlag -> del_flag</li>
     * <li>operateDate -> operate_date</li>
     * <li>operateTime -> operate_time</li>
     * <li>operateUser -> operate_user</li>
     * <li>operateType -> operate_type</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "taskId": return "task_id";
            case "taskName": return "task_name";
            case "projectId": return "project_id";
            case "budgetId": return "budget_id";
            case "deptId": return "dept_id";
            case "memberIds": return "member_ids";
            case "memberNames": return "member_names";
            case "planStartTime": return "plan_start_time";
            case "planEndTime": return "plan_end_time";
            case "priority": return "priority";
            case "delFlag": return "del_flag";
            case "operateDate": return "operate_date";
            case "operateTime": return "operate_time";
            case "operateUser": return "operate_user";
            case "operateType": return "operate_type";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>task_id -> taskId</li>
     * <li>task_name -> taskName</li>
     * <li>project_id -> projectId</li>
     * <li>budget_id -> budgetId</li>
     * <li>dept_id -> deptId</li>
     * <li>member_ids -> memberIds</li>
     * <li>member_names -> memberNames</li>
     * <li>plan_start_time -> planStartTime</li>
     * <li>plan_end_time -> planEndTime</li>
     * <li>priority -> priority</li>
     * <li>del_flag -> delFlag</li>
     * <li>operate_date -> operateDate</li>
     * <li>operate_time -> operateTime</li>
     * <li>operate_user -> operateUser</li>
     * <li>operate_type -> operateType</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "task_id": return "taskId";
            case "task_name": return "taskName";
            case "project_id": return "projectId";
            case "budget_id": return "budgetId";
            case "dept_id": return "deptId";
            case "member_ids": return "memberIds";
            case "member_names": return "memberNames";
            case "plan_start_time": return "planStartTime";
            case "plan_end_time": return "planEndTime";
            case "priority": return "priority";
            case "del_flag": return "delFlag";
            case "operate_date": return "operateDate";
            case "operate_time": return "operateTime";
            case "operate_user": return "operateUser";
            case "operate_type": return "operateType";
            default: return null;
        }
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
     * get 任务ID
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * set 任务ID
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    /**
     * get 任务名称
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * set 任务名称
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
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
     * get 参与成员ID
     */
    public String getMemberIds() {
        return memberIds;
    }

    /**
     * set 参与成员ID
     */
    public void setMemberIds(String memberIds) {
        this.memberIds = memberIds;
    }
    /**
     * get 参与成员姓名
     */
    public String getMemberNames() {
        return memberNames;
    }

    /**
     * set 参与成员姓名
     */
    public void setMemberNames(String memberNames) {
        this.memberNames = memberNames;
    }
    /**
     * get 任务计划开始时间
     */
    public Date getPlanStartTime() {
        return planStartTime;
    }

    /**
     * set 任务计划开始时间
     */
    public void setPlanStartTime(Date planStartTime) {
        this.planStartTime = planStartTime;
    }
    /**
     * get 任务计划结束时间
     */
    public Date getPlanEndTime() {
        return planEndTime;
    }

    /**
     * set 任务计划结束时间
     */
    public void setPlanEndTime(Date planEndTime) {
        this.planEndTime = planEndTime;
    }
    /**
     * get 优先级
     */
    public String getPriority() {
        return priority;
    }

    /**
     * set 优先级
     */
    public void setPriority(String priority) {
        this.priority = priority;
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
}
