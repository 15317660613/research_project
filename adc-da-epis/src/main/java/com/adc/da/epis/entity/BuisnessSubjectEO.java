package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>BUISNESS_SUBJECT BuisnessSubjectEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuisnessSubjectEO extends BaseEntity {

    private String id;
    private String projectId;
    private String departmentId;
    private String productionId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date creatTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modify;
    private String createUser;
    private String subjectName;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>departmentId -> department_id</li>
     * <li>productionId -> production_id</li>
     * <li>creatTime -> creat_time</li>
     * <li>modify -> modify</li>
     * <li>createUser -> create_user</li>
     * <li>subjectName -> subject_name</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "projectId": return "project_id";
            case "departmentId": return "department_id";
            case "productionId": return "production_id";
            case "creatTime": return "creat_time";
            case "modify": return "modify";
            case "createUser": return "create_user";
            case "subjectName": return "subject_name";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>department_id -> departmentId</li>
     * <li>production_id -> productionId</li>
     * <li>creat_time -> creatTime</li>
     * <li>modify -> modify</li>
     * <li>create_user -> createUser</li>
     * <li>subject_name -> subjectName</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "project_id": return "projectId";
            case "department_id": return "departmentId";
            case "production_id": return "productionId";
            case "creat_time": return "creatTime";
            case "modify": return "modify";
            case "create_user": return "createUser";
            case "subject_name": return "subjectName";
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
    public String getDepartmentId() {
        return this.departmentId;
    }

    /**  **/
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    /**  **/
    public String getProductionId() {
        return this.productionId;
    }

    /**  **/
    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    /**  **/
    public Date getCreatTime() {
        return this.creatTime;
    }

    /**  **/
    public void setCreatTime(Date creatTime) {
        this.creatTime = creatTime;
    }

    /**  **/
    public Date getModify() {
        return this.modify;
    }

    /**  **/
    public void setModify(Date modify) {
        this.modify = modify;
    }

    /**  **/
    public String getCreateUser() {
        return this.createUser;
    }

    /**  **/
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**  **/
    public String getSubjectName() {
        return this.subjectName;
    }

    /**  **/
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

}
