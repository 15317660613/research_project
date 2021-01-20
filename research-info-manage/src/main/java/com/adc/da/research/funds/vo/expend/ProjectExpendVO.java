package com.adc.da.research.funds.vo.expend;

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
public class ProjectExpendVO {
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
     * 科目名称
     */
    @Excel(name = "科目名称",orderNum = "3",width = 20)
    private String subjectName;
    /**
     * 本部
     */
    @Excel(name = "本部",orderNum = "4",width = 20)
    private String parentName;
    /**
     * 部门
     */
    @Excel(name = "部门",orderNum = "5",width = 20)
    private String deptName;

    /**
     * 凭证号
     */
    @Excel(name = "凭证号",orderNum = "6",width = 20)
    private String voucherNumber;

    /**
     * 到账金额
     */
    @Excel(name = "报销金额",orderNum = "7",width = 20)
    private Double expendAmount;

    /**
     * 入账日期
     */
    @Excel(name = "报销日期",orderNum = "8",width = 20,format="yyyy-MM-dd")
    private Date expendDate;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 项目ID
     */
    private String projectId;


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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Double getExpendAmount() {
        return expendAmount;
    }

    public void setExpendAmount(Double expendAmount) {
        this.expendAmount = expendAmount;
    }

    public Date getExpendDate() {
        return expendDate;
    }

    public void setExpendDate(Date expendDate) {
        this.expendDate = expendDate;
    }
}
