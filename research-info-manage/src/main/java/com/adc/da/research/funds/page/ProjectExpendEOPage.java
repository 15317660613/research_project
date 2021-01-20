package com.adc.da.research.funds.page;

import com.adc.da.base.page.BasePage;

/**
 * <b>功能：</b>RS_PROJECT_EXPEND ProjectExpendEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-12 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectExpendEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String projectId;
    private String projectIdOperator = "=";
    private String projectCode;
    private String projectCodeOperator = "=";
    private String deptId;
    private String deptIdOperator = "=";
    private String subjectName;
    private String subjectNameOperator = "=";
    private String voucherNumber;
    private String voucherNumberOperator = "=";
    private String expendAmount;
    private String expendAmountOperator = "=";
    private String expendDate;
    private String expendDate1;
    private String expendDate2;
    private String expendDateOperator = "=";
    private String summary;
    private String summaryOperator = "=";
    private String createUserId;
    private String createUserIdOperator = "=";
    private String createUserName;
    private String createUserNameOperator = "=";
    private String createTime;
    private String createTime1;
    private String createTime2;
    private String createTimeOperator = "=";
    private String modifyTime;
    private String modifyTime1;
    private String modifyTime2;
    private String modifyTimeOperator = "=";
    private String delFlag;
    private String delFlagOperator = "=";
    private String ext1;
    private String ext1Operator = "=";
    private String ext2;
    private String ext2Operator = "=";
    private String ext3;
    private String ext3Operator = "=";
    private String ext4;
    private String ext4Operator = "=";
    private String ext5;
    private String ext5Operator = "=";

    private String projectName;
    private String deptName;
    private String parentName;
    private String expendType;
    private String expendAmountBegin;
    private String expendAmountEnd;
    private String expendDateBegin;
    private String expendDateEnd;
    private Integer expendTypeNotState;
    private String ids;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
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

    public String getProjectId() {
        return this.projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectIdOperator() {
        return this.projectIdOperator;
    }

    public void setProjectIdOperator(String projectIdOperator) {
        this.projectIdOperator = projectIdOperator;
    }

    public String getProjectCode() {
        return this.projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectCodeOperator() {
        return this.projectCodeOperator;
    }

    public void setProjectCodeOperator(String projectCodeOperator) {
        this.projectCodeOperator = projectCodeOperator;
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

    public String getSubjectName() {
        return this.subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectNameOperator() {
        return this.subjectNameOperator;
    }

    public void setSubjectNameOperator(String subjectNameOperator) {
        this.subjectNameOperator = subjectNameOperator;
    }

    public String getVoucherNumber() {
        return this.voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getVoucherNumberOperator() {
        return this.voucherNumberOperator;
    }

    public void setVoucherNumberOperator(String voucherNumberOperator) {
        this.voucherNumberOperator = voucherNumberOperator;
    }

    public String getExpendAmount() {
        return this.expendAmount;
    }

    public void setExpendAmount(String expendAmount) {
        this.expendAmount = expendAmount;
    }

    public String getExpendAmountOperator() {
        return this.expendAmountOperator;
    }

    public void setExpendAmountOperator(String expendAmountOperator) {
        this.expendAmountOperator = expendAmountOperator;
    }

    public String getExpendDate() {
        return this.expendDate;
    }

    public void setExpendDate(String expendDate) {
        this.expendDate = expendDate;
    }

    public String getExpendDate1() {
        return this.expendDate1;
    }

    public void setExpendDate1(String expendDate1) {
        this.expendDate1 = expendDate1;
    }

    public String getExpendDate2() {
        return this.expendDate2;
    }

    public void setExpendDate2(String expendDate2) {
        this.expendDate2 = expendDate2;
    }

    public String getExpendDateOperator() {
        return this.expendDateOperator;
    }

    public void setExpendDateOperator(String expendDateOperator) {
        this.expendDateOperator = expendDateOperator;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummaryOperator() {
        return this.summaryOperator;
    }

    public void setSummaryOperator(String summaryOperator) {
        this.summaryOperator = summaryOperator;
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

    public String getModifyTime() {
        return this.modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyTime1() {
        return this.modifyTime1;
    }

    public void setModifyTime1(String modifyTime1) {
        this.modifyTime1 = modifyTime1;
    }

    public String getModifyTime2() {
        return this.modifyTime2;
    }

    public void setModifyTime2(String modifyTime2) {
        this.modifyTime2 = modifyTime2;
    }

    public String getModifyTimeOperator() {
        return this.modifyTimeOperator;
    }

    public void setModifyTimeOperator(String modifyTimeOperator) {
        this.modifyTimeOperator = modifyTimeOperator;
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

    public String getExt1() {
        return this.ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt1Operator() {
        return this.ext1Operator;
    }

    public void setExt1Operator(String ext1Operator) {
        this.ext1Operator = ext1Operator;
    }

    public String getExt2() {
        return this.ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt2Operator() {
        return this.ext2Operator;
    }

    public void setExt2Operator(String ext2Operator) {
        this.ext2Operator = ext2Operator;
    }

    public String getExt3() {
        return this.ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getExt3Operator() {
        return this.ext3Operator;
    }

    public void setExt3Operator(String ext3Operator) {
        this.ext3Operator = ext3Operator;
    }

    public String getExt4() {
        return this.ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    public String getExt4Operator() {
        return this.ext4Operator;
    }

    public void setExt4Operator(String ext4Operator) {
        this.ext4Operator = ext4Operator;
    }

    public String getExt5() {
        return this.ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    public String getExt5Operator() {
        return this.ext5Operator;
    }

    public void setExt5Operator(String ext5Operator) {
        this.ext5Operator = ext5Operator;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getExpendType() {
        return expendType;
    }

    public void setExpendType(String expendType) {
        this.expendType = expendType;
    }

    public String getExpendAmountBegin() {
        return expendAmountBegin;
    }

    public void setExpendAmountBegin(String expendAmountBegin) {
        this.expendAmountBegin = expendAmountBegin;
    }

    public String getExpendAmountEnd() {
        return expendAmountEnd;
    }

    public void setExpendAmountEnd(String expendAmountEnd) {
        this.expendAmountEnd = expendAmountEnd;
    }

    public String getExpendDateBegin() {
        return expendDateBegin;
    }

    public void setExpendDateBegin(String expendDateBegin) {
        this.expendDateBegin = expendDateBegin;
    }

    public String getExpendDateEnd() {
        return expendDateEnd;
    }

    public void setExpendDateEnd(String expendDateEnd) {
        this.expendDateEnd = expendDateEnd;
    }

    public Integer getExpendTypeNotState() {
        return expendTypeNotState;
    }

    public void setExpendTypeNotState(Integer expendTypeNotState) {
        this.expendTypeNotState = expendTypeNotState;
    }
}
