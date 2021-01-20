package com.adc.da.budget.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 项目日程请求DTO
 * created by chenhaidong 2018/11/22
 */
@Data
public class ProjectScheduleRequestDTO {
    /**
     * 日程表类型  1-月历  2-周历  3-列表
     */
//    private Integer scheduleType;
    /**
     * 项目名搜索
     */
//    private String projectName;
    /**
     * 日程表开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date scheduleBeginDate;
    /**
     * 日程表结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date scheduleEndDate;
    /**
     * 项目id
     */
    private String projectId;

    public Date getScheduleBeginDate() {
        return scheduleBeginDate;
    }

    public void setScheduleBeginDate(Date scheduleBeginDate) {
        this.scheduleBeginDate = scheduleBeginDate;
    }

    public Date getScheduleEndDate() {
        return scheduleEndDate;
    }

    public void setScheduleEndDate(Date scheduleEndDate) {
        this.scheduleEndDate = scheduleEndDate;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
