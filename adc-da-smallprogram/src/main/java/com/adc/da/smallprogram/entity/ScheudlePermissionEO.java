package com.adc.da.smallprogram.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * <b>功能：</b>TS_SCHEUDLE_PERMISSION ScheudlePermissionEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScheudlePermissionEO extends BaseEntity {

    private String id;
    private String originUserId;
    private String destUserId;
    private String originUserName;
    private String destUserName;
    private String destUserMap;
    private String configType;  //0-行程权限配置，1-任务代理配置

    private String maintenancePersonName;
    private String maintenancePersonMap;
    private String maintenancePersonId;


    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>originUserId -> origin_user_id</li>
     * <li>destUserId -> dest_user_id</li>
     * <li>originUserName -> origin_user_name</li>
     * <li>destUserName -> dest_user_name</li>
     * <li>destUserMap -> dest_user_map</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "originUserId": return "origin_user_id";
            case "destUserId": return "dest_user_id";
            case "originUserName": return "origin_user_name";
            case "destUserName": return "dest_user_name";
            case "destUserMap": return "dest_user_map";

            case "maintenancePersonName": return "maintenance_person_name";
            case "maintenancePersonMap": return "maintenance_person_map";
            case "maintenancePersonId": return "maintenance_person_id";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>origin_user_id -> originUserId</li>
     * <li>dest_user_id -> destUserId</li>
     * <li>origin_user_name -> originUserName</li>
     * <li>dest_user_name -> destUserName</li>
     * <li>dest_user_map -> destUserMap</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "origin_user_id": return "originUserId";
            case "dest_user_id": return "destUserId";
            case "origin_user_name": return "originUserName";
            case "dest_user_name": return "destUserName";
            case "dest_user_map": return "destUserMap";
            case "maintenance_person_name": return "maintenancePersonName";
            case "maintenance_person_map": return "maintenancePersonMap";
            case "maintenance_person_id": return "maintenancePersonId";
            default: return null;
        }
    }

    public String getMaintenancePersonId() {
        return maintenancePersonId;
    }

    public void setMaintenancePersonId(String maintenancePersonId) {
        this.maintenancePersonId = maintenancePersonId;
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
    public String getOriginUserId() {
        return this.originUserId;
    }

    /**  **/
    public void setOriginUserId(String originUserId) {
        this.originUserId = originUserId;
    }

    /**  **/
    public String getDestUserId() {
        return this.destUserId;
    }

    /**  **/
    public void setDestUserId(String destUserId) {
        this.destUserId = destUserId;
    }

    /**  **/
    public String getOriginUserName() {
        return this.originUserName;
    }

    /**  **/
    public void setOriginUserName(String originUserName) {
        this.originUserName = originUserName;
    }

    /**  **/
    public String getDestUserName() {
        return this.destUserName;
    }

    /**  **/
    public void setDestUserName(String destUserName) {
        this.destUserName = destUserName;
    }

    /**  **/
    public String getDestUserMap() {
        return this.destUserMap;
    }

    /**  **/
    public void setDestUserMap(String destUserMap) {
        this.destUserMap = destUserMap;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getMaintenancePersonName() {
        return maintenancePersonName;
    }

    public void setMaintenancePersonName(String maintenancePersonName) {
        this.maintenancePersonName = maintenancePersonName;
    }

    public String getMaintenancePersonMap() {
        return maintenancePersonMap;
    }

    public void setMaintenancePersonMap(String maintenancePersonMap) {
        this.maintenancePersonMap = maintenancePersonMap;
    }
}
