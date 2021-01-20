package com.adc.da.budget.vo;

import lombok.Data;

/**
 * 项目 - 创收支出
 *
 * @author liuzixi
 * date 2019-03-19
 */
@Data
public class ProjectDealIncomeVO {

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 本部
     */
    private String firstDeptId;

    /**
     * 本部
     */
    private String firstDept;

    /**
     * 部门
     */
    private String deptId;

    /**
     * 部门
     */
    private String dept;

    /**
     * 组
     */
    private String groupId;

    /**
     * 组
     */
    private String group;

    /**
     * 项目负责人
     */
    private String projectLeaderId;

    /**
     * 项目负责人
     */
    private String projectLeader;

    /**
     * 支出
     */
    private Integer deal;

    /**
     * 收入
     */
    private Integer income;
}
