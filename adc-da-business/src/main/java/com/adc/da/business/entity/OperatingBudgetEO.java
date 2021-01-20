package com.adc.da.business.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;


/**
 * <b>功能：</b>TS_OPERATING_BUDGET OperatingBudgetEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-05-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class OperatingBudgetEO extends BaseEntity {

    private String id;
    @Excel(name = "年", orderNum = "1")
    private Long year;
    @Excel(name = "月", orderNum = "2")
    private Long month;
    private String deptId;
    @Excel(name = "部门", orderNum = "3")
    private String deptName;
    @Excel(name = "业务名称", orderNum = "4")
    private String businessName;
    @Excel(name = "合同额", orderNum = "5")
    private Float contractSum = 0.0f;
    @Excel(name = "开票额", orderNum = "6")
    private Float invoiceAmount = 0.0f;
    @Excel(name = "进账额", orderNum = "7")
    private Float contributionAmount = 0.0f;
    @Excel(name = "应收账款", orderNum = "8")
    private Float accountsReceivable = 0.0f;
    @Excel(name = "业务收入预算", orderNum = "9")
    private Float revenueBudget = 0.0f;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;
    private String extInfo6;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>deptId -> dept_id</li>
     * <li>deptName -> dept_name</li>
     * <li>businessName -> business_name</li>
     * <li>contractSum -> contract_sum</li>
     * <li>invoiceAmount -> invoice_amount</li>
     * <li>contributionAmount -> contribution_amount</li>
     * <li>accountsReceivable -> accounts_receivable</li>
     * <li>revenueBudget -> revenue_budget</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>extInfo5 -> ext_info5</li>
     * <li>extInfo6 -> ext_info6</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "year": return "year";
            case "month": return "month";
            case "deptId": return "dept_id";
            case "deptName": return "dept_name";
            case "businessName": return "business_name";
            case "contractSum": return "contract_sum";
            case "invoiceAmount": return "invoice_amount";
            case "contributionAmount": return "contribution_amount";
            case "accountsReceivable": return "accounts_receivable";
            case "revenueBudget": return "revenue_budget";
            case "extInfo1": return "ext_info1";
            case "extInfo2": return "ext_info2";
            case "extInfo3": return "ext_info3";
            case "extInfo4": return "ext_info4";
            case "extInfo5": return "ext_info5";
            case "extInfo6": return "ext_info6";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>dept_id -> deptId</li>
     * <li>dept_name -> deptName</li>
     * <li>business_name -> businessName</li>
     * <li>contract_sum -> contractSum</li>
     * <li>invoice_amount -> invoiceAmount</li>
     * <li>contribution_amount -> contributionAmount</li>
     * <li>accounts_receivable -> accountsReceivable</li>
     * <li>revenue_budget -> revenueBudget</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>ext_info5 -> extInfo5</li>
     * <li>ext_info6 -> extInfo6</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "year": return "year";
            case "month": return "month";
            case "dept_id": return "deptId";
            case "dept_name": return "deptName";
            case "business_name": return "businessName";
            case "contract_sum": return "contractSum";
            case "invoice_amount": return "invoiceAmount";
            case "contribution_amount": return "contributionAmount";
            case "accounts_receivable": return "accountsReceivable";
            case "revenue_budget": return "revenueBudget";
            case "ext_info1": return "extInfo1";
            case "ext_info2": return "extInfo2";
            case "ext_info3": return "extInfo3";
            case "ext_info4": return "extInfo4";
            case "ext_info5": return "extInfo5";
            case "ext_info6": return "extInfo6";
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
    public Long getYear() {
        return this.year;
    }

    /**  **/
    public void setYear(Long year) {
        this.year = year;
    }

    /**  **/
    public Long getMonth() {
        return this.month;
    }

    /**  **/
    public void setMonth(Long month) {
        this.month = month;
    }

    /**  **/
    public String getDeptId() {
        return this.deptId;
    }

    /**  **/
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**  **/
    public String getDeptName() {
        return this.deptName;
    }

    /**  **/
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**  **/
    public String getBusinessName() {
        return this.businessName;
    }

    /**  **/
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    /**  **/
    public Float getContractSum() {
        return this.contractSum;
    }

    /**  **/
    public void setContractSum(Float contractSum) {
        this.contractSum = contractSum;
    }

    /**  **/
    public Float getInvoiceAmount() {
        return this.invoiceAmount;
    }

    /**  **/
    public void setInvoiceAmount(Float invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    /**  **/
    public Float getContributionAmount() {
        return this.contributionAmount;
    }

    /**  **/
    public void setContributionAmount(Float contributionAmount) {
        this.contributionAmount = contributionAmount;
    }

    /**  **/
    public Float getAccountsReceivable() {
        return this.accountsReceivable;
    }

    /**  **/
    public void setAccountsReceivable(Float accountsReceivable) {
        this.accountsReceivable = accountsReceivable;
    }

    /**  **/
    public Float getRevenueBudget() {
        return this.revenueBudget;
    }

    /**  **/
    public void setRevenueBudget(Float revenueBudget) {
        this.revenueBudget = revenueBudget;
    }

    /**  **/
    public String getExtInfo1() {
        return this.extInfo1;
    }

    /**  **/
    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    /**  **/
    public String getExtInfo2() {
        return this.extInfo2;
    }

    /**  **/
    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

    /**  **/
    public String getExtInfo3() {
        return this.extInfo3;
    }

    /**  **/
    public void setExtInfo3(String extInfo3) {
        this.extInfo3 = extInfo3;
    }

    /**  **/
    public String getExtInfo4() {
        return this.extInfo4;
    }

    /**  **/
    public void setExtInfo4(String extInfo4) {
        this.extInfo4 = extInfo4;
    }

    /**  **/
    public String getExtInfo5() {
        return this.extInfo5;
    }

    /**  **/
    public void setExtInfo5(String extInfo5) {
        this.extInfo5 = extInfo5;
    }

    /**  **/
    public String getExtInfo6() {
        return this.extInfo6;
    }

    /**  **/
    public void setExtInfo6(String extInfo6) {
        this.extInfo6 = extInfo6;
    }

    @Override
    public String toString() {
        return "OperatingBudgetEO{" +
                "id='" + id + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", businessName='" + businessName + '\'' +
                ", contractSum=" + contractSum +
                ", invoiceAmount=" + invoiceAmount +
                ", contributionAmount=" + contributionAmount +
                ", accountsReceivable=" + accountsReceivable +
                ", revenueBudget=" + revenueBudget +
                ", extInfo1='" + extInfo1 + '\'' +
                ", extInfo2='" + extInfo2 + '\'' +
                ", extInfo3='" + extInfo3 + '\'' +
                ", extInfo4='" + extInfo4 + '\'' +
                ", extInfo5='" + extInfo5 + '\'' +
                ", extInfo6='" + extInfo6 + '\'' +
                '}';
    }
}
