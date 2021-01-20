package com.adc.da.research.project.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_CHECK_TARGET CheckTargetEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class CheckTargetEO extends BaseEntity {

    private String id;
    private String projectId;
    private String checkTypeId;
    private String checkName;
    private Double checkNum;
    private Double finishedNum;
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
    private String deliverableName;
    private String deliverableTarget;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>checkTypeId -> check_type_id</li>
     * <li>checkName -> check_name</li>
     * <li>checkNum -> check_num</li>
     * <li>finishedNum -> finished_num</li>
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
            case "checkTypeId": return "check_type_id";
            case "checkName": return "check_name";
            case "checkNum": return "check_num";
            case "finishedNum": return "finished_num";
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
            case "deliverableName": return "DELIVERABLE_NAME";
            case "deliverableTarget": return "DELIVERABLE_TARGET";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>check_type_id -> checkTypeId</li>
     * <li>check_name -> checkName</li>
     * <li>check_num -> checkNum</li>
     * <li>finished_num -> finishedNum</li>
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
            case "check_type_id": return "checkTypeId";
            case "check_name": return "checkName";
            case "check_num": return "checkNum";
            case "finished_num": return "finishedNum";
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
            case "DELIVERABLE_NAME": return "deliverableName";
            case "DELIVERABLE_TARGET": return "deliverableTarget";
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
    public String getCheckTypeId() {
        return this.checkTypeId;
    }

    /**  **/
    public void setCheckTypeId(String checkTypeId) {
        this.checkTypeId = checkTypeId;
    }

    /**  **/
    public String getCheckName() {
        return this.checkName;
    }

    /**  **/
    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    /**  **/
    public Double getCheckNum() {
        return this.checkNum;
    }

    /**  **/
    public void setCheckNum(Double checkNum) {
        this.checkNum = checkNum;
    }

    /**  **/
    public Double getFinishedNum() {
        return this.finishedNum;
    }

    /**  **/
    public void setFinishedNum(Double finishedNum) {
        this.finishedNum = finishedNum;
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

    public String getDeliverableName() {
        return deliverableName;
    }

    public void setDeliverableName(String deliverableName) {
        this.deliverableName = deliverableName;
    }

    public String getDeliverableTarget() {
        return deliverableTarget;
    }

    public void setDeliverableTarget(String deliverableTarget) {
        this.deliverableTarget = deliverableTarget;
    }
}
