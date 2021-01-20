package com.adc.da.knowledge.vo;
import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>K_PATENT PatentEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class PatentVO extends BaseEntity {

    private String belongUserAddress;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;
    private String extInfo6;
    private String id;
    private String type;
    private String name;
    private String num;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date applyDate;
    private String belongUserName;
    private String belongUserId;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date authorizeDate;
    private String authorizeNum;
    private String certificateNum;
    private String uploadUserId;
    private String designerUserNames;
    private String designerUserIds;
    private String uploadUserName;
    private String fileId;
    private String fileName;
    private String deptId;
    private String deptName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date uploadTime;
    private String autoNumber;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>belongUserAddress -> belong_user_address_</li>
     * <li>extInfo1 -> ext_info1_</li>
     * <li>extInfo2 -> ext_info2_</li>
     * <li>extInfo3 -> ext_info3_</li>
     * <li>extInfo4 -> ext_info4_</li>
     * <li>extInfo5 -> ext_info5_</li>
     * <li>extInfo6 -> ext_info6_</li>
     * <li>id -> id_</li>
     * <li>type -> type_</li>
     * <li>name -> name_</li>
     * <li>num -> num_</li>
     * <li>applyDate -> apply_date_</li>
     * <li>belongUserName -> belong_user_name_</li>
     * <li>belongUserId -> belong_user_id_</li>
     * <li>authorizeDate -> authorize_date_</li>
     * <li>authorizeNum -> authorize_num_</li>
     * <li>certificateNum -> certificate_num_</li>
     * <li>uploadUserId -> upload_user_id_</li>
     * <li>designerUserNames -> designer_user_names_</li>
     * <li>designerUserIds -> designer_user_ids_</li>
     * <li>uploadUserName -> upload_user_name_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "belongUserAddress": return "belong_user_address_";
            case "extInfo1": return "ext_info1_";
            case "extInfo2": return "ext_info2_";
            case "extInfo3": return "ext_info3_";
            case "extInfo4": return "ext_info4_";
            case "extInfo5": return "ext_info5_";
            case "extInfo6": return "ext_info6_";
            case "id": return "id_";
            case "type": return "type_";
            case "name": return "name_";
            case "num": return "num_";
            case "applyDate": return "apply_date_";
            case "belongUserName": return "belong_user_name_";
            case "belongUserId": return "belong_user_id_";
            case "authorizeDate": return "authorize_date_";
            case "authorizeNum": return "authorize_num_";
            case "certificateNum": return "certificate_num_";
            case "uploadUserId": return "upload_user_id_";
            case "designerUserNames": return "designer_user_names_";
            case "designerUserIds": return "designer_user_ids_";
            case "uploadUserName": return "upload_user_name_";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>belong_user_address_ -> belongUserAddress</li>
     * <li>ext_info1_ -> extInfo1</li>
     * <li>ext_info2_ -> extInfo2</li>
     * <li>ext_info3_ -> extInfo3</li>
     * <li>ext_info4_ -> extInfo4</li>
     * <li>ext_info5_ -> extInfo5</li>
     * <li>ext_info6_ -> extInfo6</li>
     * <li>id_ -> id</li>
     * <li>type_ -> type</li>
     * <li>name_ -> name</li>
     * <li>num_ -> num</li>
     * <li>apply_date_ -> applyDate</li>
     * <li>belong_user_name_ -> belongUserName</li>
     * <li>belong_user_id_ -> belongUserId</li>
     * <li>authorize_date_ -> authorizeDate</li>
     * <li>authorize_num_ -> authorizeNum</li>
     * <li>certificate_num_ -> certificateNum</li>
     * <li>upload_user_id_ -> uploadUserId</li>
     * <li>designer_user_names_ -> designerUserNames</li>
     * <li>designer_user_ids_ -> designerUserIds</li>
     * <li>upload_user_name_ -> uploadUserName</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "belong_user_address_": return "belongUserAddress";
            case "ext_info1_": return "extInfo1";
            case "ext_info2_": return "extInfo2";
            case "ext_info3_": return "extInfo3";
            case "ext_info4_": return "extInfo4";
            case "ext_info5_": return "extInfo5";
            case "ext_info6_": return "extInfo6";
            case "id_": return "id";
            case "type_": return "type";
            case "name_": return "name";
            case "num_": return "num";
            case "apply_date_": return "applyDate";
            case "belong_user_name_": return "belongUserName";
            case "belong_user_id_": return "belongUserId";
            case "authorize_date_": return "authorizeDate";
            case "authorize_num_": return "authorizeNum";
            case "certificate_num_": return "certificateNum";
            case "upload_user_id_": return "uploadUserId";
            case "designer_user_names_": return "designerUserNames";
            case "designer_user_ids_": return "designerUserIds";
            case "upload_user_name_": return "uploadUserName";
            default: return null;
        }
    }

    /**  **/
    public String getBelongUserAddress() {
        return this.belongUserAddress;
    }

    /**  **/
    public void setBelongUserAddress(String belongUserAddress) {
        this.belongUserAddress = belongUserAddress;
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

    /**  **/
    public String getId() {
        return this.id;
    }

    /**  **/
    public void setId(String id) {
        this.id = id;
    }

    /**  **/
    public String getType() {
        return this.type;
    }

    /**  **/
    public void setType(String type) {
        this.type = type;
    }

    /**  **/
    public String getName() {
        return this.name;
    }

    /**  **/
    public void setName(String name) {
        this.name = name;
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
    public Date getApplyDate() {
        return this.applyDate;
    }

    /**  **/
    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    /**  **/
    public String getBelongUserName() {
        return this.belongUserName;
    }

    /**  **/
    public void setBelongUserName(String belongUserName) {
        this.belongUserName = belongUserName;
    }

    /**  **/
    public String getBelongUserId() {
        return this.belongUserId;
    }

    /**  **/
    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    /**  **/
    public Date getAuthorizeDate() {
        return this.authorizeDate;
    }

    /**  **/
    public void setAuthorizeDate(Date authorizeDate) {
        this.authorizeDate = authorizeDate;
    }

    /**  **/
    public String getAuthorizeNum() {
        return this.authorizeNum;
    }

    /**  **/
    public void setAuthorizeNum(String authorizeNum) {
        this.authorizeNum = authorizeNum;
    }

    /**  **/
    public String getCertificateNum() {
        return this.certificateNum;
    }

    /**  **/
    public void setCertificateNum(String certificateNum) {
        this.certificateNum = certificateNum;
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
    public String getDesignerUserNames() {
        return this.designerUserNames;
    }

    /**  **/
    public void setDesignerUserNames(String designerUserNames) {
        this.designerUserNames = designerUserNames;
    }

    /**  **/
    public String getDesignerUserIds() {
        return this.designerUserIds;
    }

    /**  **/
    public void setDesignerUserIds(String designerUserIds) {
        this.designerUserIds = designerUserIds;
    }

    /**  **/
    public String getUploadUserName() {
        return this.uploadUserName;
    }

    /**  **/
    public void setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
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

    public String getAutoNumber() {
        return autoNumber;
    }

    public void setAutoNumber(String autoNumber) {
        this.autoNumber = autoNumber;
    }
}
