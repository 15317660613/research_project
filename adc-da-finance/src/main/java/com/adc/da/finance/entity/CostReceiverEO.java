package com.adc.da.finance.entity;

import com.adc.da.base.entity.BaseEntity;

/**
 * <b>功能：</b>F_COST_RECEIVER CostReceiverEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class CostReceiverEO extends BaseEntity {

    private String id;

    private String orgId;

    private String orgName;

    private String userIds;

    private String userNames;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>orgId -> org_id</li>
     * <li>orgName -> org_name</li>
     * <li>userIds -> user_ids</li>
     * <li>userNames -> user_names</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
        case "id":
            return "id";
        case "orgId":
            return "org_id";
        case "orgName":
            return "org_name";
        case "userIds":
            return "user_ids";
        case "userNames":
            return "user_names";
        default:
            return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>org_id -> orgId</li>
     * <li>org_name -> orgName</li>
     * <li>user_ids -> userIds</li>
     * <li>user_names -> userNames</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
        case "id":
            return "id";
        case "org_id":
            return "orgId";
        case "org_name":
            return "orgName";
        case "user_ids":
            return "userIds";
        case "user_names":
            return "userNames";
        default:
            return null;
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
    public String getOrgId() {
        return this.orgId;
    }

    /**  **/
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**  **/
    public String getOrgName() {
        return this.orgName;
    }

    /**  **/
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**  **/
    public String getUserIds() {
        return this.userIds;
    }

    /**  **/
    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    /**  **/
    public String getUserNames() {
        return this.userNames;
    }

    /**  **/
    public void setUserNames(String userNames) {
        this.userNames = userNames;
    }

}
