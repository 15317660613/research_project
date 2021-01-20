package com.adc.da.smallprogram.vo;

import com.adc.da.smallprogram.entity.ScheduleHourEO;
import lombok.Data;

import java.util.Date;

/**
 * 返回值
 *
 * @author liuzixi
 * date 2019/4/26
 */
@Data
public class ScheduleVO extends ScheduleHourEO {

    /**
     * 前一条日程日期
     */
    private Date preDate;

    /**
     * 前一条日程时间
     */
    private String preHour;

    /**
     * 后一条日程日期
     */
    private Date postDate;

    /**
     * 后一条日程时间
     */
    private String postHour;

    public void setScheduleDateHour(Date scheduleDate, String scheduleHour) {
        setScheduleDate(scheduleDate);
        setScheduleHour(scheduleHour);
        // 计算
        int hour = Integer.parseInt(scheduleHour);
        long oneDay = 1000L* 60 * 60 * 24;
        Date preDate;
        Date postDate;
        String preHour;
        String postHour;
        if (hour == 0) {
            preDate = new Date(scheduleDate.getTime() - oneDay);
            postDate = scheduleDate;
            preHour = "1";
            postHour = "1";
        } else {
            preDate = scheduleDate;
            postDate = new Date(scheduleDate.getTime() + oneDay);
            preHour = "0";
            postHour = "0";
        }
        setPreDate(preDate);
        setPostDate(postDate);
        setPreHour(preHour);
        setPostHour(postHour);
    }
}
