package com.adc.da.research.funds.eum;

public enum AmountType {

    STATE_FUND("1", "国拨"),
    SELF_FUND("2","自筹");

    private String value;
    private String label;

    private AmountType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}