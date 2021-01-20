package com.adc.da.crm.entity.excel;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import com.adc.da.excel.annotation.ExcelEntity;

import java.util.Date;

/**
 * <b>功能：</b>CONTRACT_INFO ContractInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class CompareInfo extends BaseEntity {
	
	@Excel(name = "本部", orderNum = "1",mergeVertical=true)
    private String benbu;
	@Excel(name = "部门", orderNum = "2")
    private String bumen;
	
	@Excel(name = "上年已签订合同尾款（万元）", orderNum = "3")
    private double nopay;
	@Excel(name = "今年已签订合同金额（万元）", orderNum = "4")
    private double contractTotal; 
	
	@Excel(name = "今年跟进中合同金额（万元）", orderNum = "5")
    private String contractThisYear; 
	@Excel(name = "合计", orderNum = "6")
    private double total;

	public String getBenbu() {
		return benbu;
	}

	public void setBenbu(String benbu) {
		this.benbu = benbu;
	}

	public String getBumen() {
		return bumen;
	}

	public void setBumen(String bumen) {
		this.bumen = bumen;
	}

	public double getNopay() {
		return nopay;
	}

	public void setNopay(double nopay) {
		this.nopay = nopay;
	}

	public double getContractTotal() {
		return contractTotal;
	}

	public void setContractTotal(double contractTotal) {
		this.contractTotal = contractTotal;
	}

	public String getContractThisYear() {
		return contractThisYear;
	}

	public void setContractThisYear(String contractThisYear) {
		this.contractThisYear = contractThisYear;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

 
}
