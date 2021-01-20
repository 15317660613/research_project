package com.adc.da.finance.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>F_CASHFLOW_MANAGEMENT CashflowManagementEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class CashflowManagementEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String businessGonfigId;
    private String businessGonfigIdOperator = "=";
    private String cmYear;
    private String cmYearOperator = "=";
    private String cmMonth;
    private String cmMonthOperator = "=";
    private String flowInMoney;
    private String flowInMoneyOperator = "=";
    private String flowOutMoney;
    private String flowOutMoneyOperator = "=";
    private String surplusMoney;
    private String surplusMoneyOperator = "=";
    private String createTime;
    private String createTime1;
    private String createTime2;
    private String createTimeOperator = "=";
    private String updateTime;
    private String updateTime1;
    private String updateTime2;
    private String updateTimeOperator = "=";
    private String delFlag;
    private String delFlagOperator = "=";
    private String businessGonfigName;
    private String businessGonfigNameOperator = "=";

}
