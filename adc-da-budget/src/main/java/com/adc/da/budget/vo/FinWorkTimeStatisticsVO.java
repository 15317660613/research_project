package com.adc.da.budget.vo;

import com.adc.da.budget.entity.StatisticsEntity;
import lombok.Data;

import java.util.List;

/**
 * @description:财务系统工时统计
 * @author: qichunxu
 * @create: 2019-03-21 13:04
 **/
@Data
public class FinWorkTimeStatisticsVO {

    /**
     * 任务总工时
     */
    private Double taskAllWorkTime;
    /**
     * 任务列表
     */
    private List<StatisticsVO> taskStatistics;
    /**
     * 项目总工时
     */
    private Double projectAllWorkTime;
    /**
     * 项目列表
     */
    private List<StatisticsVO> projectStatistics;
    /**
     * 业务总工时
     */
    private Double businessAllWorkTime;
    /**
     * 业务列表
     */
    private List<StatisticsVO> businessStatistics;
    /**
     * 机构工时总计
     */
    private Integer orgAllWorkTime;
    /**
     * 机构工时统计列表信息
     */
    private List<StatisticsEntity> orgStatistics;



}
