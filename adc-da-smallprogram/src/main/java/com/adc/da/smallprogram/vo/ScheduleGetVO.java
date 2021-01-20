package com.adc.da.smallprogram.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 获取日程的入参
 *
 * @author liuzixi
 * date 2019/4/26
 */
@Data
public class ScheduleGetVO {

    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date scheduleDate;

    /**
     * 上午0下午1
     */
    private String scheduleHour;

    /**
     * 用户id
     */
    private String userId;
}
