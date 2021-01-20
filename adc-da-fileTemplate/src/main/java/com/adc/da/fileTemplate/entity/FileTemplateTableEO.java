package com.adc.da.fileTemplate.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>FILE_TEMPLATE_TABLE FileTemplateTableEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-09-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class FileTemplateTableEO extends BaseEntity {

    private String id;
    private String tempName;
    private String tempCode;
    private String version;
    private String tempTypeId;
    private String fileId;
    private String fileName;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private Integer state;
    private Integer delFlag;
    private String htmlUrl;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>tempName -> temp_name</li>
     * <li>tempCode -> temp_code</li>
     * <li>version -> version</li>
     * <li>tempTypeId -> temp_type_id</li>
     * <li>fileId -> file_id</li>
     * <li>fileName -> file_name</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>createTime -> create_time</li>
     * <li>modifyTime -> modify_time</li>
     * <li>state -> state</li>
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
            case "tempName": return "temp_name";
            case "tempCode": return "temp_code";
            case "version": return "version";
            case "tempTypeId": return "temp_type_id";
            case "fileId": return "file_id";
            case "fileName": return "file_name";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "createTime": return "create_time";
            case "modifyTime": return "modify_time";
            case "state": return "state";
            case "delFlag": return "del_flag";
            case "ext1": return "ext1";
            case "htmlUrl": return "html_url";
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
     * <li>temp_name -> tempName</li>
     * <li>temp_code -> tempCode</li>
     * <li>version -> version</li>
     * <li>temp_type_id -> tempTypeId</li>
     * <li>file_id -> fileId</li>
     * <li>file_name -> fileName</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>create_time -> createTime</li>
     * <li>modify_time -> modifyTime</li>
     * <li>state -> state</li>
     * <li>del_flag -> delFlag</li>
     * <li>ext1 -> ext1</li>
     * case "html_url": return "htmlUrl";
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "temp_name": return "tempName";
            case "temp_code": return "tempCode";
            case "version": return "version";
            case "temp_type_id": return "tempTypeId";
            case "file_id": return "fileId";
            case "file_name": return "fileName";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "create_time": return "createTime";
            case "modify_time": return "modifyTime";
            case "state": return "state";
            case "del_flag": return "delFlag";
            case "ext1": return "ext1";
            case "html_url": return "htmlUrl";
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
    public String getTempName() {
        return this.tempName;
    }

    /**  **/
    public void setTempName(String tempName) {
        this.tempName = tempName;
    }

    /**  **/
    public String getTempCode() {
        return this.tempCode;
    }

    /**  **/
    public void setTempCode(String tempCode) {
        this.tempCode = tempCode;
    }

    /**  **/
    public String getVersion() {
        return this.version;
    }

    /**  **/
    public void setVersion(String version) {
        this.version = version;
    }

    /**  **/
    public String getTempTypeId() {
        return this.tempTypeId;
    }

    /**  **/
    public void setTempTypeId(String tempTypeId) {
        this.tempTypeId = tempTypeId;
    }

    /**  **/
    public String getFileId() {
        return this.fileId;
    }

    /**  **/
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /**  **/
    public String getFileName() {
        return this.fileName;
    }

    /**  **/
    public void setFileName(String fileName) {
        this.fileName = fileName;
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
    public Integer getState() {
        return this.state;
    }

    /**  **/
    public void setState(Integer state) {
        this.state = state;
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

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }
}
