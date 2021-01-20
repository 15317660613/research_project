package com.adc.da.research.project.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;
import java.util.Objects;

/**
 * <b>功能：</b>RS_PROJECT_WARN ProjectWarnEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectWarnEO extends BaseEntity {

    private String id;
    private String projectId;
    private String title;
    private String projectTypeName;
    private String projectCode;
    private String projectName;
    private String expendProgress;
    private String receivedUser;
    private String receivedContent;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date sendTime;
    private String sendStatus;
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

    private String technicalIds;

    public String getTechnicalIds() {
        return technicalIds;
    }

    public void setTechnicalIds(String technicalIds) {
        this.technicalIds = technicalIds;
    }

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>title -> title</li>
     * <li>projectTypeName -> project_type_name</li>
     * <li>projectCode -> project_code</li>
     * <li>projectName -> project_name</li>
     * <li>expendProgress -> expend_progress</li>
     * <li>receivedUser -> received_user</li>
     * <li>receivedContent -> received_content</li>
     * <li>sendTime -> send_time</li>
     * <li>sendStatus -> send_status</li>
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
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "projectId": return "project_id";
            case "title": return "title";
            case "projectTypeName": return "project_type_name";
            case "projectCode": return "project_code";
            case "projectName": return "project_name";
            case "expendProgress": return "expend_progress";
            case "receivedUser": return "received_user";
            case "receivedContent": return "received_content";
            case "sendTime": return "send_time";
            case "sendStatus": return "send_status";
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
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>title -> title</li>
     * <li>project_type_name -> projectTypeName</li>
     * <li>project_code -> projectCode</li>
     * <li>project_name -> projectName</li>
     * <li>expend_progress -> expendProgress</li>
     * <li>received_user -> receivedUser</li>
     * <li>received_content -> receivedContent</li>
     * <li>send_time -> sendTime</li>
     * <li>send_status -> sendStatus</li>
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
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "project_id": return "projectId";
            case "title": return "title";
            case "project_type_name": return "projectTypeName";
            case "project_code": return "projectCode";
            case "project_name": return "projectName";
            case "expend_progress": return "expendProgress";
            case "received_user": return "receivedUser";
            case "received_content": return "receivedContent";
            case "send_time": return "sendTime";
            case "send_status": return "sendStatus";
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
    public String getTitle() {
        return this.title;
    }

    /**  **/
    public void setTitle(String title) {
        this.title = title;
    }

    /**  **/
    public String getProjectTypeName() {
        return this.projectTypeName;
    }

    /**  **/
    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
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
    public String getExpendProgress() {
        return this.expendProgress;
    }

    /**  **/
    public void setExpendProgress(String expendProgress) {
        this.expendProgress = expendProgress;
    }

    /**  **/
    public String getReceivedUser() {
        return this.receivedUser;
    }

    /**  **/
    public void setReceivedUser(String receivedUser) {
        this.receivedUser = receivedUser;
    }

    public String getReceivedContent() {
        return receivedContent;
    }

    public void setReceivedContent(String receivedContent) {
        this.receivedContent = receivedContent;
    }

    /**  **/
    public Date getSendTime() {
        return this.sendTime;
    }

    /**  **/
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    /**  **/
    public String getSendStatus() {
        return this.sendStatus;
    }

    /**  **/
    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectWarnEO that = (ProjectWarnEO) o;
        return Objects.equals(projectId, that.projectId) && Objects.equals(title, that.title) && Objects.equals(projectTypeName, that.projectTypeName) && Objects.equals(projectCode, that.projectCode) && Objects.equals(projectName, that.projectName) && Objects.equals(expendProgress, that.expendProgress) && Objects.equals(receivedContent, that.receivedContent) && Objects.equals(sendTime, that.sendTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, title, projectTypeName, projectCode, projectName, expendProgress, receivedContent, sendTime);
    }
}
