package com.adc.da.pad.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.adc.da.base.entity.BaseEntity;

import java.math.BigDecimal;
import java.text.Collator;
import java.util.Date;

/**
 * <b>功能：</b>PAD_OPERATION_MANAGE PadOperationManageEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class PadOperationManageEO extends BaseEntity implements IExcelModel, IExcelDataModel {

    private String errorMsg;

    private int rowNum;

    private String id;
    @Excel(name = "*年", orderNum = "0")
    private Integer year;
    @Excel(name = "*月", orderNum = "1")
    private Integer month;
    @Excel(name = "*本部", orderNum = "2")
    private String bigOrgName;
    private String bigOrgId;
    @Excel(name = "*当月合同额（元）", orderNum = "3")
    private BigDecimal contractAmount;
    @Excel(name = "*当月开票额（元）", orderNum = "4")
    private BigDecimal invoiceAmount;
    @Excel(name = "*当月进账额（元）", orderNum = "5")
    private BigDecimal incomeAmount;
    private String updateUserId;
    private String updateUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Integer delFlag;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>bigOrgName -> big_org_name</li>
     * <li>bigOrgId -> big_org_id</li>
     * <li>contractAmount -> contract_amount</li>
     * <li>invoiceAmount -> invoice_amount</li>
     * <li>incomeAmount -> income_amount</li>
     * <li>updateUserId -> update_user_id</li>
     * <li>updateUserName -> update_user_name</li>
     * <li>updateTime -> update_time</li>
     * <li>delFlag -> del_flag</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "year": return "year";
            case "month": return "month";
            case "bigOrgName": return "big_org_name";
            case "bigOrgId": return "big_org_id";
            case "contractAmount": return "contract_amount";
            case "invoiceAmount": return "invoice_amount";
            case "incomeAmount": return "income_amount";
            case "updateUserId": return "update_user_id";
            case "updateUserName": return "update_user_name";
            case "updateTime": return "update_time";
            case "delFlag": return "del_flag";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>big_org_name -> bigOrgName</li>
     * <li>big_org_id -> bigOrgId</li>
     * <li>contract_amount -> contractAmount</li>
     * <li>invoice_amount -> invoiceAmount</li>
     * <li>income_amount -> incomeAmount</li>
     * <li>update_user_id -> updateUserId</li>
     * <li>update_user_name -> updateUserName</li>
     * <li>update_time -> updateTime</li>
     * <li>del_flag -> delFlag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "year": return "year";
            case "month": return "month";
            case "big_org_name": return "bigOrgName";
            case "big_org_id": return "bigOrgId";
            case "contract_amount": return "contractAmount";
            case "invoice_amount": return "invoiceAmount";
            case "income_amount": return "incomeAmount";
            case "update_user_id": return "updateUserId";
            case "update_user_name": return "updateUserName";
            case "update_time": return "updateTime";
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
    public Integer getYear() {
        return this.year;
    }

    /**  **/
    public void setYear(Integer year) {
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

    /**  **/
    public String getBigOrgName() {
        return this.bigOrgName;
    }

    /**  **/
    public void setBigOrgName(String bigOrgName) {
        this.bigOrgName = bigOrgName;
    }

    /**  **/
    public String getBigOrgId() {
        return this.bigOrgId;
    }

    /**  **/
    public void setBigOrgId(String bigOrgId) {
        this.bigOrgId = bigOrgId;
    }

    /**  **/
    public BigDecimal getContractAmount() {
        return this.contractAmount;
    }

    /**  **/
    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    /**  **/
    public BigDecimal getInvoiceAmount() {
        return this.invoiceAmount;
    }

    /**  **/
    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    /**  **/
    public BigDecimal getIncomeAmount() {
        return this.incomeAmount;
    }

    /**  **/
    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
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
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**  **/
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**  **/
    public Integer getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public int getRowNum() {
        return this.rowNum;
    }

    @Override
    public void setRowNum(int i) {
        this.rowNum = i;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public void setErrorMsg(String s) {
        this.errorMsg = s;
    }

//    @Override
//    public int compareTo(PadOperationManageEO o) {
//        return Collator.getInstance(java.util.Locale.CHINA).compare(this.getBigOrgName(), o.getBigOrgName());
//    }
}
