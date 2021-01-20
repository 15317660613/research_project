package com.adc.da.industymeeting.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>RECEIVABLE_INCOME_FILED ReceivableIncomeFiledEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-03 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ReceivableIncomeFiledEO extends BaseEntity {

    private String id;

    @Excel(name = "年",orderNum = "0")
    private String year;
    @Excel(name = "月",orderNum = "1")
    private String month;

    private String company;
    @Excel(name = "当月应收余额（元）",orderNum = "4")
    private BigDecimal receivableBalance;
    @Excel(name = "当月进账额（元）",orderNum = "5")
    private BigDecimal income;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String createUserId;
    @Excel(name = "更新时间",orderNum = "6",exportFormat = "yyyy/MM/dd")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String updateUserId;
    private Integer delFlag;

    private String area;

    private String createUser;
    @Excel(name = "更新人",orderNum = "7")
    private String updateUser;
    /**
     * 区域名称
     */
    @Excel(name = "区域",orderNum = "3")
    private String areaName;
    /**
     * 公司名称
     */
    @Excel(name = "公司",orderNum = "2")
    private String companyName;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     * <li>company -> company</li>
     * <li>receivableBalance -> receivable_balance</li>
     * <li>income -> income</li>
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
            case "company": return "company";
            case "receivableBalance": return "receivable_balance";
            case "income": return "income";
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
     * <li>company -> company</li>
     * <li>receivable_balance -> receivableBalance</li>
     * <li>income -> income</li>
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
            case "company": return "company";
            case "receivable_balance": return "receivableBalance";
            case "income": return "income";
            case "create_time": return "createTime";
            case "create_user_id": return "createUserId";
            case "update_time": return "updateTime";
            case "update_user_id": return "updateUserId";
            case "del_flag": return "delFlag";
            default: return null;
        }
    }
}
