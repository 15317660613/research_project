package com.adc.da.crm.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Clob;
import java.util.Date;

/**
 * <b>功能：</b>PROJECT_CLOSURE_APPROVAL ProjectClosureApprovalEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectClosureApprovalEO extends BaseEntity {
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
    private String id;
    
    @MatchField("项目经理")
    private String projecrManagerId;
    
    @MatchField("所属部门")
    private String deptId;
    
    @MatchField("结束日期")
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    
    private String projectId;
    
    @MatchField("项目编号")
    private String projectNumber;
    
    @MatchField("项目名称")
    private String projectName;
    
    @MatchField("项目描述")
    private String discription;
    
    @MatchField("成果文件")
    private String achievementFile;
    
    @MatchField("项目阶段")
    private String proPeriod;
    
    @MatchField("部门意见")
    private String deptHeadOpinion;
    
    @MatchField("领导意见")
    private String leaderOpinion;
    
    private String delFlag;
    
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    
    private String createdUser;
    
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;
    
    private String modifiedUser;
    
    private String checkStatus;
    
    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projecrManagerId -> projecr_manager_id</li>
     * <li>deptId -> dept_id</li>
     * <li>endDate -> end_date</li>
     * <li>projectId -> project_id</li>
     * <li>projectNumber -> project_number</li>
     * <li>projectName -> project_name</li>
     * <li>discription -> discription</li>
     * <li>achievementFile -> achievement_file</li>
     * <li>proPeriod -> pro_period</li>
     * <li>deptHeadOpinion -> dept_head_opinion</li>
     * <li>leaderOpinion -> leader_opinion</li>
     * <li>delFlag -> del_flag</li>
     * <li>createdTime -> created_time</li>
     * <li>createdUser -> created_user</li>
     * <li>modifiedTime -> modified_time</li>
     * <li>modifiedUser -> modified_user</li>
     * <li>checkStatus -> check_status</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id":
                return "id";
            case "projecrManagerId":
                return "projecr_manager_id";
            case "deptId":
                return "dept_id";
            case "endDate":
                return "end_date";
            case "projectId":
                return "project_id";
            case "projectNumber":
                return "project_number";
            case "projectName":
                return "project_name";
            case "discription":
                return "discription";
            case "achievementFile":
                return "achievement_file";
            case "proPeriod":
                return "pro_period";
            case "deptHeadOpinion":
                return "dept_head_opinion";
            case "leaderOpinion":
                return "leader_opinion";
            case "delFlag":
                return "del_flag";
            case "createdTime":
                return "created_time";
            case "createdUser":
                return "created_user";
            case "modifiedTime":
                return "modified_time";
            case "modifiedUser":
                return "modified_user";
            case "checkStatus":
                return "check_status";
            default:
                return null;
        }
    }
    
    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projecr_manager_id -> projecrManagerId</li>
     * <li>dept_id -> deptId</li>
     * <li>end_date -> endDate</li>
     * <li>project_id -> projectId</li>
     * <li>project_number -> projectNumber</li>
     * <li>project_name -> projectName</li>
     * <li>discription -> discription</li>
     * <li>achievement_file -> achievementFile</li>
     * <li>pro_period -> proPeriod</li>
     * <li>dept_head_opinion -> deptHeadOpinion</li>
     * <li>leader_opinion -> leaderOpinion</li>
     * <li>del_flag -> delFlag</li>
     * <li>created_time -> createdTime</li>
     * <li>created_user -> createdUser</li>
     * <li>modified_time -> modifiedTime</li>
     * <li>modified_user -> modifiedUser</li>
     * <li>check_status -> checkStatus</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id":
                return "id";
            case "projecr_manager_id":
                return "projecrManagerId";
            case "dept_id":
                return "deptId";
            case "end_date":
                return "endDate";
            case "project_id":
                return "projectId";
            case "project_number":
                return "projectNumber";
            case "project_name":
                return "projectName";
            case "discription":
                return "discription";
            case "achievement_file":
                return "achievementFile";
            case "pro_period":
                return "proPeriod";
            case "dept_head_opinion":
                return "deptHeadOpinion";
            case "leader_opinion":
                return "leaderOpinion";
            case "del_flag":
                return "delFlag";
            case "created_time":
                return "createdTime";
            case "created_user":
                return "createdUser";
            case "modified_time":
                return "modifiedTime";
            case "modified_user":
                return "modifiedUser";
            case "check_status":
                return "checkStatus";
            default:
                return null;
        }
    }
    
    /**
     *
     **/
    public String getId() {
        return this.id;
    }
    
    /**
     *
     **/
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     *
     **/
    public String getProjecrManagerId() {
        return this.projecrManagerId;
    }
    
    /**
     *
     **/
    public void setProjecrManagerId(String projecrManagerId) {
        this.projecrManagerId = projecrManagerId;
    }
    
    /**
     *
     **/
    public String getDeptId() {
        return this.deptId;
    }
    
    /**
     *
     **/
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    
    /**
     *
     **/
    public Date getEndDate() {
        return this.endDate;
    }
    
    /**
     *
     **/
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    /**
     *
     **/
    public String getProjectId() {
        return this.projectId;
    }
    
    /**
     *
     **/
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    
    /**
     *
     **/
    public String getProjectNumber() {
        return this.projectNumber;
    }
    
    /**
     *
     **/
    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }
    
    /**
     *
     **/
    public String getProjectName() {
        return this.projectName;
    }
    
    /**
     *
     **/
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    
    /**
     *
     **/
    public String getDiscription() {
        return this.discription;
    }
    
    /**
     *
     **/
    public void setDiscription(String discription) {
        this.discription = discription;
    }
    
    /**
     *
     **/
    public String getAchievementFile() {
        return this.achievementFile;
    }
    
    /**
     *
     **/
    public void setAchievementFile(String achievementFile) {
        this.achievementFile = achievementFile;
    }
    
    /**
     *
     **/
    public String getProPeriod() {
        return this.proPeriod;
    }
    
    /**
     *
     **/
    public void setProPeriod(String proPeriod) {
        this.proPeriod = proPeriod;
    }
    
    /**
     *
     **/
    public String getDeptHeadOpinion() {
        return this.deptHeadOpinion;
    }
    
    /**
     *
     **/
    public void setDeptHeadOpinion(String deptHeadOpinion) {
        this.deptHeadOpinion = deptHeadOpinion;
    }
    
    /**
     *
     **/
    public String getLeaderOpinion() {
        return this.leaderOpinion;
    }
    
    /**
     *
     **/
    public void setLeaderOpinion(String leaderOpinion) {
        this.leaderOpinion = leaderOpinion;
    }
    
    /**
     *
     **/
    public String getDelFlag() {
        return this.delFlag;
    }
    
    /**
     *
     **/
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    
    /**
     *
     **/
    public Date getCreatedTime() {
        return this.createdTime;
    }
    
    /**
     *
     **/
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
    
    /**
     *
     **/
    public String getCreatedUser() {
        return this.createdUser;
    }
    
    /**
     *
     **/
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }
    
    /**
     *
     **/
    public Date getModifiedTime() {
        return this.modifiedTime;
    }
    
    /**
     *
     **/
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
    
    /**
     *
     **/
    public String getModifiedUser() {
        return this.modifiedUser;
    }
    
    /**
     *
     **/
    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }
    
    /**
     *
     **/
    public String getCheckStatus() {
        return this.checkStatus;
    }
    
    /**
     *
     **/
    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }
    
}
