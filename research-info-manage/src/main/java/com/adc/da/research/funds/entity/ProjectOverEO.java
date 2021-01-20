package com.adc.da.research.funds.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_PROJECT_OVER ProjectOverEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectOverEO extends BaseEntity {

    private String id;
    private String projectId;
    private String projectCode;
    private String deptId;
    private String subjectName;
    private Double overAmount;
    private Double overPercent;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date overDateBegin;
    private String summary;
    private Integer overState;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private Integer delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date overDateEnd;
    private String technicalDirector;
    private String orgName;
    private String projectTypeName;
    private String projectName;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>projectCode -> project_code</li>
     * <li>deptId -> dept_id</li>
     * <li>subjectName -> subject_name</li>
     * <li>overAmount -> over_amount</li>
     * <li>overPercent -> over_percent</li>
     * <li>overDateBegin -> over_date_begin</li>
     * <li>summary -> summary</li>
     * <li>overState -> over_state</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>createTime -> create_time</li>
     * <li>modifyTime -> modify_time</li>
     * <li>delFlag -> del_flag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     * <li>overDateEnd -> over_date_end</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "projectId": return "project_id";
            case "projectCode": return "project_code";
            case "deptId": return "dept_id";
            case "subjectName": return "subject_name";
            case "overAmount": return "over_amount";
            case "overPercent": return "over_percent";
            case "overDateBegin": return "over_date_begin";
            case "summary": return "summary";
            case "overState": return "over_state";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "createTime": return "create_time";
            case "modifyTime": return "modify_time";
            case "delFlag": return "del_flag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            case "overDateEnd": return "over_date_end";
            case "technicalDirector": return"technical_director";
            case "orgName": return "org_name";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>project_code -> projectCode</li>
     * <li>dept_id -> deptId</li>
     * <li>subject_name -> subjectName</li>
     * <li>over_amount -> overAmount</li>
     * <li>over_percent -> overPercent</li>
     * <li>over_date_begin -> overDateBegin</li>
     * <li>summary -> summary</li>
     * <li>over_state -> overState</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>create_time -> createTime</li>
     * <li>modify_time -> modifyTime</li>
     * <li>del_flag -> delFlag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     * <li>over_date_end -> overDateEnd</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "project_id": return "projectId";
            case "project_code": return "projectCode";
            case "dept_id": return "deptId";
            case "subject_name": return "subjectName";
            case "over_amount": return "overAmount";
            case "over_percent": return "overPercent";
            case "over_date_begin": return "overDateBegin";
            case "summary": return "summary";
            case "over_state": return "overState";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "create_time": return "createTime";
            case "modify_time": return "modifyTime";
            case "del_flag": return "delFlag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            case "over_date_end": return "overDateEnd";
            case "technical_director": return"technicalDirector";
            case "org_name": return "orgName";
            default: return null;
        }
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
    public String getProjectId() {
        return this.projectId;
    }

    /**  **/
    public void setProjectId(String projectId) {
        this.projectId = projectId;
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
    public String getDeptId() {
        return this.deptId;
    }

    /**  **/
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**  **/
    public String getSubjectName() {
        return this.subjectName;
    }

    /**  **/
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    /**  **/
    public Double getOverAmount() {
        return this.overAmount;
    }

    /**  **/
    public void setOverAmount(Double overAmount) {
        this.overAmount = overAmount;
    }

    /**  **/
    public Double getOverPercent() {
        return this.overPercent;
    }

    /**  **/
    public void setOverPercent(Double overPercent) {
        this.overPercent = overPercent;
    }

    /**  **/
    public Date getOverDateBegin() {
        return this.overDateBegin;
    }

    /**  **/
    public void setOverDateBegin(Date overDateBegin) {
        this.overDateBegin = overDateBegin;
    }

    /**  **/
    public String getSummary() {
        return this.summary;
    }

    /**  **/
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**  **/
    public Integer getOverState() {
        return this.overState;
    }

    /**  **/
    public void setOverState(Integer overState) {
        this.overState = overState;
    }

    /**  **/
    public String getCreateUserId() {
        return this.createUserId;
    }

    /**  **/
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**  **/
    public String getCreateUserName() {
        return this.createUserName;
    }

    /**  **/
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    /**  **/
    public Date getCreateTime() {
        return this.createTime;
    }

    /**  **/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**  **/
    public Date getModifyTime() {
        return this.modifyTime;
    }

    /**  **/
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
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

    /**  **/
    public Date getOverDateEnd() {
        return this.overDateEnd;
    }

    /**  **/
    public void setOverDateEnd(Date overDateEnd) {
        this.overDateEnd = overDateEnd;
    }

    public String getTechnicalDirector() {
        return technicalDirector;
    }

    public void setTechnicalDirector(String technicalDirector) {
        this.technicalDirector = technicalDirector;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
