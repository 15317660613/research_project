package com.adc.da.research.funds.vo.perform;

import lombok.Data;

/**
 *
 * @Auther: yanyujie
 * @Date: 2020/11/23
 * @Description:
 */

@Data
public class SubjectProgressVO {
    //项目Id
    private String projectId;

    //预算科目
    private String subjcetName;

    //国拨预算经费
    private Double stateBudgetFunds;

    //国拨执行经费
    private Double stateExecuteFunds;

    //国拨执行率
    private String stateRate;

    //自筹预算经费
    private Double selfBudgetFunds;

    //自筹执行经费
    private Double selfExecuteFunds;

    //自筹执行率
    private String selfRate;

    //中心预算经费
    private Double centerBudgetFunds;

    //中心执行经费
    private Double centerExecuteFunds;

    //自筹执行率
    private String centerRate;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSubjcetName() {
        return subjcetName;
    }

    public void setSubjcetName(String subjcetName) {
        this.subjcetName = subjcetName;
    }

    public Double getStateBudgetFunds() {
        return stateBudgetFunds;
    }

    public void setStateBudgetFunds(Double stateBudgetFunds) {
        this.stateBudgetFunds = stateBudgetFunds;
    }

    public Double getStateExecuteFunds() {
        return stateExecuteFunds;
    }

    public void setStateExecuteFunds(Double stateExecuteFunds) {
        this.stateExecuteFunds = stateExecuteFunds;
    }

    public String getStateRate() {
        return stateRate;
    }

    public void setStateRate(String stateRate) {
        this.stateRate = stateRate;
    }

    public Double getSelfBudgetFunds() {
        return selfBudgetFunds;
    }

    public void setSelfBudgetFunds(Double selfBudgetFunds) {
        this.selfBudgetFunds = selfBudgetFunds;
    }

    public Double getSelfExecuteFunds() {
        return selfExecuteFunds;
    }

    public void setSelfExecuteFunds(Double selfExecuteFunds) {
        this.selfExecuteFunds = selfExecuteFunds;
    }

    public String getSelfRate() {
        return selfRate;
    }

    public void setSelfRate(String selfRate) {
        this.selfRate = selfRate;
    }

    public Double getCenterBudgetFunds() {
        return centerBudgetFunds;
    }

    public void setCenterBudgetFunds(Double centerBudgetFunds) {
        this.centerBudgetFunds = centerBudgetFunds;
    }

    public Double getCenterExecuteFunds() {
        return centerExecuteFunds;
    }

    public void setCenterExecuteFunds(Double centerExecuteFunds) {
        this.centerExecuteFunds = centerExecuteFunds;
    }

    public String getCenterRate() {
        return centerRate;
    }

    public void setCenterRate(String centerRate) {
        this.centerRate = centerRate;
    }
}