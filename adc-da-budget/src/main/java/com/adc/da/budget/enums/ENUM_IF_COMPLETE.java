package com.adc.da.budget.enums;

/**
 * 是否完成枚举
 * created by chenhaidong 2018/11/28
 */
public enum ENUM_IF_COMPLETE {
    YES("是","已完成"),
    NO("否","未完成");

    private String code;
    private String label;

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    ENUM_IF_COMPLETE(String code, String label) {
        this.code = code;
        this.label = label;
    }

    ENUM_IF_COMPLETE() {
    }
}
