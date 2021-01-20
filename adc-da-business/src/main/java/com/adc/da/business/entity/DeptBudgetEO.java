package com.adc.da.business.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;


/**
 * <b>功能：</b>TS_DEPT_BUDGET DeptBudgetEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-05-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class DeptBudgetEO extends BaseEntity {

    private String id;
    @Excel(name = "年", orderNum = "1")
    private String year;
    @Excel(name = "月", orderNum = "2")
    private String month;
    private String deptId;
    @Excel(name = "部门", orderNum = "3")
    private String deptName;
    @Excel(name = "实际合同额", orderNum = "4")
    private Float actualContractAmount = 0.0f;
    @Excel(name = "实际开票", orderNum = "5")
    private Float actualTicketOpening = 0.0f;
    @Excel(name = "实际利润", orderNum = "6")
    private Float actualProfit = 0.0f;
    @Excel(name = "实际成本", orderNum = "7")
    private Float actualCost = 0.0f;
    @Excel(name = "办公用品支出", orderNum = "8")
    private Float officeSuppliesExpenditure = 0.0f;
    @Excel(name = "耗材支出", orderNum = "9")
    private Float consumablesExpenditure = 0.0f;
    @Excel(name = "差旅支出", orderNum = "10")
    private Float travelExpenses = 0.0f;
    @Excel(name = "开票预算", orderNum = "11")
    private Float invoicingBudget = 0.0f;
    @Excel(name = "成本费用预算", orderNum = "12")
    private Float costBudget = 0.0f;
    @Excel(name = "利润预算", orderNum = "13")
    private Float profitBudget = 0.0f;
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
     * <li>actualContractAmount -> actual_contract_amount</li>
     * <li>actualTicketOpening -> actual_ticket_opening</li>
     * <li>actualProfit -> actual_profit</li>
     * <li>actualCost -> actual_cost</li>
     * <li>officeSuppliesExpenditure -> office_supplies_expenditure</li>
     * <li>consumablesExpenditure -> consumables_expenditure</li>
     * <li>travelExpenses -> travel_expenses</li>
     * <li>invoicingBudget -> invoicing_budget</li>
     * <li>costBudget -> cost_budget</li>
     * <li>profitBudget -> profit_budget</li>
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
            case "actualContractAmount": return "actual_contract_amount";
            case "actualTicketOpening": return "actual_ticket_opening";
            case "actualProfit": return "actual_profit";
            case "actualCost": return "actual_cost";
            case "officeSuppliesExpenditure": return "office_supplies_expenditure";
            case "consumablesExpenditure": return "consumables_expenditure";
            case "travelExpenses": return "travel_expenses";
            case "invoicingBudget": return "invoicing_budget";
            case "costBudget": return "cost_budget";
            case "profitBudget": return "profit_budget";
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
     * <li>actual_contract_amount -> actualContractAmount</li>
     * <li>actual_ticket_opening -> actualTicketOpening</li>
     * <li>actual_profit -> actualProfit</li>
     * <li>actual_cost -> actualCost</li>
     * <li>office_supplies_expenditure -> officeSuppliesExpenditure</li>
     * <li>consumables_expenditure -> consumablesExpenditure</li>
     * <li>travel_expenses -> travelExpenses</li>
     * <li>invoicing_budget -> invoicingBudget</li>
     * <li>cost_budget -> costBudget</li>
     * <li>profit_budget -> profitBudget</li>
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
            case "actual_contract_amount": return "actualContractAmount";
            case "actual_ticket_opening": return "actualTicketOpening";
            case "actual_profit": return "actualProfit";
            case "actual_cost": return "actualCost";
            case "office_supplies_expenditure": return "officeSuppliesExpenditure";
            case "consumables_expenditure": return "consumablesExpenditure";
            case "travel_expenses": return "travelExpenses";
            case "invoicing_budget": return "invoicingBudget";
            case "cost_budget": return "costBudget";
            case "profit_budget": return "profitBudget";
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
    public String getYear() {
        return this.year;
    }

    /**  **/
    public void setYear(String year) {
        this.year = year;
    }

    /**  **/
    public String getMonth() {
        return this.month;
    }

    /**  **/
    public void setMonth(String month) {
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
    public Float getActualContractAmount() {
        return this.actualContractAmount;
    }

    /**  **/
    public void setActualContractAmount(Float actualContractAmount) {
        this.actualContractAmount = actualContractAmount;
    }

    /**  **/
    public Float getActualTicketOpening() {
        return this.actualTicketOpening;
    }

    /**  **/
    public void setActualTicketOpening(Float actualTicketOpening) {
        this.actualTicketOpening = actualTicketOpening;
    }

    /**  **/
    public Float getActualProfit() {
        return this.actualProfit;
    }

    /**  **/
    public void setActualProfit(Float actualProfit) {
        this.actualProfit = actualProfit;
    }

    /**  **/
    public Float getActualCost() {
        return this.actualCost;
    }

    /**  **/
    public void setActualCost(Float actualCost) {
        this.actualCost = actualCost;
    }

    /**  **/
    public Float getOfficeSuppliesExpenditure() {
        return this.officeSuppliesExpenditure;
    }

    /**  **/
    public void setOfficeSuppliesExpenditure(Float officeSuppliesExpenditure) {
        this.officeSuppliesExpenditure = officeSuppliesExpenditure;
    }

    /**  **/
    public Float getConsumablesExpenditure() {
        return this.consumablesExpenditure;
    }

    /**  **/
    public void setConsumablesExpenditure(Float consumablesExpenditure) {
        this.consumablesExpenditure = consumablesExpenditure;
    }

    /**  **/
    public Float getTravelExpenses() {
        return this.travelExpenses;
    }

    /**  **/
    public void setTravelExpenses(Float travelExpenses) {
        this.travelExpenses = travelExpenses;
    }

    /**  **/
    public Float getInvoicingBudget() {
        return this.invoicingBudget;
    }

    /**  **/
    public void setInvoicingBudget(Float invoicingBudget) {
        this.invoicingBudget = invoicingBudget;
    }

    /**  **/
    public Float getCostBudget() {
        return this.costBudget;
    }

    /**  **/
    public void setCostBudget(Float costBudget) {
        this.costBudget = costBudget;
    }

    /**  **/
    public Float getProfitBudget() {
        return this.profitBudget;
    }

    /**  **/
    public void setProfitBudget(Float profitBudget) {
        this.profitBudget = profitBudget;
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
        return "DeptBudgetEO{" +
                "id='" + id + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", actualContractAmount=" + actualContractAmount +
                ", actualTicketOpening=" + actualTicketOpening +
                ", actualProfit=" + actualProfit +
                ", actualCost=" + actualCost +
                ", officeSuppliesExpenditure=" + officeSuppliesExpenditure +
                ", consumablesExpenditure=" + consumablesExpenditure +
                ", travelExpenses=" + travelExpenses +
                ", invoicingBudget=" + invoicingBudget +
                ", costBudget=" + costBudget +
                ", profitBudget=" + profitBudget +
                ", extInfo1='" + extInfo1 + '\'' +
                ", extInfo2='" + extInfo2 + '\'' +
                ", extInfo3='" + extInfo3 + '\'' +
                ", extInfo4='" + extInfo4 + '\'' +
                ", extInfo5='" + extInfo5 + '\'' +
                ", extInfo6='" + extInfo6 + '\'' +
                '}';
    }
}
