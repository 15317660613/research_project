package com.adc.da.progress.util;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-07-19
 */
public class StatusUtils {
    private StatusUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 对于项目进度-顺序 表示 未发起
     * 对于项目进度-流程 表示 进行中
     */
    public static final int NOT_COMPLETED = 0;

    /**
     * 完成
     */
    public static final int COMPLETED = 1;

    /**
     * 流程异常终止
     */
    public static final int CANCEL = 2;

}
