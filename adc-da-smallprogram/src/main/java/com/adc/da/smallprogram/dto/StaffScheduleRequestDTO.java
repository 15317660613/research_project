package com.adc.da.smallprogram.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 人员日程请求DTO
 *
 */
@Data
public class StaffScheduleRequestDTO {

    /**
     * 日报创建人Id
     */
    private String createUserId;

    /**
     * 日报创建人Id
     */
    private String orgId;
    /**
     * 日程表开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date scheduleBeginDate;
    /**
     * 日程表结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date scheduleEndDate;
}
