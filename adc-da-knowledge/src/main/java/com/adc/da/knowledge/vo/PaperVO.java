package com.adc.da.knowledge.vo;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>K_PAPER PaperEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class PaperVO extends BaseEntity {

    private String id;
    private String name;
    private String publishedJournals;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date publishedTime;
    private String publishedIssue;
    private String keywords;
    private String index;
    private String company;
    private String authorUserNames;
    private String deptId;
    private String uploadUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String paperAbstract;
    private String deptName;
    private String uploadUserId;
    private String authorUserIds;
    private String securityLevel;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String num;
    private String extInfo5;
    private String extInfo6;
    private String fileId;
    private String fileName;
    private String autoNumber;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id_</li>
     * <li>name -> name_</li>
     * <li>publishedJournals -> published_journals_</li>
     * <li>publishedTime -> published_time_</li>
     * <li>publishedIssue -> published_issue_</li>
     * <li>keywords -> keywords_</li>
     * <li>index -> index_</li>
     * <li>company -> company_</li>
     * <li>authorUserNames -> author_user_names_</li>
     * <li>deptId -> dept_id_</li>
     * <li>uploadUserName -> upload_user_name_</li>
     * <li>updateTime -> update_time_</li>
     * <li>paperAbstract -> paper_abstract_</li>
     * <li>deptName -> dept_name_</li>
     * <li>uploadUserId -> upload_user_id_</li>
     * <li>authorUserIds -> author_user_ids_</li>
     * <li>securityLevel -> security_level_</li>
     * <li>extInfo1 -> ext_info1_</li>
     * <li>extInfo2 -> ext_info2_</li>
     * <li>extInfo3 -> ext_info3_</li>
     * <li>extInfo4 -> ext_info4_</li>
     * <li>num -> num_</li>
     * <li>extInfo5 -> ext_info5_</li>
     * <li>extInfo6 -> ext_info6_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id_";
            case "name": return "name_";
            case "publishedJournals": return "published_journals_";
            case "publishedTime": return "published_time_";
            case "publishedIssue": return "published_issue_";
            case "keywords": return "keywords_";
            case "index": return "index_";
            case "company": return "company_";
            case "authorUserNames": return "author_user_names_";
            case "deptId": return "dept_id_";
            case "uploadUserName": return "upload_user_name_";
            case "updateTime": return "update_time_";
            case "paperAbstract": return "paper_abstract_";
            case "deptName": return "dept_name_";
            case "uploadUserId": return "upload_user_id_";
            case "authorUserIds": return "author_user_ids_";
            case "securityLevel": return "security_level_";
            case "extInfo1": return "ext_info1_";
            case "extInfo2": return "ext_info2_";
            case "extInfo3": return "ext_info3_";
            case "extInfo4": return "ext_info4_";
            case "num": return "num_";
            case "extInfo5": return "ext_info5_";
            case "extInfo6": return "ext_info6_";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id_ -> id</li>
     * <li>name_ -> name</li>
     * <li>published_journals_ -> publishedJournals</li>
     * <li>published_time_ -> publishedTime</li>
     * <li>published_issue_ -> publishedIssue</li>
     * <li>keywords_ -> keywords</li>
     * <li>index_ -> index</li>
     * <li>company_ -> company</li>
     * <li>author_user_names_ -> authorUserNames</li>
     * <li>dept_id_ -> deptId</li>
     * <li>upload_user_name_ -> uploadUserName</li>
     * <li>update_time_ -> updateTime</li>
     * <li>paper_abstract_ -> paperAbstract</li>
     * <li>dept_name_ -> deptName</li>
     * <li>upload_user_id_ -> uploadUserId</li>
     * <li>author_user_ids_ -> authorUserIds</li>
     * <li>security_level_ -> securityLevel</li>
     * <li>ext_info1_ -> extInfo1</li>
     * <li>ext_info2_ -> extInfo2</li>
     * <li>ext_info3_ -> extInfo3</li>
     * <li>ext_info4_ -> extInfo4</li>
     * <li>num_ -> num</li>
     * <li>ext_info5_ -> extInfo5</li>
     * <li>ext_info6_ -> extInfo6</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id_": return "id";
            case "name_": return "name";
            case "published_journals_": return "publishedJournals";
            case "published_time_": return "publishedTime";
            case "published_issue_": return "publishedIssue";
            case "keywords_": return "keywords";
            case "index_": return "index";
            case "company_": return "company";
            case "author_user_names_": return "authorUserNames";
            case "dept_id_": return "deptId";
            case "upload_user_name_": return "uploadUserName";
            case "update_time_": return "updateTime";
            case "paper_abstract_": return "paperAbstract";
            case "dept_name_": return "deptName";
            case "upload_user_id_": return "uploadUserId";
            case "author_user_ids_": return "authorUserIds";
            case "security_level_": return "securityLevel";
            case "ext_info1_": return "extInfo1";
            case "ext_info2_": return "extInfo2";
            case "ext_info3_": return "extInfo3";
            case "ext_info4_": return "extInfo4";
            case "num_": return "num";
            case "ext_info5_": return "extInfo5";
            case "ext_info6_": return "extInfo6";
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
    public String getName() {
        return this.name;
    }

    /**  **/
    public void setName(String name) {
        this.name = name;
    }

    public void setAuthorUserNames(String authorUserNames) {
        this.authorUserNames = authorUserNames;
    }


    public void setAuthorUserIds(String authorUserIds) {
        this.authorUserIds = authorUserIds;
    }


    /**  **/
    public String getPublishedJournals() {
        return this.publishedJournals;
    }

    /**  **/
    public void setPublishedJournals(String publishedJournals) {
        this.publishedJournals = publishedJournals;
    }

    /**  **/
    public Date getPublishedTime() {
        return this.publishedTime;
    }

    /**  **/
    public void setPublishedTime(Date publishedTime) {
        this.publishedTime = publishedTime;
    }

    /**  **/
    public String getPublishedIssue() {
        return this.publishedIssue;
    }

    /**  **/
    public void setPublishedIssue(String publishedIssue) {
        this.publishedIssue = publishedIssue;
    }

    /**  **/
    public String getKeywords() {
        return this.keywords;
    }

    /**  **/
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    /**  **/
    public String getIndex() {
        return this.index;
    }

    /**  **/
    public void setIndex(String index) {
        this.index = index;
    }

    /**  **/
    public String getCompany() {
        return this.company;
    }

    /**  **/
    public void setCompany(String company) {
        this.company = company;
    }

    /**  **/
    public String getAuthorUserNames() {
        return this.authorUserNames;
    }



    /**  **/
    public String getDeptId() {
        return this.deptId;
    }

    /**  **/
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**  **/
    public String getUploadUserName() {
        return this.uploadUserName;
    }

    /**  **/
    public void setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
    }

    /**  **/
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**  **/
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**  **/
    public String getPaperAbstract() {
        return this.paperAbstract;
    }

    /**  **/
    public void setPaperAbstract(String paperAbstract) {
        this.paperAbstract = paperAbstract;
    }

    /**  **/
    public String getDeptName() {
        return this.deptName;
    }

    /**  **/
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    /**  **/
    public String getUploadUserId() {
        return this.uploadUserId;
    }

    /**  **/
    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId;
    }

    /**  **/
    public String getAuthorUserIds() {
        return this.authorUserIds;
    }




    /**  **/
    public String getSecurityLevel() {
        return this.securityLevel;
    }

    /**  **/
    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }

    /**  **/
    public String getExtInfo1() {
        return this.extInfo1;
    }

    /**  **/
    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    /**  **/
    public String getExtInfo2() {
        return this.extInfo2;
    }

    /**  **/
    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

    /**  **/
    public String getExtInfo3() {
        return this.extInfo3;
    }

    /**  **/
    public void setExtInfo3(String extInfo3) {
        this.extInfo3 = extInfo3;
    }

    /**  **/
    public String getExtInfo4() {
        return this.extInfo4;
    }

    /**  **/
    public void setExtInfo4(String extInfo4) {
        this.extInfo4 = extInfo4;
    }

    /**  **/
    public String getNum() {
        return this.num;
    }

    /**  **/
    public void setNum(String num) {
        this.num = num;
    }

    /**  **/
    public String getExtInfo5() {
        return this.extInfo5;
    }

    /**  **/
    public void setExtInfo5(String extInfo5) {
        this.extInfo5 = extInfo5;
    }

    /**  **/
    public String getExtInfo6() {
        return this.extInfo6;
    }

    /**  **/
    public void setExtInfo6(String extInfo6) {
        this.extInfo6 = extInfo6;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAutoNumber() {
        return autoNumber;
    }

    public void setAutoNumber(String autoNumber) {
        this.autoNumber = autoNumber;
    }
}
