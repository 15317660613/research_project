package com.adc.da.budget.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 气泡数据DTO
 * created by chenhaidong 2018/11/29
 */
@Data
public class BubbleDTO {
    /**
     * 人力投入
     */
    private Integer x;
    /**
     * 合同收入金额（万元）
     */
    private BigDecimal y;
    /**
     * 项目名称
     */
    private String title;
    /**
     * 利润（万元）
     */
    private BigDecimal radius;
    /**
     * 业务名称
     */
    private String Business;

}
