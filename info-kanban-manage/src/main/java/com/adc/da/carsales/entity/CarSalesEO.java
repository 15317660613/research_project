package com.adc.da.carsales.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>DB_CAR_SALES CarSalesEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-03 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class CarSalesEO extends BaseEntity {

    /** 主键 **/
    private String id;
    /** 企业id **/
    private String enterpriseId;
    /** 月度销量 **/
    private BigDecimal monthSales;
    /** 增长率 **/
    private String growthRate;
    /** 创建人id **/
    private String createUserId;
    /** 创建人姓名 **/
    private String createUserName;
    /** 创建时间 **/
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /** 删除标志 0正常 1删除 **/
    private String delFlag;
    /**  **/
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;

    /**企业名称-外键  **/
    @Transient
    private String enterpriseName;
    /**占比 **/
    @Transient
    private String proportion;

    @Transient
    private String monthSalesKanban;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>enterpriseId -> enterprise_id</li>
     * <li>monthSales -> month_sales</li>
     * <li>growthRate -> growth_rate</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
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
            case "enterpriseId": return "enterprise_id";
            case "monthSales": return "month_sales";
            case "growthRate": return "growth_rate";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
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
     * <li>enterprise_id -> enterpriseId</li>
     * <li>month_sales -> monthSales</li>
     * <li>growth_rate -> growthRate</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
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
            case "enterprise_id": return "enterpriseId";
            case "month_sales": return "monthSales";
            case "growth_rate": return "growthRate";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
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
