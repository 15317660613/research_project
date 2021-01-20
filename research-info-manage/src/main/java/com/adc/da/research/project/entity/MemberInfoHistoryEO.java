package com.adc.da.research.project.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_MEMBER_INFO_HISTORY MemberInfoHistoryEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class MemberInfoHistoryEO extends BaseEntity {

    private String id;
    private String projectId;
    private String memberUserId;
    private String undertakingTypeId;
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
    private String deptId;
    private String taskDivision;
    private Double workHours;
    private String studyAbroadType;
    private Integer sort;
    private String memberProfession;

    private String jobTitle; //职称
    private String jobLevel;
    private String lastDegree;
    private String usname;
    private String num;
    private String memberSex;
    private String memberEducation;
    private String deptName;

    private String identityNumber;
    private String finalDegree;
    private String officePhone;
    private String cellPhoneNumber;
    private String email;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>memberUserId -> member_user_id</li>
     * <li>undertakingTypeId -> undertaking_type_id</li>
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
     * <li>deptId -> dept_id</li>
     * <li>taskDivision -> task_division</li>
     * <li>workHours -> work_hours</li>
     * <li>studyAbroadType -> study_abroad_type</li>
     * <li>sort -> sort</li>
     * <li>memberProfession -> member_profession</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "projectId": return "project_id";
            case "memberUserId": return "member_user_id";
            case "undertakingTypeId": return "undertaking_type_id";
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
            case "deptId": return "dept_id";
            case "taskDivision": return "task_division";
            case "workHours": return "work_hours";
            case "studyAbroadType": return "study_abroad_type";
            case "sort": return "sort";
            case "memberProfession": return "member_profession";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>member_user_id -> memberUserId</li>
     * <li>undertaking_type_id -> undertakingTypeId</li>
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
     * <li>dept_id -> deptId</li>
     * <li>task_division -> taskDivision</li>
     * <li>work_hours -> workHours</li>
     * <li>study_abroad_type -> studyAbroadType</li>
     * <li>sort -> sort</li>
     * <li>member_profession -> memberProfession</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "project_id": return "projectId";
            case "member_user_id": return "memberUserId";
            case "undertaking_type_id": return "undertakingTypeId";
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
            case "dept_id": return "deptId";
            case "task_division": return "taskDivision";
            case "work_hours": return "workHours";
            case "study_abroad_type": return "studyAbroadType";
            case "sort": return "sort";
            case "member_profession": return "memberProfession";
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
    public String getMemberUserId() {
        return this.memberUserId;
    }

    /**  **/
    public void setMemberUserId(String memberUserId) {
        this.memberUserId = memberUserId;
    }

    /**  **/
    public String getUndertakingTypeId() {
        return this.undertakingTypeId;
    }

    /**  **/
    public void setUndertakingTypeId(String undertakingTypeId) {
        this.undertakingTypeId = undertakingTypeId;
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
    public String getDeptId() {
        return this.deptId;
    }

    /**  **/
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**  **/
    public String getTaskDivision() {
        return this.taskDivision;
    }

    /**  **/
    public void setTaskDivision(String taskDivision) {
        this.taskDivision = taskDivision;
    }

    /**  **/
    public Double getWorkHours() {
        return this.workHours;
    }

    /**  **/
    public void setWorkHours(Double workHours) {
        this.workHours = workHours;
    }

    /**  **/
    public String getStudyAbroadType() {
        return this.studyAbroadType;
    }

    /**  **/
    public void setStudyAbroadType(String studyAbroadType) {
        this.studyAbroadType = studyAbroadType;
    }

    /**  **/
    public Integer getSort() {
        return this.sort;
    }

    /**  **/
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**  **/
    public String getMemberProfession() {
        return this.memberProfession;
    }

    /**  **/
    public void setMemberProfession(String memberProfession) {
        this.memberProfession = memberProfession;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }

    public String getLastDegree() {
        return lastDegree;
    }

    public void setLastDegree(String lastDegree) {
        this.lastDegree = lastDegree;
    }

    public String getUsname() {
        return usname;
    }

    public void setUsname(String usname) {
        this.usname = usname;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMemberSex() {
        return memberSex;
    }

    public void setMemberSex(String memberSex) {
        this.memberSex = memberSex;
    }

    public String getMemberEducation() {
        return memberEducation;
    }

    public void setMemberEducation(String memberEducation) {
        this.memberEducation = memberEducation;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getFinalDegree() {
        return finalDegree;
    }

    public void setFinalDegree(String finalDegree) {
        this.finalDegree = finalDegree;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
