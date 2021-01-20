package com.adc.da.carsales.dto;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>DB_CAR_SALES CarSalesEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-03 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class CarSalesDto extends BaseEntity {


    /** 企业名称 **/
    @Excel(name = "企业名称",orderNum = "1")
    private String enterpriseName;
    /** 月度销量 **/
    @Excel(name = "月度销量",orderNum = "2")
    private Integer monthSales;
    /** 增长率 **/
    @Excel(name = "增长率",orderNum = "3")
    private String growthRate;

}
