package com.adc.da.crm.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * <b>功能：</b>B_TRAVEL_PROJECT_VISIT_RECORD BTravelProjectVisitRecordEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BTravelProjectVisitRecordEO extends BaseEntity {

    private String id;
    private String travelApprovalId;
    private String orderNumber;
    @MatchField("项目编号")
    private String proTargetBottomId;
    @MatchField("项目名称")
    private String projectName;
    @MatchField("沟通内容")
    private String communicationcontent;
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
     * <li>travelApprovalId -> travel_approval_id</li>
     * <li>orderNumber -> order_number</li>
     * <li>proTargetBottomId -> pro_target_bottom_id</li>
     * <li>projectName -> project_name</li>
     * <li>communicationcontent -> communicationcontent</li>
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
            case "travelApprovalId": return "travel_approval_id";
            case "orderNumber": return "order_number";
            case "proTargetBottomId": return "pro_target_bottom_id";
            case "projectName": return "project_name";
            case "communicationcontent": return "communicationcontent";
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
     * <li>travel_approval_id -> travelApprovalId</li>
     * <li>order_number -> orderNumber</li>
     * <li>pro_target_bottom_id -> proTargetBottomId</li>
     * <li>project_name -> projectName</li>
     * <li>communicationcontent -> communicationcontent</li>
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
            case "travel_approval_id": return "travelApprovalId";
            case "order_number": return "orderNumber";
            case "pro_target_bottom_id": return "proTargetBottomId";
            case "project_name": return "projectName";
            case "communicationcontent": return "communicationcontent";
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
    public String getTravelApprovalId() {
        return this.travelApprovalId;
    }

    /**  **/
    public void setTravelApprovalId(String travelApprovalId) {
        this.travelApprovalId = travelApprovalId;
    }

    /**  **/
    public String getOrderNumber() {
        return this.orderNumber;
    }

    /**  **/
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**  **/
    public String getProTargetBottomId() {
        return this.proTargetBottomId;
    }

    /**  **/
    public void setProTargetBottomId(String proTargetBottomId) {
        this.proTargetBottomId = proTargetBottomId;
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
