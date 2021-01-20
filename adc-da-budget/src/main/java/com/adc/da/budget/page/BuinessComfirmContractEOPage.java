package com.adc.da.budget.page;

import com.adc.da.base.page.BasePage;
import com.adc.da.excel.annotation.Excel;

import java.util.List;

/**
 * <b>功能：</b>BUINESS_COMFIRM_CONTRACT BuinessComfirmContractEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-08 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuinessComfirmContractEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String meetingDate;
    private String meetingDateOperator = "=";
    private String deptId;
    private String deptIdOperator = "=";
    private String projectName;
    private String projectNameOperator = "=";
    private String proMoney;
    private String proMoneyOperator = "=";
    private String proState;
    private String proStateOperator = "=";
    private String isContract;
    private String isContractOperator = "=";
    private String proType1;
    private String proType1Operator = "=";
    private String proType;
    private String proTypeOperator = "=";
    private String isStock;
    private String isStockOperator = "=";
    private String contractNo;
    private String contractNoOperator = "=";
    private String secondParty;
    private String secondPartyOperator = "=";
    private String proMoney2;
    private String proMoney2Operator = "=";
    private String ybxfpje;
    private String ybxfpjeOperator = "=";
    private String yzfxmje;
    private String yzfxmjeOperator = "=";
    private String bxfpje2018s4;
    private String bxfpje2018s4Operator = "=";
    private String zfje2018s4;
    private String zfje2018s4Operator = "=";
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
    private String stratTime;
    private String stratTime1;
    private String stratTime2;
    private String stratTimeOperator = "=";
    private String updateTime;
    private String updateTime1;
    private String updateTime2;
    private String updateTimeOperator = "=";
    private String admin;
    private String adminOperator = "=";
    private String islock;
    private String islockOperator = "=";
    private String ifContinueExecute;
    private String ifContinueExecuteOperator = "=";
    private String contractType;
    private String contractTypeOperator = "=";
    private String contractSigningTime;
    private String contractSigningTimeOperator = "=";
    private String processInstanceId;
    private String processInstanceIdOperator = "=";
    private String processInstanceIdIsNull = ""; // 三种情况，true：为空， false：不为空，其他：不传条件

    private List<String> orgNames;
    
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

    public String getMeetingDate() {
        return this.meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingDateOperator() {
        return this.meetingDateOperator;
    }

    public void setMeetingDateOperator(String meetingDateOperator) {
        this.meetingDateOperator = meetingDateOperator;
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

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectNameOperator() {
        return this.projectNameOperator;
    }

    public void setProjectNameOperator(String projectNameOperator) {
        this.projectNameOperator = projectNameOperator;
    }

    public String getProMoney() {
        return this.proMoney;
    }

    public void setProMoney(String proMoney) {
        this.proMoney = proMoney;
    }

    public String getProMoneyOperator() {
        return this.proMoneyOperator;
    }

    public void setProMoneyOperator(String proMoneyOperator) {
        this.proMoneyOperator = proMoneyOperator;
    }

    public String getProState() {
        return this.proState;
    }

    public void setProState(String proState) {
        this.proState = proState;
    }

    public String getProStateOperator() {
        return this.proStateOperator;
    }

    public void setProStateOperator(String proStateOperator) {
        this.proStateOperator = proStateOperator;
    }

    public String getIsContract() {
        return this.isContract;
    }

    public void setIsContract(String isContract) {
        this.isContract = isContract;
    }

    public String getIsContractOperator() {
        return this.isContractOperator;
    }

    public void setIsContractOperator(String isContractOperator) {
        this.isContractOperator = isContractOperator;
    }

    public String getProType1() {
        return this.proType1;
    }

    public void setProType1(String proType1) {
        this.proType1 = proType1;
    }

    public String getProType1Operator() {
        return this.proType1Operator;
    }

    public void setProType1Operator(String proType1Operator) {
        this.proType1Operator = proType1Operator;
    }

    public String getProType() {
        return this.proType;
    }

    public void setProType(String proType) {
        this.proType = proType;
    }

    public String getProTypeOperator() {
        return this.proTypeOperator;
    }

    public void setProTypeOperator(String proTypeOperator) {
        this.proTypeOperator = proTypeOperator;
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

    public String getProMoney2() {
        return this.proMoney2;
    }

    public void setProMoney2(String proMoney2) {
        this.proMoney2 = proMoney2;
    }

    public String getProMoney2Operator() {
        return this.proMoney2Operator;
    }

    public void setProMoney2Operator(String proMoney2Operator) {
        this.proMoney2Operator = proMoney2Operator;
    }

    public String getYbxfpje() {
        return this.ybxfpje;
    }

    public void setYbxfpje(String ybxfpje) {
        this.ybxfpje = ybxfpje;
    }

    public String getYbxfpjeOperator() {
        return this.ybxfpjeOperator;
    }

    public void setYbxfpjeOperator(String ybxfpjeOperator) {
        this.ybxfpjeOperator = ybxfpjeOperator;
    }

    public String getYzfxmje() {
        return this.yzfxmje;
    }

    public void setYzfxmje(String yzfxmje) {
        this.yzfxmje = yzfxmje;
    }

    public String getYzfxmjeOperator() {
        return this.yzfxmjeOperator;
    }

    public void setYzfxmjeOperator(String yzfxmjeOperator) {
        this.yzfxmjeOperator = yzfxmjeOperator;
    }

    public String getBxfpje2018s4() {
        return this.bxfpje2018s4;
    }

    public void setBxfpje2018s4(String bxfpje2018s4) {
        this.bxfpje2018s4 = bxfpje2018s4;
    }

    public String getBxfpje2018s4Operator() {
        return this.bxfpje2018s4Operator;
    }

    public void setBxfpje2018s4Operator(String bxfpje2018s4Operator) {
        this.bxfpje2018s4Operator = bxfpje2018s4Operator;
    }

    public String getZfje2018s4() {
        return this.zfje2018s4;
    }

    public void setZfje2018s4(String zfje2018s4) {
        this.zfje2018s4 = zfje2018s4;
    }

    public String getZfje2018s4Operator() {
        return this.zfje2018s4Operator;
    }

    public void setZfje2018s4Operator(String zfje2018s4Operator) {
        this.zfje2018s4Operator = zfje2018s4Operator;
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

    public String getStratTime() {
        return this.stratTime;
    }

    public void setStratTime(String stratTime) {
        this.stratTime = stratTime;
    }

    public String getStratTime1() {
        return this.stratTime1;
    }

    public void setStratTime1(String stratTime1) {
        this.stratTime1 = stratTime1;
    }

    public String getStratTime2() {
        return this.stratTime2;
    }

    public void setStratTime2(String stratTime2) {
        this.stratTime2 = stratTime2;
    }

    public String getStratTimeOperator() {
        return this.stratTimeOperator;
    }

    public void setStratTimeOperator(String stratTimeOperator) {
        this.stratTimeOperator = stratTimeOperator;
    }

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTime1() {
        return this.updateTime1;
    }

    public void setUpdateTime1(String updateTime1) {
        this.updateTime1 = updateTime1;
    }

    public String getUpdateTime2() {
        return this.updateTime2;
    }

    public void setUpdateTime2(String updateTime2) {
        this.updateTime2 = updateTime2;
    }

    public String getUpdateTimeOperator() {
        return this.updateTimeOperator;
    }

    public void setUpdateTimeOperator(String updateTimeOperator) {
        this.updateTimeOperator = updateTimeOperator;
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

    public String getIfContinueExecute() {
        return ifContinueExecute;
    }

    public void setIfContinueExecute(String ifContinueExecute) {
        this.ifContinueExecute = ifContinueExecute;
    }

    public String getIfContinueExecuteOperator() {
        return ifContinueExecuteOperator;
    }

    public void setIfContinueExecuteOperator(String ifContinueExecuteOperator) {
        this.ifContinueExecuteOperator = ifContinueExecuteOperator;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getContractTypeOperator() {
        return contractTypeOperator;
    }

    public void setContractTypeOperator(String contractTypeOperator) {
        this.contractTypeOperator = contractTypeOperator;
    }

    public String getContractSigningTime() {
        return contractSigningTime;
    }

    public void setContractSigningTime(String contractSigningTime) {
        this.contractSigningTime = contractSigningTime;
    }

    public String getContractSigningTimeOperator() {
        return contractSigningTimeOperator;
    }

    public void setContractSigningTimeOperator(String contractSigningTimeOperator) {
        this.contractSigningTimeOperator = contractSigningTimeOperator;
    }

    public String getProcessInstanceIdIsNull() {
        return processInstanceIdIsNull;
    }

    public void setProcessInstanceIdIsNull(String processInstanceIdIsNull) {
        this.processInstanceIdIsNull = processInstanceIdIsNull;
    }
}
