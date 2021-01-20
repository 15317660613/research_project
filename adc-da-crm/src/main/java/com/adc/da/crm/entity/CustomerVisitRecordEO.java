package com.adc.da.crm.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import com.adc.da.excel.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * <b>功能：</b>CUSTOMER_VISIT_RECORD CustomerVisitRecordEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class CustomerVisitRecordEO extends BaseEntity {

    private String id;
    private String cusRecordId;
    private String customerNumber;
    @Excel(name = "拜访时间", orderNum = "1", exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy/MM/dd HH:mm:ss")
    @MatchField("拜访时间")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date visitedTime;
    @Excel(name = "拜访部门", orderNum = "2")
    @MatchField("拜访部门")
    private String visitedDept;
    @Excel(name = "拜访人员", orderNum = "3")
    @MatchField("拜访人员")
    private String visitor;
    @Excel(name = "沟通内容", orderNum = "4")
    @MatchField("沟通内容")
    private String communicationcontent;
    @Excel(name = "拜访备注", orderNum = "5")
    @MatchField("拜访备注")
    private String remark;
    private String travelApprovalId;
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
     * <li>visitedTime -> visited_time</li>
     * <li>visitedDept -> visited_dept</li>
     * <li>visitor -> visitor</li>
     * <li>communicationcontent -> communicationcontent</li>
     * <li>remark -> remark</li>
     * <li>travelApprovalId -> travel_approval_id</li>
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
            case "visitedTime": return "visited_time";
            case "visitedDept": return "visited_dept";
            case "visitor": return "visitor";
            case "communicationcontent": return "communicationcontent";
            case "remark": return "remark";
            case "travelApprovalId": return "travel_approval_id";
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
     * <li>visited_time -> visitedTime</li>
     * <li>visited_dept -> visitedDept</li>
     * <li>visitor -> visitor</li>
     * <li>communicationcontent -> communicationcontent</li>
     * <li>remark -> remark</li>
     * <li>travel_approval_id -> travelApprovalId</li>
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
            case "visited_time": return "visitedTime";
            case "visited_dept": return "visitedDept";
            case "visitor": return "visitor";
            case "communicationcontent": return "communicationcontent";
            case "remark": return "remark";
            case "travel_approval_id": return "travelApprovalId";
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
    public Date getVisitedTime() {
        return this.visitedTime;
    }

    /**  **/
    public void setVisitedTime(Date visitedTime) {
        this.visitedTime = visitedTime;
    }

    /**  **/
    public String getVisitedDept() {
        return this.visitedDept;
    }

    /**  **/
    public void setVisitedDept(String visitedDept) {
        this.visitedDept = visitedDept;
    }

    /**  **/
    public String getVisitor() {
        return this.visitor;
    }

    /**  **/
    public void setVisitor(String visitor) {
        this.visitor = visitor;
    }

    /**  **/
    public String getCommunicationcontent() {
        return this.communicationcontent;
    }

    /**  **/
    public void setCommunicationcontent(String communicationcontent) {
        this.communicationcontent = communicationcontent;
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
    public String getTravelApprovalId() {
        return this.travelApprovalId;
    }

    /**  **/
    public void setTravelApprovalId(String travelApprovalId) {
        this.travelApprovalId = travelApprovalId;
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
