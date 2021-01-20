package com.adc.da.customerresourcemanage.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * <b>功能：</b>DB_ENTERPRISE EnterpriseEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class EnterpriseEO extends BaseEntity {

    /** ID **/
    private String id;
    /** 企业名称 **/
    private String enterpriseName;
    /** 企业类型 **/
    private String enterpriseType;
    /** 企业领域 **/
    private String enterpriseDomain;
    /**企业省份名称**/
    private String enterpriseProvince;
    /**企业省份Id 关联表 DB_PROVINCE_AREA 主键id **/
    private Integer enterpriseProvinceId;
    /** 企业地址 **/
    private String enterpriseAddress;
    /** 创建人id **/
    private String createUserId;
    /**创建人名称  **/
    private String createUserName;
    /**创建时间  **/
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /** 更新时间 **/
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**删除标志 0-正常 1-删除  **/
    private String delFlag;
    /** 其他 **/
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;



    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>enterpriseName -> enterprise_name</li>
     * <li>enterpriseType -> enterprise_type</li>
     * <li>enterpriseDomain -> enterprise_domain</li>
     * <li>enterpriseProvince -> enterprise_province</li>
     * <li>enterpriseAddress -> enterprise_address</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>createTime -> create_time</li>
     * <li>updateTime -> update_time</li>
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
            case "enterpriseName": return "enterprise_name";
            case "enterpriseType": return "enterprise_type";
            case "enterpriseDomain": return "enterprise_domain";
            case "enterpriseProvince": return "enterprise_province";
            case "enterpriseAddress": return "enterprise_address";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "createTime": return "create_time";
            case "updateTime": return "update_time";
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
     * <li>enterprise_name -> enterpriseName</li>
     * <li>enterprise_type -> enterpriseType</li>
     * <li>enterprise_domain -> enterpriseDomain</li>
     * <li>enterprise_province -> enterpriseProvince</li>
     * <li>enterprise_address -> enterpriseAddress</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>create_time -> createTime</li>
     * <li>update_time -> updateTime</li>
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
            case "enterprise_name": return "enterpriseName";
            case "enterprise_type": return "enterpriseType";
            case "enterprise_domain": return "enterpriseDomain";
            case "enterprise_province": return "enterpriseProvince";
            case "enterprise_address": return "enterpriseAddress";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "create_time": return "createTime";
            case "update_time": return "updateTime";
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
