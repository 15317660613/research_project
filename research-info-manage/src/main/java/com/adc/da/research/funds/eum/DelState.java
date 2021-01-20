package com.adc.da.research.funds.eum;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: yanyujie
 * @Date: 2020/11/17
 * @Description: 删除类别
 */
public enum DelState {
    NOT_DELETED(0, "未删除"),
    DELETED(1,"已删除");

    private Integer code;
    private String label;

    private DelState(Integer value, String label) {
        this.code = value;
        this.label = label;
    }

    public Integer getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
