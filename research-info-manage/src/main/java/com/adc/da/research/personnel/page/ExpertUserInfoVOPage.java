package com.adc.da.research.personnel.page;

import com.adc.da.base.page.BasePage;

import java.util.Date;

/**
 * @description
 * @date 2020/10/27 16:15
 * @auth zhn
 */
public class ExpertUserInfoVOPage extends BasePage {
    /**
     * 选择人员名称
     */
    private String SelectPersonnel;

    /**
     * 专家人员信息表id(RS_EXPERT_USER_INFO)
     */
    private String id;

    /**
     * 专家类别id
     */
    private String expertTypeId;

    /**
     * 用户id(选择人员id)
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String userName;
    private String userNameOperator = "=";;

    /**
     * 性别
     */
    private String gender;

    /**
     * 身份证号
     */
    private String identityNumber;

    /**
     * 出生日期
     */
    private String birthDate;

    /**
     * 手机号
     */
    private String cellphoneNumber;

    /**
     * 最后学历
     */
    private String lastDegree;
    private String lastDegreeOperator = "=";;

    /**
     * 最后学位
     */
    private String finalDegree;

    /**
     * 职称
     */
    private String jobTitle;
    private String jobTitleOperator = "=";;

    /**
     * 单位名称
     */
    private String companyName;
    private String companyNameOperator = "=";;

    /**
     * 专家组id
     */
    private String expertGroupId;

    /**
     * 专家组名称
     */
    private String expertGroupName;

    /**
     * 个人简历
     */
    private String resume;

    /**
     * 研究方向
     */
    private String researchDirection;

    /**
     * 创建人id
     */
    private String createUserId;

    /**
     * 创建人姓名
     */
    private String createUserName;

    /**
     * 创建时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * 删除标记
     */
    private Integer delFlag;

    private String[] idS;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExpertTypeId() {
        return expertTypeId;
    }

    public void setExpertTypeId(String expertTypeId) {
        this.expertTypeId = expertTypeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public String getLastDegree() {
        return lastDegree;
    }

    public void setLastDegree(String lastDegree) {
        this.lastDegree = lastDegree;
    }

    public String getFinalDegree() {
        return finalDegree;
    }

    public void setFinalDegree(String finalDegree) {
        this.finalDegree = finalDegree;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getExpertGroupId() {
        return expertGroupId;
    }

    public void setExpertGroupId(String expertGroupId) {
        this.expertGroupId = expertGroupId;
    }

    public String getExpertGroupName() {
        return expertGroupName;
    }

    public void setExpertGroupName(String expertGroupName) {
        this.expertGroupName = expertGroupName;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getResearchDirection() {
        return researchDirection;
    }

    public void setResearchDirection(String researchDirection) {
        this.researchDirection = researchDirection;
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

    public String getSelectPersonnel() {
        return SelectPersonnel;
    }

    public void setSelectPersonnel(String selectPersonnel) {
        SelectPersonnel = selectPersonnel;
    }

    public String getUserNameOperator() {
        return userNameOperator;
    }

    public void setUserNameOperator(String userNameOperator) {
        this.userNameOperator = userNameOperator;
    }

    public String getLastDegreeOperator() {
        return lastDegreeOperator;
    }

    public void setLastDegreeOperator(String lastDegreeOperator) {
        this.lastDegreeOperator = lastDegreeOperator;
    }

    public String getJobTitleOperator() {
        return jobTitleOperator;
    }

    public void setJobTitleOperator(String jobTitleOperator) {
        this.jobTitleOperator = jobTitleOperator;
    }

    public String getCompanyNameOperator() {
        return companyNameOperator;
    }

    public void setCompanyNameOperator(String companyNameOperator) {
        this.companyNameOperator = companyNameOperator;
    }

    public String[] getIdS() {
        return idS;
    }

    public void setIdS(String[] idS) {
        this.idS = idS;
    }
}
