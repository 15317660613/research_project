package com.adc.da.industymeeting.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>BUDGET_MANAGEMENT_INFO BudgetManagementInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class BudgetManagementInfoEO extends BaseEntity {

    private String id;
    private String headquartersId;
    private String departmentId;
    private String year;

    private BigDecimal ouputTarget;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String updateUserId;
    private Integer delFlag;
    private String createUserId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String headquarters;
    private String department;
    private String createUser;
    private String updateUser;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>headquartersId -> headquarters_id</li>
     * <li>departmentId -> department_id</li>
     * <li>year -> year</li>
     * <li>ouputTarget -> ouput_target</li>
     * <li>updateTime -> update_time</li>
     * <li>updateUserId -> update_user_id</li>
     * <li>delFlag -> del_flag</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "headquartersId": return "headquarters_id";
            case "departmentId": return "department_id";
            case "year": return "year";
            case "ouputTarget": return "ouput_target";
            case "updateTime": return "update_time";
            case "updateUserId": return "update_user_id";
            case "delFlag": return "del_flag";
            case "createUserId": return "create_user_id";
            case "createTime": return "create_time";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>headquarters_id -> headquartersId</li>
     * <li>department_id -> departmentId</li>
     * <li>year -> year</li>
     * <li>ouput_target -> ouputTarget</li>
     * <li>update_time -> updateTime</li>
     * <li>update_user_id -> updateUserId</li>
     * <li>del_flag -> delFlag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "headquarters_id": return "headquartersId";
            case "department_id": return "departmentId";
            case "year": return "year";
            case "ouput_target": return "ouputTarget";
            case "update_time": return "updateTime";
            case "update_user_id": return "updateUserId";
            case "del_flag": return "delFlag";
            case "create_user_id": return "createUserId";
            case "create_time": return "createTime";
            default: return null;
        }
    }

}
