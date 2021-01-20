package com.adc.da.budget.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类
 */
@Slf4j
public class DateUtil {

    /**
     * 默认的日期格式
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 默认的日期格式组合，用来将字符串转化为日期用
     */
    protected static final String[] DATE_PARSE_PATTERNS = {"yyyy/MM/dd",
            DEFAULT_DATE_PATTERN, "yyyy年MM月dd日"};

    /**
     * 默认的时间格式
     */
    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";

    /**
     * 日期代码，周日
     */
    public static final int SUNDAY = 1;

    /**
     * 日期代码，周一
     */
    public static final int MONDAY = 2;

    /**
     * 日期代码，周二
     */
    public static final int TUESDAY = 3;

    /**
     * 日期代码，周三
     */
    public static final int WEDNESDAY = 4;

    /**
     * 日期代码，周四
     */
    public static final int THURSDAY = 5;

    /**
     * 日期代码，周五
     */
    public static final int FRIDAY = 6;

    /**
     * 日期代码，周六
     */
    public static final int SATURDAY = 7;

    /**
     * 日期精度，秒
     */
    public static final int ACCURACY_SECOND = 1;

    /**
     * 日期精度，分
     */
    public static final int ACCURACY_MINUTE = 2;

    /**
     * 日期精度，小时
     */
    public static final int ACCURACY_HOUR = 3;

    /**
     * 日期精度，天
     */
    public static final int ACCURACY_DAY = 4;

    /**
     * 日期精度，月
     */
    public static final int ACCURACY_MONTH = 5;

    /**
     * 日期精度，年
     */
    public static final int ACCURACY_YEAR = 6;

    /**
     * 比较用日期格式，精度为年
     */
    public static final String ACCURACY_PATTERN_YEAR = "yyyy";

    /**
     * 比较用日期格式，精度为月
     */
    public static final String ACCURACY_PATTERN_MONTH = "yyyyMM";

    /**
     * 比较用日期格式，精度为日
     */
    public static final String ACCURACY_PATTERN_DAY = "yyyyMMdd";

    /**
     * 比较用日期格式，精度为时
     */
    public static final String ACCURACY_PATTERN_HOUR = "yyyyMMddHH";

    /**
     * 比较用日期格式，精度为分
     */
    public static final String ACCURACY_PATTERN_MINUTE = "yyyyMMddHHmm";

    /**
     * 比较用日期格式，精度为秒
     */
    public static final String ACCURACY_PATTERN_SECOND = "yyyyMMddHHmmss";

    /**
     * 单一属性格式，时
     */
    public static final String SINGLE_YEAR = "yyyy";

    /**
     * 单一属性格式，时
     */
    public static final String SINGLE_MONTH = "M";

    /**
     * 单一属性格式，时
     */
    public static final String SINGLE_DAY = "d";

    /**
     * 单一属性格式，时
     */
    public static final String SINGLE_HOUR = "H";

    /**
     * 单一属性格式，分
     */
    public static final String SINGLE_MINUTE = "m";

    /**
     * 单一属性格式，秒
     */
    public static final String SINGLE_SECOND = "s";

    /**
     *
     */
    public static final long MILLISECONDS_PER_SECOND = 1000;

    /**
     *
     */
    public static final long MILLISECONDS_PER_MINUTE = 1000L * 60;

    /**
     *
     */
    public static final long MILLISECONDS_PER_HOUR = 1000L * 60 * 60;

    /**
     *
     */
    public static final long MILLISECONDS_PER_DAY = 1000L * 60 * 60 * 24;

