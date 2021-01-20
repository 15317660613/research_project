package com.adc.da.budget.page;

import com.adc.da.base.page.BasePage;
import com.adc.da.excel.annotation.Excel;

import java.util.List;

/**
 * <b>功能：</b>BUINESS_PERFONMANCE_CONTRACT BuinessPerfonmanceContractEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-08 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuinessPerfonmanceContractEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String deptId;
    private String deptIdOperator = "=";
    private String contractNo;
    private String contractNoOperator = "=";
    private String secondParty;
    private String secondPartyOperator = "=";
    private String startDate;
    private String startDateOperator = "=";
    private String endDate;
    private String endDateOperator = "=";
    private String contractName;
    private String contractNameOperator = "=";
    private String conMoney;
    private String conMoneyOperator = "=";
    private String conState;
    private String conStateOperator = "=";
    private String conType;
    private String conTypeOperator = "=";
    private String conType1;
    private String conType1Operator = "=";
    private String isStock;
    private String isStockOperator = "=";
    private String wkpje;
    private String wkpjeOperator = "=";
    private String wzcje;
    private String wzcjeOperator = "=";
    private String yjbxfpje2018s4;
    private String yjbxfpje2018s4Operator = "=";
    private String yjzfje2018s4;
    private String yjzfje2018s4Operator = "=";
    private String yjbxfpje2019;
    private String yjbxfpje2019Operator = "=";
    private String yjzfje2019;
    private String yjzfje2019Operator = "=";
    private String yjbxfpje2020;
    private String yjbxfpje2020Operator = "=";
    private String yjzfje2020;
    private String yjzfje2020Operator = "=";
    private String zerenren;
    private String zerenrenOperator = "=";
    private String remark;
    private String remarkOperator = "=";
    private String startTimre;
    private String startTimre1;
    private String startTimre2;
    private String startTimreOperator = "=";
    private String updatetTime;
    private String updatetTime1;
    private String updatetTime2;
    private String updatetTimeOperator = "=";
    private String admin;
    private String adminOperator = "=";
    private String islock;
    private String islockOperator = "=";
    private List<String> orgNames;
    private String ifWillApprove;
    private String ifWillApproveOperator = "=";
    private String willTime;
    private String willTimeOperator = "=";
    private String willProjectName;
    private String willProjectNameOperator = "=";
    private String contractBelong;
    private String contractBelongOperator = "=";
    private String hasInvoice;
    private String hasInvoiceOperator = "=";
    private String prepaid;
    private String prepaidOperator = "=";
    private String expenseNov;
    private String expenseNovOperator = "=";
    private String expenditureNov;
    private String expenditureNovOperator = "=";
    private String processInstanceId;
    private String processInstanceIdOperator = "=";
    private String processInstanceIdIsNull = ""; // 三种情况，true：为空， false：不为空，其他：不传条件


    public void setOrgNames(List<String> orgNames) {
        this.orgNames = orgNames;
    }
    
    public List<String> getOrgNames() {
        return orgNames;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceIdOperator() {
        return processInstanceIdOperator;
    }

    public void setProcessInstanceIdOperator(String processInstanceIdOperator) {
        this.processInstanceIdOperator = processInstanceIdOperator;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdOperator() {
        return this.idOperator;
    }

    public void setIdOperator(String idOperator) {
        this.idOperator = idOperator;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptIdOperator() {
        return this.deptIdOperator;
    }

    public void setDeptIdOperator(String deptIdOperator) {
        this.deptIdOperator = deptIdOperator;
    }

    public String getContractNo() {
        return this.contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractNoOperator() {
        return this.contractNoOperator;
    }

    public void setContractNoOperator(String contractNoOperator) {
        this.contractNoOperator = contractNoOperator;
    }

    public String getSecondParty() {
        return this.secondParty;
    }

    public void setSecondParty(String secondParty) {
        this.secondParty = secondParty;
    }

    public String getSecondPartyOperator() {
        return this.secondPartyOperator;
    }

    public void setSecondPartyOperator(String secondPartyOperator) {
        this.secondPartyOperator = secondPartyOperator;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDateOperator() {
        return this.startDateOperator;
    }

    public void setStartDateOperator(String startDateOperator) {
        this.startDateOperator = startDateOperator;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDateOperator() {
        return this.endDateOperator;
    }

    public void setEndDateOperator(String endDateOperator) {
        this.endDateOperator = endDateOperator;
    }

    public String getContractName() {
        return this.contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getContractNameOperator() {
        return this.contractNameOperator;
    }

    public void setContractNameOperator(String contractNameOperator) {
        this.contractNameOperator = contractNameOperator;
    }

    public String getConMoney() {
        return this.conMoney;
    }

    public void setConMoney(String conMoney) {
        this.conMoney = conMoney;
    }

    public String getConMoneyOperator() {
        return this.conMoneyOperator;
    }

    public void setConMoneyOperator(String conMoneyOperator) {
        this.conMoneyOperator = conMoneyOperator;
    }

    public String getConState() {
        return this.conState;
    }

    public void setConState(String conState) {
        this.conState = conState;
    }

    public String getConStateOperator() {
        return this.conStateOperator;
    }

    public void setConStateOperator(String conStateOperator) {
        this.conStateOperator = conStateOperator;
    }

    public String getConType() {
        return this.conType;
    }

    public void setConType(String conType) {
        this.conType = conType;
    }

    public String getConTypeOperator() {
        return this.conTypeOperator;
    }

    public void setConTypeOperator(String conTypeOperator) {
        this.conTypeOperator = conTypeOperator;
    }

    public String getConType1() {
        return this.conType1;
    }

    public void setConType1(String conType1) {
        this.conType1 = conType1;
    }

    public String getConType1Operator() {
        return this.conType1Operator;
    }

    public void setConType1Operator(String conType1Operator) {
        this.conType1Operator = conType1Operator;
    }

    public String getIsStock() {
        return this.isStock;
    }

    public void setIsStock(String isStock) {
        this.isStock = isStock;
    }

    public String getIsStockOperator() {
        return this.isStockOperator;
    }

    public void setIsStockOperator(String isStockOperator) {
        this.isStockOperator = isStockOperator;
    }

    public String getWkpje() {
        return this.wkpje;
    }

    public void setWkpje(String wkpje) {
        this.wkpje = wkpje;
    }

    public String getWkpjeOperator() {
        return this.wkpjeOperator;
    }

    public void setWkpjeOperator(String wkpjeOperator) {
        this.wkpjeOperator = wkpjeOperator;
    }

    public String getWzcje() {
        return this.wzcje;
    }

    public void setWzcje(String wzcje) {
        this.wzcje = wzcje;
    }

    public String getWzcjeOperator() {
        return this.wzcjeOperator;
    }

    public void setWzcjeOperator(String wzcjeOperator) {
        this.wzcjeOperator = wzcjeOperator;
    }

    public String getYjbxfpje2018s4() {
        return this.yjbxfpje2018s4;
    }

    public void setYjbxfpje2018s4(String yjbxfpje2018s4) {
        this.yjbxfpje2018s4 = yjbxfpje2018s4;
    }

    public String getYjbxfpje2018s4Operator() {
        return this.yjbxfpje2018s4Operator;
    }

    public void setYjbxfpje2018s4Operator(String yjbxfpje2018s4Operator) {
        this.yjbxfpje2018s4Operator = yjbxfpje2018s4Operator;
    }

    public String getYjzfje2018s4() {
        return this.yjzfje2018s4;
    }

    public void setYjzfje2018s4(String yjzfje2018s4) {
        this.yjzfje2018s4 = yjzfje2018s4;
    }

    public String getYjzfje2018s4Operator() {
        return this.yjzfje2018s4Operator;
    }

    public void setYjzfje2018s4Operator(String yjzfje2018s4Operator) {
        this.yjzfje2018s4Operator = yjzfje2018s4Operator;
    }

    public String getYjbxfpje2019() {
        return this.yjbxfpje2019;
    }

    public void setYjbxfpje2019(String yjbxfpje2019) {
        this.yjbxfpje2019 = yjbxfpje2019;
    }

    public String getYjbxfpje2019Operator() {
        return this.yjbxfpje2019Operator;
    }

    public void setYjbxfpje2019Operator(String yjbxfpje2019Operator) {
        this.yjbxfpje2019Operator = yjbxfpje2019Operator;
    }

    public String getYjzfje2019() {
        return this.yjzfje2019;
    }

    public void setYjzfje2019(String yjzfje2019) {
        this.yjzfje2019 = yjzfje2019;
    }

    public String getYjzfje2019Operator() {
        return this.yjzfje2019Operator;
    }

    public void setYjzfje2019Operator(String yjzfje2019Operator) {
        this.yjzfje2019Operator = yjzfje2019Operator;
    }

    public String getYjbxfpje2020() {
        return this.yjbxfpje2020;
    }

    public void setYjbxfpje2020(String yjbxfpje2020) {
        this.yjbxfpje2020 = yjbxfpje2020;
    }

    public String getYjbxfpje2020Operator() {
        return this.yjbxfpje2020Operator;
    }

    public void setYjbxfpje2020Operator(String yjbxfpje2020Operator) {
        this.yjbxfpje2020Operator = yjbxfpje2020Operator;
    }

    public String getYjzfje2020() {
        return this.yjzfje2020;
    }

    public void setYjzfje2020(String yjzfje2020) {
        this.yjzfje2020 = yjzfje2020;
    }

    public String getYjzfje2020Operator() {
        return this.yjzfje2020Operator;
    }

    public void setYjzfje2020Operator(String yjzfje2020Operator) {
        this.yjzfje2020Operator = yjzfje2020Operator;
    }

    public String getZerenren() {
        return this.zerenren;
    }

    public void setZerenren(String zerenren) {
        this.zerenren = zerenren;
    }

    public String getZerenrenOperator() {
        return this.zerenrenOperator;
    }

    public void setZerenrenOperator(String zerenrenOperator) {
        this.zerenrenOperator = zerenrenOperator;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkOperator() {
        return this.remarkOperator;
    }

    public void setRemarkOperator(String remarkOperator) {
        this.remarkOperator = remarkOperator;
    }

    public String getStartTimre() {
        return this.startTimre;
    }

    public void setStartTimre(String startTimre) {
        this.startTimre = startTimre;
    }

    public String getStartTimre1() {
        return this.startTimre1;
    }

    public void setStartTimre1(String startTimre1) {
        this.startTimre1 = startTimre1;
    }

    public String getStartTimre2() {
        return this.startTimre2;
    }

    public void setStartTimre2(String startTimre2) {
        this.startTimre2 = startTimre2;
    }

    public String getStartTimreOperator() {
        return this.startTimreOperator;
    }

    public void setStartTimreOperator(String startTimreOperator) {
        this.startTimreOperator = startTimreOperator;
    }

    public String getUpdatetTime() {
        return this.updatetTime;
    }

    public void setUpdatetTime(String updatetTime) {
        this.updatetTime = updatetTime;
    }

    public String getUpdatetTime1() {
        return this.updatetTime1;
    }

    public void setUpdatetTime1(String updatetTime1) {
        this.updatetTime1 = updatetTime1;
    }

    public String getUpdatetTime2() {
        return this.updatetTime2;
    }

    public void setUpdatetTime2(String updatetTime2) {
        this.updatetTime2 = updatetTime2;
    }

    public String getUpdatetTimeOperator() {
        return this.updatetTimeOperator;
    }

    public void setUpdatetTimeOperator(String updatetTimeOperator) {
        this.updatetTimeOperator = updatetTimeOperator;
    }

    public String getAdmin() {
        return this.admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getAdminOperator() {
        return this.adminOperator;
    }

    public void setAdminOperator(String adminOperator) {
        this.adminOperator = adminOperator;
    }

    public String getIslock() {
        return islock;
    }

    public void setIslock(String islock) {
        this.islock = islock;
    }

    public String getIslockOperator() {
        return islockOperator;
    }

    public void setIslockOperator(String islockOperator) {
        this.islockOperator = islockOperator;
    }

    public String getIfWillApprove() {
        return ifWillApprove;
    }

    public void setIfWillApprove(String ifWillApprove) {
        this.ifWillApprove = ifWillApprove;
    }

    public String getIfWillApproveOperator() {
        return ifWillApproveOperator;
    }

    public void setIfWillApproveOperator(String ifWillApproveOperator) {
        this.ifWillApproveOperator = ifWillApproveOperator;
    }

    public String getWillTime() {
        return willTime;
    }

    public void setWillTime(String willTime) {
        this.willTime = willTime;
    }

    public String getWillTimeOperator() {
        return willTimeOperator;
    }

    public void setWillTimeOperator(String willTimeOperator) {
        this.willTimeOperator = willTimeOperator;
    }

    public String getWillProjectName() {
        return willProjectName;
    }

    public void setWillProjectName(String willProjectName) {
        this.willProjectName = willProjectName;
    }

    public String getWillProjectNameOperator() {
        return willProjectNameOperator;
    }

    public void setWillProjectNameOperator(String willProjectNameOperator) {
        this.willProjectNameOperator = willProjectNameOperator;
    }

    public String getContractBelong() {
        return contractBelong;
    }

    public void setContractBelong(String contractBelong) {
        this.contractBelong = contractBelong;
    }

    public String getContractBelongOperator() {
        return contractBelongOperator;
    }

    public void setContractBelongOperator(String contractBelongOperator) {
        this.contractBelongOperator = contractBelongOperator;
    }

    public String getHasInvoice() {
        return hasInvoice;
    }

    public void setHasInvoice(String hasInvoice) {
        this.hasInvoice = hasInvoice;
    }

    public String getHasInvoiceOperator() {
        return hasInvoiceOperator;
    }

    public void setHasInvoiceOperator(String hasInvoiceOperator) {
        this.hasInvoiceOperator = hasInvoiceOperator;
    }

    public String getPrepaid() {
        return prepaid;
    }

    public void setPrepaid(String prepaid) {
        this.prepaid = prepaid;
    }

    public String getPrepaidOperator() {
        return prepaidOperator;
    }

    public void setPrepaidOperator(String prepaidOperator) {
        this.prepaidOperator = prepaidOperator;
    }

    public String getExpenseNov() {
        return expenseNov;
    }

    public void setExpenseNov(String expenseNov) {
        this.expenseNov = expenseNov;
    }

    public String getExpenseNovOperator() {
        return expenseNovOperator;
    }

    public void setExpenseNovOperator(String expenseNovOperator) {
        this.expenseNovOperator = expenseNovOperator;
    }

    public String getExpenditureNov() {
        return expenditureNov;
    }

    public void setExpenditureNov(String expenditureNov) {
        this.expenditureNov = expenditureNov;
    }

    public String getExpenditureNovOperator() {
        return expenditureNovOperator;
    }

    public void setExpenditureNovOperator(String expenditureNovOperator) {
        this.expenditureNovOperator = expenditureNovOperator;
    }

    public String getProcessInstanceIdIsNull() {
        return processInstanceIdIsNull;
    }

    public void setProcessInstanceIdIsNull(String processInstanceIdIsNull) {
        this.processInstanceIdIsNull = processInstanceIdIsNull;
    }
}
