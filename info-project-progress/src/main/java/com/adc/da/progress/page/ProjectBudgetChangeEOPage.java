package com.adc.da.progress.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <b>功能：</b>PR_PROJECT_BUDGET_CHANGE ProjectBudgetChangeEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProjectBudgetChangeEOPage extends BasePage {

    private String id;

    private String idOperator = "=";

    private String personCost;

    private String personCostOperator = "=";

    private String purchaseCost;

    private String purchaseCostOperator = "=";

    private String cooperationCost;

    private String cooperationCostOperator = "=";

    private String otherCost;

    private String otherCostOperator = "=";

    private String amountCount;

    private String amountCountOperator = "=";

    private String changeVersion;

    private String changeVersionOperator = "=";

    private String extInfo1;

    private String extInfo1Operator = "=";

    private String extInfo2;

    private String extInfo2Operator = "=";

    private String extInfo3;

    private String extInfo3Operator = "=";

    private String extInfo4;

    private String extInfo4Operator = "=";

    private String extInfo5;

    private String extInfo5Operator = "=";

    private String extInfo6;

    private String extInfo6Operator = "=";

    private String projectId;

    private String projectIdOperator = "=";

    private String projectName;

    private String projectNameOperator = "=";

}
