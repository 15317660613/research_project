package com.adc.da.listener.utils;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-08-02
 */
public class FormType {

    private FormType() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 项目id
     */
    public static final int PROJECT_ID = 0;

    /**
     * 项目名称
     */
    public static final int PROJECT_NAME = 1;

    /**
     * 业务id
     */
    public static final int BUDGET_ID = 2;

    /**
     * 开始时间
     */
    public static final int BEGIN_TIME = 0;

    /**
     * 结束时间
     */
    public static final int END_TIME = 1;

    public static final int INT_ID = 0;

    public static final int INT_NAME = 1;

    public static final int INT_OLD = 0;

    public static final int INT_NEW = 1;

    /**
     * 开始
     */
    public static final int INT_BEGIN = 0;

    /**
     * 结束
     */
    public static final int INT_END = 1;
}
