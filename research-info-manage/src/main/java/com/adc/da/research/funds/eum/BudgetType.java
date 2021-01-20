package com.adc.da.research.funds.eum;

public enum BudgetType {

    STATE_FUND("国拨经费"),
    SELF_FUND("自筹经费");

    private String value;

    private BudgetType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}