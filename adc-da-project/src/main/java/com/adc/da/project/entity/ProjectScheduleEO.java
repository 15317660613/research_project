package com.adc.da.project.entity;

import com.adc.da.base.entity.BaseEntity;


/**
 * <b>功能：</b>PROJECT_SCHEDULE ProjectScheduleEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-04-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectScheduleEO extends BaseEntity {

    private String id;
    private String projectType;
    private String mark;
    private String projectName;
    private String completion;
    private String implementation;
    private String department;
    private String remark;
    private String createTime;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;
    private String year;
    private String month;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectType -> project_type</li>
     * <li>mark -> mark</li>
     * <li>projectName -> project_name</li>
     * <li>completion -> completion</li>
     * <li>implementation -> implementation</li>
     * <li>department -> department</li>
     * <li>remark -> remark</li>
     * <li>createTime -> create_time</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>extInfo5 -> ext_info5</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null ;};
        switch (fieldName) {
            case "id": return "id";
            case "projectType": return "project_type";
            case "mark": return "mark";
            case "projectName": return "project_name";
            case "completion": return "completion";
            case "implementation": return "implementation";
            case "department": return "department";
            case "remark": return "remark";
            case "createTime": return "create_time";
            case "extInfo1": return "ext_info1";
            case "extInfo2": return "ext_info2";
            case "extInfo3": return "ext_info3";
            case "extInfo4": return "ext_info4";
            case "extInfo5": return "ext_info5";
            case "year": return "year";
            case "month": return "month";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_type -> projectType</li>
     * <li>mark -> mark</li>
     * <li>project_name -> projectName</li>
     * <li>completion -> completion</li>
     * <li>implementation -> implementation</li>
     * <li>department -> department</li>
     * <li>remark -> remark</li>
     * <li>create_time -> createTime</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>ext_info5 -> extInfo5</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; };
        switch (columnName) {
            case "id": return "id";
            case "project_type": return "projectType";
            case "mark": return "mark";
            case "project_name": return "projectName";
            case "completion": return "completion";
            case "implementation": return "implementation";
            case "department": return "department";
            case "remark": return "remark";
            case "create_time": return "createTime";
            case "ext_info1": return "extInfo1";
            case "ext_info2": return "extInfo2";
            case "ext_info3": return "extInfo3";
            case "ext_info4": return "extInfo4";
            case "ext_info5": return "extInfo5";
            case "year": return "year";
            case "month": return "month";
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
    public String getCompletion() {
        return this.completion;
    }

    /**  **/
    public void setCompletion(String completion) {
        this.completion = completion;
    }

    /**  **/
    public String getImplementation() {
        return this.implementation;
    }

    /**  **/
    public void setImplementation(String implementation) {
        this.implementation = implementation;
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
    public String getRemark() {
        return this.remark;
    }

    /**  **/
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**  **/
    public String getCreateTime() {
        return this.createTime;
    }

    /**  **/
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**  **/
    public String getExtInfo1() {
        return this.extInfo1;
    }

    /**  **/
    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    /**  **/
    public String getExtInfo2() {
        return this.extInfo2;
    }

    /**  **/
    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

    /**  **/
    public String getExtInfo3() {
        return this.extInfo3;
    }

    /**  **/
    public void setExtInfo3(String extInfo3) {
        this.extInfo3 = extInfo3;
    }

    /**  **/
    public String getExtInfo4() {
        return this.extInfo4;
    }

    /**  **/
    public void setExtInfo4(String extInfo4) {
        this.extInfo4 = extInfo4;
    }

    /**  **/
    public String getExtInfo5() {
        return this.extInfo5;
    }

    /**  **/
    public void setExtInfo5(String extInfo5) {
        this.extInfo5 = extInfo5;
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

}
