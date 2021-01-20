package com.adc.da.research.funds.vo.change;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: yanyujie
 * @Date: 2020/12/09/16:38
 * @Description:
 */
public class BudgetFundVO {
    private String type;
    private String year;
    private String budgetType;
    private String beforeNum;
    private String num;
    private String afterNum;

    private String group;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public String getBeforeNum() {
        return beforeNum;
    }

    public void setBeforeNum(String beforeNum) {
        this.beforeNum = beforeNum;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getAfterNum() {
        return afterNum;
    }

    public void setAfterNum(String afterNum) {
        this.afterNum = afterNum;
    }
}
