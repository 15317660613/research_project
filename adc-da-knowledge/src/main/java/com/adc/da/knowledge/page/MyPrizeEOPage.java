package com.adc.da.knowledge.page;

import com.adc.da.base.page.BasePage;

import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>K_PRIZE PrizeEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class MyPrizeEOPage extends BasePage {

    private String id;
    private String num;
    private String prizeName;
    private String projectName;
    private String level;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date prizeTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startPrizeTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endPrizeTime;
    private String issuedDept;
    private String belongedUserName;
    private String belongedUserId;
    private String deptName;
    private String deptId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String uploadUserName;
    private String uploadUserId;
    private String prizeAbstract;
    private List<String> deptIdList;

    public List<String> getDeptIdList() {
        return deptIdList;
    }

    public void setDeptIdList(List<String> deptIdList) {
        this.deptIdList = deptIdList;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPrizeName() {
        return this.prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIssuedDept() {
        return this.issuedDept;
    }

    public void setIssuedDept(String issuedDept) {
        this.issuedDept = issuedDept;
    }

    public String getBelongedUserName() {
        return this.belongedUserName;
    }

    public void setBelongedUserName(String belongedUserName) {
        this.belongedUserName = belongedUserName;
    }

    public String getBelongedUserId() {
        return this.belongedUserId;
    }

    public void setBelongedUserId(String belongedUserId) {
        this.belongedUserId = belongedUserId;
    }

    public String getDeptName() {
        return this.deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptId() {
        return this.deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

   public String getUploadUserName() {
        return this.uploadUserName;
    }

    public void setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
    }

    public String getUploadUserId() {
        return this.uploadUserId;
    }

    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public Date getPrizeTime() {
        return prizeTime;
    }

    public void setPrizeTime(Date prizeTime) {
        this.prizeTime = prizeTime;
    }

    public Date getStartPrizeTime() {
        return startPrizeTime;
    }

    public void setStartPrizeTime(Date startPrizeTime) {
        this.startPrizeTime = startPrizeTime;
    }

    public Date getEndPrizeTime() {
        return endPrizeTime;
    }

    public void setEndPrizeTime(Date endPrizeTime) {
        this.endPrizeTime = endPrizeTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getPrizeAbstract() {
        return prizeAbstract;
    }

    public void setPrizeAbstract(String prizeAbstract) {
        this.prizeAbstract = prizeAbstract;
    }
}
