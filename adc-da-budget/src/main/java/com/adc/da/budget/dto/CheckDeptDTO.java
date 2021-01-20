package com.adc.da.budget.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class CheckDeptDTO {

    /**
     * 项目组成员
     */
    @NotEmpty(message = "项目组成员不能为空！")
    @ApiModelProperty("项目组成员Ids")
    private String[] personIds;
}
