package com.adc.da.budget.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/29 13:53
 * @Description:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="项目概览情况统计",description="项目概览情况统计")
public class ProjectSmaryNum {
    //参加的
    @ApiModelProperty(value="任务总数",name="taskTotal")
    private Integer taskTotal;
    @ApiModelProperty(value = "参见的人数",name = "joinPersion")
    private Integer joinPersion;
    @ApiModelProperty(value = "完成的任务数",name = "finisheNum")
    private Integer finisheNum;
    @ApiModelProperty(value = "项目支出总费用",name = "projectExpense")
    private double projectExpense;

    public ProjectSmaryNum() {
    }

    public ProjectSmaryNum(Integer taskTotal, Integer joinPersion, Integer finisheNum, double projectExpense) {
        this.taskTotal = taskTotal;
        this.joinPersion = joinPersion;
        this.finisheNum = finisheNum;
        this.projectExpense = projectExpense;
    }
}
