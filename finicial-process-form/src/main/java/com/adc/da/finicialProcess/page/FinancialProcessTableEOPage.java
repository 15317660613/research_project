package com.adc.da.finicialProcess.page;

import com.adc.da.base.page.BasePage;

import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>FINANCIAL_PROCESS_TABLE FinancialProcessTableEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-09-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class FinancialProcessTableEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String financialTableName;
    private String financialTableNameOperator = "=";
    private String createUserId;
    private String createUserIdOperator = "=";
    private String createUserName;
    private String createUserNameOperator = "=";
    private String receiveUserId;
    private String receiveUserIdOperator = "=";
    private String receiveUserName;
    private String receiveUserNameOperator = "=";
    private String deptId;
    private String deptIdOperator = "=";
    private String deptName;
    private String deptNameOperator = "=";
    private String createTime;
    private String createTime1;
    private String createTime2;
    private String createTimeOperator = "=";
    private String receiveTime;
    private String receiveTime1;
    private String receiveTime2;
    private String receiveTimeOperator = "=";
    private String state;
    private String stateOperator = "=";
    private String parentId;
    private String parentIdOperator = "=";
    private String fileId;
    private String fileIdOperator = "=";
    private String delFlag;
    private String delFlagOperator = "=";
    private String extInfo1;
    private String extInfo1Operator = "=";
    private String extInfo2;
    private String extInfo2Operator = "=";
    private String extInfo3;
    private String extInfo3Operator = "=";
    private String extInfo4;
    private String extInfo4Operator = "=";
    private String extInfo5;
    private String extInfo5Operator = "=";
    private String extInfo6;
    private String extInfo6Operator = "=";
    private String financialTableType;
    private List<String> receiveUserIdList;

    public List<String> getReceiveUserIdList() {
        return receiveUserIdList;
    }

    public void setReceiveUserIdList(List<String> receiveUserIdList) {
        this.receiveUserIdList = receiveUserIdList;
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

    public String getFinancialTableName() {
        return this.financialTableName;
    }

    public void setFinancialTableName(String financialTableName) {
        this.financialTableName = financialTableName;
    }

    public String getFinancialTableNameOperator() {
        return this.financialTableNameOperator;
    }

    public void setFinancialTableNameOperator(String financialTableNameOperator) {
        this.financialTableNameOperator = financialTableNameOperator;
    }

    public String getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserIdOperator() {
        return this.createUserIdOperator;
    }

    public void setCreateUserIdOperator(String createUserIdOperator) {
        this.createUserIdOperator = createUserIdOperator;
    }

    public String getCreateUserName() {
        return this.createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getCreateUserNameOperator() {
        return this.createUserNameOperator;
    }

    public void setCreateUserNameOperator(String createUserNameOperator) {
        this.createUserNameOperator = createUserNameOperator;
    }

    public String getReceiveUserId() {
        return this.receiveUserId;
    }

    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getReceiveUserIdOperator() {
        return this.receiveUserIdOperator;
    }

    public void setReceiveUserIdOperator(String receiveUserIdOperator) {
        this.receiveUserIdOperator = receiveUserIdOperator;
    }

    public String getReceiveUserName() {
        return this.receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getReceiveUserNameOperator() {
        return this.receiveUserNameOperator;
    }

    public void setReceiveUserNameOperator(String receiveUserNameOperator) {
        this.receiveUserNameOperator = receiveUserNameOperator;
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

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptNameOperator() {
        return this.deptNameOperator;
    }

    public void setDeptNameOperator(String deptNameOperator) {
        this.deptNameOperator = deptNameOperator;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTime1() {
        return this.createTime1;
    }

    public void setCreateTime1(String createTime1) {
        this.createTime1 = createTime1;
    }

    public String getCreateTime2() {
        return this.createTime2;
    }

    public void setCreateTime2(String createTime2) {
        this.createTime2 = createTime2;
    }

    public String getCreateTimeOperator() {
        return this.createTimeOperator;
    }

    public void setCreateTimeOperator(String createTimeOperator) {
        this.createTimeOperator = createTimeOperator;
    }

    public String getReceiveTime() {
        return this.receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getReceiveTime1() {
        return this.receiveTime1;
    }

    public void setReceiveTime1(String receiveTime1) {
        this.receiveTime1 = receiveTime1;
    }

    public String getReceiveTime2() {
        return this.receiveTime2;
    }

    public void setReceiveTime2(String receiveTime2) {
        this.receiveTime2 = receiveTime2;
    }

    public String getReceiveTimeOperator() {
        return this.receiveTimeOperator;
    }

    public void setReceiveTimeOperator(String receiveTimeOperator) {
        this.receiveTimeOperator = receiveTimeOperator;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateOperator() {
        return this.stateOperator;
    }

    public void setStateOperator(String stateOperator) {
        this.stateOperator = stateOperator;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIdOperator() {
        return this.parentIdOperator;
    }

    public void setParentIdOperator(String parentIdOperator) {
        this.parentIdOperator = parentIdOperator;
    }

    public String getFileId() {
        return this.fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileIdOperator() {
        return this.fileIdOperator;
    }

    public void setFileIdOperator(String fileIdOperator) {
        this.fileIdOperator = fileIdOperator;
    }

    public String getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlagOperator() {
        return this.delFlagOperator;
    }

    public void setDelFlagOperator(String delFlagOperator) {
        this.delFlagOperator = delFlagOperator;
    }

    public String getExtInfo1() {
        return this.extInfo1;
    }

    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    public String getExtInfo1Operator() {
        return this.extInfo1Operator;
    }

    public void setExtInfo1Operator(String extInfo1Operator) {
        this.extInfo1Operator = extInfo1Operator;
    }

    public String getExtInfo2() {
        return this.extInfo2;
    }

    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

    public String getExtInfo2Operator() {
        return this.extInfo2Operator;
    }

    public void setExtInfo2Operator(String extInfo2Operator) {
        this.extInfo2Operator = extInfo2Operator;
    }

    public String getExtInfo3() {
        return this.extInfo3;
    }

    public void setExtInfo3(String extInfo3) {
        this.extInfo3 = extInfo3;
    }

    public String getExtInfo3Operator() {
        return this.extInfo3Operator;
    }

    public void setExtInfo3Operator(String extInfo3Operator) {
        this.extInfo3Operator = extInfo3Operator;
    }

    public String getExtInfo4() {
        return this.extInfo4;
    }

    public void setExtInfo4(String extInfo4) {
        this.extInfo4 = extInfo4;
    }

    public String getExtInfo4Operator() {
        return this.extInfo4Operator;
    }

    public void setExtInfo4Operator(String extInfo4Operator) {
        this.extInfo4Operator = extInfo4Operator;
    }

    public String getExtInfo5() {
        return this.extInfo5;
    }

    public void setExtInfo5(String extInfo5) {
        this.extInfo5 = extInfo5;
    }

    public String getExtInfo5Operator() {
        return this.extInfo5Operator;
    }

    public void setExtInfo5Operator(String extInfo5Operator) {
        this.extInfo5Operator = extInfo5Operator;
    }

    public String getExtInfo6() {
        return this.extInfo6;
    }

    public void setExtInfo6(String extInfo6) {
        this.extInfo6 = extInfo6;
    }

    public String getExtInfo6Operator() {
        return this.extInfo6Operator;
    }

    public void setExtInfo6Operator(String extInfo6Operator) {
        this.extInfo6Operator = extInfo6Operator;
    }
    public String getFinancialTableType() {
        return financialTableType;
    }

    public void setFinancialTableType(String financialTableType) {
        this.financialTableType = financialTableType;
    }
}
