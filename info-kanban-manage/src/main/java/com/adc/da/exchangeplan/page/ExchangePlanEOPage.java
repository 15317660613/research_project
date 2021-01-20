package com.adc.da.exchangeplan.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>DB_EXCHANGE_PLAN ExchangePlanEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ExchangePlanEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String epDate;
    private String epDate1;
    private String epDate2;
    private String epDateOperator = "=";
    private String epForm;
    private String epFormOperator = "=";
    private String epEnterprise;
    private String epEnterpriseOperator = "=";
    private String epExchangeDomain;
    private String epExchangeDomainOperator = "=";
    private String epLeaderName;
    private String epLeaderNameOperator = "=";
    private String createUserId;
    private String createUserIdOperator = "=";
    private String createUserName;
    private String createUserNameOperator = "=";
    private String createTime;
    private String createTime1;
    private String createTime2;
    private String createTimeOperator = "=";
    private String delFlag;
    private String delFlagOperator = "=";
    private String ext1;
    private String ext1Operator = "=";
    private String ext2;
    private String ext2Operator = "=";
    private String ext3;
    private String ext3Operator = "=";
    private String ext4;
    private String ext4Operator = "=";
    private String ext5;
    private String ext5Operator = "=";


}
