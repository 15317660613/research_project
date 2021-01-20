package com.adc.da.finance.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>F_RESEARCH_ISSUE ResearchIssueEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class ResearchIssueEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String issueName;
    private String issueNameOperator = "=";
    private String issueNo;
    private String issueNoOperator = "=";
    private String orgId;
    private String orgIdOperator = "=";
    private String status;
    private String statusOperator = "=";
    private String rstatus;
    private String rstatusOperator = "=";
    private String delFlag;
    private String delFlagOperator = "=";
    private String createUserId;
    private String createUserIdOperator = "=";
    private String createTime;
    private String createTime1;
    private String createTime2;
    private String createTimeOperator = "=";
    private String updateUserId;
    private String updateUserIdOperator = "=";
    private String updateTime;
    private String updateTime1;
    private String updateTime2;
    private String updateTimeOperator = "=";
    private String extInfo;
    private String extInfoOperator = "=";
    private String extInfo2;
    private String extInfo2Operator = "=";
    private String extInfo3;
    private String extInfo3Operator = "=";
}