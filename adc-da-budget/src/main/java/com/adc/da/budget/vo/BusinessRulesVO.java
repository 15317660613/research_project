package com.adc.da.budget.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Administrator
 */
@Data
public class BusinessRulesVO {

    @ApiModelProperty("规则ID")
    private String id;

    @NotBlank(message = "规则名称不能为空！")
    @ApiModelProperty("规则名称")
    private String name;

    @NotBlank(message = "规则内容不能为空！")
    @ApiModelProperty("规则内容")
    private String content;

    @NotBlank(message = "规则分值不能为空！")
    @ApiModelProperty("规则分值")
    private String integral;

    @NotBlank(message = "所属项目不能为空！")
    @ApiModelProperty("所属项目ID")
    private String parentProjectId;
}
