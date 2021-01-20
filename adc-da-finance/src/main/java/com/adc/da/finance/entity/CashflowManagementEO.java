package com.adc.da.finance.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>F_CASHFLOW_MANAGEMENT CashflowManagementEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class CashflowManagementEO extends BaseEntity {

    /** 业务名称**/
    private String id;
    /** 业务id**/
    private String businessGonfigId;
    /** 年**/
    private String cmYear;
    /** 月**/
    private String cmMonth;
    /** 现金流入（元）**/
    private BigDecimal flowInMoney;
    /** 现金流出（元）**/
    private BigDecimal flowOutMoney;
    /** 现金余额（元）**/
    private BigDecimal surplusMoney;
    /** 创建时间**/
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 更新时间**/
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /** 删除标志 0-正常 1-删除**/
    private String delFlag;
    @Transient
    /** 业务名称**/
    private String businessGonfigName;


    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>businessGonfigId -> business_gonfig_id</li>
     * <li>cmYear -> cm_year</li>
     * <li>cmMonth -> cm_month</li>
     * <li>flowInMoney -> flow_in_money</li>
     * <li>flowOutMoney -> flow_out_money</li>
     * <li>surplusMoney -> surplus_money</li>
     * <li>createTime -> create_time</li>
     * <li>updateTime -> update_time</li>
     * <li>delFlag -> del_flag</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "businessGonfigId": return "business_gonfig_id";
            case "cmYear": return "cm_year";
            case "cmMonth": return "cm_month";
            case "flowInMoney": return "flow_in_money";
            case "flowOutMoney": return "flow_out_money";
            case "surplusMoney": return "surplus_money";
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
     * <li>cm_year -> cmYear</li>
     * <li>cm_month -> cmMonth</li>
     * <li>flow_in_money -> flowInMoney</li>
     * <li>flow_out_money -> flowOutMoney</li>
     * <li>surplus_money -> surplusMoney</li>
     * <li>create_time -> createTime</li>
     * <li>update_time -> updateTime</li>
     * <li>del_flag -> delFlag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "business_gonfig_id": return "businessGonfigId";
            case "cm_year": return "cmYear";
            case "cm_month": return "cmMonth";
            case "flow_in_money": return "flowInMoney";
            case "flow_out_money": return "flowOutMoney";
            case "surplus_money": return "surplusMoney";
            case "create_time": return "createTime";
            case "update_time": return "updateTime";
            case "del_flag": return "delFlag";
            default: return null;
        }
    }
}
