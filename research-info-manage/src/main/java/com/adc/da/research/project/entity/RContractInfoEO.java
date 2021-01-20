package com.adc.da.research.project.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_CONTRACT_INFO ContractInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class RContractInfoEO extends BaseEntity {

    private String id;
    private String projectId;
    private String partyaName;
    private String partyaUser;
    private String partyaTel;
    private String partyaEmail;
    private String partybName;
    private String partybUser;
    private String partybDept;
    private String partybTel;
    private String partybMobile;
    private String partybEmail;
    private String partybFax;
    private String partycName;
    private String partycUser;
    private String researchContact;
    private String partycTel;
    private String partycEmail;
    private String partycFax;
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

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>partyaName -> partya_name</li>
     * <li>partyaUser -> partya_user</li>
     * <li>partyaTel -> partya_tel</li>
     * <li>partyaEmail -> partya_email</li>
     * <li>partybName -> partyb_name</li>
     * <li>partybUser -> partyb_user</li>
     * <li>partybDept -> partyb_dept</li>
     * <li>partybTel -> partyb_tel</li>
     * <li>partybEmail -> partyb_email</li>
     * <li>partybFax -> partyb_fax</li>
     * <li>partycName -> partyc_name</li>
     * <li>partycUser -> partyc_user</li>
     * <li>researchContact -> research_contact</li>
     * <li>partycTel -> partyc_tel</li>
     * <li>partycEmail -> partyc_email</li>
     * <li>partycFax -> partyc_fax</li>
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
            case "projectId": return "project_id";
            case "partyaName": return "partya_name";
            case "partyaUser": return "partya_user";
            case "partyaTel": return "partya_tel";
            case "partyaEmail": return "partya_email";
            case "partybName": return "partyb_name";
            case "partybUser": return "partyb_user";
            case "partybDept": return "partyb_dept";
            case "partybTel": return "partyb_tel";
            case "partybMobile": return "PARTYB_MOBILE";
            case "partybEmail": return "partyb_email";
            case "partybFax": return "partyb_fax";
            case "partycName": return "partyc_name";
            case "partycUser": return "partyc_user";
            case "researchContact": return "research_contact";
            case "partycTel": return "partyc_tel";
            case "partycEmail": return "partyc_email";
            case "partycFax": return "partyc_fax";
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
     * <li>project_id -> projectId</li>
     * <li>partya_name -> partyaName</li>
     * <li>partya_user -> partyaUser</li>
     * <li>partya_tel -> partyaTel</li>
     * <li>partya_email -> partyaEmail</li>
     * <li>partyb_name -> partybName</li>
     * <li>partyb_user -> partybUser</li>
     * <li>partyb_dept -> partybDept</li>
     * <li>partyb_tel -> partybTel</li>
     * <li>partyb_email -> partybEmail</li>
     * <li>partyb_fax -> partybFax</li>
     * <li>partyc_name -> partycName</li>
     * <li>partyc_user -> partycUser</li>
     * <li>research_contact -> researchContact</li>
     * <li>partyc_tel -> partycTel</li>
     * <li>partyc_email -> partycEmail</li>
     * <li>partyc_fax -> partycFax</li>
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
            case "project_id": return "projectId";
            case "partya_name": return "partyaName";
            case "partya_user": return "partyaUser";
            case "partya_tel": return "partyaTel";
            case "partya_email": return "partyaEmail";
            case "partyb_name": return "partybName";
            case "partyb_user": return "partybUser";
            case "partyb_dept": return "partybDept";
            case "partyb_tel": return "partybTel";
            case "PARTYB_MOBILE": return "partybMobile";
            case "partyb_email": return "partybEmail";
            case "partyb_fax": return "partybFax";
            case "partyc_name": return "partycName";
            case "partyc_user": return "partycUser";
            case "research_contact": return "researchContact";
            case "partyc_tel": return "partycTel";
            case "partyc_email": return "partycEmail";
            case "partyc_fax": return "partycFax";
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
    public String getProjectId() {
        return this.projectId;
    }

    /**  **/
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**  **/
    public String getPartyaName() {
        return this.partyaName;
    }

    /**  **/
    public void setPartyaName(String partyaName) {
        this.partyaName = partyaName;
    }

    /**  **/
    public String getPartyaUser() {
        return this.partyaUser;
    }

    /**  **/
    public void setPartyaUser(String partyaUser) {
        this.partyaUser = partyaUser;
    }

    /**  **/
    public String getPartyaTel() {
        return this.partyaTel;
    }

    /**  **/
    public void setPartyaTel(String partyaTel) {
        this.partyaTel = partyaTel;
    }

    /**  **/
    public String getPartyaEmail() {
        return this.partyaEmail;
    }

    /**  **/
    public void setPartyaEmail(String partyaEmail) {
        this.partyaEmail = partyaEmail;
    }

    /**  **/
    public String getPartybName() {
        return this.partybName;
    }

    /**  **/
    public void setPartybName(String partybName) {
        this.partybName = partybName;
    }

    /**  **/
    public String getPartybUser() {
        return this.partybUser;
    }

    /**  **/
    public void setPartybUser(String partybUser) {
        this.partybUser = partybUser;
    }

    /**  **/
    public String getPartybDept() {
        return this.partybDept;
    }

    /**  **/
    public void setPartybDept(String partybDept) {
        this.partybDept = partybDept;
    }

    /**  **/
    public String getPartybTel() {
        return this.partybTel;
    }

    /**  **/
    public void setPartybTel(String partybTel) {
        this.partybTel = partybTel;
    }

    /**  **/
    public String getPartybEmail() {
        return this.partybEmail;
    }

    /**  **/
    public void setPartybEmail(String partybEmail) {
        this.partybEmail = partybEmail;
    }

    /**  **/
    public String getPartybFax() {
        return this.partybFax;
    }

    /**  **/
    public void setPartybFax(String partybFax) {
        this.partybFax = partybFax;
    }

    /**  **/
    public String getPartycName() {
        return this.partycName;
    }

    /**  **/
    public void setPartycName(String partycName) {
        this.partycName = partycName;
    }

    /**  **/
    public String getPartycUser() {
        return this.partycUser;
    }

    /**  **/
    public void setPartycUser(String partycUser) {
        this.partycUser = partycUser;
    }

    /**  **/
    public String getResearchContact() {
        return this.researchContact;
    }

    /**  **/
    public void setResearchContact(String researchContact) {
        this.researchContact = researchContact;
    }

    /**  **/
    public String getPartycTel() {
        return this.partycTel;
    }

    /**  **/
    public void setPartycTel(String partycTel) {
        this.partycTel = partycTel;
    }

    /**  **/
    public String getPartycEmail() {
        return this.partycEmail;
    }

    /**  **/
    public void setPartycEmail(String partycEmail) {
        this.partycEmail = partycEmail;
    }

    /**  **/
    public String getPartycFax() {
        return this.partycFax;
    }

    /**  **/
    public void setPartycFax(String partycFax) {
        this.partycFax = partycFax;
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

    public String getPartybMobile() {
        return partybMobile;
    }

    public void setPartybMobile(String partybMobile) {
        this.partybMobile = partybMobile;
    }
}
