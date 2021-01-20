package com.adc.da.ext.sys.branchedleaders.entity;

import com.adc.da.base.entity.BaseEntity;


/**
 * <b>功能：</b>TS_BRANCHED_LEADERS BranchedLeadersEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BranchedLeadersEO extends BaseEntity {

    private String id;
    private String orgId;
    private String userId;
    private String userName;
    private String configType;
    private String userMap;
    private String assiantantIds;
    private String assiantantNames;
    private String contractsManagerIds;
    private String contractsManagers;
    private String orgName;
    private String parentOrgName;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>orgId -> org_id</li>
     * <li>userId -> user_id</li>
     * <li>userName -> user_name</li>
     * <li>configType -> config_type</li>
     * <li>userMap -> user_map</li>
     * <li>assiantantIds -> assiantant_ids</li>
     * <li>assiantantNames -> assiantant_names</li>
     * <li>contractsManagerIds -> contracts_manager_ids</li>
     * <li>contractsManagers -> contracts_managers</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "bid";
            case "orgId": return "org_id";
            case "userId": return "user_id";
            case "userName": return "user_name";
            case "configType": return "config_type";
            case "userMap": return "user_map";
            case "assiantantIds": return "assiantant_ids";
            case "assiantantNames": return "assiantant_names";
            case "contractsManagerIds": return "contracts_manager_ids";
            case "contractsManagers": return "contracts_managers";
            case "orgName": return "org_name";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>org_id -> orgId</li>
     * <li>user_id -> userId</li>
     * <li>user_name -> userName</li>
     * <li>config_type -> configType</li>
     * <li>user_map -> userMap</li>
     * <li>assiantant_ids -> assiantantIds</li>
     * <li>assiantant_names -> assiantantNames</li>
     * <li>contracts_manager_ids -> contractsManagerIds</li>
     * <li>contracts_managers -> contractsManagers</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "bid": return "id";
            case "org_id": return "orgId";
            case "user_id": return "userId";
            case "user_name": return "userName";
            case "config_type": return "configType";
            case "user_map": return "userMap";
            case "assiantant_ids": return "assiantantIds";
            case "assiantant_names": return "assiantantNames";
            case "contracts_manager_ids": return "contractsManagerIds";
            case "contracts_managers": return "contractsManagers";
            case "org_name": return "orgName";
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
    public String getOrgId() {
        return this.orgId;
    }

    /**  **/
    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    /**  **/
    public String getUserId() {
        return this.userId;
    }

    /**  **/
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**  **/
    public String getUserName() {
        return this.userName;
    }

    /**  **/
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**  **/
    public String getConfigType() {
        return this.configType;
    }

    /**  **/
    public void setConfigType(String configType) {
        this.configType = configType;
    }

    /**  **/
    public String getUserMap() {
        return this.userMap;
    }

    /**  **/
    public void setUserMap(String userMap) {
        this.userMap = userMap;
    }

    /**  **/
    public String getAssiantantIds() {
        return this.assiantantIds;
    }

    /**  **/
    public void setAssiantantIds(String assiantantIds) {
        this.assiantantIds = assiantantIds;
    }

    /**  **/
    public String getAssiantantNames() {
        return this.assiantantNames;
    }

    /**  **/
    public void setAssiantantNames(String assiantantNames) {
        this.assiantantNames = assiantantNames;
    }

    /**  **/
    public String getContractsManagerIds() {
        return this.contractsManagerIds;
    }

    /**  **/
    public void setContractsManagerIds(String contractsManagerIds) {
        this.contractsManagerIds = contractsManagerIds;
    }

    /**  **/
    public String getContractsManagers() {
        return this.contractsManagers;
    }

    /**  **/
    public void setContractsManagers(String contractsManagers) {
        this.contractsManagers = contractsManagers;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }
}
