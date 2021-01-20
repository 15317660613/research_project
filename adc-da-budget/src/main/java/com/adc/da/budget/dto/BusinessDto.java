package com.adc.da.budget.dto;

import com.adc.da.excel.annotation.Excel;

public class BusinessDto {

    @Excel(name = "业务名称", orderNum = "1")
    private String projectName;

    @Excel(name = "所属部门", orderNum = "2")
    private String deptId;

    @Excel(name = "部门名称", orderNum = "3")
    private String deptName;

    @Excel(name = "业务属性", orderNum = "4")
    private String property;

    @Excel(name = "项目组名称", orderNum = "5")
    private String teamName;

    @Excel(name = "业务经理", orderNum = "6")
    private String pm;

    @Excel(name = "业务周期", orderNum = "7")
    private String cycle;

    @Excel(name = "起始时间", orderNum = "8")
    private String cycleBegin;

    @Excel(name = "结束时间", orderNum = "9")
    private String cycleEnd;

    @Excel(name = "业务性质", orderNum = "10")
    private String propertyId;

    @Excel(name = "当前年实际合同", orderNum = "11")
    private String currentYearDeal;

    @Excel(name = "当前年实际收入", orderNum = "12")
    private String currentYearEstimate;

    @Excel(name = "备注", orderNum = "13")
    private String remark;

    @Excel(name = "1月合同", orderNum = "14")
    private String nextYear1Deal;

    @Excel(name = "1月收入", orderNum = "15")
    private String nextYear1Income;

    @Excel(name = "2月合同", orderNum = "16")
    private String nextYear2Deal;

    @Excel(name = "2月收入", orderNum = "17")
    private String nextYear2Income;

    @Excel(name = "3月合同", orderNum = "18")
    private String nextYear3Deal;

    @Excel(name = "3月收入", orderNum = "19")
    private String nextYear3Income;

    @Excel(name = "4月合同", orderNum = "20")
    private String nextYear4Deal;

    @Excel(name = "4月收入", orderNum = "21")
    private String nextYear4Income;

    @Excel(name = "5月合同", orderNum = "22")
    private String nextYear5Deal;

    @Excel(name = "5月收入", orderNum = "23")
    private String nextYear5Income;

    @Excel(name = "6月合同", orderNum = "24")
    private String nextYear6Deal;

    @Excel(name = "6月收入", orderNum = "25")
    private String nextYear6Income;

    @Excel(name = "7月合同", orderNum = "26")
    private String nextYear7Deal;

    @Excel(name = "7月收入", orderNum = "27")
    private String nextYear7Income;

    @Excel(name = "8月合同", orderNum = "28")
    private String nextYear8Deal;

    @Excel(name = "8月收入", orderNum = "29")
    private String nextYear8Income;

    @Excel(name = "9月合同", orderNum = "30")
    private String nextYear9Deal;

    @Excel(name = "9月收入", orderNum = "31")
    private String nextYear9Income;

    @Excel(name = "10月合同", orderNum = "32")
    private String nextYear10Deal;

    @Excel(name = "10月收入", orderNum = "33")
    private String nextYear10Income;

    @Excel(name = "11月合同", orderNum = "34")
    private String nextYear11Deal;

    @Excel(name = "11月收入", orderNum = "35")
    private String nextYear11Income;

    @Excel(name = "12月合同", orderNum = "36")
    private String nextYear12Deal;

    @Excel(name = "12月收入", orderNum = "37")
    private String nextYear12Income;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getCycleBegin() {
        return cycleBegin;
    }

    public void setCycleBegin(String cycleBegin) {
        this.cycleBegin = cycleBegin;
    }

    public String getCycleEnd() {
        return cycleEnd;
    }

