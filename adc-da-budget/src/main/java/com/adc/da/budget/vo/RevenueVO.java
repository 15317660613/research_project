package com.adc.da.budget.vo;

import com.adc.da.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.Date;

@Data
public class RevenueVO extends BaseEntity {


    @ApiModelProperty("ID")
    private String id;

    @NotBlank(message = "所属项目不能为空！")
    @ApiModelProperty("所属项目ID")
    private String parentProjectId;

    @NotBlank(message = "类型不能为空！")
    @ApiModelProperty("类型")
    private String feeType;

    @ApiModelProperty("费用备注")
    private String feeContent;

    @NotEmpty(message = "参与者不能为空！")
    @ApiModelProperty("参与者")
    private String[] participateMembers;

    @Min(value = 0, message = "金额不能小于0")
    @ApiModelProperty("金额")
    private Double revenueAmount;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
