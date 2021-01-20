package com.adc.da.statistics.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>ST_BUSINESS_WORKTIME BusinessWorktimeEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
public class BusinessWorktimeEO extends BaseEntity {

    private String id;
    private String businessId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date dailyTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private Float worktime;
    private String departmentId;
    private String extinfo1;
    private String extinfo2;
    private String extinfo3;
    private String extinfo4;
    private String extinfo5;
    private String extinfo6;
    private String year;
    private String month;

    /**
     * 用于本地计算，不会存入数据库
     */
    private BigDecimal workTimeLocal;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>businessId -> business_id</li>
     * <li>dailyTime -> daily_time</li>
     * <li>createTime -> create_time</li>
     * <li>worktime -> worktime</li>
     * <li>departmentId -> department_id</li>
     * <li>extinfo1 -> extinfo1</li>
     * <li>extinfo2 -> extinfo2</li>
     * <li>extinfo3 -> extinfo3</li>
     * <li>extinfo4 -> extinfo4</li>
     * <li>extinfo5 -> extinfo5</li>
     * <li>extinfo6 -> extinfo6</li>
     * <li>year -> year</li>
     * <li>month -> month</li>
     */

}
