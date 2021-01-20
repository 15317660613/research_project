package com.adc.da.statics.vo;

import lombok.Data;


@Data
public class StaticOperationCompanyVO {
    boolean ISCOMPANY = true; //是否为公司

    String orgName; // 部门名称

    double yearContractAmount = 0.0D; //年累计合同额

    double yearInvoiceAmount = 0.0D;//年累计开票额

    double yearIncomeAmount = 0.0D; //年累计进账额

    double yearReceivableAmount = 0.0D;//年应收余额

    double salesvolume = 0.0D;//销量

    private double GDPrate;//占比
}
