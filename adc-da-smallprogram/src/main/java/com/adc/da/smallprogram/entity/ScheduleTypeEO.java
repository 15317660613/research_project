package com.adc.da.smallprogram.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>TS_SCHEDULE_TYPE TsScheduleTypeEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-03-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScheduleTypeEO extends BaseEntity {

    /**
     * 
     */
    private String id;
    /**
     * 
     */
    private String typeName;
    /**
     * 
     */
    private String typeDescribe;
    /**
     * 
     */
    private String delFlag;
    /**
     * 
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>typeName -> type_name</li>
     * <li>typeDescribe -> type_describe</li>
     * <li>delFlag -> del_flag</li>
     * <li>createTime -> create_time</li>
     * <li>updateTime -> update_time</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) return null;
        switch (fieldName) {
            case "id": return "id";
            case "typeName": return "type_name";
            case "typeDescribe": return "type_describe";
            case "delFlag": return "del_flag";
            case "createTime": return "create_time";
            case "updateTime": return "update_time";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>type_name -> typeName</li>
     * <li>type_describe -> typeDescribe</li>
     * <li>del_flag -> delFlag</li>
     * <li>create_time -> createTime</li>
     * <li>update_time -> updateTime</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) return null;
        switch (columnName) {
            case "id": return "id";
            case "type_name": return "typeName";
            case "type_describe": return "typeDescribe";
            case "del_flag": return "delFlag";
            case "create_time": return "createTime";
            case "update_time": return "updateTime";
            default: return null;
        }
    }
    
    /**
     * get 
     */
    public String getId() {
        return id;
    }

    /**
     * set 
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * get 
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * set 
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    /**
     * get 
     */
    public String getTypeDescribe() {
        return typeDescribe;
    }

    /**
     * set 
     */
    public void setTypeDescribe(String typeDescribe) {
        this.typeDescribe = typeDescribe;
    }
    /**
     * get 
     */
    public String getDelFlag() {
        return delFlag;
    }

    /**
     * set 
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    /**
     * get 
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * set 
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     * get 
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * set 
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
