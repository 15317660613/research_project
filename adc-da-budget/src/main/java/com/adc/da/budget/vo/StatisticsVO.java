package com.adc.da.budget.vo;

import lombok.Data;

/**
 * @description: 统计VO
 * @author: qichunxu
 * @create: 2019-03-21 13:01
 **/
@Data
public class StatisticsVO implements Comparable<StatisticsVO> {

    /**
     * id
     */
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 工时
     */
    private Double workTime;
    /**
     * 工时占比
     */
    private Double Percentage;

    /**
     *  人日
     */
    private Double manDay;


    public StatisticsVO(String id, String name, Double workTime, Double manDay) {
        this.id = id;
        this.name = name;
        this.workTime = workTime;
        this.manDay = manDay;
    }

    @Override
    public int compareTo(StatisticsVO o) {
        return  o.getWorkTime().compareTo(this.getWorkTime());
    }
}
