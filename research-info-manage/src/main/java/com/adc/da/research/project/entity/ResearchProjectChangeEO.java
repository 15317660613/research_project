package com.adc.da.research.project.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_RESEARCH_PROJECT_CHANGE ResearchProjectChangeEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ResearchProjectChangeEO extends BaseEntity {

    private String id;
    private String changePersonal;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date changeTime;
    private String changeAttribute;
    private String changeContent;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String projectId;
    private String contractId;
    private String changeStatus;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>changePersonal -> change_personal</li>
     * <li>changeTime -> change_time</li>
     * <li>changeAttribute -> change_attribute</li>
     * <li>changeContent -> change_content</li>
     * <li>createTime -> create_time</li>
     * <li>projectId -> project_id</li>
     * <li>contractId -> contract_id</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "changePersonal": return "change_personal";
            case "changeTime": return "change_time";
            case "changeAttribute": return "change_attribute";
            case "changeContent": return "change_content";
            case "createTime": return "create_time";
            case "projectId": return "project_id";
            case "contractId": return "contract_id";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>change_personal -> changePersonal</li>
     * <li>change_time -> changeTime</li>
     * <li>change_attribute -> changeAttribute</li>
     * <li>change_content -> changeContent</li>
     * <li>create_time -> createTime</li>
     * <li>project_id -> projectId</li>
     * <li>contract_id -> contractId</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "change_personal": return "changePersonal";
            case "change_time": return "changeTime";
            case "change_attribute": return "changeAttribute";
            case "change_content": return "changeContent";
            case "create_time": return "createTime";
            case "project_id": return "projectId";
            case "contract_id": return "contractId";
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
    public String getChangePersonal() {
        return this.changePersonal;
    }

    /**  **/
    public void setChangePersonal(String changePersonal) {
        this.changePersonal = changePersonal;
    }

    /**  **/
    public Date getChangeTime() {
        return this.changeTime;
    }

    /**  **/
    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    /**  **/
    public String getChangeAttribute() {
        return this.changeAttribute;
    }

    /**  **/
    public void setChangeAttribute(String changeAttribute) {
        this.changeAttribute = changeAttribute;
    }

    /**  **/
    public String getChangeContent() {
        return this.changeContent;
    }

    /**  **/
    public void setChangeContent(String changeContent) {
        this.changeContent = changeContent;
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
    public String getProjectId() {
        return this.projectId;
    }

    /**  **/
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**  **/
    public String getContractId() {
        return this.contractId;
    }

    /**  **/
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(String changeStatus) {
        this.changeStatus = changeStatus;
    }
}
