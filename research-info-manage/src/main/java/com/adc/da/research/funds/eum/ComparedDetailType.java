package com.adc.da.research.funds.eum;

public enum ComparedDetailType {

    YEAR(1, "年"),
    QUARTER(2,"季度"),
    MONTH(3,"月度"),
    ALL(4,"全部");

    private Integer value;
    private String label;

    private ComparedDetailType(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }
}