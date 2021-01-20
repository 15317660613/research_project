package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>BUISNESS_INCOME_SPEEDING BuisnessIncomeSpeedingEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuisnessIncomeSpeedingEO extends BaseEntity {

    private String id;
    private String incomeContactId;
    private String speedingContractId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private String createUser;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>incomeContactId -> income_contact_id</li>
     * <li>speedingContractId -> speeding_contract_id</li>
     * <li>createTime -> create_time</li>
     * <li>modifyTime -> modify_time</li>
     * <li>createUser -> create_user</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "incomeContactId": return "income_contact_id";
            case "speedingContractId": return "speeding_contract_id";
            case "createTime": return "create_time";
            case "modifyTime": return "modify_time";
            case "createUser": return "create_user";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>income_contact_id -> incomeContactId</li>
     * <li>speeding_contract_id -> speedingContractId</li>
     * <li>create_time -> createTime</li>
     * <li>modify_time -> modifyTime</li>
     * <li>create_user -> createUser</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "income_contact_id": return "incomeContactId";
            case "speeding_contract_id": return "speedingContractId";
            case "create_time": return "createTime";
            case "modify_time": return "modifyTime";
            case "create_user": return "createUser";
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
    public String getIncomeContactId() {
        return this.incomeContactId;
    }

    /**  **/
    public void setIncomeContactId(String incomeContactId) {
        this.incomeContactId = incomeContactId;
    }

    /**  **/
    public String getSpeedingContractId() {
        return this.speedingContractId;
    }

    /**  **/
    public void setSpeedingContractId(String speedingContractId) {
        this.speedingContractId = speedingContractId;
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

}
