package com.adc.da.listener.utils;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-11-05
 */
public class RSProjectBusinessKey {
    private RSProjectBusinessKey() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 科研项目id
     */
    public static final int RS_PROJECT_ID = 0;

    /**
     * 科研项目key
     */
    public static final int RS_BUSINESS_KEY = 1;

    /**
     * 将 科研类项目的 businessKey 进行切割
     *
     * @param businessKey
     * @return
     */
    public static String[] spitBusinessKey(String businessKey) {
        return businessKey.split(":");
    }


}
