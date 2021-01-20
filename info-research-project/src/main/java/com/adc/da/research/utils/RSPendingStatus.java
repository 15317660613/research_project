package com.adc.da.research.utils;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-10-24
 */
public class RSPendingStatus {
    private RSPendingStatus() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 待发-删除
     */
    public static final String RS_PENDING_DELETE = "-1";

    /**
     * 待发-草稿
     */
    public static final String RS_PENDING_CREATION = "0";

    /**
     * 待发-完成
     */
    public static final String RS_PENDING_FINISHED = "1";

    /**
     * 待发已完成
     */
    public static final String RS_PENDING_UPDATE = "2";

    /**
     * 待发流程中
     */
    public static final String RS_PENDING_IN_PROCESS = "3";

    /**
     *  未提交
     */
    public static final int RS_DECLARATION_PENDING = 0;

    /**
     *  已提交
     */
    public static final int RS_DECLARATION_COMMIT = 1;
}
