package com.adc.da.finance.constant;

public enum StatusEnum {

    PROGRESS(0, "进行"),
    FINISHED(1, "完成");

    private int value;
    private String label;

    private StatusEnum(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return this.value;
    }

    public String getLabel() {
        return this.label;
    }
}
