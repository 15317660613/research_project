package com.adc.da.statistics.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>ST_DATA_BOARD_BUSINESS DataBoardBusinessEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-19 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class DataBoardBusinessEO extends BaseEntity {

    private String id;
    private String projectId;
    private String budgetId;
    private String projectNo;
    private Double contractAmount;
    private String deptId;
    private Double billing;
    private Double credit;
    private Double expenditure;
    private Double workTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Integer year;
    private Integer month;
    private String extInfo01;
    private String extInfo02;
    private String extInfo03;
    private String extInfo04;
    private String extInfo05;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id_</li>
     * <li>projectId -> project_id_</li>
     * <li>budgetId -> budget_id_</li>
     * <li>projectNo -> project_no_</li>
     * <li>contractAmount -> contract_amount</li>
     * <li>deptId -> dept_id_</li>
     * <li>billing -> billing_</li>
     * <li>credit -> credit_</li>
     * <li>expenditure -> expenditure_</li>
     * <li>workTime -> work_time_</li>
     * <li>createTime -> create_time_</li>
     * <li>year -> year_</li>
     * <li>month -> month_</li>
     * <li>extInfo01 -> ext_info_01_</li>
     * <li>extInfo02 -> ext_info_02_</li>
     * <li>extInfo03 -> ext_info_03_</li>
     * <li>extInfo04 -> ext_info_04_</li>
     * <li>extInfo05 -> ext_info_05_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id_";
            case "projectId": return "project_id_";
            case "budgetId": return "budget_id_";
            case "projectNo": return "project_no_";
            case "contractAmount": return "contract_amount";
            case "deptId": return "dept_id_";
            case "billing": return "billing_";
            case "credit": return "credit_";
            case "expenditure": return "expenditure_";
            case "workTime": return "work_time_";
            case "createTime": return "create_time_";
            case "year": return "year_";
            case "month": return "month_";
            case "extInfo01": return "ext_info_01_";
            case "extInfo02": return "ext_info_02_";
            case "extInfo03": return "ext_info_03_";
            case "extInfo04": return "ext_info_04_";
            case "extInfo05": return "ext_info_05_";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id_ -> id</li>
     * <li>project_id_ -> projectId</li>
     * <li>budget_id_ -> budgetId</li>
     * <li>project_no_ -> projectNo</li>
     * <li>contract_amount -> contractAmount</li>
     * <li>dept_id_ -> deptId</li>
     * <li>billing_ -> billing</li>
     * <li>credit_ -> credit</li>
     * <li>expenditure_ -> expenditure</li>
     * <li>work_time_ -> workTime</li>
     * <li>create_time_ -> createTime</li>
     * <li>year_ -> year</li>
     * <li>month_ -> month</li>
     * <li>ext_info_01_ -> extInfo01</li>
     * <li>ext_info_02_ -> extInfo02</li>
     * <li>ext_info_03_ -> extInfo03</li>
     * <li>ext_info_04_ -> extInfo04</li>
     * <li>ext_info_05_ -> extInfo05</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id_": return "id";
            case "project_id_": return "projectId";
            case "budget_id_": return "budgetId";
            case "project_no_": return "projectNo";
            case "contract_amount": return "contractAmount";
            case "dept_id_": return "deptId";
            case "billing_": return "billing";
            case "credit_": return "credit";
            case "expenditure_": return "expenditure";
            case "work_time_": return "workTime";
            case "create_time_": return "createTime";
            case "year_": return "year";
            case "month_": return "month";
            case "ext_info_01_": return "extInfo01";
            case "ext_info_02_": return "extInfo02";
            case "ext_info_03_": return "extInfo03";
            case "ext_info_04_": return "extInfo04";
            case "ext_info_05_": return "extInfo05";
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
    public String getProjectId() {
        return this.projectId;
    }

    /**  **/
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**  **/
    public String getBudgetId() {
        return this.budgetId;
    }

    /**  **/
    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    /**  **/
    public String getProjectNo() {
        return this.projectNo;
    }

    /**  **/
    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    /**  **/
    public Double getContractAmount() {
        return this.contractAmount;
    }

    /**  **/
    public void setContractAmount(Double contractAmount) {
        this.contractAmount = contractAmount;
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
    public Double getBilling() {
        return this.billing;
    }

    /**  **/
    public void setBilling(Double billing) {
        this.billing = billing;
    }

    /**  **/
    public Double getCredit() {
        return this.credit;
    }

    /**  **/
    public void setCredit(Double credit) {
        this.credit = credit;
    }

    /**  **/
    public Double getExpenditure() {
        return this.expenditure;
    }

    /**  **/
    public void setExpenditure(Double expenditure) {
        this.expenditure = expenditure;
    }

    /**  **/
    public Double getWorkTime() {
        return this.workTime;
    }

    /**  **/
    public void setWorkTime(Double workTime) {
        this.workTime = workTime;
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
    public String getExtInfo01() {
        return this.extInfo01;
    }

    /**  **/
    public void setExtInfo01(String extInfo01) {
        this.extInfo01 = extInfo01;
    }

    /**  **/
    public String getExtInfo02() {
        return this.extInfo02;
    }

    /**  **/
    public void setExtInfo02(String extInfo02) {
        this.extInfo02 = extInfo02;
    }

    /**  **/
    public String getExtInfo03() {
        return this.extInfo03;
    }

    /**  **/
    public void setExtInfo03(String extInfo03) {
        this.extInfo03 = extInfo03;
    }

    /**  **/
    public String getExtInfo04() {
        return this.extInfo04;
    }

    /**  **/
    public void setExtInfo04(String extInfo04) {
        this.extInfo04 = extInfo04;
    }

    /**  **/
    public String getExtInfo05() {
        return this.extInfo05;
    }

    /**  **/
    public void setExtInfo05(String extInfo05) {
        this.extInfo05 = extInfo05;
    }

}
