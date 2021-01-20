package com.adc.da.statistics.dto;

import com.adc.da.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <b>功能：</b>ST_DATA_BOARD_ORG DataBoardOrgEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-15 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Getter
@Setter
public class DataBoardOrgDTO extends BaseEntity {

    private Integer year;

    private Integer month;

    private Integer day;

    private String id;

    private String deptName;

    /**
     * 用于导入 相关数据进入数据库
     * 标识 支出额 或 应收账款
     */
    private Double data;

    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}
