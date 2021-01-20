package com.adc.da.research.project.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_WORK_PROGRESS WorkProgressEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class WorkProgressEO extends BaseEntity {

    private Integer delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private String id;
    private String projectId;
    private String examineType;
    private String examineContent;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date examineTime;
    private String examineDescription;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date actualExamineTime;
    private String rectifyDescription;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private String nodeGoals;
    private String approveComment;
    private String checkMethod;
    private ImplementationProcFileEO file;

    private String fileArr;
    private String checkFileArr;
    private String processId;
    private String checkUser;
    private String checkResult;
    private String checkUserId;
    private String reviewRemark;//评审备注


    private String apply;//验收申请
    private String finish;//验收结项


    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>delFlag -> del_flag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>examineType -> examine_type</li>
     * <li>examineContent -> examine_content</li>
     * <li>examineTime -> examine_time</li>
     * <li>examineDescription -> examine_description</li>
     * <li>actualExamineTime -> actual_examine_time</li>
     * <li>rectifyDescription -> rectify_description</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>createTime -> create_time</li>
     * <li>modifyTime -> modify_time</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "delFlag": return "del_flag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            case "id": return "id";
            case "projectId": return "project_id";
            case "examineType": return "examine_type";
            case "examineContent": return "examine_content";
            case "examineTime": return "examine_time";
            case "examineDescription": return "examine_description";
            case "actualExamineTime": return "actual_examine_time";
            case "rectifyDescription": return "rectify_description";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "createTime": return "create_time";
            case "modifyTime": return "modify_time";
            case "approveComment": return "approveComment";
            case "checkMethod": return "checkMethod";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>del_flag -> delFlag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>examine_type -> examineType</li>
     * <li>examine_content -> examineContent</li>
     * <li>examine_time -> examineTime</li>
     * <li>examine_description -> examineDescription</li>
     * <li>actual_examine_time -> actualExamineTime</li>
     * <li>rectify_description -> rectifyDescription</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>create_time -> createTime</li>
     * <li>modify_time -> modifyTime</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "del_flag": return "delFlag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            case "id": return "id";
            case "project_id": return "projectId";
            case "examine_type": return "examineType";
            case "examine_content": return "examineContent";
            case "examine_time": return "examineTime";
            case "examine_description": return "examineDescription";
            case "actual_examine_time": return "actualExamineTime";
            case "rectify_description": return "rectifyDescription";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "create_time": return "createTime";
            case "modify_time": return "modifyTime";
            case "approveComment": return "approveComment";
            case "checkMethod": return "checkMethod";
            default: return null;
        }
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
    public String getExamineType() {
        return this.examineType;
    }

    /**  **/
    public void setExamineType(String examineType) {
        this.examineType = examineType;
    }

    /**  **/
    public String getExamineContent() {
        return this.examineContent;
    }

    /**  **/
    public void setExamineContent(String examineContent) {
        this.examineContent = examineContent;
    }

    /**  **/
    public Date getExamineTime() {
        return this.examineTime;
    }

    /**  **/
    public void setExamineTime(Date examineTime) {
        this.examineTime = examineTime;
    }

    /**  **/
    public String getExamineDescription() {
        return this.examineDescription;
    }

    /**  **/
    public void setExamineDescription(String examineDescription) {
        this.examineDescription = examineDescription;
    }

    /**  **/
    public Date getActualExamineTime() {
        return this.actualExamineTime;
    }

    /**  **/
    public void setActualExamineTime(Date actualExamineTime) {
        this.actualExamineTime = actualExamineTime;
    }

    /**  **/
    public String getRectifyDescription() {
        return this.rectifyDescription;
    }

    /**  **/
    public void setRectifyDescription(String rectifyDescription) {
        this.rectifyDescription = rectifyDescription;
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

    public String getNodeGoals() {
        return nodeGoals;
    }

    public void setNodeGoals(String nodeGoals) {
        this.nodeGoals = nodeGoals;
    }

    public String getApproveComment() {
        return approveComment;
    }

    public void setApproveComment(String approveComment) {
        this.approveComment = approveComment;
    }

    public String getCheckMethod() {
        return checkMethod;
    }

    public void setCheckMethod(String checkMethod) {
        this.checkMethod = checkMethod;
    }

    public ImplementationProcFileEO getFile() {
        return file;
    }

    public void setFile(ImplementationProcFileEO file) {
        this.file = file;
    }

    public String getFileArr() {
        return fileArr;
    }

    public void setFileArr(String fileArr) {
        this.fileArr = fileArr;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }

    public String getCheckUserId() {
        return checkUserId;
    }

    public void setCheckUserId(String checkUserId) {
        this.checkUserId = checkUserId;
    }
    public String getApply() {
        return apply;
    }

    public void setApply(String apply) {
        this.apply = apply;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;

    }

    public String getCheckFileArr() {
        return checkFileArr;
    }

    public void setCheckFileArr(String checkFileArr) {
        this.checkFileArr = checkFileArr;
    }

    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark;
    }
}
