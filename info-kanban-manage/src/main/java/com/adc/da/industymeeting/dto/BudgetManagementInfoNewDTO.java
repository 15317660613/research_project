package com.adc.da.industymeeting.dto;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class BudgetManagementInfoNewDTO implements IExcelModel, IExcelDataModel {

    @Excel(name = "本部", orderNum = "1")
    @NotNull(message = "本部不能为空！")
    private String headquarters;

    @Excel(name = "部门", orderNum = "2")
    @NotNull(message = "部门不能为空！")
    private String department;

    @Excel(name = "年份", orderNum = "3")
    private String year;

    @Excel(name = "产值目标（元）", orderNum = "4")
    private Double ouputTarget = 0.0;

    private String errorMsg;

    private int rowNum;
}
