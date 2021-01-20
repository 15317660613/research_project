package com.adc.da.statistics.util;

import java.util.Calendar;
import java.util.Date;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-11-20
 */
public class DataBoardUtils {
    private DataBoardUtils() {
        throw new IllegalStateException("Utility class");
    }

    /** 开始 */
    public static final int INT_BEGIN = 0;

    /** 结束 */
    public static final int INT_END = 1;

    /** 初始化时间 */
    public static Date[] initDate(int year) {
        Date[] date = new Date[2];
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
//        01-01 00:00.000
        date[INT_BEGIN] = calendar.getTime();

//        12-31 23:59:999
        calendar.add(Calendar.YEAR, +1);
        calendar.add(Calendar.MILLISECOND, -1);
        date[INT_END] = calendar.getTime();
        return date;
    }
}
