package com.adc.da.research.funds.vo.fundsList;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: yanyujie
 * @Date: 2020/11/10
 * @Description:
 */
@Data
public class FundsListVO {
    private String id;
    /**
     * 项目编号
     */
    @Excel(name = "项目编号",orderNum = "1",width = 10)
    private String projectCode;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称",orderNum = "2",width = 20)
    private String projectName;
    /**
     * 项目类别ID
     */
    @Excel(name = "项目类别",orderNum = "3",width = 10)
    private String projectTypeName;

    //课题负责人
    @Excel(name = "课题负责人",orderNum = "4",width = 10)
    private String subjectDirector;
    /**
     * 技术负责人
     */
    @Excel(name = "项目负责人",orderNum = "5",width = 10)
    private String technicalDirector;
    /**
     * 申报单位id
     */
    @Excel(name = "申报单位",orderNum = "6",width = 20)
    private String reportingUnitName;

    /**
     * 总经费
     */
    @Excel(name = "总经费(万元)",orderNum = "7",width = 8)
    private Double totalFunding;

    /**
     * 国拨经费
     */
    @Excel(name = "国拨经费",orderNum = "8",width = 8)
    private Double stateFunding;

    @Excel(name = "累计到账",groupName = "已到账经费",orderNum = "9",width = 8,fixedIndex=7)
    private Double stateIncomeSum;

    @Excel(name = "本年度到账",groupName = "已到账经费",orderNum = "10",width = 8,fixedIndex=8)
    private Double stateIncomeDate;

    /**
     * 国拨经费支出
     */
    @Excel(name = "自筹经费支出",orderNum = "11",width = 8)
    private Double stateExpend;


    /**
     * 自筹经费
     */
    @Excel(name = "自筹经费",orderNum = "12",width = 8)
    private Double selfFunded;

    /**
     * 自筹经费支出
     */
    @Excel(name = "自筹经费支出",orderNum = "13",width = 8)
    private Double selfExpend;

    /**
     * 中汽中心经费
     */
    @Excel(name = "中汽中心经费",orderNum = "14",width = 8)
    private Double centerBudgetApply;

    private String budgetsum;


    /**
     * 立项时间
     */
    @Excel(name = "立项日期",orderNum = "15",width = 8,format="yyyy-MM-dd")
    private Date projectTime;

    private String projectTypeId;
    private String reportingUnitId;

    public String getBudgetsum() {
        return budgetsum;
    }

    public void setBudgetsum(String budgetsum) {
        this.budgetsum = budgetsum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getSubjectDirector() {
        return subjectDirector;
    }

    public void setSubjectDirector(String subjectDirector) {
        this.subjectDirector = subjectDirector;
    }

    public String getTechnicalDirector() {
        return technicalDirector;
    }

    public void setTechnicalDirector(String technicalDirector) {
        this.technicalDirector = technicalDirector;
    }

    public String getReportingUnitName() {
        return reportingUnitName;
    }

    public void setReportingUnitName(String reportingUnitName) {
        this.reportingUnitName = reportingUnitName;
    }

    public Double getTotalFunding() {
        return totalFunding;
    }

    public void setTotalFunding(Double totalFunding) {
        this.totalFunding = totalFunding;
    }

    public Double getStateFunding() {
        return stateFunding;
    }

    public void setStateFunding(Double stateFunding) {
        this.stateFunding = stateFunding;
    }

    public Double getStateIncomeSum() {
        return stateIncomeSum;
    }

    public void setStateIncomeSum(Double stateIncomeSum) {
        this.stateIncomeSum = stateIncomeSum;
    }

    public Double getStateIncomeDate() {
        return stateIncomeDate;
    }

    public void setStateIncomeDate(Double stateIncomeDate) {
        this.stateIncomeDate = stateIncomeDate;
    }

    public Double getStateExpend() {
        return stateExpend;
    }

    public void setStateExpend(Double stateExpend) {
        this.stateExpend = stateExpend;
    }

    public Double getSelfFunded() {
        return selfFunded;
    }

    public void setSelfFunded(Double selfFunded) {
        this.selfFunded = selfFunded;
    }

    public Double getSelfExpend() {
        return selfExpend;
    }

    public void setSelfExpend(Double selfExpend) {
        this.selfExpend = selfExpend;
    }

    public Double getCenterBudgetApply() {
        return centerBudgetApply;
    }

    public void setCenterBudgetApply(Double centerBudgetApply) {
        this.centerBudgetApply = centerBudgetApply;
    }

    public Date getProjectTime() {
        return projectTime;
    }

    public void setProjectTime(Date projectTime) {
        this.projectTime = projectTime;
    }

    public String getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public String getReportingUnitId() {
        return reportingUnitId;
    }

    public void setReportingUnitId(String reportingUnitId) {
        this.reportingUnitId = reportingUnitId;
    }
}
