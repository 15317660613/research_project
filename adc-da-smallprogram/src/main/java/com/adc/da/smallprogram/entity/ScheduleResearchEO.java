package com.adc.da.smallprogram.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>TS_SCHEDULE_RESEARCH ScheduleResearchEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-05-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScheduleResearchEO extends BaseEntity {

    private String id;
    private String title;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Integer status;
    private Integer delFlag;
    private Long year;
    private Integer month;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>title -> title</li>
     * <li>createTime -> create_time</li>
     * <li>updateTime -> update_time</li>
     * <li>status -> status</li>
     * <li>delFlag -> del_flag</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "title": return "title";
            case "createTime": return "create_time";
            case "updateTime": return "update_time";
            case "status": return "status";
            case "delFlag": return "del_flag";
            case "year": return "year";
            case "month": return "month";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>title -> title</li>
     * <li>create_time -> createTime</li>
     * <li>update_time -> updateTime</li>
     * <li>status -> status</li>
     * <li>del_flag -> delFlag</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "title": return "title";
            case "create_time": return "createTime";
            case "update_time": return "updateTime";
            case "status": return "status";
            case "del_flag": return "delFlag";
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
    public String getTitle() {
        return this.title;
    }

    /**  **/
    public void setTitle(String title) {
        this.title = title;
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
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**  **/
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**  **/
    public Integer getStatus() {
        return this.status;
    }

    /**  **/
    public void setStatus(Integer status) {
        this.status = status;
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
    public Long getYear() {
        return this.year;
    }

    /**  **/
    public void setYear(Long year) {
        this.year = year;
    }

    /**  **/
    public Integer getMonth() {
        return this.month;
    }

    /**  **/
    public void setMonth(Integer month) {
        this.month = month;
    }

}
