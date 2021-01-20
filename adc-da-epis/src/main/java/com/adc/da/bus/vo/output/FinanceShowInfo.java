package com.adc.da.bus.vo.output;

import java.util.List;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/9 15:21
 * @Description:
 */
public class FinanceShowInfo {

    FinanceViewEntity totalEntity;

    /**
     * @Date: 2018/11/9 15:21
     * @Description: 划转一览表
     */
    private List<FinanceViewEntity> turnList;

    /**
     * @Date: 2018/11/9 15:21
     * @Description:非结转一览表
     */
    private List<FinanceViewEntity> moneyList;


    public FinanceViewEntity getTotalEntity() {
        return totalEntity;
    }

    public void setTotalEntity(FinanceViewEntity totalEntity) {
        this.totalEntity = totalEntity;
    }

    public List<FinanceViewEntity> getTurnList() {
        return turnList;
    }

    public void setTurnList(List<FinanceViewEntity> turnList) {
        this.turnList = turnList;
    }

    public List<FinanceViewEntity> getMoneyList() {
        return moneyList;
    }

    public void setMoneyList(List<FinanceViewEntity> moneyList) {
        this.moneyList = moneyList;
    }
}
