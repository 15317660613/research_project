package com.adc.da.industymeeting.dto;

import com.adc.da.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceivableIncomeFiledOutputDTO {

    @Excel(name = "年",orderNum = "1")
    private String year;

    @Excel(name = "月",orderNum = "2")
    private String month;

    @Excel(name = "公司",orderNum = "3")
    private String company;

    @Excel(name = "区域",orderNum = "4")
    private String areaName;

    private Double receivableBalance = 0.0;

    @Excel(name = "当月应收账款余额(元)",orderNum = "5")
    private String receivableBalanceStr;

    private Double income;

    @Excel(name = "当月进账额(元)",orderNum = "6")
    private String incomeStr;

    public void setReceivableBalance(Double receivableBalance) {
        this.receivableBalance = receivableBalance;
        BigDecimal bd = new BigDecimal(receivableBalance.toString());
        this.receivableBalanceStr = bd.toString();
    }

    public void setIncome(Double income) {
        this.income = income;
        BigDecimal bd = new BigDecimal(income.toString());
        this.incomeStr = bd.toString();
    }
}