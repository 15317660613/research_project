package com.adc.da.exchangeplan.dto;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>DB_EXCHANGE_PLAN ExchangePlanEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ExchangePlanDto extends BaseEntity {


    /**
     * 日期
     */
    @Excel(name = "日期",orderNum = "1")
    private Date epDate;

    @Excel(name = "日期（看板展示）",orderNum = "2")
    private String ext2;
    /**
     * 形式
     */
    @Excel(name = "形式",orderNum = "3")
    private String epForm;
    /**
     * 企业
     */
    @Excel(name = "企业",orderNum = "4")
    private String epEnterprise;
    /**
     * 交流领域
     */
    @Excel(name = "交流领域",orderNum = "5")
    private String epExchangeDomain;
    /**
     * 建议领导
     */
    @Excel(name = "建议领导",orderNum = "6")
    private String epLeaderName;



}
