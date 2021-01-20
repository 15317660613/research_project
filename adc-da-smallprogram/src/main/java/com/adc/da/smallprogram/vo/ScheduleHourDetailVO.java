package com.adc.da.smallprogram.vo;

import com.adc.da.smallprogram.entity.ScheduleDetailEO;
import com.adc.da.smallprogram.entity.ScheduleHourEO;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 返回值
 *
 * @author liuzixi
 * date 2019/4/26
 */
@Data
public class ScheduleHourDetailVO extends ScheduleHourEO {

    List<ScheduleDetailEO> scheduleDetailEOs = new ArrayList<>();

    private int detailType;

    private String destUserId;

    private String[] ids;

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

    private int updateFlag;

    private String  updateUserName;

    private String  updateUserId;


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
