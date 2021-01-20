package com.adc.da.finance.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.adc.da.base.entity.BaseEntity;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * <b>功能：</b>F_CASHFLOW_MANAGEMENT CashflowManagementEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class CashflowManagementDto extends BaseEntity {


    /** 业务名称**/
    @Excel(name = "业务名称",orderNum = "1",width = 60)
    private String businessGonfigName;
    /** 年**/
    @Excel(name = "年",orderNum = "2" )
    private String cmYear;
    /** 月**/
    @Excel(name = "月",orderNum = "3")
    private String cmMonth;
    /** 现金流入（元）**/
    @Excel(name = "现金流入（元）",orderNum = "4",width = 20)
    private Double flowInMoney;
    /** 现金流出（元）**/
    @Excel(name = "现金流出（元）",orderNum = "5",width = 20)
    private Double flowOutMoney;
    /** 现金余额（元）**/
    @Excel(name = "现金余额（元）",orderNum = "6",width = 20)
    private Double surplusMoney;
}
