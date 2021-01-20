package com.adc.da.research.funds.vo.over;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.util.Date;
import java.util.List;

/**
 * ProjectOver VO类
 */
public class ProjectOverVO {

    private String id;
    private String projectId;
    /**
     * 项目编号
     */
    @Excel(name = "项目编号",orderNum = "1",width = 20)
    private String projectCode;
    private String deptId;
    private String subjectName;
    /**
     * 结转金额
     */
    @Excel(name = "结转金额",orderNum = "6",width = 20)
    private Double overAmount;
    private Double overPercent;
    //开始时间
    private Date overDateBegin;
    //摘要
    private String summary;
    /**
     * 结转状态
     */
    @Excel(name = "结转状态",orderNum = "8",width = 20,replace = {"未结转_0","已结转_1"})
    private Integer overState;
    private String createUserId;
    private String createUserName;
    private Date createTime;
    /**
     * 结转日期
     */
    @Excel(name = "结转日期",orderNum = "7",width = 20,format="yyyy-MM-dd")
    private Date modifyTime;
    private Integer delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    //结束时间
    private Date overDateEnd;


    /**
     * 总金额
     */
    private Double sumAmount;

    /**
     * 技术负责人
     */
    @Excel(name = "技术负责人",orderNum = "5",width = 20)
    private String technicalDirector;
    /**
     * 部门
     */
    @Excel(name = "部门",orderNum = "4",width = 20)
    private String orgName;
    /**
     * 项目类别
     */
    @Excel(name = "项目类别",orderNum = "3",width = 20)
    private String projectTypeName;
    /**
     * 项目名称
     */
    @Excel(name = "项目名称",orderNum = "2",width = 20)
    private String projectName;

    private Double balance;

    private String year;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    private List<ItemDataVO> projectOverVOList;

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Double getOverAmount() {
        return overAmount;
    }

    public void setOverAmount(Double overAmount) {
        this.overAmount = overAmount;
    }

    public Double getOverPercent() {
        return overPercent;
    }

    public void setOverPercent(Double overPercent) {
        this.overPercent = overPercent;
    }

    public Date getOverDateBegin() {
        return overDateBegin;
    }

    public void setOverDateBegin(Date overDateBegin) {
        this.overDateBegin = overDateBegin;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getOverState() {
        return overState;
    }

    public void setOverState(Integer overState) {
        this.overState = overState;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    public String getExt5() {
        return ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    public Date getOverDateEnd() {
        return overDateEnd;
    }

    public void setOverDateEnd(Date overDateEnd) {
        this.overDateEnd = overDateEnd;
    }

    public String getTechnicalDirector() {
        return technicalDirector;
    }

    public void setTechnicalDirector(String technicalDirector) {
        this.technicalDirector = technicalDirector;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Double getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(Double sumAmount) {
        this.sumAmount = sumAmount;
    }

    public List<ItemDataVO> getProjectOverVOList() {
        return projectOverVOList;
    }

    public void setProjectOverVOList(List<ItemDataVO> projectOverVOList) {
        this.projectOverVOList = projectOverVOList;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
