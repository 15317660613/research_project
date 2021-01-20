package com.adc.da.research.project.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_APPROVAL_COMMENT ApprovalCommentEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ApprovalCommentEO extends BaseEntity {

    private String id;
    //科研项目ID
    private String projectId;
    //意见类型
    private String commentType;
    //意见内容
    private String commentContent;
    //审批人
    private String approvalName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    //审批时间
    private Date approvalTime;
    //审批流程类型
    private String processType;
    //删除标记 默认为0
    private Integer delFlag = 0;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private String token;//用户签名，用户验证用户有效性

    private String processStatus;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>commentType -> comment_type</li>
     * <li>commentContent -> comment_content</li>
     * <li>approvalName -> approval_name</li>
     * <li>approvalTime -> approval_time</li>
     * <li>processType -> process_type</li>
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
            case "id": return "id";
            case "projectId": return "project_id";
            case "commentType": return "comment_type";
            case "commentContent": return "comment_content";
            case "approvalName": return "approval_name";
            case "approvalTime": return "approval_time";
            case "processType": return "process_type";
            case "delFlag": return "del_flag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            case "processStatus": return "PROCESS_STATUS";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>comment_type -> commentType</li>
     * <li>comment_content -> commentContent</li>
     * <li>approval_name -> approvalName</li>
     * <li>approval_time -> approvalTime</li>
     * <li>process_type -> processType</li>
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
            case "id": return "id";
            case "project_id": return "projectId";
            case "comment_type": return "commentType";
            case "comment_content": return "commentContent";
            case "approval_name": return "approvalName";
            case "approval_time": return "approvalTime";
            case "process_type": return "processType";
            case "del_flag": return "delFlag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            case "PROCESS_STATUS": return "processStatus";
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
    public String getCommentType() {
        return this.commentType;
    }

    /**  **/
    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    /**  **/
    public String getCommentContent() {
        return this.commentContent;
    }

    /**  **/
    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    /**  **/
    public String getApprovalName() {
        return this.approvalName;
    }

    /**  **/
    public void setApprovalName(String approvalName) {
        this.approvalName = approvalName;
    }

    /**  **/
    public Date getApprovalTime() {
        return this.approvalTime;
    }

    /**  **/
    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
    }

    /**  **/
    public String getProcessType() {
        return this.processType;
    }

    /**  **/
    public void setProcessType(String processType) {
        this.processType = processType;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
    }
}
