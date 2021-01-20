package com.adc.da.research.project.vo;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_BUDGET_FUND_HISTORY BudgetFundHistoryEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BudgetFundVO extends BaseEntity {

    private String changeUser;
    private String changeUserId;
    private String id;
    private String projectId;
    private String budgetType;
    private String budgetYear;
    private Double budgetAmount;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private Long delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private Double depreciation;
    private Double staffCosts;
    private String remark;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>changeUser -> change_user</li>
     * <li>changeUserId -> change_user_id</li>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>budgetType -> budget_type</li>
     * <li>budgetYear -> budget_year</li>
     * <li>budgetAmount -> budget_amount</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>createTime -> create_time</li>
     * <li>modifyTime -> modify_time</li>
     * <li>delFlag -> del_flag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "changeUser": return "change_user";
            case "changeUserId": return "change_user_id";
            case "id": return "id";
            case "projectId": return "project_id";
            case "budgetType": return "budget_type";
            case "budgetYear": return "budget_year";
            case "budgetAmount": return "budget_amount";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "createTime": return "create_time";
            case "modifyTime": return "modify_time";
            case "delFlag": return "del_flag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>change_user -> changeUser</li>
     * <li>change_user_id -> changeUserId</li>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>budget_type -> budgetType</li>
     * <li>budget_year -> budgetYear</li>
     * <li>budget_amount -> budgetAmount</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>create_time -> createTime</li>
     * <li>modify_time -> modifyTime</li>
     * <li>del_flag -> delFlag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "change_user": return "changeUser";
            case "change_user_id": return "changeUserId";
            case "id": return "id";
            case "project_id": return "projectId";
            case "budget_type": return "budgetType";
            case "budget_year": return "budgetYear";
            case "budget_amount": return "budgetAmount";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "create_time": return "createTime";
            case "modify_time": return "modifyTime";
            case "del_flag": return "delFlag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            default: return null;
        }
    }
    
    /**  **/
    public String getChangeUser() {
        return this.changeUser;
    }

    /**  **/
    public void setChangeUser(String changeUser) {
        this.changeUser = changeUser;
    }

    /**  **/
    public String getChangeUserId() {
        return this.changeUserId;
    }

    /**  **/
    public void setChangeUserId(String changeUserId) {
        this.changeUserId = changeUserId;
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
    public String getBudgetType() {
        return this.budgetType;
    }

    /**  **/
    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    /**  **/
    public String getBudgetYear() {
        return this.budgetYear;
    }

    /**  **/
    public void setBudgetYear(String budgetYear) {
        this.budgetYear = budgetYear;
    }

    /**  **/
    public Double getBudgetAmount() {
        return this.budgetAmount;
    }

    /**  **/
    public void setBudgetAmount(Double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    /**  **/
    public String getCreateUserId() {
        return this.createUserId;
    }

    /**  **/
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**  **/
    public String getCreateUserName() {
        return this.createUserName;
    }

    /**  **/
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
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
    public Long getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Long delFlag) {
        this.delFlag = delFlag;
    }

    /**  **/
    public String getExt1() {
        return this.ext1;
    }

    /**  **/
    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    /**  **/
    public String getExt2() {
        return this.ext2;
    }

    /**  **/
    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    /**  **/
    public String getExt3() {
        return this.ext3;
    }

    /**  **/
    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    /**  **/
    public String getExt4() {
        return this.ext4;
    }

    /**  **/
    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    /**  **/
    public String getExt5() {
        return this.ext5;
    }

    /**  **/
    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    public Double getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(Double depreciation) {
        this.depreciation = depreciation;
    }

    public Double getStaffCosts() {
        return staffCosts;
    }

    public void setStaffCosts(Double staffCosts) {
        this.staffCosts = staffCosts;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
