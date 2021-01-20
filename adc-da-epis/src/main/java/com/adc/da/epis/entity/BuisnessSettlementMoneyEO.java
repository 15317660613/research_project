package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>BUISNESS_SETTLEMENT_MONEY BuisnessSettlementMoneyEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuisnessSettlementMoneyEO extends BaseEntity {

    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date settleTime;
    private Integer batchNum;
    private Integer finishedStatus;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private String createUser;
    private String integralId;
    private String note;
    private Float invoiceMoney;
    private String id;
    private Float money;
    private Float settlementMoney;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>settleTime -> settle_time</li>
     * <li>batchNum -> batch_num</li>
     * <li>finishedStatus -> finished_status</li>
     * <li>createTime -> create_time</li>
     * <li>modifyTime -> modify_time</li>
     * <li>createUser -> create_user</li>
     * <li>integralId -> integral_id</li>
     * <li>note -> note</li>
     * <li>invoiceMoney -> invoice_money</li>
     * <li>id -> id</li>
     * <li>money -> money</li>
     * <li>settlementMoney -> settlement_money</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "settleTime": return "settle_time";
            case "batchNum": return "batch_num";
            case "finishedStatus": return "finished_status";
            case "createTime": return "create_time";
            case "modifyTime": return "modify_time";
            case "createUser": return "create_user";
            case "integralId": return "integral_id";
            case "note": return "note";
            case "invoiceMoney": return "invoice_money";
            case "id": return "id";
            case "money": return "money";
            case "settlementMoney": return "settlement_money";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>settle_time -> settleTime</li>
     * <li>batch_num -> batchNum</li>
     * <li>finished_status -> finishedStatus</li>
     * <li>create_time -> createTime</li>
     * <li>modify_time -> modifyTime</li>
     * <li>create_user -> createUser</li>
     * <li>integral_id -> integralId</li>
     * <li>note -> note</li>
     * <li>invoice_money -> invoiceMoney</li>
     * <li>id -> id</li>
     * <li>money -> money</li>
     * <li>settlement_money -> settlementMoney</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "settle_time": return "settleTime";
            case "batch_num": return "batchNum";
            case "finished_status": return "finishedStatus";
            case "create_time": return "createTime";
            case "modify_time": return "modifyTime";
            case "create_user": return "createUser";
            case "integral_id": return "integralId";
            case "note": return "note";
            case "invoice_money": return "invoiceMoney";
            case "id": return "id";
            case "money": return "money";
            case "settlement_money": return "settlementMoney";
            default: return null;
        }
    }
    
    /**  **/
    public Date getSettleTime() {
        return this.settleTime;
    }

    /**  **/
    public void setSettleTime(Date settleTime) {
        this.settleTime = settleTime;
    }

    /**  **/
    public Integer getBatchNum() {
        return this.batchNum;
    }

    /**  **/
    public void setBatchNum(Integer batchNum) {
        this.batchNum = batchNum;
    }

    /**  **/
    public Integer getFinishedStatus() {
        return this.finishedStatus;
    }

    /**  **/
    public void setFinishedStatus(Integer finishedStatus) {
        this.finishedStatus = finishedStatus;
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
    public String getIntegralId() {
        return this.integralId;
    }

    /**  **/
    public void setIntegralId(String integralId) {
        this.integralId = integralId;
    }

    /**  **/
    public String getNote() {
        return this.note;
    }

    /**  **/
    public void setNote(String note) {
        this.note = note;
    }

    /**  **/
    public Float getInvoiceMoney() {
        return this.invoiceMoney;
    }

    /**  **/
    public void setInvoiceMoney(Float invoiceMoney) {
        this.invoiceMoney = invoiceMoney;
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
    public Float getMoney() {
        return this.money;
    }

    /**  **/
    public void setMoney(Float money) {
        this.money = money;
    }

    /**  **/
    public Float getSettlementMoney() {
        return this.settlementMoney;
    }

    /**  **/
    public void setSettlementMoney(Float settlementMoney) {
        this.settlementMoney = settlementMoney;
    }

}
