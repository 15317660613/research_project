package com.adc.da.statics.vo;

import lombok.Data;

//@Data
public class StaticOperationDashBoardHeaderVO {
    private Double yearContractAmount;
    private Double yearInvoiceAmount;
    private Double yearIncomeAmount;
    private Double monthContractAmount;
    private Double monthInvoiceAmount;
    private Double monthIncomeAmount;
    private Double receivableAmount;

    public Double getYearContractAmount() {
        return yearContractAmount;
    }

    public void setYearContractAmount(Double yearContractAmount) {
        this.yearContractAmount = yearContractAmount;
    }

    public Double getYearInvoiceAmount() {
        return yearInvoiceAmount;
    }

    public void setYearInvoiceAmount(Double yearInvoiceAmount) {
        this.yearInvoiceAmount = yearInvoiceAmount;
    }

    public Double getYearIncomeAmount() {
        return yearIncomeAmount;
    }

    public void setYearIncomeAmount(Double yearIncomeAmount) {
        this.yearIncomeAmount = yearIncomeAmount;
    }

    public Double getMonthContractAmount() {
        return monthContractAmount;
    }

    public void setMonthContractAmount(Double monthContractAmount) {
        this.monthContractAmount = monthContractAmount;
    }

    public Double getMonthInvoiceAmount() {
        return monthInvoiceAmount;
    }

    public void setMonthInvoiceAmount(Double monthInvoiceAmount) {
        this.monthInvoiceAmount = monthInvoiceAmount;
    }

    public Double getMonthIncomeAmount() {
        return monthIncomeAmount;
    }

    public void setMonthIncomeAmount(Double monthIncomeAmount) {
        this.monthIncomeAmount = monthIncomeAmount;
    }

    public Double getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(Double receivableAmount) {
        this.receivableAmount = receivableAmount;
    }
}
