package com.adc.da.statics.vo;

import lombok.Data;


public class StaticOperationAmountChartVO {

    private String area;

    private String province;

    private Double amount;

    private Double lastAmount;

    private Double yearAmount;

    private Double rate;

    private String year;

    private Integer month;

    private String departmentId;

    private String departmentName;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(Double lastAmount) {
        this.lastAmount = lastAmount;
    }

    public Double getYearAmount() {
        return yearAmount;
    }

    public void setYearAmount(Double yearAmount) {
        this.yearAmount = yearAmount;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
