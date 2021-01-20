package com.adc.da.smallprogram.enums;

/**
 * 删除标记枚举
 *
 * @author liuzixi
 * date 2019-01-28
 */
public enum DeleteCodeEnum {

    /**
     * 未删除
     */
    NORMAL("0", "0"),
    /**
     * 已删除
     */
    DELETED("1", "1");

    /**
     * int格式
     */
    private String code;

    /**
     * String格式
     */
    private String codeStr;

    DeleteCodeEnum(String code, String codeStr) {
        this.code = code;
        this.codeStr = codeStr;
    }

    /**
     * Gets the value of code.
     *
     * @return the value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the value of codeStr.
     *
     * @return the value of codeStr
     */
    public String getCodeStr() {
        return codeStr;
    }
}
