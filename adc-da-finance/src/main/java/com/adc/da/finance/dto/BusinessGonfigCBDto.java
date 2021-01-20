package com.adc.da.finance.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <b>功能：</b>F__BUSINESS_GONFIG BusinessGonfigEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class BusinessGonfigCBDto implements IExcelModel, IExcelDataModel {

    @Excel(name = "*成本名称",orderNum = "1",width = 50)
    @NotNull(message = "成本名称不能为空！")
    private String bgName;
    @Excel(name = "*成本编号",orderNum = "2",width = 30)
    @NotNull(message = "成本编号不能为空！")
    private String bgNumber;
    @Excel(name = "*负责部门",orderNum = "3",width = 30)
    @NotNull(message = "负责部门不能为空！")
    private String departName;
    @Excel(name = "*成本状态",orderNum = "4",width = 15)
    @NotNull(message = "成本状态不能为空！")
    private String bgStatusName;

    private int rowNum;

    private String errorMsg;
}
