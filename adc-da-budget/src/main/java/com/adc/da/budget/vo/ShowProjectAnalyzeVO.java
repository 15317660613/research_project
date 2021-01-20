package com.adc.da.budget.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/22 14:58
 * @Description: 页面展示情况汇总实体
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value="项目概览情况统计",description="项目概览情况统计")
public class ShowProjectAnalyzeVO {
    // 概括总数
    @ApiModelProperty(value = "项目概览",name = "smaryNum")
    private ProjectSmaryNum smaryNum;
    //任务完成情况
    private List<TaskFinishVO> taskStatus;
    // 投入工时表
    private List<Map<String, Object>> workTime;
    // 甘特图数据显示
    List<ProjectStatusFinishedVO> projectStatusFinished;
    // 当前用户是否是项目负责人
    private boolean projectLeader;

    public ShowProjectAnalyzeVO() {
    }

    public ShowProjectAnalyzeVO(ProjectSmaryNum smaryNum, List<TaskFinishVO> taskStatus, List<Map<String, Object>> workTime, List<ProjectStatusFinishedVO> projectStatusFinished, boolean isProjectLeader) {
        this.smaryNum = smaryNum;
        this.taskStatus = taskStatus;
        this.workTime = workTime;
        this.projectStatusFinished = projectStatusFinished;
        this.projectLeader = isProjectLeader;
    }

    public ProjectSmaryNum getSmaryNum() {
        return smaryNum;
    }
}
