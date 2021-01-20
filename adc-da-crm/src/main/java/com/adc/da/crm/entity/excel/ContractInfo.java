package com.adc.da.crm.entity.excel;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;

import java.util.Date;

/**
 * <b>功能：</b>CONTRACT_INFO ContractInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ContractInfo extends BaseEntity {
	
	@Excel(name = "月份", orderNum = "1")
    private String year;
	@Excel(name = "一月", orderNum = "2")
    private String January;
	@Excel(name = "二月", orderNum = "3")
    private String February;
	@Excel(name = "三月", orderNum = "4")
    private String March;
	@Excel(name = "四月", orderNum = "5")
    private String April ;
	@Excel(name = "五月", orderNum = "6")
    private String May ;
	@Excel(name = "六月", orderNum = "7")
    private String June ;
	@Excel(name = "七月", orderNum = "8")
	private String July;
	@Excel(name = "八月", orderNum = "9")
	private String August ;
	@Excel(name = "九月", orderNum = "10")
	private String September ;
	@Excel(name = "十月", orderNum = "11")
	private String October ;
	@Excel(name = "十一月", orderNum = "12")
	private String November ;
	@Excel(name = "十二月", orderNum = "13")
	private String December;
	@Excel(name = "合计", orderNum = "14" )
	private String total;
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getJanuary() {
		return January;
	}
	public void setJanuary(String january) {
		January = january;
	}
	public String getFebruary() {
		return February;
	}
	public void setFebruary(String february) {
		February = february;
	}
	public String getMarch() {
		return March;
	}
	public void setMarch(String march) {
		March = march;
	}
	public String getApril() {
		return April;
	}
	public void setApril(String april) {
		April = april;
	}
	public String getMay() {
		return May;
	}
	public void setMay(String may) {
		May = may;
	}
	public String getJune() {
		return June;
	}
	public void setJune(String june) {
		June = june;
	}
	public String getJuly() {
		return July;
	}
	public void setJuly(String july) {
		July = july;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getAugust() {
		return August;
	}
	public void setAugust(String august) {
		August = august;
	}
	public String getSeptember() {
		return September;
	}
	public void setSeptember(String september) {
		September = september;
	}
	public String getOctober() {
		return October;
	}
	public void setOctober(String october) {
		October = october;
	}
	public String getNovember() {
		return November;
	}
	public void setNovember(String november) {
		November = november;
	}
	public String getDecember() {
		return December;
	}
	public void setDecember(String december) {
		December = december;
	}
	
  //  @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
  //  private Date createdTime;
    
 
}
