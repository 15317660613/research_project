package com.adc.da.research.personnel.vo;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @date 2020/10/27 10:23
 * @auth zhn
 */
public class ExpertUserInfoVO extends BaseEntity {

    /**
     * 选择人员名称
     */
    private String selectPersonnel;

    /**
     * 专家人员信息表id(RS_EXPERT_USER_INFO)
     */
    private String id;

    /**
     * 专家类别id
     */
    private String expertTypeId;

    /**
     * 专家类别id
     */
    private String expertTypeName;

    /**
     * 用户id(选择人员id)
     */
    private String userId;

    /**
     * 用户姓名
     */
    private String userName;

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

    /**
     * 最后学位
     */
    private String finalDegree;

    /**
     * 职称
     */
    private String jobTitle;

    /**
     * 单位名称
     */
    private String companyName;

    /**
     * 专家组id集合
     */
    private List<String> expertGroupId;

    /**
     * 专家组名称集合
     */
    private List<String> expertGroupName;

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

    public List<String> getExpertGroupId() {
        return expertGroupId;
    }

    public void setExpertGroupId(List<String> expertGroupId) {
        this.expertGroupId = expertGroupId;
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
        return selectPersonnel;
    }

    public void setSelectPersonnel(String selectPersonnel) {
        this.selectPersonnel = selectPersonnel;
    }

    public String getExpertTypeName() {
        return expertTypeName;
    }

    public void setExpertTypeName(String expertTypeName) {
        this.expertTypeName = expertTypeName;
    }

    public List<String> getExpertGroupName() {
        return expertGroupName;
    }

    public void setExpertGroupName(List<String> expertGroupName) {
        this.expertGroupName = expertGroupName;
    }
}
