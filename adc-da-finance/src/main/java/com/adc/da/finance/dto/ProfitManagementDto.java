package com.adc.da.finance.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>F_PROFIT_MANAGEMENT ProfitManagementEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ProfitManagementDto extends BaseEntity {

    /** 业务名称**/
    @Excel(name = "业务名称",orderNum = "1",width = 60)
    private String businessGonfigName;
    /** 年 **/
    @Excel(name = "年",orderNum = "2")
    private String pmYear;
    /** 月 **/
    @Excel(name = "月",orderNum = "3")
    private String pmMonth;
    /** 成本金额 **/
    @Excel(name = "成本金额（元）",orderNum = "4",width = 20)
    private BigDecimal costMoney;
    /** 收入金额 **/
    @Excel(name = "收入金额（元）",orderNum = "5",width = 20)
    private BigDecimal incomeMoney;
    /** 利润金额 **/
    @Excel(name = "利润金额（元）",orderNum = "6",width = 20)
    private BigDecimal profitMoney;
    /** 利润率 **/
    @Excel(name = "利润率",orderNum = "7",width = 20)
    private String profitMargin;
}
