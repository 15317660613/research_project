package com.adc.da.statics.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>S_STATIC_OPERATION_AMOUNT SStaticOperationAmountEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-09-10 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class SStaticOperationAmountEO extends BaseEntity {

    private Integer isBigorg;
    private String departmentName;
    private String departmentId;
    private String area;
    private String province;
    private String id;
    private String year;
    private String month;
    private Double amount;
    private Integer amountType;
    private Double growthRate;
    private String createuserId;
    private String createuserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String updaterId;
    private String updaterName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private Integer delFlag;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>isBigorg -> is_bigorg</li>
     * <li>departmentName -> department_name</li>
     * <li>departmentId -> department_id</li>
     * <li>area -> area</li>
     * <li>province -> province</li>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>amount -> amount</li>
     * <li>amountType -> amount_type</li>
     * <li>growthRate -> growth_rate</li>
     * <li>createuserId -> createuser_id</li>
     * <li>createuserName -> createuser_name</li>
     * <li>createTime -> create_time</li>
     * <li>updaterId -> updater_id</li>
     * <li>updaterName -> updater_name</li>
     * <li>updateTime -> update_time</li>
     * <li>delFlag -> del_flag</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "isBigorg": return "is_bigorg";
            case "departmentName": return "department_name";
            case "departmentId": return "department_id";
            case "area": return "area";
            case "province": return "province";
            case "id": return "id";
            case "year": return "year";
            case "month": return "month";
            case "amount": return "amount";
            case "amountType": return "amount_type";
            case "growthRate": return "growth_rate";
            case "createuserId": return "createuser_id";
            case "createuserName": return "createuser_name";
            case "createTime": return "create_time";
            case "updaterId": return "updater_id";
            case "updaterName": return "updater_name";
            case "updateTime": return "update_time";
            case "delFlag": return "del_flag";
            case "extInfo1": return "ext_info1";
            case "extInfo2": return "ext_info2";
            case "extInfo3": return "ext_info3";
            case "extInfo4": return "ext_info4";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>is_bigorg -> isBigorg</li>
     * <li>department_name -> departmentName</li>
     * <li>department_id -> departmentId</li>
     * <li>area -> area</li>
     * <li>province -> province</li>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>amount -> amount</li>
     * <li>amount_type -> amountType</li>
     * <li>growth_rate -> growthRate</li>
     * <li>createuser_id -> createuserId</li>
     * <li>createuser_name -> createuserName</li>
     * <li>create_time -> createTime</li>
     * <li>updater_id -> updaterId</li>
     * <li>updater_name -> updaterName</li>
     * <li>update_time -> updateTime</li>
     * <li>del_flag -> delFlag</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "is_bigorg": return "isBigorg";
            case "department_name": return "departmentName";
            case "department_id": return "departmentId";
            case "area": return "area";
            case "province": return "province";
            case "id": return "id";
            case "year": return "year";
            case "month": return "month";
            case "amount": return "amount";
            case "amount_type": return "amountType";
            case "growth_rate": return "growthRate";
            case "createuser_id": return "createuserId";
            case "createuser_name": return "createuserName";
            case "create_time": return "createTime";
            case "updater_id": return "updaterId";
            case "updater_name": return "updaterName";
            case "update_time": return "updateTime";
            case "del_flag": return "delFlag";
            case "ext_info1": return "extInfo1";
            case "ext_info2": return "extInfo2";
            case "ext_info3": return "extInfo3";
            case "ext_info4": return "extInfo4";
            default: return null;
        }
    }
    
    /**  **/
    public Integer getIsBigorg() {
        return this.isBigorg;
    }

    /**  **/
    public void setIsBigorg(Integer isBigorg) {
        this.isBigorg = isBigorg;
    }

    /**  **/
    public String getDepartmentName() {
        return this.departmentName;
    }

    /**  **/
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**  **/
    public String getDepartmentId() {
        return this.departmentId;
    }

    /**  **/
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    /**  **/
    public String getArea() {
        return this.area;
    }

    /**  **/
    public void setArea(String area) {
        this.area = area;
    }

    /**  **/
    public String getProvince() {
        return this.province;
    }

    /**  **/
    public void setProvince(String province) {
        this.province = province;
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
    public Double getAmount() {
        return this.amount;
    }

    /**  **/
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**  **/
    public Integer getAmountType() {
        return this.amountType;
    }

    /**  **/
    public void setAmountType(Integer amountType) {
        this.amountType = amountType;
    }

    /**  **/
    public Double getGrowthRate() {
        return this.growthRate;
    }

    /**  **/
    public void setGrowthRate(Double growthRate) {
        this.growthRate = growthRate;
    }

    /**  **/
    public String getCreateuserId() {
        return this.createuserId;
    }

    /**  **/
    public void setCreateuserId(String createuserId) {
        this.createuserId = createuserId;
    }

    /**  **/
    public String getCreateuserName() {
        return this.createuserName;
    }

    /**  **/
    public void setCreateuserName(String createuserName) {
        this.createuserName = createuserName;
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
    public String getUpdaterId() {
        return this.updaterId;
    }

    /**  **/
    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    /**  **/
    public String getUpdaterName() {
        return this.updaterName;
    }

    /**  **/
    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
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

}
