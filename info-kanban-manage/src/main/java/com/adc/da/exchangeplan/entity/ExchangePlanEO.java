package com.adc.da.exchangeplan.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>DB_EXCHANGE_PLAN ExchangePlanEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ExchangePlanEO extends BaseEntity {

    /**
     * 主键
     */
    private String id;
    /**
     * 日期
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date epDate;
    /**
     * 形式
     */
    private String epForm;
    /**
     * 企业
     */
    private String epEnterprise;
    /**
     * 交流领域
     */
    private String epExchangeDomain;
    /**
     * 建议领导
     */
    private String epLeaderName;
    /**
     * 创建人id
     */
    private String createUserId;
    /**
     * 创建人姓名
     */
    private String createUserName;
    /**
     * 创建日期
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 删除标志 0-正常 1-删除
     */
    private String delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>epDate -> ep_date</li>
     * <li>epForm -> ep_form</li>
     * <li>epEnterprise -> ep_enterprise</li>
     * <li>epExchangeDomain -> ep_exchange_domain</li>
     * <li>epLeaderName -> ep_leader_name</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>createTime -> create_time</li>
     * <li>delFlag -> del_flag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "epDate": return "ep_date";
            case "epForm": return "ep_form";
            case "epEnterprise": return "ep_enterprise";
            case "epExchangeDomain": return "ep_exchange_domain";
            case "epLeaderName": return "ep_leader_name";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "createTime": return "create_time";
            case "delFlag": return "del_flag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>ep_date -> epDate</li>
     * <li>ep_form -> epForm</li>
     * <li>ep_enterprise -> epEnterprise</li>
     * <li>ep_exchange_domain -> epExchangeDomain</li>
     * <li>ep_leader_name -> epLeaderName</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>create_time -> createTime</li>
     * <li>del_flag -> delFlag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "ep_date": return "epDate";
            case "ep_form": return "epForm";
            case "ep_enterprise": return "epEnterprise";
            case "ep_exchange_domain": return "epExchangeDomain";
            case "ep_leader_name": return "epLeaderName";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "create_time": return "createTime";
            case "del_flag": return "delFlag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            default: return null;
        }
    }

}