    /**
     * 将给定的日期字符串，按照预定的日期格式，转化为Date型数据
     *
     * @param dateStr 日期字符字符串
     * @return 日期型结果
     */
    public static Date parseDate(String dateStr) {
        Date date = null;
        try {
            date = org.apache.commons.lang3.time.DateUtils.parseDate(dateStr,
                    DATE_PARSE_PATTERNS);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return date;
    }

    /**
     * 根据指定格式转化String型日期到Date型
     *
     * @param dateStr      String型日期
     * @param parsePattern 指定的格式
     * @return Date型日期
     */
    public static Date parseDate(String dateStr, String parsePattern) {
        Date date = null;
        try {
            date = org.apache.commons.lang3.time.DateUtils.parseDate(dateStr,
                    new String[]{parsePattern});
        } catch (ParseException e) {
            log.error(e.getMessage());
        }
        return date;
    }

    /**
     * 返回系统当前时间（Date型）
     *
     * @return 系统当前时间
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 日期计算，日加减
     *
     * @param date   初始日期
     * @param amount 天数增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addDays(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addDays(date, amount);
    }

    /**
     * 日期计算，周加减
     *
     * @param date   初始日期
     * @param amount 周数增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addWeeks(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addWeeks(date, amount);
    }

    /**
     * 日期计算，月加减
     *
     * @param date   初始日期
     * @param amount 月数增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addMonths(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMonths(date, amount);
    }

    /**
     * 日期计算，年加减
     *
     * @param date   初始日期
     * @param amount 年数增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addYears(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addYears(date, amount);
    }

    /**
     * 日期计算，小时加减
     *
     * @param date   初始日期
     * @param amount 小时增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addHours(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addHours(date, amount);
    }

    /**
     * 日期计算，分钟加减
     *
     * @param date   初始日期
     * @param amount 分钟增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addMinutes(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addMinutes(date, amount);
    }

    /**
     * 日期计算，秒加减
     *
     * @param date   初始日期
     * @param amount 秒增量（负数为减）
     * @return 计算后的日期
     */
    public static Date addSeconds(Date date, int amount) {
        return org.apache.commons.lang3.time.DateUtils.addSeconds(date, amount);
    }

    /**
     * 根据指定格式，返回日期时间字符串
     *
     * @param date    日期变量
     * @param pattern 日期格式
     * @return 日期时间字符串
     */
    public static String getDateStr(Date date, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * 输出时间String(默认格式)
     *
     * @param date 日期
     * @return 默认格式化的日期
     */
    public static String getTimeStr(Date date) {
        return getDateStr(date, DEFAULT_TIME_PATTERN);
    }

    /**
     * 取指定日期所在月的第一天的日期
     *
     * @param date 指定的日期
     * @return 指定日期所在月的第一天
     */
    public static Date getFirstDayOfMonth(Date date) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.DATE, 1);
        return cal.getTime();
    }

    /**
     * 返回指定日期的季的第一天
     *
     * @param
     * @param
     * @return
     */


    public static Date getCurrentQuarterStartTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");

