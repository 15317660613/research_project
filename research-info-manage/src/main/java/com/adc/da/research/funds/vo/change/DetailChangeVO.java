package com.adc.da.research.funds.vo.change;

import java.util.Date;

/**
 *
 * @Auther: yanyujie
 * @Date: 2020/12/03
 * @Description:
 */
public class DetailChangeVO {
    private String type;
    //年份
    private String year;
    //季度
    private String quarter;
    //月份
    private String month;
    //资金类型
    private String budgetType;
    //经费名称
    private String budgetName;
    //原有金额
    private String originalAmount;
    //现有金额
    private String existingAmount;
    //相差金额
    private String differenceAmount;
    //调整日期
    private Date changeTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBudgetType() {
        return budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getExistingAmount() {
        return existingAmount;
    }

    public void setExistingAmount(String existingAmount) {
        this.existingAmount = existingAmount;
    }

    public String getDifferenceAmount() {
        return differenceAmount;
    }

    public void setDifferenceAmount(String differenceAmount) {
        this.differenceAmount = differenceAmount;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }
}
