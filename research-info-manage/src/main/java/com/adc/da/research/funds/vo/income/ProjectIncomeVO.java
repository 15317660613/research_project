package com.adc.da.research.funds.vo.income;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: yanyujie
 * @Date: 2020/11/12
 * @Description:
 */
@Data
public class ProjectIncomeVO {
    private String id;
    /**
     * 项目编号
     */
    @Excel(name = "项目编号",orderNum = "1",width = 20)
    private String projectCode;
    /**
     * 项目档案名称
     */
    @Excel(name = "项目档案名称",orderNum = "2",width = 20)
    private String projectName;
    /**
     * 部门名称
     */
    @Excel(name = "部门名称",orderNum = "3",width = 20)
    private String deptName;
    /**
     * 科目名称
     */
    @Excel(name = "科目名称",orderNum = "4",width = 20)
    private String subjectName;
    /**
     * 凭证号
     */
    @Excel(name = "凭证号",orderNum = "5",width = 20)
    private String voucherNumber;

    /**
     * 到账金额
     */
    @Excel(name = "到账金额",orderNum = "6",width = 20)
    private Double incomeAmount;

    /**
     * 入账日期
     */
    @Excel(name = "入账日期",orderNum = "7",width = 20,format="yyyy-MM-dd")
    private Date incomeDate;


    private String summary;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getVoucherNumber() {
        return voucherNumber;
    }

    public void setVoucherNumber(String voucherNumber) {
        this.voucherNumber = voucherNumber;
    }

    public Double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(Double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public Date getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(Date incomeDate) {
        this.incomeDate = incomeDate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
