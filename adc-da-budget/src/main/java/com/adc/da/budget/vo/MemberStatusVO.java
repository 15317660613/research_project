package com.adc.da.budget.vo;

import lombok.Data;

@Data
public class MemberStatusVO {
    private Integer type;
    private String name;
    private String position;
    private Integer taskNum;
    private Integer completeTaskNum;
    private String mark;
    private String statistics;

    public MemberStatusVO(Integer type, String name, String position, Integer taskNum, Integer completeTaskNum, String mark, String statistics) {
        this.type = type;
        this.name = name;
        this.position = position;
        this.taskNum = taskNum;
        this.completeTaskNum = completeTaskNum;
        this.mark = mark;
        this.statistics = statistics;
    }

    public MemberStatusVO(String name, String position, Integer taskNum, Integer completeTaskNum, String mark) {
        this.name = name;
        this.position = position;
        this.taskNum = taskNum;
        this.completeTaskNum = completeTaskNum;
        this.mark = mark;
    }
}
