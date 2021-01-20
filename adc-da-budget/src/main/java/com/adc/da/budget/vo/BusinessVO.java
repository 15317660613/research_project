package com.adc.da.budget.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/19 16:21
 * @Description:
 */
@Data
public class BusinessVO {

    @ApiModelProperty("业务ID")
    private String id;

    @NotBlank(message = "业务名称不能为空！")
    @ApiModelProperty("业务名称")
    private String name;

    @ApiModelProperty("父ID")
    private String parentId;

}
