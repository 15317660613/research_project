package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;

import java.util.Date;

/**
 * <b>功能：</b>BUISNESS_PROJECT BuisnessProjectEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuisnessProjectEO extends BaseEntity {
    /**
     * 缺少项目编号
     */
    private String id;
    @Excel(name = "项目名称")
    private String projectName;
    private String departmentId;
    private String productionId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String createUser;
    private String modefiyTime;
    private String compenetStr;
    @Excel(name = "执行方式", replace = { "自研_1", "外包_2" })
    private String projectType;
    private String performType;
    private String develStatus;
    private String projectLeader;
    @Excel(name = "项目组")
    private String projectHead;
    @Excel(name = "项目对接人")
    private String dockingPerson;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectName -> project_name</li>
     * <li>departmentId -> department_id</li>
     * <li>productionId -> production_id</li>
     * <li>createTime -> create_time</li>
     * <li>createUser -> create_user</li>
     * <li>modefiyTime -> modefiy_time</li>
     * <li>compenetStr -> compenet_str</li>
     * <li>projectType -> project_type</li>
     * <li>performType -> perform_type</li>
     * <li>develStatus -> devel_status</li>
     * <li>projectLeader -> project_leader</li>
     * <li>projectHead -> project_head</li>
     * <li>dockingPerson -> docking_person</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "projectName": return "project_name";
            case "departmentId": return "department_id";
            case "productionId": return "production_id";
            case "createTime": return "create_time";
            case "createUser": return "create_user";
            case "modefiyTime": return "modefiy_time";
            case "compenetStr": return "compenet_str";
            case "projectType": return "project_type";
            case "performType": return "perform_type";
            case "develStatus": return "devel_status";
            case "projectLeader": return "project_leader";
            case "projectHead": return "project_head";
            case "dockingPerson": return "docking_person";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_name -> projectName</li>
     * <li>department_id -> departmentId</li>
     * <li>production_id -> productionId</li>
     * <li>create_time -> createTime</li>
     * <li>create_user -> createUser</li>
     * <li>modefiy_time -> modefiyTime</li>
     * <li>compenet_str -> compenetStr</li>
     * <li>project_type -> projectType</li>
     * <li>perform_type -> performType</li>
     * <li>devel_status -> develStatus</li>
     * <li>project_leader -> projectLeader</li>
     * <li>project_head -> projectHead</li>
     * <li>docking_person -> dockingPerson</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "project_name": return "projectName";
            case "department_id": return "departmentId";
            case "production_id": return "productionId";
            case "create_time": return "createTime";
            case "create_user": return "createUser";
            case "modefiy_time": return "modefiyTime";
            case "compenet_str": return "compenetStr";
            case "project_type": return "projectType";
            case "perform_type": return "performType";
            case "devel_status": return "develStatus";
            case "project_leader": return "projectLeader";
            case "project_head": return "projectHead";
            case "docking_person": return "dockingPerson";
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
    public String getProjectName() {
        return this.projectName;
    }

    /**  **/
    public void setProjectName(String projectName) {
        this.projectName = projectName;
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
    public Date getCreateTime() {
        return this.createTime;
    }

    /**  **/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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
    public String getModefiyTime() {
        return this.modefiyTime;
    }

    /**  **/
    public void setModefiyTime(String modefiyTime) {
        this.modefiyTime = modefiyTime;
    }

    /**  **/
    public String getCompenetStr() {
        return this.compenetStr;
    }

    /**  **/
    public void setCompenetStr(String compenetStr) {
        this.compenetStr = compenetStr;
    }

    /**  **/
    public String getProjectType() {
        return this.projectType;
    }

    /**  **/
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    /**  **/
    public String getPerformType() {
        return this.performType;
    }

    /**  **/
    public void setPerformType(String performType) {
        this.performType = performType;
    }

    /**  **/
    public String getDevelStatus() {
        return this.develStatus;
    }

    /**  **/
    public void setDevelStatus(String develStatus) {
        this.develStatus = develStatus;
    }

    /**  **/
    public String getProjectLeader() {
        return this.projectLeader;
    }

    /**  **/
    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    /**  **/
    public String getProjectHead() {
        return this.projectHead;
    }

    /**  **/
    public void setProjectHead(String projectHead) {
        this.projectHead = projectHead;
    }

    /**  **/
    public String getDockingPerson() {
        return this.dockingPerson;
    }

    /**  **/
    public void setDockingPerson(String dockingPerson) {
        this.dockingPerson = dockingPerson;
    }

}
