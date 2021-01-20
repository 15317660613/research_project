package com.adc.da.budget.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProjectWorkTimeChartVO {

    private String id;
    private String[] time;
    private Integer[] completeWorkTime;
    private Integer[] executeWorkTime;
    private Integer[] laveWorkTime;
    private Map<String, Map<String, Integer>> memberWorkTimeMap;
    private ProjectAchievementsChartVO projectAchievementsChartVO;
    private List<Double[]> bugDensity;
    private Map<String, Integer[]> financeChart;
    private Map<Integer, List<MemberStatusVO>> memberStatusVO;

    private Boolean isNew;

    public ProjectWorkTimeChartVO() {

    }

    public ProjectWorkTimeChartVO(String id, String[] time, Integer[] completeWorkTime, Integer[] executeWorkTime, Integer[] laveWorkTime, Map<String, Map<String, Integer>> memberWorkTimeMap, ProjectAchievementsChartVO projectAchievementsChartVO, List<Double[]> bugDensity, Map<String, Integer[]> financeChart, Map<Integer, List<MemberStatusVO>> memberStatusVO) {
        this.id = id;
        this.time = time;
        this.completeWorkTime = completeWorkTime;
        this.executeWorkTime = executeWorkTime;
        this.laveWorkTime = laveWorkTime;
        this.memberWorkTimeMap = memberWorkTimeMap;
        this.projectAchievementsChartVO = projectAchievementsChartVO;
        this.bugDensity = bugDensity;
        this.financeChart = financeChart;
        this.memberStatusVO = memberStatusVO;
    }

    public ProjectWorkTimeChartVO(String id, String[] time, Integer[] completeWorkTime, Integer[] executeWorkTime, Integer[] laveWorkTime, Map<String, Map<String, Integer>> memberWorkTimeMap, ProjectAchievementsChartVO projectAchievementsChartVO, List<Double[]> bugDensity, Map<String, Integer[]> financeChart, Map<Integer, List<MemberStatusVO>> memberStatusVO, Boolean isNew) {
        this.id = id;
        this.time = time;
        this.completeWorkTime = completeWorkTime;
        this.executeWorkTime = executeWorkTime;
        this.laveWorkTime = laveWorkTime;
        this.memberWorkTimeMap = memberWorkTimeMap;
        this.projectAchievementsChartVO = projectAchievementsChartVO;
        this.bugDensity = bugDensity;
        this.financeChart = financeChart;
        this.memberStatusVO = memberStatusVO;
        this.isNew = isNew;
    }
}
