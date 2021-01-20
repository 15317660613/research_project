package com.adc.da.research.config.entity;

import com.adc.da.base.entity.BaseEntity;

import java.sql.Clob;
import java.util.Date;

/**
 * <b>功能：</b>RS_WARN_NOTICE WarnNoticeEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class WarnNoticeEO extends BaseEntity {

    private String id;
    private String title;
    private String projectTypeId;
    private String nationalLowestProgress;
    private String selfLowestProgress;
    private String warnCotent;
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

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>title -> title</li>
     * <li>projectTypeId -> project_type_id</li>
     * <li>nationalLowestProgress -> national_lowest_progress</li>
     * <li>selfLowestProgress -> self_lowest_progress</li>
     * <li>warnCotent -> warn_cotent</li>
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
            case "title": return "title";
            case "projectTypeId": return "project_type_id";
            case "nationalLowestProgress": return "national_lowest_progress";
            case "selfLowestProgress": return "self_lowest_progress";
            case "warnCotent": return "warn_cotent";
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
     * <li>title -> title</li>
     * <li>project_type_id -> projectTypeId</li>
     * <li>national_lowest_progress -> nationalLowestProgress</li>
     * <li>self_lowest_progress -> selfLowestProgress</li>
     * <li>warn_cotent -> warnCotent</li>
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
            case "title": return "title";
            case "project_type_id": return "projectTypeId";
            case "national_lowest_progress": return "nationalLowestProgress";
            case "self_lowest_progress": return "selfLowestProgress";
            case "warn_cotent": return "warnCotent";
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
    public String getTitle() {
        return this.title;
    }

    /**  **/
    public void setTitle(String title) {
        this.title = title;
    }

    /**  **/
    public String getProjectTypeId() {
        return this.projectTypeId;
    }

    /**  **/
    public void setProjectTypeId(String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    /**  **/
    public String getNationalLowestProgress() {
        return this.nationalLowestProgress;
    }

    /**  **/
    public void setNationalLowestProgress(String nationalLowestProgress) {
        this.nationalLowestProgress = nationalLowestProgress;
    }

    /**  **/
    public String getSelfLowestProgress() {
        return this.selfLowestProgress;
    }

    /**  **/
    public void setSelfLowestProgress(String selfLowestProgress) {
        this.selfLowestProgress = selfLowestProgress;
    }

    /**  **/
    public String getWarnCotent() {
        return this.warnCotent;
    }

    /**  **/
    public void setWarnCotent(String warnCotent) {
        this.warnCotent = warnCotent;
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

}
