package com.adc.da.leaderview.vo;

import lombok.Data;

import java.util.Date;

@Data
public class TaskLeaderViewVO {
    /**
     *
     */
    private String taskId;

    /** 任务名称 */
    private String names;

    /** 任务类型 */
    private String types;

    /**
     *
     */
    private String taskBelongBudgetId;

    /** 所属业务名称 */
    private String projectNames;

    /**
     *
     */
    private String taskBelongProjectId;

    /** 所属任务名称 */
    private String budgetNames;

    /** 任务成员 */
    private String memberNames;

    /** 任务开始事件 */
    private Date planStartTimes;

    /** 任务结束事件 */
    private Date planEndTimes;

    /** 投入工时 */
    private String taskWorkTime;

    /** 任务状态 */
    private String taskStatus;

    /**
     * 任务负责人id
     */
    private String approveUserId;

    /**
     * 任务负责人名称
     */
    private String approveUserName;

}
