package com.adc.da.finance.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>F__RECEIVABLES_MANAGEMENT ReceivablesManagementEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ReceivablesManagementEO extends BaseEntity {

    private String id;
    private String departId;
    private String departName;
    private String remYear;
    private String remMonth;
    private String businessGonfigId;
    private BigDecimal remMoney;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String updateUserId;
    private String updateUserName;
    private String delFlag;

    @Transient
    private String businessGonfigName;

    @Transient
    private String remYearOld;
    @Transient
    private String remMonthOld;
    @Transient
    private String businessGonfigIdOld;


    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>departId -> depart_id</li>
     * <li>departName -> depart_name</li>
     * <li>remYear -> rem_year</li>
     * <li>remMonth -> rem_month</li>
     * <li>businessGonfigId -> business_gonfig_id</li>
     * <li>remMoney -> rem_money</li>
     * <li>createTime -> create_time</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>updateTime -> update_time</li>
     * <li>updateUserId -> update_user_id</li>
     * <li>updateUserName -> update_user_name</li>
     * <li>delFlag -> del_flag</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "departId": return "depart_id";
            case "departName": return "depart_name";
            case "remYear": return "rem_year";
            case "remMonth": return "rem_month";
            case "businessGonfigId": return "business_gonfig_id";
            case "remMoney": return "rem_money";
            case "createTime": return "create_time";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "updateTime": return "update_time";
            case "updateUserId": return "update_user_id";
            case "updateUserName": return "update_user_name";
            case "delFlag": return "del_flag";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>depart_id -> departId</li>
     * <li>depart_name -> departName</li>
     * <li>rem_year -> remYear</li>
     * <li>rem_month -> remMonth</li>
     * <li>business_gonfig_id -> businessGonfigId</li>
     * <li>rem_money -> remMoney</li>
     * <li>create_time -> createTime</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>update_time -> updateTime</li>
     * <li>update_user_id -> updateUserId</li>
     * <li>update_user_name -> updateUserName</li>
     * <li>del_flag -> delFlag</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "depart_id": return "departId";
            case "depart_name": return "departName";
            case "rem_year": return "remYear";
            case "rem_month": return "remMonth";
            case "business_gonfig_id": return "businessGonfigId";
            case "rem_money": return "remMoney";
            case "create_time": return "createTime";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "update_time": return "updateTime";
            case "update_user_id": return "updateUserId";
            case "update_user_name": return "updateUserName";
            case "del_flag": return "delFlag";
            default: return null;
        }
    }
}
