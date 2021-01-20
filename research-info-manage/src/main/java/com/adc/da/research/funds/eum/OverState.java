package com.adc.da.research.funds.eum;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: yanyujie
 * @Date: 2020/11/17
 * @Description: 结转类别
 */
public enum OverState {
    NOT_OVER(0, "未结转"),
    OVER(1,"已结转");

    private Integer code;
    private String label;

    private OverState(Integer value, String label) {
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
