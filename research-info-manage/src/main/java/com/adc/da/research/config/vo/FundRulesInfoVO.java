package com.adc.da.research.config.vo;

import java.util.Date;

/**
 * @description
 * @date 2020/10/26 14:17
 * @auth zhn
 */
public class FundRulesInfoVO {
    /**
     * 经费科目规则id
     */
    private String fundRulesId;

    /**
     * 经费科目详情id
     */
    private String fundDetailsId;

    /**
     *经费模板名称
     */
    private String fundTemplateName;

    /**
     *创建人ID
     */
    private String createUserId;

    /**
     *创建人姓名
     */
    private String createUserName;

    /**
     *经费科目规则创建时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fundRulesCreateTime;

    /**
     *经费科目规则修改时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fundRulesModifyTime;

    /**
     *经费科目规则删除标记
     */
    private Integer fundRulesDelFlag;


    /**
     *预算科目名称
     */
    private String budgetAccountName;

    /**
     *预算封顶经费
     */
    private Double cappedBudget;

    /**
     *经费科目详情是否可修改标记
     */
    private Integer modifiedFlag;

    /**
     *允许调动经费额度
     */
    private Double allowTransferFund;

    /**
     *经费科目详情创建时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fundDetailsCreateTime;

    /**
     *经费科目详情修改时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date fundDetailsModifyTime;

    /**
     *经费科目详情删除标记
     */
    private Integer fundDetailsDelFlag;

    private Integer fundDetailsSort;

    private String fundDetailsParentId;

    public String getFundRulesId() {
        return fundRulesId;
    }

    public void setFundRulesId(String fundRulesId) {
        this.fundRulesId = fundRulesId;
    }

    public String getFundTemplateName() {
        return fundTemplateName;
    }

    public void setFundTemplateName(String fundTemplateName) {
        this.fundTemplateName = fundTemplateName;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getFundRulesCreateTime() {
        return fundRulesCreateTime;
    }

    public void setFundRulesCreateTime(Date fundRulesCreateTime) {
        this.fundRulesCreateTime = fundRulesCreateTime;
    }

    public Date getFundRulesModifyTime() {
        return fundRulesModifyTime;
    }

    public void setFundRulesModifyTime(Date fundRulesModifyTime) {
        this.fundRulesModifyTime = fundRulesModifyTime;
    }

    public Integer getFundRulesDelFlag() {
        return fundRulesDelFlag;
    }

    public void setFundRulesDelFlag(Integer fundRulesDelFlag) {
        this.fundRulesDelFlag = fundRulesDelFlag;
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

    public Date getFundDetailsCreateTime() {
        return fundDetailsCreateTime;
    }

    public void setFundDetailsCreateTime(Date fundDetailsCreateTime) {
        this.fundDetailsCreateTime = fundDetailsCreateTime;
    }

    public Date getFundDetailsModifyTime() {
        return fundDetailsModifyTime;
    }

    public void setFundDetailsModifyTime(Date fundDetailsModifyTime) {
        this.fundDetailsModifyTime = fundDetailsModifyTime;
    }

    public Integer getFundDetailsDelFlag() {
        return fundDetailsDelFlag;
    }

    public void setFundDetailsDelFlag(Integer fundDetailsDelFlag) {
        this.fundDetailsDelFlag = fundDetailsDelFlag;
    }

    public String getFundDetailsId() {
        return fundDetailsId;
    }

    public void setFundDetailsId(String fundDetailsId) {
        this.fundDetailsId = fundDetailsId;
    }

    public Integer getFundDetailsSort() {
        return fundDetailsSort;
    }

    public void setFundDetailsSort(Integer fundDetailsSort) {
        this.fundDetailsSort = fundDetailsSort;
    }

    public String getFundDetailsParentId() {
        return fundDetailsParentId;
    }

    public void setFundDetailsParentId(String fundDetailsParentId) {
        this.fundDetailsParentId = fundDetailsParentId;
    }
}
