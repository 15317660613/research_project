package com.adc.da.crm.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * <b>功能：</b>MONTH_BUDGET MonthBudgetEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class MonthBudgetEO extends BaseEntity {

    private String id;
    private String proTargetBotomId;
    @MatchField("月份")
    private String month;
    @MatchField("合同目标值")
    private Long contractTarget;
    @MatchField("开票目标值")
    private Long ticketTarget;
    private String delFlag;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private String createdUser;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;
    private String modifiedUser;
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>proTargetBotomId -> pro_target_botom_id</li>
     * <li>month -> month</li>
     * <li>contractTarget -> contract_target</li>
     * <li>ticketTarget -> ticket_target</li>
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
            case "proTargetBotomId": return "pro_target_botom_id";
            case "month": return "month";
            case "contractTarget": return "contract_target";
            case "ticketTarget": return "ticket_target";
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
     * <li>pro_target_botom_id -> proTargetBotomId</li>
     * <li>month -> month</li>
     * <li>contract_target -> contractTarget</li>
     * <li>ticket_target -> ticketTarget</li>
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
            case "pro_target_botom_id": return "proTargetBotomId";
            case "month": return "month";
            case "contract_target": return "contractTarget";
            case "ticket_target": return "ticketTarget";
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
    public String getProTargetBotomId() {
        return this.proTargetBotomId;
    }

    /**  **/
    public void setProTargetBotomId(String proTargetBotomId) {
        this.proTargetBotomId = proTargetBotomId;
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
    public Long getContractTarget() {
        return this.contractTarget;
    }

    /**  **/
    public void setContractTarget(Long contractTarget) {
        this.contractTarget = contractTarget;
    }

    /**  **/
    public Long getTicketTarget() {
        return this.ticketTarget;
    }

    /**  **/
    public void setTicketTarget(Long ticketTarget) {
        this.ticketTarget = ticketTarget;
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

}
