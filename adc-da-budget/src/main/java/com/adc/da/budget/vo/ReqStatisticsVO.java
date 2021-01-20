package com.adc.da.budget.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * @description: 财务系统工时统计前台参数
 * @author: qichunxu
 * @create: 2019-03-21 10:04
 **/
@Data
public class ReqStatisticsVO {

    /**
     * id
     */
    @ApiModelProperty("Id")
    private String id;
    /**
     * week
     * month
     * season
     * year
     */
    @NotBlank(message = "维度类型不能为空")
    @ApiModelProperty("维度")
    private String type;

    /**
     * 机构id
     */
    private String orgId;
    /**
     * 时间段类型
     */
    private String timeType;

    /**
     * 用户ID
     */
    private String userId;
}
