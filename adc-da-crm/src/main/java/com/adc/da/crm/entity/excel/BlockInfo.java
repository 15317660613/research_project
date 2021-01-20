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
public class BlockInfo extends BaseEntity {
	
	@Excel(name = "版块", orderNum = "1",mergeVertical=true)
    private String block;
	@Excel(name = "年度", orderNum = "2")
    private String year;
	@Excel(name = "一季度", orderNum = "3")
    private String Q1th;
	@Excel(name = "二季度", orderNum = "4")
    private String Q2nd;
	@Excel(name = "三季度", orderNum = "5")
    private String Q3rd;
	@Excel(name = "四季度", orderNum = "6")
    private String Q4th ;
	@Excel(name = "合计", orderNum = "7")
	private String total;

	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getQ1th() {
		return Q1th;
	}
	public void setQ1th(String q1th) {
		Q1th = q1th;
	}
	public String getQ2nd() {
		return Q2nd;
	}
	public void setQ2nd(String q2nd) {
		Q2nd = q2nd;
	}
	public String getQ3rd() {
		return Q3rd;
	}
	public void setQ3rd(String q3rd) {
		Q3rd = q3rd;
	}
	public String getQ4th() {
		return Q4th;
	}
	public void setQ4th(String q4th) {
		Q4th = q4th;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getBlock() {
		return block;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	 
 
}
