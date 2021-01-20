package com.adc.da.budget.vo;

import lombok.Data;

/**
 * @author ：mengqingchen
 * @date ：Created in 2019/2/23 16:23
 * @description：项目绩效图
 * @modified By：
 */
@Data
public class ProjectAchievementsChartVO {
    private String[] names;
    private Integer[] firstQuarter;
    private Integer[] secondQuarter;
    private Integer[] thirdQuarter;
    private Integer[] fourthQuarter;
    private Integer[] aveQuarter;

    public ProjectAchievementsChartVO(String[] names,Integer[] firstQuarter,Integer[] secondQuarter,Integer[] thirdQuarter,Integer[] fourthQuarter,Integer[] aveQuarter){
        this.names = names;
        this.firstQuarter = firstQuarter;
        this.secondQuarter = secondQuarter;
        this.thirdQuarter = thirdQuarter;
        this.fourthQuarter = fourthQuarter;
        this.aveQuarter = aveQuarter;
    }

    public ProjectAchievementsChartVO() {

    }
}
