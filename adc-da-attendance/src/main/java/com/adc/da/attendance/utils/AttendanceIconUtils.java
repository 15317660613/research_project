package com.adc.da.attendance.utils;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-12-13
 */
public class AttendanceIconUtils {
    private AttendanceIconUtils() {
        throw new IllegalStateException("Utility class");
    }

    /** 年假 */
    private static final String ICON_ANNUAL_LEAVE = "○";

    /** 病假 */
    private static final String ICON_SICK_LEAVE = "●";

    /** 事假 */
    private static final String ICON_BUSINESS_LEAVE = "△";

    /** 婚假 */
    private static final String ICON_WEDDING_LEAVE = "▲";

    /** 产假 */
    private static final String ICON_MATERNITY_LEAVE = "◎";

    /** 探亲假 */
    private static final String ICON_FAMILY_LEAVE = "☆";

    /** 丧假 */
    private static final String ICON_FUNERAL_LEAVE = "★";


    /** 旷工 */
    private static final String ICON_ABSENTEEISM = "◇";

    /** 迟到 */
    private static final String ICON_LATE = "◆";

    /** 出差 */
    private static final String ICON_TRAVEL = "⊙";

    /** 早退 */
    private static final String ICON_EARLY = "*";
}
