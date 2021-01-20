package com.adc.da.industymeeting.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>RECEIVABLE_INCOME ReceivableIncomeEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ReceivableIncomeEO extends BaseEntity {

    private String id;
    private String year;
    private String month;
    private String day;
    private String headquartersId;
    private String departmentId;
    private String project;
    private String corpname;

    private BigDecimal weeklyArrival;

    private BigDecimal amountReceivable;
    private String accountTime;
    private String company;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String createUserId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String updateUserId;
    private Integer delFlag;

    /**
     * 本部
     */
    private String headquarters;
    /**
     * 部门
     */
    private String department;
    private String createUser;
    private String updateUser;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 企业名称
     */
    private String companyName;

    @Transient
    private String departmentName;//部门名称

    @Transient
    private Double totalAmountReceivable;
    @Transient
    private String amountReceivableStr;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>day -> day</li>
     * <li>headquartersId -> headquarters_id</li>
     * <li>departmentId -> department_id</li>
     * <li>project -> project</li>
     * <li>corpname -> corpname</li>
     * <li>weeklyArrival -> weekly_arrival</li>
     * <li>amountReceivable -> amount_receivable</li>
     * <li>accountTime -> account_time</li>
     * <li>company -> company</li>
     * <li>createTime -> create_time</li>
     * <li>createUserId -> create_user_id</li>
     * <li>updateTime -> update_time</li>
     * <li>updateUserId -> update_user_id</li>
     * <li>delFlag -> del_flag</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "year": return "year";
            case "month": return "month";
            case "day": return "day";
            case "headquartersId": return "headquarters_id";
            case "departmentId": return "department_id";
            case "project": return "project";
            case "corpname": return "corpname";
            case "weeklyArrival": return "weekly_arrival";
            case "amountReceivable": return "amount_receivable";
            case "accountTime": return "account_time";
            case "company": return "company";
            case "createTime": return "create_time";
            case "createUserId": return "create_user_id";
            case "updateTime": return "update_time";
            case "updateUserId": return "update_user_id";
            case "delFlag": return "del_flag";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>day -> day</li>
     * <li>headquarters_id -> headquartersId</li>
     * <li>department_id -> departmentId</li>
     * <li>project -> project</li>
     * <li>corpname -> corpname</li>
     * <li>weekly_arrival -> weeklyArrival</li>
     * <li>amount_receivable -> amountReceivable</li>
     * <li>account_time -> accountTime</li>
     * <li>company -> company</li>
     * <li>create_time -> createTime</li>
     * <li>create_user_id -> createUserId</li>
     * <li>update_time -> updateTime</li>
     * <li>update_user_id -> updateUserId</li>
     * <li>del_flag -> delFlag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "year": return "year";
            case "month": return "month";
            case "day": return "day";
            case "headquarters_id": return "headquartersId";
            case "department_id": return "departmentId";
            case "project": return "project";
            case "corpname": return "corpname";
            case "weekly_arrival": return "weeklyArrival";
            case "amount_receivable": return "amountReceivable";
            case "account_time": return "accountTime";
            case "company": return "company";
            case "create_time": return "createTime";
            case "create_user_id": return "createUserId";
            case "update_time": return "updateTime";
            case "update_user_id": return "updateUserId";
            case "del_flag": return "delFlag";
            default: return null;
        }
    }

}
