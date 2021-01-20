package com.adc.da.statistics.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <b>功能：</b>ST_DATA_BOARD_ORG DataBoardOrgEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-15 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 * /**
 * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
 * <p>字段列表：</p>
 * <li>extInfo02 -> ext_info_02_</li>
 * <li>extInfo03 -> ext_info_03_</li>
 * <li>extInfo04 -> ext_info_04_</li>
 * <li>extInfo05 -> ext_info_05_</li>
 * <li>id -> id_</li>
 * <li>deptId -> dept_id_</li>
 * <li>billing -> billing_</li>
 * <li>credit -> credit_</li>
 * <li>expenditure -> expenditure_</li>
 * <li>workTime -> work_time_</li>
 * <li>createTime -> create_time_</li>
 * <li>year -> year</li>
 * <li>month -> month</li>
 * <li>extInfo01 -> ext_info_01_</li>
 */
@Getter
@Setter

public class DataBoardOrgEO extends BaseEntity {

    /***/
    private String extInfo02;

    /***/
    private String extInfo03;

    /***/
    private String extInfo04;

    /***/
    private String extInfo05;

    /***/
    private String id;

    /***/
    private String deptId;

    /***/
    private Double billing;

    /***/
    private Double credit;

    /***/
    private Double expenditure;

    /***/
    private Double workTime;

    /***/
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /***/
    private Integer year;

    /***/
    private Integer month;

    /**
     * 若部门查询不到，这个字段存储 部门名称
     */
    private String extInfo01;

}
