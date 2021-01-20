package com.adc.da.budget.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * created by chenhaidong 2019/1/17
 */
@Data
public class DailyConditionDTO {
    /**
     * 第几页
     */
    private Integer page;
    /**
     * 每页数量
     */
    private Integer size;
    /**
     * 日报创建时间开始
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date dailyCreateTimeBegin;
    /**
     * 日报创建时间结束
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date dailyCreateTimeEnd;

    //业务id集合
    private List<String> budgetIds;
    //项目id集合
    private List<String> projectIds;
    //任务id集合
    private List<String> taskIds;
    //子任务id集合
    private List<String> childrenTaskIds;
}
