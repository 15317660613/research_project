package com.adc.da.budget.dto;

import com.adc.da.excel.annotation.Excel;

import java.util.Date;

public class BudgetDto {

	@Excel(name = "责任部门", orderNum = "2")
	private String deptName;

	@Excel(name = "项目名称", orderNum = "3")
	private String projectName;

	@Excel(name = "项目组名称", orderNum = "4")
	private String teamName;

	@Excel(name = "项目性质", orderNum = "5")
	private String propertyId;

	@Excel(name = "项目经理", orderNum = "6")
	private String pm;

	@Excel(name = "项目所属平台/领域", orderNum = "7")
	private String domainId;

	@Excel(name = "项目周期", orderNum = "8")
	private String cycle;

	@Excel(name = "本年收入实际预估", orderNum = "9")
	private Double currentYearEstimate;

	@Excel(name = "", orderNum = "12")
	private Double nextYear1Deal;

	@Excel(name = "", orderNum = "13")
	private Double nextYear1Income;

	@Excel(name = "", orderNum = "14")
	private Double nextYear2Deal;

	@Excel(name = "", orderNum = "15")
	private Double nextYear2Income;

	@Excel(name = "", orderNum = "16")
	private Double nextYear3Deal;

	@Excel(name = "", orderNum = "17")
	private Double nextYear3Income;

	@Excel(name = "", orderNum = "18")
	private Double nextYear4Deal;

	@Excel(name = "", orderNum = "19")
	private Double nextYear4Income;

	@Excel(name = "", orderNum = "20")
	private Double nextYear5Deal;

	@Excel(name = "", orderNum = "21")
	private Double nextYear5Income;

	@Excel(name = "", orderNum = "22")
	private Double nextYear6Deal;

	@Excel(name = "", orderNum = "23")
	private Double nextYear6Income;

	@Excel(name = "", orderNum = "24")
	private Double nextYear7Deal;

	@Excel(name = "", orderNum = "25")
	private Double nextYear7Income;

	@Excel(name = "", orderNum = "26")
	private Double nextYear8Deal;

	@Excel(name = "", orderNum = "27")
	private Double nextYear8Income;

	@Excel(name = "", orderNum = "28")
	private Double nextYear9Deal;

	@Excel(name = "", orderNum = "29")
	private Double nextYear9Income;

	@Excel(name = "", orderNum = "30")
	private Double nextYear10Deal;

	@Excel(name = "", orderNum = "31")
	private Double nextYear10Income;

	@Excel(name = "", orderNum = "32")
	private Double nextYear11Deal;

	@Excel(name = "", orderNum = "33")
	private Double nextYear11Income;

	@Excel(name = "", orderNum = "34")
	private Double nextYear12Deal;

	@Excel(name = "", orderNum = "35")
	private Double nextYear12Income;

	@Excel(name = "", orderNum = "36")
	private String remark;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public Double getCurrentYearEstimate() {
		return currentYearEstimate;
	}

	public void setCurrentYearEstimate(Double currentYearEstimate) {
		this.currentYearEstimate = currentYearEstimate;
	}

	public Double getNextYear1Deal() {
		return nextYear1Deal;
	}

	public void setNextYear1Deal(Double nextYear1Deal) {
		this.nextYear1Deal = nextYear1Deal;
	}

	public Double getNextYear1Income() {
		return nextYear1Income;
	}

	public void setNextYear1Income(Double nextYear1Income) {
		this.nextYear1Income = nextYear1Income;
	}

	public Double getNextYear2Deal() {
		return nextYear2Deal;
	}

	public void setNextYear2Deal(Double nextYear2Deal) {
		this.nextYear2Deal = nextYear2Deal;
	}

	public Double getNextYear2Income() {
		return nextYear2Income;
	}

	public void setNextYear2Income(Double nextYear2Income) {
		this.nextYear2Income = nextYear2Income;
	}

	public Double getNextYear3Deal() {
		return nextYear3Deal;
	}

