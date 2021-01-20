package com.adc.da.research.funds.vo.change;

/**
 *
 * @Auther: yanyujie
 * @Date: 2020/12/03
 * @Description:
 */
public class FundChangeVO {
    //年份
    private String year;
    //资金类型
    private String budgetType;
    //原有金额
    private String originalAmount;
    //现有金额
    private String existingAmount;
    //相差金额
    private String differenceAmount;

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
}
