package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>BUISNESS_BUG_DETAIL BuisnessBugDetailEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuisnessBugDetailEO extends BaseEntity {

    private String id;
    private String projectId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date cheackYearWeek;
    private Integer bugNum;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modify;
    private String createUser;
    private Integer createBugNum;
    private Integer solveBugNum;
    private Integer leftBugNum;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>cheackYearWeek -> cheack_year_week</li>
     * <li>bugNum -> bug_num</li>
     * <li>createTime -> create_time</li>
     * <li>modify -> modify</li>
     * <li>createUser -> create_user</li>
     * <li>createBugNum -> create_bug_num</li>
     * <li>solveBugNum -> solve_bug_num</li>
     * <li>leftBugNum -> left_bug_num</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "projectId": return "project_id";
            case "cheackYearWeek": return "cheack_year_week";
            case "bugNum": return "bug_num";
            case "createTime": return "create_time";
            case "modify": return "modify";
            case "createUser": return "create_user";
            case "createBugNum": return "create_bug_num";
            case "solveBugNum": return "solve_bug_num";
            case "leftBugNum": return "left_bug_num";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>cheack_year_week -> cheackYearWeek</li>
     * <li>bug_num -> bugNum</li>
     * <li>create_time -> createTime</li>
     * <li>modify -> modify</li>
     * <li>create_user -> createUser</li>
     * <li>create_bug_num -> createBugNum</li>
     * <li>solve_bug_num -> solveBugNum</li>
     * <li>left_bug_num -> leftBugNum</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "project_id": return "projectId";
            case "cheack_year_week": return "cheackYearWeek";
            case "bug_num": return "bugNum";
            case "create_time": return "createTime";
            case "modify": return "modify";
            case "create_user": return "createUser";
            case "create_bug_num": return "createBugNum";
            case "solve_bug_num": return "solveBugNum";
            case "left_bug_num": return "leftBugNum";
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
    public Date getCheackYearWeek() {
        return this.cheackYearWeek;
    }

    /**  **/
    public void setCheackYearWeek(Date cheackYearWeek) {
        this.cheackYearWeek = cheackYearWeek;
    }

    /**  **/
    public Integer getBugNum() {
        return this.bugNum;
    }

    /**  **/
    public void setBugNum(Integer bugNum) {
        this.bugNum = bugNum;
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
    public Integer getCreateBugNum() {
        return this.createBugNum;
    }

    /**  **/
    public void setCreateBugNum(Integer createBugNum) {
        this.createBugNum = createBugNum;
    }

    /**  **/
    public Integer getSolveBugNum() {
        return this.solveBugNum;
    }

    /**  **/
    public void setSolveBugNum(Integer solveBugNum) {
        this.solveBugNum = solveBugNum;
    }

    /**  **/
    public Integer getLeftBugNum() {
        return this.leftBugNum;
    }

    /**  **/
    public void setLeftBugNum(Integer leftBugNum) {
        this.leftBugNum = leftBugNum;
    }

}
