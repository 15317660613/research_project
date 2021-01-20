package com.adc.da.budget.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * <b>功能：</b>TS_BUDGET BudgetEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-10-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class BudgetEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private List<String> deptIds;

    private Set<String> businessIds;

    private String deptIdOperator = "=";
    private String projectName;
    private String projectNameOperator = "=";
    private String teamName;
    private String teamNameOperator = "=";
    private String propertyId;
    private String propertyIdOperator = "=";
    private String pm;
    private String pmOperator = "=";
    private String domainId;
    private String domainIdOperator = "=";

    private Set<String> domainIds;

    private String cycle;
    private String cycleOperator = "=";
    private Date cycleBegin;
    private String cycleBeginOperator = "=";
    private Date cycleEnd;
    private String cycleEndOperator = "=";
    private String currentYear;
    private String currentYearOperator = "=";
    private String currentYearEstimate;
    private String currentYearEstimateOperator = "=";
    private String currentYearDeal;
    private String currentYearDealOperator = "=";
    private String nextYear1Deal;
    private String nextYear1DealOperator = "=";
    private String nextYear1Income;
    private String nextYear1IncomeOperator = "=";
    private String nextYear2Deal;
    private String nextYear2DealOperator = "=";
    private String nextYear2Income;
    private String nextYear2IncomeOperator = "=";
    private String nextYear3Deal;
    private String nextYear3DealOperator = "=";
    private String nextYear3Income;
    private String nextYear3IncomeOperator = "=";
    private String nextYear4Deal;
    private String nextYear4DealOperator = "=";
    private String nextYear4Income;
    private String nextYear4IncomeOperator = "=";
    private String nextYear5Income;
    private String nextYear5IncomeOperator = "=";
    private String nextYear5Deal;
    private String nextYear5DealOperator = "=";
    private String nextYear6Income;
    private String nextYear6IncomeOperator = "=";
    private String nextYear6Deal;
    private String nextYear6DealOperator = "=";
    private String nextYear7Income;
    private String nextYear7IncomeOperator = "=";
    private String nextYear7Deal;
    private String nextYear7DealOperator = "=";
    private String nextYear8Income;
    private String nextYear8IncomeOperator = "=";
    private String nextYear8Deal;
    private String nextYear8DealOperator = "=";
    private String nextYear9Income;
    private String nextYear9IncomeOperator = "=";
    private String nextYear9Deal;
    private String nextYear9DealOperator = "=";
    private String nextYear10Income;
    private String nextYear10IncomeOperator = "=";
    private String nextYear10Deal;
    private String nextYear10DealOperator = "=";
    private String nextYear11Income;
    private String nextYear11IncomeOperator = "=";
    private String nextYear11Deal;
    private String nextYear11DealOperator = "=";
    private String nextYear12Income;
    private String nextYear12IncomeOperator = "=";
    private String nextYear12Deal;
    private String nextYear12DealOperator = "=";
    private String remark;
    private String remarkOperator = "=";
    private String deptName;
    private String deptNameOperator = "=";
    private String property;
    private String propertyOperator = "=";
    private String createUserId;
    private String createUserIdOperator = "=";
    private String projectTeam;
    private String projectTeamOperator = "=";
}
