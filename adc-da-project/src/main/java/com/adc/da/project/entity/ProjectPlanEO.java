package com.adc.da.project.entity;

import com.adc.da.base.entity.BaseEntity;


/**
 * <b>功能：</b>PROJECT_PLAN ProjectPlanEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-04-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectPlanEO extends BaseEntity {

    private String id;
    private String year;
    private String month;
    private String department;
    private String projectType;
    private String mark;
    private String projectName;
    private String projectContent;
    private String progressTarget;
    private String finishTime;
    private String majorPerson;
    private String participant;
    private String cooperationDepartment;
    private String extInf01;
    private String extInf02;
    private String extInf03;
    private String extInf04;
    private String extInf05;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>department -> department</li>
     * <li>projectType -> project_type</li>
     * <li>mark -> mark</li>
     * <li>projectName -> project_name</li>
     * <li>projectContent -> project_content</li>
     * <li>progressTarget -> progress_target</li>
     * <li>finishTime -> finish_time</li>
     * <li>majorPerson -> major_person</li>
     * <li>participant -> participant</li>
     * <li>cooperationDepartment -> cooperation_department</li>
     * <li>extInf01 -> ext_inf01</li>
     * <li>extInf02 -> ext_inf02</li>
     * <li>extInf03 -> ext_inf03</li>
     * <li>extInf04 -> ext_inf04</li>
     * <li>extInf05 -> ext_inf05</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; };
        switch (fieldName) {
            case "id": return "id";
            case "year": return "year";
            case "month": return "month";
            case "department": return "department";
            case "projectType": return "project_type";
            case "mark": return "mark";
            case "projectName": return "project_name";
            case "projectContent": return "project_content";
            case "progressTarget": return "progress_target";
            case "finishTime": return "finish_time";
            case "majorPerson": return "major_person";
            case "participant": return "participant";
            case "cooperationDepartment": return "cooperation_department";
            case "extInf01": return "ext_inf01";
            case "extInf02": return "ext_inf02";
            case "extInf03": return "ext_inf03";
            case "extInf04": return "ext_inf04";
            case "extInf05": return "ext_inf05";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>department -> department</li>
     * <li>project_type -> projectType</li>
     * <li>mark -> mark</li>
     * <li>project_name -> projectName</li>
     * <li>project_content -> projectContent</li>
     * <li>progress_target -> progressTarget</li>
     * <li>finish_time -> finishTime</li>
     * <li>major_person -> majorPerson</li>
     * <li>participant -> participant</li>
     * <li>cooperation_department -> cooperationDepartment</li>
     * <li>ext_inf01 -> extInf01</li>
     * <li>ext_inf02 -> extInf02</li>
     * <li>ext_inf03 -> extInf03</li>
     * <li>ext_inf04 -> extInf04</li>
     * <li>ext_inf05 -> extInf05</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null ;};
        switch (columnName) {
            case "id": return "id";
            case "year": return "year";
            case "month": return "month";
            case "department": return "department";
            case "project_type": return "projectType";
            case "mark": return "mark";
            case "project_name": return "projectName";
            case "project_content": return "projectContent";
            case "progress_target": return "progressTarget";
            case "finish_time": return "finishTime";
            case "major_person": return "majorPerson";
            case "participant": return "participant";
            case "cooperation_department": return "cooperationDepartment";
            case "ext_inf01": return "extInf01";
            case "ext_inf02": return "extInf02";
            case "ext_inf03": return "extInf03";
            case "ext_inf04": return "extInf04";
            case "ext_inf05": return "extInf05";
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
    public String getYear() {
        return this.year;
    }

    /**  **/
    public void setYear(String year) {
        this.year = year;
    }

    /**  **/
    public String getMonth() {
        return this.month;
    }

    /**  **/
    public void setMonth(String month) {
        this.month = month;
    }

    /**  **/
    public String getDepartment() {
        return this.department;
    }

    /**  **/
    public void setDepartment(String department) {
        this.department = department;
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
    public String getMark() {
        return this.mark;
    }

    /**  **/
    public void setMark(String mark) {
        this.mark = mark;
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
    public String getProjectContent() {
        return this.projectContent;
    }

    /**  **/
    public void setProjectContent(String projectContent) {
        this.projectContent = projectContent;
    }

    /**  **/
    public String getProgressTarget() {
        return this.progressTarget;
    }

    /**  **/
    public void setProgressTarget(String progressTarget) {
        this.progressTarget = progressTarget;
    }

    /**  **/
    public String getFinishTime() {
        return this.finishTime;
    }

    /**  **/
    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    /**  **/
    public String getMajorPerson() {
        return this.majorPerson;
    }

    /**  **/
    public void setMajorPerson(String majorPerson) {
        this.majorPerson = majorPerson;
    }

    /**  **/
    public String getParticipant() {
        return this.participant;
    }

    /**  **/
    public void setParticipant(String participant) {
        this.participant = participant;
    }

    /**  **/
    public String getCooperationDepartment() {
        return this.cooperationDepartment;
    }

    /**  **/
    public void setCooperationDepartment(String cooperationDepartment) {
        this.cooperationDepartment = cooperationDepartment;
    }

    /**  **/
    public String getExtInf01() {
        return this.extInf01;
    }

    /**  **/
    public void setExtInf01(String extInf01) {
        this.extInf01 = extInf01;
    }

    /**  **/
    public String getExtInf02() {
        return this.extInf02;
    }

    /**  **/
    public void setExtInf02(String extInf02) {
        this.extInf02 = extInf02;
    }

    /**  **/
    public String getExtInf03() {
        return this.extInf03;
    }

    /**  **/
    public void setExtInf03(String extInf03) {
        this.extInf03 = extInf03;
    }

    /**  **/
    public String getExtInf04() {
        return this.extInf04;
    }

    /**  **/
    public void setExtInf04(String extInf04) {
        this.extInf04 = extInf04;
    }

    /**  **/
    public String getExtInf05() {
        return this.extInf05;
    }

    /**  **/
    public void setExtInf05(String extInf05) {
        this.extInf05 = extInf05;
    }

}
