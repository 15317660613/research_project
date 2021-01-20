package com.adc.da.budget.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 质量检测
 * @author qichunxu
 */
@Data
public class GualityInspectionVO {

    @ApiModelProperty("ID")
    private String id;

    @NotBlank(message = "所属项目不能为空！")
    @ApiModelProperty("所属项目ID")
    private String parentProjectId;

    @NotBlank(message = "业务规则不能为空！")
    @ApiModelProperty("业务规则ID")
    private String businessRulesId;

    @NotBlank(message = "内容不能为空！")
    @ApiModelProperty("内容")
    private String content;

    @NotBlank(message = "分数不能为空！")
    @ApiModelProperty("分数")
    private String score;
}
