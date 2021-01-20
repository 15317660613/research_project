package com.adc.da.budget.vo;

import lombok.Data;

/**
 * 业务导出excel
 *
 * @author liuzixi
 * date 2019-03-18
 */
@Data
public class BudgetExportVO {

    /**
     * 一级部门
     */
    private String firstDept;

    /**
     * 部门
     */
    private String dept;

    /**
     * 组
     */
    private String group;

    /**
     * 业务
     */
    private String budget;

    /**
     * 业务经理
     */
    private String budgetManager;

    /**
     * 项目
     */
    private String project;

    /**
     * 项目负责人
     */
    private String projectLeader;
}
