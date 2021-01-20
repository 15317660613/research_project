package com.adc.da.budget.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import org.hibernate.validator.constraints.NotBlank;
import java.util.List;

/**
 * 子业务
 *
 * @author qichunxu
 */
@Data
public class ChildBusinessVO {

    @ApiModelProperty("子业务ID")
    private String id;
    @NotBlank(message = "子业务名称不能为空！")
    @ApiModelProperty("子业务名称")
    private String name;
    @NotBlank(message = "所属业务不能为空！")
    @ApiModelProperty("所属业务ID")
    private String parentBusinessId;

    private List<List<String>> tableArrays;
}
