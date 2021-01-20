package com.adc.da.research.project.page;

import com.adc.da.base.page.BasePage;

/**
 * <b>功能：</b>RS_BUDGET_DETAIL BudgetDetailEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BudgetDetailEOPage extends BasePage {

    private String id;
    private String idOperator = "=";

    private String parentId;
    private String parentIdOperator = "=";
    private String projectId;
    private String projectIdOperator = "=";
    private String budgetType;
    private String budgetTypeOperator = "=";
    private String budgetDetailTypeId;
    private String budgetDetailTypeIdOperator = "=";
    private String budgetDetailTypeName;
    private String budgetDetailTypeNameOperator = "=";
    private String budgetYear;
    private String budgetYearOperator = "=";
    private String budgetQuarterly;
    private String budgetQuarterlyOperator = "=";
    private String budgetMonth;
    private String budgetMonthOperator = "=";
    private String budgetAmount;
    private String budgetAmountOperator = "=";
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

    private String expendAmountBegin;
    private String expendAmountEnd;
    private String expendType;
    private String projectCode;
    private String deptId;
    private String subjectName;
    private String voucherNumber;
    private String expendAmount;
    private String expendDate;
    private String expendDate1;
    private String expendDate2;
    private String summary;
    private String expendDateBegin;
    private String expendDateEnd;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentIdOperator() {
        return parentIdOperator;
    }

    public void setParentIdOperator(String parentIdOperator) {
        this.parentIdOperator = parentIdOperator;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getExpendDate2() {
        return expendDate2;
    }

    public void setExpendDate2(String expendDate2) {
        this.expendDate2 = expendDate2;
    }

    public String getExpendDate1() {
        return expendDate1;
    }

    public void setExpendDate1(String expendDate1) {
        this.expendDate1 = expendDate1;
    }

    public String getExpendDate() {
        return expendDate;
    }

    public void setExpendDate(String expendDate) {
        this.expendDate = expendDate;
    }

    public String getExpendAmount() {
        return expendAmount;
    }

    public void setExpendAmount(String expendAmount) {
        this.expendAmount = expendAmount;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getExpendType() {
        return expendType;
    }

    public void setExpendType(String expendType) {
        this.expendType = expendType;
    }

    public String getExpendAmountEnd() {
        return expendAmountEnd;
    }

    public void setExpendAmountEnd(String expendAmountEnd) {
        this.expendAmountEnd = expendAmountEnd;
    }

    public String getExpendAmountBegin() {
        return expendAmountBegin;
    }

    public void setExpendAmountBegin(String expendAmountBegin) {
        this.expendAmountBegin = expendAmountBegin;
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

    public String getBudgetType() {
        return this.budgetType;
    }

    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    public String getBudgetTypeOperator() {
        return this.budgetTypeOperator;
    }

    public void setBudgetTypeOperator(String budgetTypeOperator) {
        this.budgetTypeOperator = budgetTypeOperator;
    }

    public String getBudgetDetailTypeId() {
        return this.budgetDetailTypeId;
    }

    public void setBudgetDetailTypeId(String budgetDetailTypeId) {
        this.budgetDetailTypeId = budgetDetailTypeId;
    }

    public String getBudgetDetailTypeIdOperator() {
        return this.budgetDetailTypeIdOperator;
    }

    public void setBudgetDetailTypeIdOperator(String budgetDetailTypeIdOperator) {
        this.budgetDetailTypeIdOperator = budgetDetailTypeIdOperator;
    }

    public String getBudgetDetailTypeName() {
        return this.budgetDetailTypeName;
    }

    public void setBudgetDetailTypeName(String budgetDetailTypeName) {
        this.budgetDetailTypeName = budgetDetailTypeName;
    }

    public String getBudgetDetailTypeNameOperator() {
        return this.budgetDetailTypeNameOperator;
    }

    public void setBudgetDetailTypeNameOperator(String budgetDetailTypeNameOperator) {
        this.budgetDetailTypeNameOperator = budgetDetailTypeNameOperator;
    }

    public String getBudgetYear() {
        return this.budgetYear;
    }

    public void setBudgetYear(String budgetYear) {
        this.budgetYear = budgetYear;
    }

    public String getBudgetYearOperator() {
        return this.budgetYearOperator;
    }

    public void setBudgetYearOperator(String budgetYearOperator) {
        this.budgetYearOperator = budgetYearOperator;
    }

    public String getBudgetQuarterly() {
        return this.budgetQuarterly;
    }

    public void setBudgetQuarterly(String budgetQuarterly) {
        this.budgetQuarterly = budgetQuarterly;
    }

    public String getBudgetQuarterlyOperator() {
        return this.budgetQuarterlyOperator;
    }

    public void setBudgetQuarterlyOperator(String budgetQuarterlyOperator) {
        this.budgetQuarterlyOperator = budgetQuarterlyOperator;
    }

    public String getBudgetMonth() {
        return this.budgetMonth;
    }

    public void setBudgetMonth(String budgetMonth) {
        this.budgetMonth = budgetMonth;
    }

    public String getBudgetMonthOperator() {
        return this.budgetMonthOperator;
    }

    public void setBudgetMonthOperator(String budgetMonthOperator) {
        this.budgetMonthOperator = budgetMonthOperator;
    }

    public String getBudgetAmount() {
        return this.budgetAmount;
    }

    public void setBudgetAmount(String budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public String getBudgetAmountOperator() {
        return this.budgetAmountOperator;
    }

    public void setBudgetAmountOperator(String budgetAmountOperator) {
        this.budgetAmountOperator = budgetAmountOperator;
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

}