	public void setNextYear3Deal(Double nextYear3Deal) {
		this.nextYear3Deal = nextYear3Deal;
	}

	public Double getNextYear3Income() {
		return nextYear3Income;
	}

	public void setNextYear3Income(Double nextYear3Income) {
		this.nextYear3Income = nextYear3Income;
	}

	public Double getNextYear4Deal() {
		return nextYear4Deal;
	}

	public void setNextYear4Deal(Double nextYear4Deal) {
		this.nextYear4Deal = nextYear4Deal;
	}

	public Double getNextYear4Income() {
		return nextYear4Income;
	}

	public void setNextYear4Income(Double nextYear4Income) {
		this.nextYear4Income = nextYear4Income;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getNextYear5Deal() {
		return nextYear5Deal;
	}

	public void setNextYear5Deal(Double nextYear5Deal) {
		this.nextYear5Deal = nextYear5Deal;
	}

	public Double getNextYear5Income() {
		return nextYear5Income;
	}

	public void setNextYear5Income(Double nextYear5Income) {
		this.nextYear5Income = nextYear5Income;
	}

	public Double getNextYear6Deal() {
		return nextYear6Deal;
	}

	public void setNextYear6Deal(Double nextYear6Deal) {
		this.nextYear6Deal = nextYear6Deal;
	}

	public Double getNextYear6Income() {
		return nextYear6Income;
	}

	public void setNextYear6Income(Double nextYear6Income) {
		this.nextYear6Income = nextYear6Income;
	}

	public Double getNextYear7Deal() {
		return nextYear7Deal;
	}

	public void setNextYear7Deal(Double nextYear7Deal) {
		this.nextYear7Deal = nextYear7Deal;
	}

	public Double getNextYear7Income() {
		return nextYear7Income;
	}

	public void setNextYear7Income(Double nextYear7Income) {
		this.nextYear7Income = nextYear7Income;
	}

	public Double getNextYear8Deal() {
		return nextYear8Deal;
	}

	public void setNextYear8Deal(Double nextYear8Deal) {
		this.nextYear8Deal = nextYear8Deal;
	}

	public Double getNextYear8Income() {
		return nextYear8Income;
	}

	public void setNextYear8Income(Double nextYear8Income) {
		this.nextYear8Income = nextYear8Income;
	}

	public Double getNextYear9Deal() {
		return nextYear9Deal;
	}

	public void setNextYear9Deal(Double nextYear9Deal) {
		this.nextYear9Deal = nextYear9Deal;
	}

	public Double getNextYear9Income() {
		return nextYear9Income;
	}

	public void setNextYear9Income(Double nextYear9Income) {
		this.nextYear9Income = nextYear9Income;
	}

	public Double getNextYear10Deal() {
		return nextYear10Deal;
	}

	public void setNextYear10Deal(Double nextYear10Deal) {
		this.nextYear10Deal = nextYear10Deal;
	}

	public Double getNextYear10Income() {
		return nextYear10Income;
	}

	public void setNextYear10Income(Double nextYear10Income) {
		this.nextYear10Income = nextYear10Income;
	}

	public Double getNextYear11Deal() {
		return nextYear11Deal;
	}

	public void setNextYear11Deal(Double nextYear11Deal) {
		this.nextYear11Deal = nextYear11Deal;
	}

	public Double getNextYear11Income() {
		return nextYear11Income;
	}

	public void setNextYear11Income(Double nextYear11Income) {
		this.nextYear11Income = nextYear11Income;
	}

	public Double getNextYear12Deal() {
		return nextYear12Deal;
	}

	public void setNextYear12Deal(Double nextYear12Deal) {
		this.nextYear12Deal = nextYear12Deal;
	}

	public Double getNextYear12Income() {
		return nextYear12Income;
	}

	public void setNextYear12Income(Double nextYear12Income) {
		this.nextYear12Income = nextYear12Income;
	}
}
