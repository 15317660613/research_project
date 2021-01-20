package com.adc.da.bus.vo.output;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/9 09:19
 * @Description: 财务合同一览信息表
 */
public class FinanceViewEntity {


    /**
     * 统计的金额
     */
    private float turn_total;

    private float in_come;


    /**
     * 支出和收入一览信息
     */
    private String unitTime;
    private double currentMoney;
    private double sequentialMoney;
    private double compareMoney;

    public String getUnitTime() {
        return unitTime;
    }

    public void setUnitTime(String unitTime) {
        this.unitTime = unitTime;
    }

    public double getCurrentMoney() {
        return currentMoney;
    }

    public void setCurrentMoney(double currentMoney) {
        this.currentMoney = currentMoney;
    }

    public double getSequentialMoney() {
        return sequentialMoney;
    }

    public void setSequentialMoney(double sequentialMoney) {
        this.sequentialMoney = sequentialMoney;
    }

    public double getCompareMoney() {
        return compareMoney;
    }

    public void setCompareMoney(double compareMoney) {
        this.compareMoney = compareMoney;
    }

    public float getTurn_total() {
        return turn_total;
    }

    public void setTurn_total(float turn_total) {
        this.turn_total = turn_total;
    }

    public float getIn_come() {
        return in_come;
    }

    public void setIn_come(float in_come) {
        this.in_come = in_come;
    }
}
