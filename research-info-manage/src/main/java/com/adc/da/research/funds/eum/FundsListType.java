package com.adc.da.research.funds.eum;

public enum FundsListType {

    PAGE(1, "分页查询"),
    EXPORT(2,"导出");

    private Integer value;
    private String label;

    private FundsListType(Integer value, String label) {
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