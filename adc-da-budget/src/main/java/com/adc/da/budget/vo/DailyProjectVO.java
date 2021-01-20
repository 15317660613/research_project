package com.adc.da.budget.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

/**
 * created by chenhaidong 2018/11/20
 */
@Data
public class DailyProjectVO {

    @ApiModelProperty("项目日报ID")
    private String id;

    @NotBlank(message = "所属项目不能为空！")
    @ApiModelProperty("所属项目ID")
    private String parentProjectId;

    @NotBlank(message = "所属任务不能为空！")
    @ApiModelProperty("所属任务ID")
    private String taskId;

    @NotBlank(message = "任务工时不能为空！")
    @ApiModelProperty("项目工时")
    private String projectWorkingHours;

    @NotBlank(message = "任务进度不能为空！")
    @ApiModelProperty("任务进度百分比")
    private String taskProgressPercentage;


}
