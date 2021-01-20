package com.adc.da.research.config.entity;

import io.swagger.annotations.ApiModelProperty;

/**
 * @description
 * @date 2020/10/28 18:18
 * @auth zhn
 */
public class FundRulesInfoEO {
    @ApiModelProperty("评分规则创建时间")
    private String fundTemplateName;

    @ApiModelProperty("预算科目名称")
    private String budgetAccountName;

    @ApiModelProperty("预算封顶经费")
    private Double cappedBudget;

    @ApiModelProperty("是否可修改")
    private Integer modifiedFlag;

    @ApiModelProperty("允许调动经费额度")
    private Double allowTransferFund;

    @ApiModelProperty("经费科目规则模板ID")
    private String fundRulesId;

    @ApiModelProperty("经费科目详情ID")
    private String fundDetails;

    public String getFundTemplateName() {
        return fundTemplateName;
    }

    public void setFundTemplateName(String fundTemplateName) {
        this.fundTemplateName = fundTemplateName;
    }

    public String getBudgetAccountName() {
        return budgetAccountName;
    }

    public void setBudgetAccountName(String budgetAccountName) {
        this.budgetAccountName = budgetAccountName;
    }

    public Double getCappedBudget() {
        return cappedBudget;
    }

    public void setCappedBudget(Double cappedBudget) {
        this.cappedBudget = cappedBudget;
    }

    public Integer getModifiedFlag() {
        return modifiedFlag;
    }

    public void setModifiedFlag(Integer modifiedFlag) {
        this.modifiedFlag = modifiedFlag;
    }

    public Double getAllowTransferFund() {
        return allowTransferFund;
    }

    public void setAllowTransferFund(Double allowTransferFund) {
        this.allowTransferFund = allowTransferFund;
    }

    public String getFundRulesId() {
        return fundRulesId;
    }

    public void setFundRulesId(String fundRulesId) {
        this.fundRulesId = fundRulesId;
    }

    public String getFundDetails() {
        return fundDetails;
    }

    public void setFundDetails(String fundDetails) {
        this.fundDetails = fundDetails;
    }
}
