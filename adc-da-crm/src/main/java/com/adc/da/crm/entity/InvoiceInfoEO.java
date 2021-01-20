package com.adc.da.crm.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * <b>功能：</b>INVOICE_INFO InvoiceInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class InvoiceInfoEO extends BaseEntity {

    private String type;
    private String id;
    private String contractId;
    @MatchField("项目编号")
    private String projectNo;
    @MatchField("项目名称")
    private String projectName;
    @MatchField("开票金额")
    private Long amount;
    @MatchField("开票时间")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date inDate;
    private String delFlag;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private String createdUser;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime2;
    private String modifiedUser2;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>type -> type</li>
     * <li>id -> id</li>
     * <li>contractId -> contract_id</li>
     * <li>projectNo -> project_no</li>
     * <li>projectName -> project_name</li>
     * <li>amount -> amount</li>
     * <li>inDate -> in_date</li>
     * <li>delFlag -> del_flag</li>
     * <li>createdTime -> created_time</li>
     * <li>createdUser -> created_user</li>
     * <li>modifiedTime2 -> modified_time2</li>
     * <li>modifiedUser2 -> modified_user2</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "type": return "type";
            case "id": return "id";
            case "contractId": return "contract_id";
            case "projectNo": return "project_no";
            case "projectName": return "project_name";
            case "amount": return "amount";
            case "inDate": return "in_date";
            case "delFlag": return "del_flag";
            case "createdTime": return "created_time";
            case "createdUser": return "created_user";
            case "modifiedTime2": return "modified_time2";
            case "modifiedUser2": return "modified_user2";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>type -> type</li>
     * <li>id -> id</li>
     * <li>contract_id -> contractId</li>
     * <li>project_no -> projectNo</li>
     * <li>project_name -> projectName</li>
     * <li>amount -> amount</li>
     * <li>in_date -> inDate</li>
     * <li>del_flag -> delFlag</li>
     * <li>created_time -> createdTime</li>
     * <li>created_user -> createdUser</li>
     * <li>modified_time2 -> modifiedTime2</li>
     * <li>modified_user2 -> modifiedUser2</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "type": return "type";
            case "id": return "id";
            case "contract_id": return "contractId";
            case "project_no": return "projectNo";
            case "project_name": return "projectName";
            case "amount": return "amount";
            case "in_date": return "inDate";
            case "del_flag": return "delFlag";
            case "created_time": return "createdTime";
            case "created_user": return "createdUser";
            case "modified_time2": return "modifiedTime2";
            case "modified_user2": return "modifiedUser2";
            default: return null;
        }
    }
    
    /**  **/
    public String getType() {
        return this.type;
    }

    /**  **/
    public void setType(String type) {
        this.type = type;
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
    public String getContractId() {
        return this.contractId;
    }

    /**  **/
    public void setContractId(String contractId) {
        this.contractId = contractId;
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
    public String getProjectName() {
        return this.projectName;
    }

    /**  **/
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**  **/
    public Long getAmount() {
        return this.amount;
    }

    /**  **/
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**  **/
    public Date getInDate() {
        return this.inDate;
    }

    /**  **/
    public void setInDate(Date inDate) {
        this.inDate = inDate;
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
    public Date getModifiedTime2() {
        return this.modifiedTime2;
    }

    /**  **/
    public void setModifiedTime2(Date modifiedTime2) {
        this.modifiedTime2 = modifiedTime2;
    }

    /**  **/
    public String getModifiedUser2() {
        return this.modifiedUser2;
    }

    /**  **/
    public void setModifiedUser2(String modifiedUser2) {
        this.modifiedUser2 = modifiedUser2;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
