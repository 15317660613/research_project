package com.adc.da.knowledge.page;

import com.adc.da.base.page.BasePage;

import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>K_PATENT PatentEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class MyPatentEOPage extends BasePage {

    private String belongUserAddress;
    private String id;
    private String type;
    private String name;
    private String num;
    private String deptId;
    private String deptName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date applyDate;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startApplyDate;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endApplyDate;


    private String belongUserName;
    private String belongUserId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date authorizeDate;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date startAuthorizeDate;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date endAuthorizeDate;
    private String authorizeNum;
    private String certificateNum;
    private String uploadUserId;
    private String designerUserNames;
    private String designerUserIds;
    private String uploadUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;
    private List<String> deptIdList;

    public List<String> getDeptIdList() {
        return deptIdList;
    }

    public void setDeptIdList(List<String> deptIdList) {
        this.deptIdList = deptIdList;
    }

    public String getBelongUserAddress() {
        return this.belongUserAddress;
    }

    public void setBelongUserAddress(String belongUserAddress) {
        this.belongUserAddress = belongUserAddress;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getBelongUserId() {
        return this.belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    public String getAuthorizeNum() {
        return this.authorizeNum;
    }

    public void setAuthorizeNum(String authorizeNum) {
        this.authorizeNum = authorizeNum;
    }

    public String getCertificateNum() {
        return this.certificateNum;
    }

    public void setCertificateNum(String certificateNum) {
        this.certificateNum = certificateNum;
    }

    public String getUploadUserId() {
        return this.uploadUserId;
    }

    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    public String getDesignerUserNames() {
        return this.designerUserNames;
    }

    public void setDesignerUserNames(String designerUserNames) {
        this.designerUserNames = designerUserNames;
    }

    public String getDesignerUserIds() {
        return this.designerUserIds;
    }

    public void setDesignerUserIds(String designerUserIds) {
        this.designerUserIds = designerUserIds;
    }

    public String getUploadUserName() {
        return this.uploadUserName;
    }

    public void setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
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

    public Date getAuthorizeDate() {
        return authorizeDate;
    }

    public void setAuthorizeDate(Date authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    public Date getStartAuthorizeDate() {
        return startAuthorizeDate;
    }

    public void setStartAuthorizeDate(Date startAuthorizeDate) {
        this.startAuthorizeDate = startAuthorizeDate;
    }

    public Date getEndAuthorizeDate() {
        return endAuthorizeDate;
    }

    public void setEndAuthorizeDate(Date endAuthorizeDate) {
        this.endAuthorizeDate = endAuthorizeDate;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }
}
