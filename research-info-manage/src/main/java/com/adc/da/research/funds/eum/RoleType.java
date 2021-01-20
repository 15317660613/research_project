package com.adc.da.research.funds.eum;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: yanyujie
 * @Date: 2020/11/13/10:52
 * @Description: 权限类别
 */
public enum RoleType {
    RS_ADMIN("RS_ADMIN", "科研管理员");

    private String code;
    private String label;

    private RoleType(String value, String label) {
        this.code = value;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }
}
