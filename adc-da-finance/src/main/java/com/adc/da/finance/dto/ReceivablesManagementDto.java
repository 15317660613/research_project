package com.adc.da.finance.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * <b>功能：</b>F__RECEIVABLES_MANAGEMENT ReceivablesManagementEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ReceivablesManagementDto implements IExcelDataModel, IExcelModel {
    //年月份有效期验证正则
    public static final String YEAR_REGEXP = "[0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3}";
    public static final String MONTH_REGEXP = "^0?[0-9]$|^1[0-2]$";

    @Excel(name = "*部门",orderNum = "1",width = 20)
    @NotNull(message = "部门不能为空！")
    private String departName;
    @Excel(name = "*年",orderNum = "2")
    @NotNull(message = "年不能为空！")
    @Pattern(regexp = YEAR_REGEXP,message = "年份不合法！")
    private String remYear;
    @Excel(name = "*月",orderNum = "3")
    @NotNull(message = "月不能为空！")
    @Pattern(regexp = MONTH_REGEXP,message = "月份不合法！")
    private String remMonth;
    @Excel(name = "*业务名称",orderNum = "4",width = 50)
    @NotNull(message = "业务名称不能为空！")
    private String businessGonfigName;
    @Excel(name = "*本月收回金额（元）",orderNum = "5",width = 30,format = "0")
    @DecimalMax(value = "999999999999.99",message="本月收回金额应小于999999999999.99")
    //@DecimalMin(value = "0",message = "本月收回金额应大于0")
    @NotNull(message = "本月收回金额不能为空！")
    private BigDecimal remMoney;

    private int rowNum;

    private String errorMsg;
}
