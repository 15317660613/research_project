package com.adc.da.research.personnel.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_USER_INFO UserInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class UserInfoEO extends BaseEntity {

    private String id;
    private String userCode;
    private String userId;
    private String educationExperience;
    private String overseaExperience;
    private String researchExperience;
    private String personalEvaluation;
    private String resume;
    private String researchDirection;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private Integer delFlag;
    private String ext1;//出生年月
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;

    private String userName;
    private String  titleOf;
    private String   lastDegree;
    private String   qualifications;

    private String   email;
    private String   sex;
    private String   idCard;
    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>userId -> user_id</li>
     * <li>educationExperience -> education_experience</li>
     * <li>overseaExperience -> oversea_experience</li>
     * <li>researchExperience -> research_experience</li>
     * <li>personalEvaluation -> personal_evaluation</li>
     * <li>resume -> resume</li>
     * <li>researchDirection -> research_direction</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>createTime -> create_time</li>
     * <li>modifyTime -> modify_time</li>
     * <li>delFlag -> del_flag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "userId": return "user_id";
            case "educationExperience": return "education_experience";
            case "overseaExperience": return "oversea_experience";
            case "researchExperience": return "research_experience";
            case "personalEvaluation": return "personal_evaluation";
            case "resume": return "resume";
            case "researchDirection": return "research_direction";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "createTime": return "create_time";
            case "modifyTime": return "modify_time";
            case "delFlag": return "del_flag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>user_id -> userId</li>
     * <li>education_experience -> educationExperience</li>
     * <li>oversea_experience -> overseaExperience</li>
     * <li>research_experience -> researchExperience</li>
     * <li>personal_evaluation -> personalEvaluation</li>
     * <li>resume -> resume</li>
     * <li>research_direction -> researchDirection</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>create_time -> createTime</li>
     * <li>modify_time -> modifyTime</li>
     * <li>del_flag -> delFlag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "user_id": return "userId";
            case "education_experience": return "educationExperience";
            case "oversea_experience": return "overseaExperience";
            case "research_experience": return "researchExperience";
            case "personal_evaluation": return "personalEvaluation";
            case "resume": return "resume";
            case "research_direction": return "researchDirection";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "create_time": return "createTime";
            case "modify_time": return "modifyTime";
            case "del_flag": return "delFlag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            default: return null;
        }
    }
    
    /**  **/
    public String getId() {
        return this.id;
    }

    /**  **/
    public void setId(String id) {
        this.id = id;
    }

    /**  **/
    public String getUserId() {
        return this.userId;
    }

    /**  **/
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**  **/
    public String getEducationExperience() {
        return this.educationExperience;
    }

    /**  **/
    public void setEducationExperience(String educationExperience) {
        this.educationExperience = educationExperience;
    }

    /**  **/
    public String getOverseaExperience() {
        return this.overseaExperience;
    }

    /**  **/
    public void setOverseaExperience(String overseaExperience) {
        this.overseaExperience = overseaExperience;
    }

    /**  **/
    public String getResearchExperience() {
        return this.researchExperience;
    }

    /**  **/
    public void setResearchExperience(String researchExperience) {
        this.researchExperience = researchExperience;
    }

    /**  **/
    public String getPersonalEvaluation() {
        return this.personalEvaluation;
    }

    /**  **/
    public void setPersonalEvaluation(String personalEvaluation) {
        this.personalEvaluation = personalEvaluation;
    }

    /**  **/
    public String getResume() {
        return this.resume;
    }

    /**  **/
    public void setResume(String resume) {
        this.resume = resume;
    }

    /**  **/
    public String getResearchDirection() {
        return this.researchDirection;
    }

    /**  **/
    public void setResearchDirection(String researchDirection) {
        this.researchDirection = researchDirection;
    }

    /**  **/
    public String getCreateUserId() {
        return this.createUserId;
    }

    /**  **/
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**  **/
    public String getCreateUserName() {
        return this.createUserName;
    }

    /**  **/
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    /**  **/
    public Date getCreateTime() {
        return this.createTime;
    }

    /**  **/
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**  **/
    public Date getModifyTime() {
        return this.modifyTime;
    }

    /**  **/
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**  **/
    public Integer getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /**  **/
    public String getExt1() {
        return this.ext1;
    }

    /**  **/
    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    /**  **/
    public String getExt2() {
        return this.ext2;
    }

    /**  **/
    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    /**  **/
    public String getExt3() {
        return this.ext3;
    }

    /**  **/
    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    /**  **/
    public String getExt4() {
        return this.ext4;
    }

    /**  **/
    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }

    /**  **/
    public String getExt5() {
        return this.ext5;
    }

    /**  **/
    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitleOf() {
        return titleOf;
    }

    public void setTitleOf(String titleOf) {
        this.titleOf = titleOf;
    }

    public String getLastDegree() {
        return lastDegree;
    }

    public void setLastDegree(String lastDegree) {
        this.lastDegree = lastDegree;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
