package com.adc.da.smallprogram.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>TS_SCHEDULE_CONFIG ScheduleConfigEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-05-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScheduleConfigEO extends BaseEntity {

    private String id;
    private String configName;
    private String configValueString;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String updateUserId;
    private String updateUserName;
    private Integer delFlag;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>configName -> config_name</li>
     * <li>configValueString -> config_value_string</li>
     * <li>createTime -> create_time</li>
     * <li>updateTime -> update_time</li>
     * <li>updateUserId -> update_user_id</li>
     * <li>updateUserName -> update_user_name</li>
     * <li>delFlag -> del_flag</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "configName": return "config_name";
            case "configValueString": return "config_value_string";
            case "createTime": return "create_time";
            case "updateTime": return "update_time";
            case "updateUserId": return "update_user_id";
            case "updateUserName": return "update_user_name";
            case "delFlag": return "del_flag";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>config_name -> configName</li>
     * <li>config_value_string -> configValueString</li>
     * <li>create_time -> createTime</li>
     * <li>update_time -> updateTime</li>
     * <li>update_user_id -> updateUserId</li>
     * <li>update_user_name -> updateUserName</li>
     * <li>del_flag -> delFlag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "config_name": return "configName";
            case "config_value_string": return "configValueString";
            case "create_time": return "createTime";
            case "update_time": return "updateTime";
            case "update_user_id": return "updateUserId";
            case "update_user_name": return "updateUserName";
            case "del_flag": return "delFlag";
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
    public String getConfigName() {
        return this.configName;
    }

    /**  **/
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    /**  **/
    public String getConfigValueString() {
        return this.configValueString;
    }

    /**  **/
    public void setConfigValueString(String configValueString) {
        this.configValueString = configValueString;
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
    public String getUpdateUserId() {
        return this.updateUserId;
    }

    /**  **/
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**  **/
    public String getUpdateUserName() {
        return this.updateUserName;
    }

    /**  **/
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    /**  **/
    public Integer getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

}
