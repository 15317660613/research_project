package com.adc.da.crm.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * <b>功能：</b>PROJECT_TARGET_BOTTOM ProjectTargetBottomEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectTargetBottomEO extends BaseEntity {
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    private String id;
    @MatchField("项目经理")
    private String projecrManagerId;
    @MatchField("所属部门")
    private String deptId;
    @MatchField("年份")
    private String year;
    private String projectId;
    @MatchField("第一季度合同目标值")
    private Long contractTarget1;
    @MatchField("第一季度开票目标值")
    private Long ticketTarget1;
    @MatchField("第二季度合同目标值")
    private Long contractTarget2;
    @MatchField("第二季度开票目标值")
    private Long ticketTarget2;
    @MatchField("第三季度合同目标值")
    private Long contractTarget3;
    @MatchField("第三季度合同目标值")
    private Long ticketTarget3;
    @MatchField("第四季度合同目标值")
    private Long contractTarget4;
    @MatchField("第四季度开票目标值")
    private Long ticketTarget4;
    @MatchField("年度合同目标值")
    private String contractTargetYear;
    @MatchField("年度开票目标值")
    private String ticketTargetYear;
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
     * <li>projecrManagerId -> projecr_manager_id</li>
     * <li>deptId -> dept_id</li>
     * <li>year -> year</li>
     * <li>projectId -> project_id</li>
     * <li>contractTarget1 -> contract_target_1</li>
     * <li>ticketTarget1 -> ticket_target_1</li>
     * <li>contractTarget2 -> contract_target_2</li>
     * <li>ticketTarget2 -> ticket_target_2</li>
     * <li>contractTarget3 -> contract_target_3</li>
     * <li>ticketTarget3 -> ticket_target_3</li>
     * <li>contractTarget4 -> contract_target_4</li>
     * <li>ticketTarget4 -> ticket_target_4</li>
     * <li>contractTargetYear -> contract_target_year</li>
     * <li>ticketTargetYear -> ticket_target_year</li>
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
            case "projecrManagerId": return "projecr_manager_id";
            case "deptId": return "dept_id";
            case "year": return "year";
            case "projectId": return "project_id";
            case "contractTarget1": return "contract_target_1";
            case "ticketTarget1": return "ticket_target_1";
            case "contractTarget2": return "contract_target_2";
            case "ticketTarget2": return "ticket_target_2";
            case "contractTarget3": return "contract_target_3";
            case "ticketTarget3": return "ticket_target_3";
            case "contractTarget4": return "contract_target_4";
            case "ticketTarget4": return "ticket_target_4";
            case "contractTargetYear": return "contract_target_year";
            case "ticketTargetYear": return "ticket_target_year";
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
     * <li>projecr_manager_id -> projecrManagerId</li>
     * <li>dept_id -> deptId</li>
     * <li>year -> year</li>
     * <li>project_id -> projectId</li>
     * <li>contract_target_1 -> contractTarget1</li>
     * <li>ticket_target_1 -> ticketTarget1</li>
     * <li>contract_target_2 -> contractTarget2</li>
     * <li>ticket_target_2 -> ticketTarget2</li>
     * <li>contract_target_3 -> contractTarget3</li>
     * <li>ticket_target_3 -> ticketTarget3</li>
     * <li>contract_target_4 -> contractTarget4</li>
     * <li>ticket_target_4 -> ticketTarget4</li>
     * <li>contract_target_year -> contractTargetYear</li>
     * <li>ticket_target_year -> ticketTargetYear</li>
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
            case "projecr_manager_id": return "projecrManagerId";
            case "dept_id": return "deptId";
            case "year": return "year";
            case "project_id": return "projectId";
            case "contract_target_1": return "contractTarget1";
            case "ticket_target_1": return "ticketTarget1";
            case "contract_target_2": return "contractTarget2";
            case "ticket_target_2": return "ticketTarget2";
            case "contract_target_3": return "contractTarget3";
            case "ticket_target_3": return "ticketTarget3";
            case "contract_target_4": return "contractTarget4";
            case "ticket_target_4": return "ticketTarget4";
            case "contract_target_year": return "contractTargetYear";
            case "ticket_target_year": return "ticketTargetYear";
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
    public String getProjecrManagerId() {
        return this.projecrManagerId;
    }

    /**  **/
    public void setProjecrManagerId(String projecrManagerId) {
        this.projecrManagerId = projecrManagerId;
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
    public String getProjectId() {
        return this.projectId;
    }

    /**  **/
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**  **/
    public Long getContractTarget1() {
        return this.contractTarget1;
    }

    /**  **/
    public void setContractTarget1(Long contractTarget1) {
        this.contractTarget1 = contractTarget1;
    }

    /**  **/
    public Long getTicketTarget1() {
        return this.ticketTarget1;
    }

    /**  **/
    public void setTicketTarget1(Long ticketTarget1) {
        this.ticketTarget1 = ticketTarget1;
    }

    /**  **/
    public Long getContractTarget2() {
        return this.contractTarget2;
    }

    /**  **/
    public void setContractTarget2(Long contractTarget2) {
        this.contractTarget2 = contractTarget2;
    }

    /**  **/
    public Long getTicketTarget2() {
        return this.ticketTarget2;
    }

    /**  **/
    public void setTicketTarget2(Long ticketTarget2) {
        this.ticketTarget2 = ticketTarget2;
    }

    /**  **/
    public Long getContractTarget3() {
        return this.contractTarget3;
    }

    /**  **/
    public void setContractTarget3(Long contractTarget3) {
        this.contractTarget3 = contractTarget3;
    }

    /**  **/
    public Long getTicketTarget3() {
        return this.ticketTarget3;
    }

    /**  **/
    public void setTicketTarget3(Long ticketTarget3) {
        this.ticketTarget3 = ticketTarget3;
    }

    /**  **/
    public Long getContractTarget4() {
        return this.contractTarget4;
    }

    /**  **/
    public void setContractTarget4(Long contractTarget4) {
        this.contractTarget4 = contractTarget4;
    }

    /**  **/
    public Long getTicketTarget4() {
        return this.ticketTarget4;
    }

    /**  **/
    public void setTicketTarget4(Long ticketTarget4) {
        this.ticketTarget4 = ticketTarget4;
    }

    /**  **/
    public String getContractTargetYear() {
        return this.contractTargetYear;
    }

    /**  **/
    public void setContractTargetYear(String contractTargetYear) {
        this.contractTargetYear = contractTargetYear;
    }

    /**  **/
    public String getTicketTargetYear() {
        return this.ticketTargetYear;
    }

    /**  **/
    public void setTicketTargetYear(String ticketTargetYear) {
        this.ticketTargetYear = ticketTargetYear;
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
