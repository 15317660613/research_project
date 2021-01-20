package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>BUINESS_CODE_DETAIL_INFO BuinessCodeDetailInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuinessCodeDetailInfoEO extends BaseEntity {

    private String id;
    private String codeStaticId;
    private Integer year;
    private Integer week;
    private Integer codeNum;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private String createUser;
    private Integer createCodeNum;
    private Integer currentCodeNum;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>codeStaticId -> code_static_id</li>
     * <li>year -> year</li>
     * <li>week -> week</li>
     * <li>codeNum -> code_num</li>
     * <li>createTime -> create_time</li>
     * <li>modifyTime -> modify_time</li>
     * <li>createUser -> create_user</li>
     * <li>createCodeNum -> create_code_num</li>
     * <li>currentCodeNum -> current_code_num</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "codeStaticId": return "code_static_id";
            case "year": return "year";
            case "week": return "week";
            case "codeNum": return "code_num";
            case "createTime": return "create_time";
            case "modifyTime": return "modify_time";
            case "createUser": return "create_user";
            case "createCodeNum": return "create_code_num";
            case "currentCodeNum": return "current_code_num";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>code_static_id -> codeStaticId</li>
     * <li>year -> year</li>
     * <li>week -> week</li>
     * <li>code_num -> codeNum</li>
     * <li>create_time -> createTime</li>
     * <li>modify_time -> modifyTime</li>
     * <li>create_user -> createUser</li>
     * <li>create_code_num -> createCodeNum</li>
     * <li>current_code_num -> currentCodeNum</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "code_static_id": return "codeStaticId";
            case "year": return "year";
            case "week": return "week";
            case "code_num": return "codeNum";
            case "create_time": return "createTime";
            case "modify_time": return "modifyTime";
            case "create_user": return "createUser";
            case "create_code_num": return "createCodeNum";
            case "current_code_num": return "currentCodeNum";
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
    public String getCodeStaticId() {
        return this.codeStaticId;
    }

    /**  **/
    public void setCodeStaticId(String codeStaticId) {
        this.codeStaticId = codeStaticId;
    }

    /**  **/
    public Integer getYear() {
        return this.year;
    }

    /**  **/
    public void setYear(Integer year) {
        this.year = year;
    }

    /**  **/
    public Integer getWeek() {
        return this.week;
    }

    /**  **/
    public void setWeek(Integer week) {
        this.week = week;
    }

    /**  **/
    public Integer getCodeNum() {
        return this.codeNum;
    }

    /**  **/
    public void setCodeNum(Integer codeNum) {
        this.codeNum = codeNum;
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
    public String getCreateUser() {
        return this.createUser;
    }

    /**  **/
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**  **/
    public Integer getCreateCodeNum() {
        return this.createCodeNum;
    }

    /**  **/
    public void setCreateCodeNum(Integer createCodeNum) {
        this.createCodeNum = createCodeNum;
    }

    /**  **/
    public Integer getCurrentCodeNum() {
        return this.currentCodeNum;
    }

    /**  **/
    public void setCurrentCodeNum(Integer currentCodeNum) {
        this.currentCodeNum = currentCodeNum;
    }

}