    public void setCycleEnd(String cycleEnd) {
        this.cycleEnd = cycleEnd;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getCurrentYearDeal() {
        return currentYearDeal;
    }

    public void setCurrentYearDeal(String currentYearDeal) {
        this.currentYearDeal = currentYearDeal;
    }

    public String getCurrentYearEstimate() {
        return currentYearEstimate;
    }

    public void setCurrentYearEstimate(String currentYearEstimate) {
        this.currentYearEstimate = currentYearEstimate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNextYear1Deal() {
        return nextYear1Deal;
    }

    public void setNextYear1Deal(String nextYear1Deal) {
        this.nextYear1Deal = nextYear1Deal;
    }

    public String getNextYear1Income() {
        return nextYear1Income;
    }

    public void setNextYear1Income(String nextYear1Income) {
        this.nextYear1Income = nextYear1Income;
    }

    public String getNextYear2Deal() {
        return nextYear2Deal;
    }

    public void setNextYear2Deal(String nextYear2Deal) {
        this.nextYear2Deal = nextYear2Deal;
    }

    public String getNextYear2Income() {
        return nextYear2Income;
    }

    public void setNextYear2Income(String nextYear2Income) {
        this.nextYear2Income = nextYear2Income;
    }

    public String getNextYear3Deal() {
        return nextYear3Deal;
    }

    public void setNextYear3Deal(String nextYear3Deal) {
        this.nextYear3Deal = nextYear3Deal;
    }

    public String getNextYear3Income() {
        return nextYear3Income;
    }

    public void setNextYear3Income(String nextYear3Income) {
        this.nextYear3Income = nextYear3Income;
    }

    public String getNextYear4Deal() {
        return nextYear4Deal;
    }

    public void setNextYear4Deal(String nextYear4Deal) {
        this.nextYear4Deal = nextYear4Deal;
    }

    public String getNextYear4Income() {
        return nextYear4Income;
    }

    public void setNextYear4Income(String nextYear4Income) {
        this.nextYear4Income = nextYear4Income;
    }

    public String getNextYear5Deal() {
        return nextYear5Deal;
    }

    public void setNextYear5Deal(String nextYear5Deal) {
        this.nextYear5Deal = nextYear5Deal;
    }

    public String getNextYear5Income() {
        return nextYear5Income;
    }

    public void setNextYear5Income(String nextYear5Income) {
        this.nextYear5Income = nextYear5Income;
    }

    public String getNextYear6Deal() {
        return nextYear6Deal;
    }

    public void setNextYear6Deal(String nextYear6Deal) {
        this.nextYear6Deal = nextYear6Deal;
    }

    public String getNextYear6Income() {
        return nextYear6Income;
    }

    public void setNextYear6Income(String nextYear6Income) {
        this.nextYear6Income = nextYear6Income;
    }

    public String getNextYear7Deal() {
        return nextYear7Deal;
    }

    public void setNextYear7Deal(String nextYear7Deal) {
        this.nextYear7Deal = nextYear7Deal;
    }

    public String getNextYear7Income() {
        return nextYear7Income;
    }

    public void setNextYear7Income(String nextYear7Income) {
        this.nextYear7Income = nextYear7Income;
    }

    public String getNextYear8Deal() {
        return nextYear8Deal;
    }

    public void setNextYear8Deal(String nextYear8Deal) {
        this.nextYear8Deal = nextYear8Deal;
    }

    public String getNextYear8Income() {
        return nextYear8Income;
    }

    public void setNextYear8Income(String nextYear8Income) {
        this.nextYear8Income = nextYear8Income;
    }

    public String getNextYear9Deal() {
        return nextYear9Deal;
    }

    public void setNextYear9Deal(String nextYear9Deal) {
        this.nextYear9Deal = nextYear9Deal;
    }

    public String getNextYear9Income() {
        return nextYear9Income;
    }

    public void setNextYear9Income(String nextYear9Income) {
        this.nextYear9Income = nextYear9Income;
    }

    public String getNextYear10Deal() {
        return nextYear10Deal;
    }

    public void setNextYear10Deal(String nextYear10Deal) {
        this.nextYear10Deal = nextYear10Deal;
    }

    public String getNextYear10Income() {
        return nextYear10Income;
    }

    public void setNextYear10Income(String nextYear10Income) {
        this.nextYear10Income = nextYear10Income;
    }

    public String getNextYear11Deal() {
        return nextYear11Deal;
    }

    public void setNextYear11Deal(String nextYear11Deal) {
        this.nextYear11Deal = nextYear11Deal;
    }

    public String getNextYear11Income() {
        return nextYear11Income;
    }

    public void setNextYear11Income(String nextYear11Income) {
        this.nextYear11Income = nextYear11Income;
    }

    public String getNextYear12Deal() {
        return nextYear12Deal;
    }

    public void setNextYear12Deal(String nextYear12Deal) {
        this.nextYear12Deal = nextYear12Deal;
    }

    public String getNextYear12Income() {
        return nextYear12Income;
    }

    public void setNextYear12Income(String nextYear12Income) {
        this.nextYear12Income = nextYear12Income;
    }
}
