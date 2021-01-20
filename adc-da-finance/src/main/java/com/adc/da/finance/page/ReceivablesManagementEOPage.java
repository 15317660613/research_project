package com.adc.da.finance.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>F__RECEIVABLES_MANAGEMENT ReceivablesManagementEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ReceivablesManagementEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String departId;
    private String departIdOperator = "=";
    private String departName;
    private String departNameOperator = "=";
    private String remYear;
    private String remYearOperator = "=";
    private String remMonth;
    private String remMonthOperator = "=";
    private String businessGonfigId;
    private String businessGonfigIdOperator = "=";
    private String remMoney;
    private String remMoneyOperator = "=";
    private String createTime;
    private String createTime1;
    private String createTime2;
    private String createTimeOperator = "=";
    private String createUserId;
    private String createUserIdOperator = "=";
    private String createUserName;
    private String createUserNameOperator = "=";
    private String updateTime;
    private String updateTime1;
    private String updateTime2;
    private String updateTimeOperator = "=";
    private String updateUserId;
    private String updateUserIdOperator = "=";
    private String updateUserName;
    private String updateUserNameOperator = "=";
    private String delFlag;
    private String delFlagOperator = "=";

    private String businessGonfigName;
    private String businessGonfigNameOperator = "=";

}
