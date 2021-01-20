package com.adc.da.industymeeting.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceivableIncomeFiledDTO implements IExcelModel, IExcelDataModel {

    private String errorMsg;

    private int rowNum;

    @Excel(name = "*年",orderNum = "0")
    private String year;

    @Excel(name = "*月",orderNum = "1")
    private String month;

    @Excel(name = "*公司",orderNum = "2")
    private String company;

    @Excel(name = "*区域",orderNum = "3")
    private String area;

    @Excel(name = "*当月应收余额（元）",orderNum = "4")
    private BigDecimal receivableBalance;

    @Excel(name = "*当月进账额（元）",orderNum = "5")
    private BigDecimal income;
}