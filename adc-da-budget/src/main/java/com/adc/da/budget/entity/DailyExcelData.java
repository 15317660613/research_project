package com.adc.da.budget.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 日报导出字段
 *
 * @author liuzixi
 * @date 2019-03-09
 */
@Getter
@Setter
@Builder
public class DailyExcelData extends Daily {

    /**
     * 业务名称
     */
    private String budgetName;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 子任务名称
     */
    private String childrenTaskName;

    /**
     * 人员
     */
    private String persons;

    /**
     * 审批状态（文字）
     */
    private String status;
}
