package com.adc.da.research.funds.vo.over;

import java.util.Date;

/**
 *
 * @Auther: yanyujie
 * @Date: 2020/11/25/
 * @Description:
 */
public class ItemDataVO {
    private Date overDate;
    private String year;
    private Date overDateBegin;
    private Date overDateEnd;
    //结转人
    private String operator;
    //剩余预算
    private Double remainAmount;
    //经费结转百分比
    private String percentage;
    //结转经费
    private Double amount;

    //技术负责人
    private String technicalDirector;


    public String getTechnicalDirector() {
        return technicalDirector;
    }

    public void setTechnicalDirector(String technicalDirector) {
        this.technicalDirector = technicalDirector;
    }

    public Date getOverDate() {
        return overDate;
    }

    public void setOverDate(Date overDate) {
        this.overDate = overDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Date getOverDateBegin() {
        return overDateBegin;
    }

    public void setOverDateBegin(Date overDateBegin) {
        this.overDateBegin = overDateBegin;
    }

    public Date getOverDateEnd() {
        return overDateEnd;
    }

    public void setOverDateEnd(Date overDateEnd) {
        this.overDateEnd = overDateEnd;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Double getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(Double remainAmount) {
        this.remainAmount = remainAmount;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
