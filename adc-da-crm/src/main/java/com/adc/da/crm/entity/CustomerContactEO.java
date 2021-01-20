package com.adc.da.crm.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import com.adc.da.excel.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * <b>功能：</b>CUSTOMER_CONTACT CustomerContactEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class CustomerContactEO extends BaseEntity {

    private String id;
    private String cusRecordId;
    private String customerNumber;
    @Excel(name = "部门", orderNum = "1")
    @MatchField("部门")
    private String dept;
    @Excel(name = "联系人", orderNum = "2")
    @MatchField("联系人")
    private String contacts;
    @Excel(name = "职务", orderNum = "3")
    @MatchField("职务")
    private String job;
    @Excel(name = "性别", orderNum = "4")
    @MatchField("性别")
    private String gender;
    @Excel(name = "手机", orderNum = "5")
    @MatchField("手机")
    private String mobile;
    @Excel(name = "座机", orderNum = "6")
    @MatchField("座机")
    private String phone;
    @Excel(name = "邮箱", orderNum = "7")
    @MatchField("邮箱")
    private String mail;
    @Excel(name = "备注", orderNum = "8")
    @MatchField("备注")
    private String remark;
    private String delFlag;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private String createdUser;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;
    private String modifiedUser;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>cusRecordId -> cus_record_id</li>
     * <li>customerNumber -> customer_number</li>
     * <li>dept -> dept</li>
     * <li>contacts -> contacts</li>
     * <li>job -> job</li>
     * <li>gender -> gender</li>
     * <li>mobile -> mobile</li>
     * <li>phone -> phone</li>
     * <li>mail -> mail</li>
     * <li>remark -> remark</li>
     * <li>delFlag -> del_flag</li>
     * <li>createdTime -> created_time</li>
     * <li>createdUser -> created_user</li>
     * <li>modifiedTime -> modified_time</li>
     * <li>modifiedUser -> modified_user</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "cusRecordId": return "cus_record_id";
            case "customerNumber": return "customer_number";
            case "dept": return "dept";
            case "contacts": return "contacts";
            case "job": return "job";
            case "gender": return "gender";
            case "mobile": return "mobile";
            case "phone": return "phone";
            case "mail": return "mail";
            case "remark": return "remark";
            case "delFlag": return "del_flag";
            case "createdTime": return "created_time";
            case "createdUser": return "created_user";
            case "modifiedTime": return "modified_time";
            case "modifiedUser": return "modified_user";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>cus_record_id -> cusRecordId</li>
     * <li>customer_number -> customerNumber</li>
     * <li>dept -> dept</li>
     * <li>contacts -> contacts</li>
     * <li>job -> job</li>
     * <li>gender -> gender</li>
     * <li>mobile -> mobile</li>
     * <li>phone -> phone</li>
     * <li>mail -> mail</li>
     * <li>remark -> remark</li>
     * <li>del_flag -> delFlag</li>
     * <li>created_time -> createdTime</li>
     * <li>created_user -> createdUser</li>
     * <li>modified_time -> modifiedTime</li>
     * <li>modified_user -> modifiedUser</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "cus_record_id": return "cusRecordId";
            case "customer_number": return "customerNumber";
            case "dept": return "dept";
            case "contacts": return "contacts";
            case "job": return "job";
            case "gender": return "gender";
            case "mobile": return "mobile";
            case "phone": return "phone";
            case "mail": return "mail";
            case "remark": return "remark";
            case "del_flag": return "delFlag";
            case "created_time": return "createdTime";
            case "created_user": return "createdUser";
            case "modified_time": return "modifiedTime";
            case "modified_user": return "modifiedUser";
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
    public String getCusRecordId() {
        return this.cusRecordId;
    }

    /**  **/
    public void setCusRecordId(String cusRecordId) {
        this.cusRecordId = cusRecordId;
    }

    /**  **/
    public String getCustomerNumber() {
        return this.customerNumber;
    }

    /**  **/
    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    /**  **/
    public String getDept() {
        return this.dept;
    }

    /**  **/
    public void setDept(String dept) {
        this.dept = dept;
    }

    /**  **/
    public String getContacts() {
        return this.contacts;
    }

    /**  **/
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    /**  **/
    public String getJob() {
        return this.job;
    }

    /**  **/
    public void setJob(String job) {
        this.job = job;
    }

    /**  **/
    public String getGender() {
        return this.gender;
    }

    /**  **/
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**  **/
    public String getMobile() {
        return this.mobile;
    }

    /**  **/
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**  **/
    public String getPhone() {
        return this.phone;
    }

    /**  **/
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**  **/
    public String getMail() {
        return this.mail;
    }

    /**  **/
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**  **/
    public String getRemark() {
        return this.remark;
    }

    /**  **/
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**  **/
    public String getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    /**  **/
    public Date getCreatedTime() {
        return this.createdTime;
    }

    /**  **/
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**  **/
    public String getCreatedUser() {
        return this.createdUser;
    }

    /**  **/
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    /**  **/
    public Date getModifiedTime() {
        return this.modifiedTime;
    }

    /**  **/
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    /**  **/
    public String getModifiedUser() {
        return this.modifiedUser;
    }

    /**  **/
    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
