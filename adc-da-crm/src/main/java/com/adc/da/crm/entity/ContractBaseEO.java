package com.adc.da.crm.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * <b>功能：</b>CONTRACT_BASE ContractBaseEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ContractBaseEO extends BaseEntity {

    private String id;
    @MatchField("区域经理")
    private String areaManagerId;
    @MatchField("所属部门")
    private String deptId;
    @MatchField("建档日期")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date recordDate;
    @MatchField("合同编号")
    private String contractNo;
    @MatchField("客户编号")
    private String customerNo;
    @MatchField("客户名称")
    private String customerName;
    @MatchField("合同期限起")
    private String begin;
    @MatchField("合同期限止")
    private String end;
    @MatchField("合同状态")
    private String status;
    @MatchField("合同金额")
    private String contractAmount;
    @MatchField("开票金额")
    private String invoiceAmount;
    @MatchField("进账金额")
    private String incomeAmount;
    @MatchField("成本金额")
    private String costAmount;
    @MatchField("合同附件")
    private String attachment;
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
     * <li>id -> id</li>
     * <li>areaManagerId -> area_manager_id</li>
     * <li>deptId -> dept_id</li>
     * <li>recordDate -> record_date</li>
     * <li>contractNo -> contract_no</li>
     * <li>customerNo -> customer_no</li>
     * <li>customerName -> customer_name</li>
     * <li>begin -> begin</li>
     * <li>end -> end</li>
     * <li>status -> status</li>
     * <li>contractAmount -> contract_amount</li>
     * <li>invoiceAmount -> invoice_amount</li>
     * <li>incomeAmount -> income_amount</li>
     * <li>costAmount -> cost_amount</li>
     * <li>attachment -> attachment</li>
     * <li>delFlag -> del_flag</li>
     * <li>createdTime -> created_time</li>
     * <li>createdUser -> created_user</li>
     * <li>modifiedTime2 -> modified_time2</li>
     * <li>modifiedUser2 -> modified_user2</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "areaManagerId": return "area_manager_id";
            case "deptId": return "dept_id";
            case "recordDate": return "record_date";
            case "contractNo": return "contract_no";
            case "customerNo": return "customer_no";
            case "customerName": return "customer_name";
            case "begin": return "begin";
            case "end": return "end";
            case "status": return "status";
            case "contractAmount": return "contract_amount";
            case "invoiceAmount": return "invoice_amount";
            case "incomeAmount": return "income_amount";
            case "costAmount": return "cost_amount";
            case "attachment": return "attachment";
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
     * <li>id -> id</li>
     * <li>area_manager_id -> areaManagerId</li>
     * <li>dept_id -> deptId</li>
     * <li>record_date -> recordDate</li>
     * <li>contract_no -> contractNo</li>
     * <li>customer_no -> customerNo</li>
     * <li>customer_name -> customerName</li>
     * <li>begin -> begin</li>
     * <li>end -> end</li>
     * <li>status -> status</li>
     * <li>contract_amount -> contractAmount</li>
     * <li>invoice_amount -> invoiceAmount</li>
     * <li>income_amount -> incomeAmount</li>
     * <li>cost_amount -> costAmount</li>
     * <li>attachment -> attachment</li>
     * <li>del_flag -> delFlag</li>
     * <li>created_time -> createdTime</li>
     * <li>created_user -> createdUser</li>
     * <li>modified_time2 -> modifiedTime2</li>
     * <li>modified_user2 -> modifiedUser2</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "area_manager_id": return "areaManagerId";
            case "dept_id": return "deptId";
            case "record_date": return "recordDate";
            case "contract_no": return "contractNo";
            case "customer_no": return "customerNo";
            case "customer_name": return "customerName";
            case "begin": return "begin";
            case "end": return "end";
            case "status": return "status";
            case "contract_amount": return "contractAmount";
            case "invoice_amount": return "invoiceAmount";
            case "income_amount": return "incomeAmount";
            case "cost_amount": return "costAmount";
            case "attachment": return "attachment";
            case "del_flag": return "delFlag";
            case "created_time": return "createdTime";
            case "created_user": return "createdUser";
            case "modified_time2": return "modifiedTime2";
            case "modified_user2": return "modifiedUser2";
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
    public String getAreaManagerId() {
        return this.areaManagerId;
    }

    /**  **/
    public void setAreaManagerId(String areaManagerId) {
        this.areaManagerId = areaManagerId;
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
    public Date getRecordDate() {
        return this.recordDate;
    }

    /**  **/
    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    /**  **/
    public String getContractNo() {
        return this.contractNo;
    }

    /**  **/
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    /**  **/
    public String getCustomerNo() {
        return this.customerNo;
    }

    /**  **/
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**  **/
    public String getCustomerName() {
        return this.customerName;
    }

    /**  **/
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**  **/
    public String getBegin() {
        return this.begin;
    }

    /**  **/
    public void setBegin(String begin) {
        this.begin = begin;
    }

    /**  **/
    public String getEnd() {
        return this.end;
    }

    /**  **/
    public void setEnd(String end) {
        this.end = end;
    }

    /**  **/
    public String getStatus() {
        return this.status;
    }

    /**  **/
    public void setStatus(String status) {
        this.status = status;
    }

    /**  **/
    public String getContractAmount() {
        return this.contractAmount;
    }

    /**  **/
    public void setContractAmount(String contractAmount) {
        this.contractAmount = contractAmount;
    }

    /**  **/
    public String getInvoiceAmount() {
        return this.invoiceAmount;
    }

    /**  **/
    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    /**  **/
    public String getIncomeAmount() {
        return this.incomeAmount;
    }

    /**  **/
    public void setIncomeAmount(String incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    /**  **/
    public String getCostAmount() {
        return this.costAmount;
    }

    /**  **/
    public void setCostAmount(String costAmount) {
        this.costAmount = costAmount;
    }

    /**  **/
    public String getAttachment() {
        return this.attachment;
    }

    /**  **/
    public void setAttachment(String attachment) {
        this.attachment = attachment;
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
