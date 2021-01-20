package com.adc.da.epis.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>BUISNESS_CONTRACT_INFO BuisnessContractInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BuisnessContractInfoEO extends BaseEntity {

    private String id;
    private String projectId;
    private Integer isSubject;
    private String subjectId;
    private String contractNumber;
    private String contrackName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date signedTime;
    private Long contrackMoney;
    private Long managerMoney;
    private Long develMoney;
    private Long develResearchMoney;
    private Long designedConsulting;
    private String performType;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private String createUser;
    private Integer contractType;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date contractStart;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date contractEnd;
    private Integer contractProjectType;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>isSubject -> is_subject</li>
     * <li>subjectId -> subject_id</li>
     * <li>contractNumber -> contract_number</li>
     * <li>contrackName -> contrack_name</li>
     * <li>signedTime -> signed_time</li>
     * <li>contrackMoney -> contrack_money</li>
     * <li>managerMoney -> manager_money</li>
     * <li>develMoney -> devel_money</li>
     * <li>develResearchMoney -> devel_research_money</li>
     * <li>designedConsulting -> designed_consulting</li>
     * <li>performType -> perform_type</li>
     * <li>createTime -> create_time</li>
     * <li>modifyTime -> modify_time</li>
     * <li>createUser -> create_user</li>
     * <li>contractType -> contract_type</li>
     * <li>contractStart -> contract_start</li>
     * <li>contractEnd -> contract_end</li>
     * <li>contractProjectType -> contract_project_type</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "projectId": return "project_id";
            case "isSubject": return "is_subject";
            case "subjectId": return "subject_id";
            case "contractNumber": return "contract_number";
            case "contrackName": return "contrack_name";
            case "signedTime": return "signed_time";
            case "contrackMoney": return "contrack_money";
            case "managerMoney": return "manager_money";
            case "develMoney": return "devel_money";
            case "develResearchMoney": return "devel_research_money";
            case "designedConsulting": return "designed_consulting";
            case "performType": return "perform_type";
            case "createTime": return "create_time";
            case "modifyTime": return "modify_time";
            case "createUser": return "create_user";
            case "contractType": return "contract_type";
            case "contractStart": return "contract_start";
            case "contractEnd": return "contract_end";
            case "contractProjectType": return "contract_project_type";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>is_subject -> isSubject</li>
     * <li>subject_id -> subjectId</li>
     * <li>contract_number -> contractNumber</li>
     * <li>contrack_name -> contrackName</li>
     * <li>signed_time -> signedTime</li>
     * <li>contrack_money -> contrackMoney</li>
     * <li>manager_money -> managerMoney</li>
     * <li>devel_money -> develMoney</li>
     * <li>devel_research_money -> develResearchMoney</li>
     * <li>designed_consulting -> designedConsulting</li>
     * <li>perform_type -> performType</li>
     * <li>create_time -> createTime</li>
     * <li>modify_time -> modifyTime</li>
     * <li>create_user -> createUser</li>
     * <li>contract_type -> contractType</li>
     * <li>contract_start -> contractStart</li>
     * <li>contract_end -> contractEnd</li>
     * <li>contract_project_type -> contractProjectType</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "project_id": return "projectId";
            case "is_subject": return "isSubject";
            case "subject_id": return "subjectId";
            case "contract_number": return "contractNumber";
            case "contrack_name": return "contrackName";
            case "signed_time": return "signedTime";
            case "contrack_money": return "contrackMoney";
            case "manager_money": return "managerMoney";
            case "devel_money": return "develMoney";
            case "devel_research_money": return "develResearchMoney";
            case "designed_consulting": return "designedConsulting";
            case "perform_type": return "performType";
            case "create_time": return "createTime";
            case "modify_time": return "modifyTime";
            case "create_user": return "createUser";
            case "contract_type": return "contractType";
            case "contract_start": return "contractStart";
            case "contract_end": return "contractEnd";
            case "contract_project_type": return "contractProjectType";
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
    public Integer getIsSubject() {
        return this.isSubject;
    }

    /**  **/
    public void setIsSubject(Integer isSubject) {
        this.isSubject = isSubject;
    }

    /**  **/
    public String getSubjectId() {
        return this.subjectId;
    }

    /**  **/
    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    /**  **/
    public String getContractNumber() {
        return this.contractNumber;
    }

    /**  **/
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    /**  **/
    public String getContrackName() {
        return this.contrackName;
    }

    /**  **/
    public void setContrackName(String contrackName) {
        this.contrackName = contrackName;
    }

    /**  **/
    public Date getSignedTime() {
        return this.signedTime;
    }

    /**  **/
    public void setSignedTime(Date signedTime) {
        this.signedTime = signedTime;
    }

    /**  **/
    public Long getContrackMoney() {
        return this.contrackMoney;
    }

    /**  **/
    public void setContrackMoney(Long contrackMoney) {
        this.contrackMoney = contrackMoney;
    }

    /**  **/
    public Long getManagerMoney() {
        return this.managerMoney;
    }

    /**  **/
    public void setManagerMoney(Long managerMoney) {
        this.managerMoney = managerMoney;
    }

    /**  **/
    public Long getDevelMoney() {
        return this.develMoney;
    }

    /**  **/
    public void setDevelMoney(Long develMoney) {
        this.develMoney = develMoney;
    }

    /**  **/
    public Long getDevelResearchMoney() {
        return this.develResearchMoney;
    }

    /**  **/
    public void setDevelResearchMoney(Long develResearchMoney) {
        this.develResearchMoney = develResearchMoney;
    }

    /**  **/
    public Long getDesignedConsulting() {
        return this.designedConsulting;
    }

    /**  **/
    public void setDesignedConsulting(Long designedConsulting) {
        this.designedConsulting = designedConsulting;
    }

    /**  **/
    public String getPerformType() {
        return this.performType;
    }

    /**  **/
    public void setPerformType(String performType) {
        this.performType = performType;
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
    public String getCreateUser() {
        return this.createUser;
    }

    /**  **/
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**  **/
    public Integer getContractType() {
        return this.contractType;
    }

    /**  **/
    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    /**  **/
    public Date getContractStart() {
        return this.contractStart;
    }

    /**  **/
    public void setContractStart(Date contractStart) {
        this.contractStart = contractStart;
    }

    /**  **/
    public Date getContractEnd() {
        return this.contractEnd;
    }

    /**  **/
    public void setContractEnd(Date contractEnd) {
        this.contractEnd = contractEnd;
    }

    /**  **/
    public Integer getContractProjectType() {
        return this.contractProjectType;
    }

    /**  **/
    public void setContractProjectType(Integer contractProjectType) {
        this.contractProjectType = contractProjectType;
    }

}
