package com.adc.da.knowledge.page;

import com.adc.da.base.page.BasePage;

import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>K_COPYRIGHT CopyrightEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class MyCopyrightEOPage extends BasePage {

    private String id;
    private String softwareName;
    private String copyrightType;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date applyDate;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startApplyDate;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endApplyDate;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date registerDate;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startRegisterDate;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endRegisterDate;
    private String registerNum;
    private String state;
    private String belongUserId;
    private String deptName;
    private String deptId;
    private String uploadUserName;
    private String uploadUserId;
    private String updateTime;
    private String num;
    private String belongUserName;
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

    public String getSoftwareName() {
        return this.softwareName;
    }

    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    public String getCopyrightType() {
        return this.copyrightType;
    }

    public void setCopyrightType(String copyrightType) {
        this.copyrightType = copyrightType;
    }

    public String getRegisterNum() {
        return this.registerNum;
    }

    public void setRegisterNum(String registerNum) {
        this.registerNum = registerNum;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBelongUserId() {
        return this.belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
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

    public String getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String num) {
        this.num = num;
    }

     public String getBelongUserName() {
        return this.belongUserName;
    }

    public void setBelongUserName(String belongUserName) {
        this.belongUserName = belongUserName;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getStartApplyDate() {
        return startApplyDate;
    }

    public void setStartApplyDate(Date startApplyDate) {
        this.startApplyDate = startApplyDate;
    }

    public Date getEndApplyDate() {
        return endApplyDate;
    }

    public void setEndApplyDate(Date endApplyDate) {
        this.endApplyDate = endApplyDate;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getStartRegisterDate() {
        return startRegisterDate;
    }

    public void setStartRegisterDate(Date startRegisterDate) {
        this.startRegisterDate = startRegisterDate;
    }

    public Date getEndRegisterDate() {
        return endRegisterDate;
    }

    public void setEndRegisterDate(Date endRegisterDate) {
        this.endRegisterDate = endRegisterDate;
    }
}
