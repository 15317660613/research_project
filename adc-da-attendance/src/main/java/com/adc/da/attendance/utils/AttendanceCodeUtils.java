package com.adc.da.attendance.utils;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-12-13
 */
public class AttendanceCodeUtils {
    private AttendanceCodeUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 迟到早退
     */
    public static final int BE_LATE_AND_LEAVE_EARLY = 0b00;

    /**
     * 迟到
     */
    public static final int LEAVE_EARLY = 0b10;

    /**
     * 早退
     */
    public static final int BE_LATE = 0b01;

    /**
     * 正常出勤
     */
    public static final int NORMAL_ATTENDANCE = 0b11;
}
