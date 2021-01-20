package com.adc.da.budget.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageVO {

    @ApiModelProperty("Id")
    private String id;

    /**
     * 页数
     */
    @ApiModelProperty("页数")
    private int page;
    /**
     * 每页条数
     */
    @ApiModelProperty("条数")
    private int size;
}
