package com.adc.da.research.utils;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-10-24
 */
public class HiConstant {
    private HiConstant() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 草稿状态 即 未发起的Key
     */
    public static final int DRAFT_STATUS = 0;

    /**
     * 业务状态 即 已发起流程的Key
     */
    public static final int BUSINESS_STATUS = 1;
}
