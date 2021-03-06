package com.adc.da.research.personnel.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_EXPERT_USER_INFO ExpertUserInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-27 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ExpertUserInfoEO extends BaseEntity {

    private String expertTypeId;
    private String id;
    private String userId;
    private String userName;
    private String companyName;
    private String expertGroupId;
    private String resume;
    private String researchDirection;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private Integer delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private String expertGroupName;
    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>expertTypeId -> expert_type_id</li>
     * <li>id -> id</li>
     * <li>userId -> user_id</li>
     * <li>userName -> user_name</li>
     * <li>companyName -> company_name</li>
     * <li>expertGroupId -> expert_group_id</li>
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
            case "expertTypeId": return "expert_type_id";
            case "id": return "id";
            case "userId": return "user_id";
            case "userName": return "user_name";
            case "companyName": return "company_name";
            case "expertGroupId": return "expert_group_id";
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
     * <li>expert_type_id -> expertTypeId</li>
     * <li>id -> id</li>
     * <li>user_id -> userId</li>
     * <li>user_name -> userName</li>
     * <li>company_name -> companyName</li>
     * <li>expert_group_id -> expertGroupId</li>
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
            case "expert_type_id": return "expertTypeId";
            case "id": return "id";
            case "user_id": return "userId";
            case "user_name": return "userName";
            case "company_name": return "companyName";
            case "expert_group_id": return "expertGroupId";
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
    public String getExpertTypeId() {
        return this.expertTypeId;
    }

    /**  **/
    public void setExpertTypeId(String expertTypeId) {
        this.expertTypeId = expertTypeId;
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
    public String getUserName() {
        return this.userName;
    }

    /**  **/
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**  **/
    public String getCompanyName() {
        return this.companyName;
    }

    /**  **/
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**  **/
    public String getExpertGroupId() {
        return this.expertGroupId;
    }

    /**  **/
    public void setExpertGroupId(String expertGroupId) {
        this.expertGroupId = expertGroupId;
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

    public String getExpertGroupName() {
        return expertGroupName;
    }

    public void setExpertGroupName(String expertGroupName) {
        this.expertGroupName = expertGroupName;
    }
}
