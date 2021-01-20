package com.adc.da.budget.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * created by chenhaidong 2018/11/20
 */
@Data
public class DailyStaffVO {


    @ApiModelProperty("人员日报ID")
    private String id;

    @NotBlank(message = "所属人员不能为空！")
    @ApiModelProperty("人员ID")
    private String staffId;

    @NotBlank(message = "所属项目不能为空！")
    @ApiModelProperty("所属项目ID")
    private String parentProjectId;

    @NotBlank(message = "项目工时不能为空！")
    @ApiModelProperty("项目工时")
    private String projectWorkingHours;


    @ApiModelProperty("工作备注")
    private String workRemark;

}
