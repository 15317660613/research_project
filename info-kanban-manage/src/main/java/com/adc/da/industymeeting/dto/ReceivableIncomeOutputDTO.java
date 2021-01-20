package com.adc.da.industymeeting.dto;

import com.adc.da.excel.annotation.Excel;
import com.adc.da.util.utils.StringUtils;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceivableIncomeOutputDTO {

    @Excel(name = "年",orderNum = "1")
    private String year;

    @Excel(name = "月",orderNum = "2")
    private String month;

    @Excel(name = "日",orderNum = "3")
    private String day;

    @Excel(name = "本部",orderNum = "4")
    private String headquarters;

    @Excel(name = "部门",orderNum = "5")
    private String department;

    @Excel(name = "项目",orderNum = "6")
    private String project;

    @Excel(name = "企业名称",orderNum = "7")
    private String corpname;

    private Double weeklyArrival = 0.0;

    @Excel(name = "本周到账",orderNum = "8")
    private String weeklyArrivalStr;

    private Double amountReceivable = 0.0;

    @Excel(name = "应收余额",orderNum = "9")
    private String amountReceivableStr;

    @Excel(name = "到账时间",orderNum = "10")
    private String accountTime;

    @Excel(name = "公司",orderNum = "11")
    private String company;

//    public void setWeeklyArrival(Double weeklyArrival) {
//        if (weeklyArrival != null) {
//            this.weeklyArrival = weeklyArrival;
//            BigDecimal bd = new BigDecimal(weeklyArrival.toString());
//            this.weeklyArrivalStr = bd.toString();
//        } else {
//            this.weeklyArrivalStr = "";
//        }
//    }

//    public void setAmountReceivable(Double amountReceivable) {
//        if (amountReceivable != null) {
//            this.amountReceivable = amountReceivable;
//            BigDecimal bd = new BigDecimal(amountReceivable.toString());
//            this.amountReceivableStr = bd.toString();
//        } else {
//            this.amountReceivableStr = "";
//        }
//    }
}