package com.adc.da.smallprogram.page;

import com.adc.da.base.page.BasePage;


/**
 * <b>功能：</b>TS_SCHEUDLE_PERMISSION ScheudlePermissionEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScheudlePermissionEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String originUserId;
    private String originUserIdOperator = "=";
    private String destUserId;
    private String destUserIdOperator = "LIKE";
    private String originUserName;
    private String originUserNameOperator = "=";
    private String destUserName;
    private String destUserNameOperator = "=";
    private String destUserMap;
    private String destUserMapOperator = "=";
    private String configType;
    private String configTypeOperator = "=";

    private String maintenancePersonName;
    private String maintenancePersonNameOperator = "=";

    private String maintenancePersonMap;
    private String maintenancePersonMapOperator = "=";

    private String maintenancePersonId;
    private String maintenancePersonIdOperator = "=";

    public String getMaintenancePersonId() {
        return maintenancePersonId;
    }

    public void setMaintenancePersonId(String maintenancePersonId) {
        this.maintenancePersonId = maintenancePersonId;
    }

    public String getMaintenancePersonIdOperator() {
        return maintenancePersonIdOperator;
    }

    public void setMaintenancePersonIdOperator(String maintenancePersonIdOperator) {
        this.maintenancePersonIdOperator = maintenancePersonIdOperator;
    }

    public String getMaintenancePersonName() {
        return maintenancePersonName;
    }

    public void setMaintenancePersonName(String maintenancePersonName) {
        this.maintenancePersonName = maintenancePersonName;
    }

    public String getMaintenancePersonNameOperator() {
        return maintenancePersonNameOperator;
    }

    public void setMaintenancePersonNameOperator(String maintenancePersonNameOperator) {
        this.maintenancePersonNameOperator = maintenancePersonNameOperator;
    }

    public String getMaintenancePersonMap() {
        return maintenancePersonMap;
    }

    public void setMaintenancePersonMap(String maintenancePersonMap) {
        this.maintenancePersonMap = maintenancePersonMap;
    }

    public String getMaintenancePersonMapOperator() {
        return maintenancePersonMapOperator;
    }

    public void setMaintenancePersonMapOperator(String maintenancePersonMapOperator) {
        this.maintenancePersonMapOperator = maintenancePersonMapOperator;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOperator() {
        return this.idOperator;
    }

    public void setIdOperator(String idOperator) {
        this.idOperator = idOperator;
    }

    public String getOriginUserId() {
        return this.originUserId;
    }

    public void setOriginUserId(String originUserId) {
        this.originUserId = originUserId;
    }

    public String getOriginUserIdOperator() {
        return this.originUserIdOperator;
    }

    public void setOriginUserIdOperator(String originUserIdOperator) {
        this.originUserIdOperator = originUserIdOperator;
    }

    public String getDestUserId() {
        return this.destUserId;
    }

    public void setDestUserId(String destUserId) {
        this.destUserId = destUserId;
    }

    public String getDestUserIdOperator() {
        return this.destUserIdOperator;
    }

    public void setDestUserIdOperator(String destUserIdOperator) {
        this.destUserIdOperator = destUserIdOperator;
    }

    public String getOriginUserName() {
        return this.originUserName;
    }

    public void setOriginUserName(String originUserName) {
        this.originUserName = originUserName;
    }

    public String getOriginUserNameOperator() {
        return this.originUserNameOperator;
    }

    public void setOriginUserNameOperator(String originUserNameOperator) {
        this.originUserNameOperator = originUserNameOperator;
    }

    public String getDestUserName() {
        return this.destUserName;
    }

    public void setDestUserName(String destUserName) {
        this.destUserName = destUserName;
    }

    public String getDestUserNameOperator() {
        return this.destUserNameOperator;
    }

    public void setDestUserNameOperator(String destUserNameOperator) {
        this.destUserNameOperator = destUserNameOperator;
    }

    public String getDestUserMap() {
        return this.destUserMap;
    }

    public void setDestUserMap(String destUserMap) {
        this.destUserMap = destUserMap;
    }

    public String getDestUserMapOperator() {
        return this.destUserMapOperator;
    }

    public void setDestUserMapOperator(String destUserMapOperator) {
        this.destUserMapOperator = destUserMapOperator;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public String getConfigTypeOperator() {
        return configTypeOperator;
    }

    public void setConfigTypeOperator(String configTypeOperator) {
        this.configTypeOperator = configTypeOperator;
    }
}
