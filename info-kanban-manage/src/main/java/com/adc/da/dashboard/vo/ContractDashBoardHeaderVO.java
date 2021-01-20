package com.adc.da.dashboard.vo;

import lombok.Data;

/**
 * @ClassName ContractDashBoardHeaderVO
 * @Description: 经营数据看板 头部四个小块展示数据包装类
 * @Author 丁强
 * @Date 2020/4/1
 * @Version V1.0
 **/
@Data
public class ContractDashBoardHeaderVO {
    Double yearContractAmount = 0.0D; //年累计合同额

    Double lastYearContractAmount = 0.0D; //年累计合同额

    Double yearContractAmountRate = 0.0D; //年累计合同额 同比增长

    Double monthContractAmount = 0.0D; //当月合同额

    Double lastMonthContractAmount = 0.0D; //当月合同额

    Double monthContractAmountRate = 0.0D; //当月合同额 同比增长

    Double yearInvoiceAmount = 0.0D;//年累计开票额

    Double lastYearInvoiceAmount = 0.0D;//年累计开票额

    Double yearInvoiceAmountRate = 0.0D; //年累计开票额同比增长

    Double monthInvoiceAmount = 0.0D;//月累计开票额

    Double lastMonthInvoiceAmount = 0.0D;//月累计开票额

    Double monthInvoiceAmountRate = 0.0D;//月累计开票额 同比增长

    Double yearIncomeAmount = 0.0D; //年累计进账额

    Double lastYearIncomeAmount = 0.0D; //年累计进账额f

    Double yearIncomeAmountRate = 0.0D;//年累计进账额 同比增长

    Double monthIncomeAmount = 0.0D;//月累计进账额

    Double lastMonthIncomeAmount = 0.0D;//月累计进账额

    Double monthIncomeAmountRate = 0.0D;//月累计进账额 同比增长

    Double receivableAmount = 0.0D; //累计应收余额

    Double receivableAmountRate = 0.0D; //同比下降

}