        Calendar c = Calendar.getInstance();

        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 0);}
            else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 3);}
            else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 4);}
            else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 9);}
            c.set(Calendar.DATE, 1);
            now = c.getTime();
            String time = format.format(now);
            now = sdf.parse(time);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return now;
    }


    /**
     * 取指定日期所在月的最后一天的日期
     *
     * @param date 指定的日期
     * @return 指定日期所在月的最后一天
     */
    public static Date getLastDayOfMonth(Date date) {
        Date nextMonth = addMonths(date, 1);
        Date firstDayOfNextMonth = getFirstDayOfMonth(nextMonth);
        return addDays(firstDayOfNextMonth, -1);
    }

    /**
     * 取指定日期所在年的第一天的日期
     *
     * @param date 指定的日期
     * @return 指定日期所在年的第一天
     */
    public static Date getFirstDayOfYear(Date date) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.DATE, 1);
        cal.set(Calendar.MONTH, 0);
        return cal.getTime();
    }

    /**
     * 取指定日期所在年的最后一天的日期
     *
     * @param date 指定的日期
     * @return 指定日期所在月的最后一天
     */
    public static Date getLastDayOfYear(Date date) {
        Date nextMonth = addYears(date, 1);
        Date firstDayOfNextYear = getFirstDayOfYear(nextMonth);
        return addDays(firstDayOfNextYear, -1);
    }

    /**
     * 取指定日期所在周的指定天的日期
     *
     * @param date     指定的日期
     * @param day      指定的天（星期几）
     * @param firstDay 一星期的起始天
     * @return 指定周星期日的日期
     */
    public static Date getDayInWeek(Date date, int day, int firstDay) {
        Calendar cal = getCalendar(date);
        cal.setFirstDayOfWeek(firstDay);
        cal.set(Calendar.DAY_OF_WEEK, day);
        return cal.getTime();
    }

    /**
     * 根据Date型的日期，取Calendar型的日期
     *
     * @param date Date型的日期
     * @return Calendar型的日期
     */
    public static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    /**
     * 日期比较（精确到天），date1晚于date2
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return date1晚于date2，返回true，否则返回false
     */
    public static boolean later(Date date1, Date date2) {
        boolean result = false;
        if (1 == compare(date1, date2, ACCURACY_DAY)) {
            result = true;
        }
        return result;
    }

    /**
     * 日期比较（精确到天），date1早于date2
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return date1早于date2，返回true，否则返回false
     */
    public static boolean earlier(Date date1, Date date2) {
        boolean result = false;
        if (-1 == compare(date1, date2, ACCURACY_DAY)) {
            result = true;
        }
        return result;
    }

    /**
     * 日期比较（精确到天），date1等于date2
     *
     * @param date1 日期1
     * @param date2 日期2
     * @return date1等于date2，返回true，否则返回false
     */
    public static boolean equals(Date date1, Date date2) {
        boolean result = false;
        if (0 == compare(date1, date2, ACCURACY_DAY)) {
            result = true;
        }
        return result;
    }

    /**
     * 根据指定规则比较日期，date1晚于date2
     *
     * @param date1    日期1
     * @param date2    日期2
     * @param accuracy 日期精度
     * @return date1晚于date2，返回true，否则返回false
     */
    public static boolean later(Date date1, Date date2, int accuracy) {
        boolean result = false;
        if (1 == compare(date1, date2, accuracy)) {
            result = true;
        }
        return result;
    }

    /**
     * 根据指定规则比较日期，date1早于date2
     *
     * @param date1    日期1
     * @param date2    日期2
     * @param accuracy 日期精度
     * @return date1早于date2，返回true，否则返回false
     */
    public static boolean earlier(Date date1, Date date2, int accuracy) {
        boolean result = false;
        if (-1 == compare(date1, date2, accuracy)) {
            result = true;
        }
        return result;
    }

    /**
     * 根据指定规则比较日期，date1等于date2
     *
     * @param date1    日期1
     * @param date2    日期2
     * @param accuracy 日期精度
     * @return date1等于date2，返回true，否则返回false
     */
    public static boolean equals(Date date1, Date date2, int accuracy) {
        boolean result = false;
        if (0 == compare(date1, date2, accuracy)) {
            result = true;
        }
        return result;
    }

    /**
     * 根据指定规则，比较日期
     *
     * @param date1    日期1
     * @param date2    日期2
     * @param accuracy 日期精度
     * @return int型，date1晚，返回1；date1早，返回-1；相等，返回0
     */
    private static int compare(Date date1, Date date2, int accuracy) {
        String pattern = DEFAULT_DATE_PATTERN;
        switch (accuracy) {
            case ACCURACY_YEAR:
                pattern = ACCURACY_PATTERN_YEAR;
                break;
            case ACCURACY_MONTH:
                pattern = ACCURACY_PATTERN_MONTH;
                break;
            case ACCURACY_DAY:
                pattern = ACCURACY_PATTERN_DAY;
                break;
            case ACCURACY_HOUR:
                pattern = ACCURACY_PATTERN_HOUR;
                break;
            case ACCURACY_MINUTE:
                pattern = ACCURACY_PATTERN_MINUTE;
                break;
            case ACCURACY_SECOND:
                pattern = ACCURACY_PATTERN_SECOND;
                break;
            default:
                break;
        }
        Date formatedDate1 = transDateFormat(date1, pattern);
        Date formatedDate2 = transDateFormat(date2, pattern);
        return formatedDate1.compareTo(formatedDate2);
    }

    /**
     * 根据指定规则，转化日期，如只取年、取年月等
     *
     * @param date    待转化日期
     * @param pattern 日期格式
     * @return 转化后的日期
     */
    public static Date transDateFormat(Date date, String pattern) {
        String dateStr = getDateStr(date, pattern);
        return parseDate(dateStr, pattern);
    }

    /**
     * 返回时定时间的年
     *
     * @param date 日期
     * @return String型的年
     */
    public static String getYearAndMonth(Date date) {
        return getDateStr(date, ACCURACY_PATTERN_MONTH);
    }

    /**
     * 返回时定时间的年
     *
     * @param date 日期
     * @return String型的年
     */
    public static String getYear(Date date) {
        return getDateStr(date, SINGLE_YEAR);
    }

    /**
     * 返回时定时间的月
     *
     * @param date 日期
     * @return String型的月
     */
    public static String getMonth(Date date) {
        return getDateStr(date, SINGLE_MONTH);
    }

    /**
     * 返回时定时间的日
     *
     * @param date 日期
     * @return String型的日
     */
    public static String getDay(Date date) {
        return getDateStr(date, SINGLE_DAY);
    }

    /**
     * 返回时定时间的小时
     *
     * @param date 日期
     * @return String型的小时
     */
    public static String getHour(Date date) {
        return getDateStr(date, SINGLE_HOUR);
    }

    /**
     * 返回时定时间的分
     *
     * @param date 日期
     * @return String型的分
     */
    public static String getMinute(Date date) {
        return getDateStr(date, SINGLE_MINUTE);
    }

    /**
     * 返回时定时间的秒
     *
     * @param date 日期
     * @return String型的秒
     */
    public static String getSecond(Date date) {
        return getDateStr(date, SINGLE_SECOND);
    }

    /**
     * 将时间日期变量的年份变为指定年, 如果日期不存在，则向后一天，如20102月
     *
     * @param date   日期时间变量
     * @param amount 指定年
     * @return 修改后的日期变量
     */
    public static Date setYear(Date date, int amount) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.YEAR, amount);
        return cal.getTime();
    }

    /**
     * 将时间日期变量的月份变为指定月
     *
     * @param date   日期时间变量
     * @param amount 指定月
     * @return 修改后的日期变量
     */
    public static Date setMonth(Date date, int amount) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.MONTH, amount - 1);
        return cal.getTime();
    }

    /**
     * 将时间日期变量的年份变为指定日
     *
     * @param date   日期时间变量
     * @param amount 指定日
     * @return 修改后的日期变量
     */
    public static Date setDay(Date date, int amount) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.DAY_OF_MONTH, amount);
        return cal.getTime();
    }

    /**
     * 将时间日期变量的小时变为指定时
     *
     * @param date   日期时间变量
     * @param amount 指定时
     * @return 修改后的日期变量
     */
    public static Date setHour(Date date, int amount) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.HOUR_OF_DAY, amount);
        return cal.getTime();
    }

    /**
     * 将时间日期变量的分钟变为指定分
     *
     * @param date   日期时间变量
     * @param amount 指定分
     * @return 修改后的日期变量
     */
    public static Date setMinute(Date date, int amount) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.MINUTE, amount);
        return cal.getTime();
    }

    /**
     * 将时间日期变量的秒变为指定秒
     *
     * @param date   日期时间变量
     * @param amount 指定秒
     * @return 修改后的日期变量
     */
    public static Date setSecond(Date date, int amount) {
        Calendar cal = getCalendar(date);
        cal.set(Calendar.SECOND, amount);
        return cal.getTime();
    }

    /**
     * 根据制定单位，计算两个日期之间的天数差
     *
     * @param a 时间点1
     * @param b 时间点2
     * @return 时间差
     */
    public static int getDateDistance(Date a, Date b) {
        return getDateDistance(a, b, ACCURACY_DAY);
    }

    /**
     * 根据制定单位，计算两个日期之间的差
     *
     * @param a    时间点1
     * @param b    时间点2
     * @param unit 时间单位
     * @return 时间差
     */
    public static int getDateDistance(Date a, Date b, int unit) {
        int result = 0;
        if (null != a && null != b) {
            String pattern = null;
            switch (unit) {
                case ACCURACY_HOUR: // '\003'
                    pattern = ACCURACY_PATTERN_HOUR;    // pattern = "yyyyMMddHH";
                    break;
                case ACCURACY_MINUTE: // '\002'
                    pattern = ACCURACY_PATTERN_MINUTE;  // pattern = "yyyyMMddHHmm";
                    break;
                case ACCURACY_SECOND: // '\001'
                    pattern = ACCURACY_PATTERN_SECOND;  // pattern = "yyyyMMddHHmmss";
                    break;
                default:
                    pattern = ACCURACY_PATTERN_DAY; // pattern = "yyyyMMdd";
            }
            Date startDate = transDateFormat(a, pattern);
            Date endDate = transDateFormat(b, pattern);
            if (1 <= unit && 4 >= unit) {
                result = getDistanceByUnit(startDate, endDate, unit);
                return result;
            }
            GregorianCalendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(startDate);
            int startYears = startCalendar.get(Calendar.YEAR);
            int startMonths = startCalendar.get(Calendar.MONTH);
            int startDays = startCalendar.get(Calendar.DAY_OF_MONTH);

            GregorianCalendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(endDate);
            int endYears = endCalendar.get(Calendar.YEAR);
            int endMonths = endCalendar.get(Calendar.MONTH);
            int endDays = endCalendar.get(Calendar.DAY_OF_MONTH);

            int yearBetween = endYears - startYears;
            int monthBetween = endMonths - startMonths;
            if (endDays < startDays
                    && endDays != endCalendar.getActualMaximum(Calendar.DATE)) {
                monthBetween--;
            }
            if (ACCURACY_YEAR == unit) {
                if (monthBetween < 0) {
                    yearBetween--;
                }
                result = yearBetween;
            }
            if (ACCURACY_MONTH == unit) {
                result = (yearBetween * 12 + monthBetween);
            }
        }
        return result;

    }

    /**
     * 内部方法，计算时间点的差距
     *
     * @param startDate 起始时间
     * @param endDate   终止时间
     * @param unit      时间单位
     * @return 时间差
     */
    private static int getDistanceByUnit(Date startDate, Date endDate, int unit) {
        int result = 0;
        long millisecondPerUnit = MILLISECONDS_PER_DAY;
        switch (unit) {
            case ACCURACY_HOUR:
                millisecondPerUnit = MILLISECONDS_PER_HOUR;
                break;
            case ACCURACY_MINUTE:
                millisecondPerUnit = MILLISECONDS_PER_MINUTE;
                break;
            case ACCURACY_SECOND:
                millisecondPerUnit = MILLISECONDS_PER_SECOND;
                break;
            default:
                break;
        }
        long start = startDate.getTime();
        long end = endDate.getTime();
        long distance = end - start;
        result = Integer.valueOf((distance / millisecondPerUnit) + "");
        return result;
    }

    /**
     * 返回指定日期是当年的第几周
     *
     * @param date 指定日期
     * @return 周数（从1开始）
     */
    public static int getWeekOfYear(Date date) {
        return getCalendar(date).get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取指定日期是星期几
     *
     * @param date 指定日期
     * @return 星期日--1; 星期一--2; 星期二--3; 星期三--4; 星期四--5; 星期五--6; 星期六--7;
     */
    public static int getWeekOfDate(Date date) {
        return getCalendar(date).get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 判断指定年份日期的年份是否为闰年
     *
     * @param date 日期
     * @return 闰年ture，非闰年false
     */
    public static boolean isLeapYear(Date date) {
        int year = getCalendar(date).get(Calendar.YEAR);
        return isLeapYear(year);
    }

    /**
     * 判断指定年份日期的年份是否为闰年
     *
     * @param year 年份数字
     * @return 闰年ture，非闰年false
     */
    public static boolean isLeapYear(int year) {
        if ((year % 400) == 0) {
            return true;
        } else if ((year % 4) == 0) {
            if ((year % 100) == 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 按照strFormat格式输出当前时间
     *
     * @param strFormat 格式
     * @return 指定格式的当前系统日期
     */
    public static String getCurrentDate(String strFormat) {
        return getDateStr(getCurrentDate(), strFormat);
    }

    /**
     * 校验日期数据（校验输入值是否为指定的日期格式）
     *
     * @param strDate   要校验的日期
     * @param strFormat 日期格式
     * @return true/false （符合/不符合）
     */
    public static boolean checkDate(String strDate, String strFormat) {
        Date date = null;
        if ((strDate != null) && (strDate.trim().length() != 0)) {
            DateFormat myDateFmt = new SimpleDateFormat(strFormat);
            try {
                date = myDateFmt.parse(strDate);

                if (!strDate.equals(myDateFmt.format(date))) {
                    date = null;
                    return false;
                }
            } catch (ParseException e) {
                date = null;
                return false;
            }
        }
        return true;
    }

    public static Date getStartOfDay(Date date) {
        //set 0不好使，毫秒数对不上
        SimpleDateFormat sf = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
        String dateStr = sf.format(date);
        try {
            return sf.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 两个日期差的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        if (date1 == null || date2 == null) {return 0;}
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        {return days;}
    }

    /**
     * 获取两个时间之内的工作日时间（只去掉两个日期之间的周末时间，法定节假日未去掉）
     *
     * @param start -起始时间，共有3个重载方法，可以传入long型，Long型，与Date型
     * @param end   -结束时间，共有3个重载方法，可以传入long型，Long型，与Date型
     * @return Long型时间差对象
     */
    public static Long getWorkdayTimeInMillis(long start, long end) {
        //如果起始时间大于结束时间，将二者交换
        if (start > end) {
            long temp = start;
            start = end;
            end = temp;
        }
        //根据参数获取起始时间与结束时间的日历类型对象
        Calendar sdate = Calendar.getInstance();
        Calendar edate = Calendar.getInstance();
        sdate.setTimeInMillis(start);
        edate.setTimeInMillis(end);
        //如果两个时间在同一周并且都不是周末日期，则直接返回时间差，增加执行效率
        if (sdate.get(Calendar.YEAR) == edate.get(Calendar.YEAR)
                && sdate.get(Calendar.WEEK_OF_YEAR) == edate.get(Calendar.WEEK_OF_YEAR)
                && sdate.get(Calendar.DAY_OF_WEEK) != 1 && sdate.get(Calendar.DAY_OF_WEEK) != 7
                && edate.get(Calendar.DAY_OF_WEEK) != 1 && edate.get(Calendar.DAY_OF_WEEK) != 7) {
            return new Long(end - start);
        }
        //首先取得起始日期与结束日期的下个周一的日期
        Calendar snextM = getNextMonday(sdate);
        Calendar enextM = getNextMonday(edate);
        //获取这两个周一之间的实际天数
        int days = getDaysBetween(snextM, enextM);
        //获取这两个周一之间的工作日数(两个周一之间的天数肯定能被7整除，并且工作日数量占其中的5/7)
        int workdays = days / 7 * 5;
        //获取开始时间的偏移量
        long scharge = 0;
        if (sdate.get(Calendar.DAY_OF_WEEK) != 1 && sdate.get(Calendar.DAY_OF_WEEK) != 7) {
            //只有在开始时间为非周末的时候才计算偏移量
            scharge += (7 - sdate.get(Calendar.DAY_OF_WEEK)) * 24 * 3600000;
            scharge -= sdate.get(Calendar.HOUR_OF_DAY) * 3600000;
            scharge -= sdate.get(Calendar.MINUTE) * 60000;
            scharge -= sdate.get(Calendar.SECOND) * 1000;
            scharge -= sdate.get(Calendar.MILLISECOND);
        }
        //获取结束时间的偏移量
        long echarge = 0;
        if (edate.get(Calendar.DAY_OF_WEEK) != 1 && edate.get(Calendar.DAY_OF_WEEK) != 7) {
            //只有在结束时间为非周末的时候才计算偏移量
            echarge += (7 - edate.get(Calendar.DAY_OF_WEEK)) * 24 * 3600000;
            echarge -= edate.get(Calendar.HOUR_OF_DAY) * 3600000;
            echarge -= edate.get(Calendar.MINUTE) * 60000;
            echarge -= edate.get(Calendar.SECOND) * 1000;
            echarge -= edate.get(Calendar.MILLISECOND);
        }
        //计算最终结果，具体为：workdays加上开始时间的时间偏移量，减去结束时间的时间偏移量
        return workdays * 24 * 3600000 + scharge - echarge;
    }

    public static Long getWorkdayTimeInMillis(Long start, Long end) {
        return getWorkdayTimeInMillis(start.longValue(), end.longValue());
    }

    public static Long getWorkdayTimeInMillis(Date start, Date end) {
        return getWorkdayTimeInMillis(start.getTime(), end.getTime());
    }

    public static Long getWorkdayTimeInMillis(String start, String end, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date sdate;
        Date edate;
        try {
            sdate = sdf.parse(start);
            edate = sdf.parse(end);
            return getWorkdayTimeInMillis(sdate, edate);
        } catch (ParseException e) {
            log.error(e.getMessage());
            return new Long(0);
        }
    }

    private static Calendar getNextMonday(Calendar cal) {
        int addnum = 9 - cal.get(Calendar.DAY_OF_WEEK);
        if (addnum == 8) {addnum = 1;}//周日的情况
        cal.add(Calendar.DATE, addnum);
        return cal;
    }

    /**
     * 获取两个日期之间的实际天数，支持跨年
     */
    public static int getDaysBetween(Calendar start, Calendar end) {
        if (start.after(end)) {
            Calendar swap = start;
            start = end;
            end = swap;
        }
        int days = end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
        int y2 = end.get(Calendar.YEAR);
        if (start.get(Calendar.YEAR) != y2) {
            start = (Calendar) start.clone();
            do {
                days += start.getActualMaximum(Calendar.DAY_OF_YEAR);
                start.add(Calendar.YEAR, 1);
            } while (start.get(Calendar.YEAR) != y2);
        }
        return days;
    }

    /**
     * 获得本月第一天0点时间
     *
     * @param month
     * @return
     */
    public static long getTimesMonthmorning(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month - 1);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }

    /**
     * 获得本月最后一天23点时间
     *
     * @param month
     * @return
     */
    public static long getTimesMonthnight(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month - 1);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();

    }
}
