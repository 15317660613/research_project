package com.adc.da.business.entity;

import com.adc.da.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-06-11
 */
@Data
public class BudgetIncomeCalculateEO extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 36586329397227L;

    /**
     * 业务id
     */
    @ApiModelProperty("业务id")
    private String businessId;

    /**
     * 业务名
     */
    @ApiModelProperty("业务名")
    private String businessName;

    /**
     * 项目负责人id
     */
    @ApiModelProperty("项目负责人id")
    private String pmId;

    /**
     * 项目负责人
     */
    @ApiModelProperty("项目负责人")
    private String pmName;

    /**
     * 收入
     */
    @ApiModelProperty("收入")
    private BigDecimal allInvoice;

    /**
     * 预算
     */
    @ApiModelProperty("预算")
    private BigDecimal revenueBudget;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private Integer year;

    /**
     * 执行率
     */
    @ApiModelProperty("执行率")
    private String executionRate;

    /**
     * 实际工时
     */
    @ApiModelProperty("实际工时")
    private Double workTime;

    /**
     * 人员数量，最少1个（负责人本身）
     */
    @ApiModelProperty("人员数量")
    private Integer personCount = 1;

}
