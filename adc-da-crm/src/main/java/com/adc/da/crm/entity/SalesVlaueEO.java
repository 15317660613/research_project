package com.adc.da.crm.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * <b>功能：</b>SALES_VLAUE SalesVlaueEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class SalesVlaueEO extends BaseEntity {
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    private String id;
    @MatchField("区域经理")
    private String areaManagerId;
    @MatchField("所属部门")
    private String deptId;
    @MatchField("年份")
    private String year;
    @MatchField("年销售任务")
    private String task;
    @MatchField("合同金额完成")
    private String contractComplete;
    @MatchField("合同完成率")
    private String contractRate;
    @MatchField("开票金额完成")
    private String invoiceComplete;
    @MatchField("开票完成率")
    private String invoiceRate;
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
     * <li>year -> year</li>
     * <li>task -> task</li>
     * <li>contractComplete -> contract_complete</li>
     * <li>contractRate -> contract_rate</li>
     * <li>invoiceComplete -> invoice_complete</li>
     * <li>invoiceRate -> invoice_rate</li>
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
            case "year": return "year";
            case "task": return "task";
            case "contractComplete": return "contract_complete";
            case "contractRate": return "contract_rate";
            case "invoiceComplete": return "invoice_complete";
            case "invoiceRate": return "invoice_rate";
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
     * <li>year -> year</li>
     * <li>task -> task</li>
     * <li>contract_complete -> contractComplete</li>
     * <li>contract_rate -> contractRate</li>
     * <li>invoice_complete -> invoiceComplete</li>
     * <li>invoice_rate -> invoiceRate</li>
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
            case "year": return "year";
            case "task": return "task";
            case "contract_complete": return "contractComplete";
            case "contract_rate": return "contractRate";
            case "invoice_complete": return "invoiceComplete";
            case "invoice_rate": return "invoiceRate";
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
    public String getYear() {
        return this.year;
    }

    /**  **/
    public void setYear(String year) {
        this.year = year;
    }

    /**  **/
    public String getTask() {
        return this.task;
    }

    /**  **/
    public void setTask(String task) {
        this.task = task;
    }

    /**  **/
    public String getContractComplete() {
        return this.contractComplete;
    }

    /**  **/
    public void setContractComplete(String contractComplete) {
        this.contractComplete = contractComplete;
    }

    /**  **/
    public String getContractRate() {
        return this.contractRate;
    }

    /**  **/
    public void setContractRate(String contractRate) {
        this.contractRate = contractRate;
    }

    /**  **/
    public String getInvoiceComplete() {
        return this.invoiceComplete;
    }

    /**  **/
    public void setInvoiceComplete(String invoiceComplete) {
        this.invoiceComplete = invoiceComplete;
    }

    /**  **/
    public String getInvoiceRate() {
        return this.invoiceRate;
    }

    /**  **/
    public void setInvoiceRate(String invoiceRate) {
        this.invoiceRate = invoiceRate;
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

}
