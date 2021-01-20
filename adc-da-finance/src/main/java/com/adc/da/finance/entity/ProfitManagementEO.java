package com.adc.da.finance.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>F_PROFIT_MANAGEMENT ProfitManagementEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfitManagementEO extends BaseEntity {

    /**主键id  **/
    private String id;
    /** 业务id 外键 **/
    private String businessGonfigId;
    /** 年 **/
    private String pmYear;
    /** 月 **/
    private String pmMonth;
    /** 成本金额 **/
    private BigDecimal costMoney;
    /** 收入金额 =来源于3-收入数据管理中的金额**/
    private BigDecimal incomeMoney;
    /** 利润金额 **/
    private BigDecimal profitMoney;
    /** 利润率 **/
    private String profitMargin;
    /** 创建时间 **/
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 更新时间 **/
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /** 删除标志 0正常  1删除 **/
    private String delFlag;

    @Transient
    /** 业务名称**/
    private String businessGonfigName;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>businessGonfigId -> business_gonfig_id</li>
     * <li>pmYear -> pm_year</li>
     * <li>pmMonth -> pm_month</li>
     * <li>costMoney -> cost_money</li>
     * <li>incomeMoney -> income_money</li>
     * <li>profitMoney -> profit_money</li>
     * <li>profitMargin -> profit_margin</li>
     * <li>createTime -> create_time</li>
     * <li>updateTime -> update_time</li>
     * <li>delFlag -> del_flag</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "businessGonfigId": return "business_gonfig_id";
            case "pmYear": return "pm_year";
            case "pmMonth": return "pm_month";
            case "costMoney": return "cost_money";
            case "incomeMoney": return "income_money";
            case "profitMoney": return "profit_money";
            case "profitMargin": return "profit_margin";
            case "createTime": return "create_time";
            case "updateTime": return "update_time";
            case "delFlag": return "del_flag";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>business_gonfig_id -> businessGonfigId</li>
     * <li>pm_year -> pmYear</li>
     * <li>pm_month -> pmMonth</li>
     * <li>cost_money -> costMoney</li>
     * <li>income_money -> incomeMoney</li>
     * <li>profit_money -> profitMoney</li>
     * <li>profit_margin -> profitMargin</li>
     * <li>create_time -> createTime</li>
     * <li>update_time -> updateTime</li>
     * <li>del_flag -> delFlag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "business_gonfig_id": return "businessGonfigId";
            case "pm_year": return "pmYear";
            case "pm_month": return "pmMonth";
            case "cost_money": return "costMoney";
            case "income_money": return "incomeMoney";
            case "profit_money": return "profitMoney";
            case "profit_margin": return "profitMargin";
            case "create_time": return "createTime";
            case "update_time": return "updateTime";
            case "del_flag": return "delFlag";
            default: return null;
        }
    }
}
