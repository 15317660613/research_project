package com.adc.da.research.project.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_BUDGET_DETAIL BudgetDetailEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BudgetDetailEO extends BaseEntity {

    private String id;
    private String projectId;
    private String parentId;
    private String budgetType;
    private String budgetDetailTypeId;
    private String budgetDetailTypeName;
    private String budgetYear;
    private String budgetQuarterly;
    private String budgetMonth;
    private Double budgetAmount;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private Integer delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>budgetType -> budget_type</li>
     * <li>budgetDetailTypeId -> budget_detail_type_id</li>
     * <li>budgetDetailTypeName -> budget_detail_type_name</li>
     * <li>budgetYear -> budget_year</li>
     * <li>budgetQuarterly -> budget_quarterly</li>
     * <li>budgetMonth -> budget_month</li>
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
            case "id": return "id";
            case "projectId": return "project_id";
            case "budgetType": return "budget_type";
            case "budgetDetailTypeId": return "budget_detail_type_id";
            case "budgetDetailTypeName": return "budget_detail_type_name";
            case "budgetYear": return "budget_year";
            case "budgetQuarterly": return "budget_quarterly";
            case "budgetMonth": return "budget_month";
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
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>budget_type -> budgetType</li>
     * <li>budget_detail_type_id -> budgetDetailTypeId</li>
     * <li>budget_detail_type_name -> budgetDetailTypeName</li>
     * <li>budget_year -> budgetYear</li>
     * <li>budget_quarterly -> budgetQuarterly</li>
     * <li>budget_month -> budgetMonth</li>
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
            case "id": return "id";
            case "project_id": return "projectId";
            case "budget_type": return "budgetType";
            case "budget_detail_type_id": return "budgetDetailTypeId";
            case "budget_detail_type_name": return "budgetDetailTypeName";
            case "budget_year": return "budgetYear";
            case "budget_quarterly": return "budgetQuarterly";
            case "budget_month": return "budgetMonth";
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
    public String getId() {
        return this.id;
    }

    /**  **/
    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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
    public String getBudgetDetailTypeId() {
        return this.budgetDetailTypeId;
    }

    /**  **/
    public void setBudgetDetailTypeId(String budgetDetailTypeId) {
        this.budgetDetailTypeId = budgetDetailTypeId;
    }

    /**  **/
    public String getBudgetDetailTypeName() {
        return this.budgetDetailTypeName;
    }

    /**  **/
    public void setBudgetDetailTypeName(String budgetDetailTypeName) {
        this.budgetDetailTypeName = budgetDetailTypeName;
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
    public String getBudgetQuarterly() {
        return this.budgetQuarterly;
    }

    /**  **/
    public void setBudgetQuarterly(String budgetQuarterly) {
        this.budgetQuarterly = budgetQuarterly;
    }

    /**  **/
    public String getBudgetMonth() {
        return this.budgetMonth;
    }

    /**  **/
    public void setBudgetMonth(String budgetMonth) {
        this.budgetMonth = budgetMonth;
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
    public Integer getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Integer delFlag) {
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

}
