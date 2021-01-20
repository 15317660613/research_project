package com.adc.da.industymeeting.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReceivableIncomeDTO implements IExcelModel,IExcelDataModel {

    @Excel(name = "年",orderNum = "1")
    @NotNull(message = "年不能为空！")
    private String year;

    @Excel(name = "月",orderNum = "2")
    @NotNull(message = "月不能为空！")
    private String month;

    @Excel(name = "日",orderNum = "3")
    @NotNull(message = "日不能为空！")
    private String day;

    @Excel(name = "本部",orderNum = "4")
    @NotNull(message = "本部不能为空！")
    private String headquarters;

    @Excel(name = "部门",orderNum = "5")
    @NotNull(message = "部门不能为空！")
    private String department;

    @Excel(name = "项目",orderNum = "6")
    @NotNull(message = "项目不能为空！")
    private String project;

    @Excel(name = "企业名称",orderNum = "7")
    @NotNull(message = "企业名称不能为空！")
    private String corpname;

    @Excel(name = "本周到账",orderNum = "8",numFormat = "0.0")
    private Double weeklyArrival = 0.0;

    @Excel(name = "应收余额",orderNum = "9",numFormat = "0.0")
    private Double amountReceivable = 0.0;

    @Excel(name = "到账时间",orderNum = "10")
    private String accountTime;

    @Excel(name = "公司",orderNum = "11")
    @NotNull(message = "公司不能为空！")
    private String company;

    private String errorMsg;

    private int rowNum;
}