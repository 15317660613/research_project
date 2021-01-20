package com.adc.da.business.vo;

import lombok.Data;

@Data
public class DeptOperationVO {
    private String deptName;

    private String deptId;


    private float actualProfit;//实际利润

    private float profitBudget;//利润预算

    private float profitCompleteRate;//利润指标完成率

    private float actualIncome;//实际收入

    private float incomeBudget;//收入预算

    private float budgetCompleteRate;//预算指标完成率

    private float actualCost;//实际成本

    private float costBudget;//成本预算

    private float costCompleteRate;//成本指标完成率

    private Double actualWorkTime;//实际工时

    private Integer workerNumber;//人员数量

}
