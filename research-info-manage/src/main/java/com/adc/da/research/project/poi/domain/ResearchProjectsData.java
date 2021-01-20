package com.adc.da.research.project.poi.domain;

import com.adc.da.research.project.poi.table.ResearchContentData;
import com.adc.da.word.config.Name;

/**
 *
 * @Auther: yanyujie
 * @Date: 2020/11/25/11:47
 * @Description:
 */
public class ResearchProjectsData extends ResearchProjectsTable {

    //项目编号
    private String projectCode;

    //项目名称
    private String projectName;
    //甲方联系人
    private String partyAContact;
    //甲方电话
    private String partyATelephone;
    //甲方电子邮件
    private String partyAEmail;

    //乙方负责人
    private String partyBprincipal;
    //乙方姓名
    private String partyBName;
    //乙方工作部门
    private String partyBDepartment;
    //已方电话
    private String partyBTelephone;
    //已方传真
    private String partyBFox;
    //已方电子邮件
    private String partyBEmail;

    //项目承担部门
    private String partyCDepartment;
    //部门负责人
    private String headDepartment;
    //科研联络人
    private String researchContact;
    //丙方电话
    private String partyCTelephone;
    //丙方传真
    private String partyCFox;
    //丙方电子邮件
    private String partyCEmail;
    //主要研究内容
    @Name("researchContent")
    private ResearchContentData researchContent ;

    //研究目标
    @Name("researchTarget")
    private ResearchContentData researchTarget ;

    @Name("userTable")
    private ResearchContentData userTable;

    @Name("progressA")
    private ResearchContentData progressA;


    @Name("progressB")
    private ResearchContentData progressB;

    //progressDate
    private String progressDate;

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPartyAContact() {
        return partyAContact;
    }

    public void setPartyAContact(String partyAContact) {
        this.partyAContact = partyAContact;
    }

    public String getPartyATelephone() {
        return partyATelephone;
    }

    public void setPartyATelephone(String partyATelephone) {
        this.partyATelephone = partyATelephone;
    }

    public String getPartyAEmail() {
        return partyAEmail;
    }

    public void setPartyAEmail(String partyAEmail) {
        this.partyAEmail = partyAEmail;
    }

    public String getPartyBName() {
        return partyBName;
    }

    public void setPartyBName(String partyBName) {
        this.partyBName = partyBName;
    }

    public String getPartyBDepartment() {
        return partyBDepartment;
    }

    public void setPartyBDepartment(String partyBDepartment) {
        this.partyBDepartment = partyBDepartment;
    }

    public String getPartyBTelephone() {
        return partyBTelephone;
    }

    public void setPartyBTelephone(String partyBTelephone) {
        this.partyBTelephone = partyBTelephone;
    }

    public String getPartyBFox() {
        return partyBFox;
    }

    public void setPartyBFox(String partyBFox) {
        this.partyBFox = partyBFox;
    }

    public String getPartyBEmail() {
        return partyBEmail;
    }

    public void setPartyBEmail(String partyBEmail) {
        this.partyBEmail = partyBEmail;
    }

    public String getPartyCDepartment() {
        return partyCDepartment;
    }

    public void setPartyCDepartment(String partyCDepartment) {
        this.partyCDepartment = partyCDepartment;
    }

    public String getHeadDepartment() {
        return headDepartment;
    }

    public void setHeadDepartment(String headDepartment) {
        this.headDepartment = headDepartment;
    }

    public String getResearchContact() {
        return researchContact;
    }

    public void setResearchContact(String researchContact) {
        this.researchContact = researchContact;
    }

    public String getPartyCTelephone() {
        return partyCTelephone;
    }

    public void setPartyCTelephone(String partyCTelephone) {
        this.partyCTelephone = partyCTelephone;
    }

    public String getPartyCFox() {
        return partyCFox;
    }

    public void setPartyCFox(String partyCFox) {
        this.partyCFox = partyCFox;
    }

    public String getPartyCEmail() {
        return partyCEmail;
    }

    public void setPartyCEmail(String partyCEmail) {
        this.partyCEmail = partyCEmail;
    }

    public String getPartyBprincipal() {
        return partyBprincipal;
    }

    public void setPartyBprincipal(String partyBprincipal) {
        this.partyBprincipal = partyBprincipal;
    }


    public ResearchContentData getResearchContent() {
        return researchContent;
    }

    public void setResearchContent(ResearchContentData researchContent) {
        this.researchContent = researchContent;
    }

    public ResearchContentData getResearchTarget() {
        return researchTarget;
    }

    public void setResearchTarget(ResearchContentData researchTarget) {
        this.researchTarget = researchTarget;
    }

    public ResearchContentData getUserTable() {
        return userTable;
    }

    public void setUserTable(ResearchContentData userTable) {
        this.userTable = userTable;
    }

    public ResearchContentData getProgressA() {
        return progressA;
    }

    public void setProgressA(ResearchContentData progressA) {
        this.progressA = progressA;
    }

    public ResearchContentData getProgressB() {
        return progressB;
    }

    public void setProgressB(ResearchContentData progressB) {
        this.progressB = progressB;
    }

    public String getProgressDate() {
        return progressDate;
    }

    public void setProgressDate(String progressDate) {
        this.progressDate = progressDate;
    }
}
