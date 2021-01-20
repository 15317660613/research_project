package com.adc.da.crm.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.sql.Clob;
import java.util.Date;

/**
 * <b>功能：</b>PROJECT_INFO ProjectInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectInfoEO extends BaseEntity {
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    private String id;
    @MatchField("项目经理")
    private String projecrManagerId;
    @MatchField("部门")
    private String deptId;
    @MatchField("项目编号")
    private String projectNumber;
    @MatchField("项目名称")
    private String projectName;
    @MatchField("项目描述")
    private String discription;
    @MatchField("所属平台")
    private String belongingPlatform;
    @MatchField("所属板块")
    private String belongingPlate;
    private String belongingCentralDept;
    private String belongingDept;
    @MatchField("手机")
    private String mobile;
    @MatchField("座机")
    private String phone;
    @MatchField("邮箱")
    private String email;
    @MatchField("项目阶段")
    private String proPeriod;
    @MatchField("立项时间")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date proEstablishTime;
    private String proApprovalId;
    @MatchField("立项附件")
    private String proEstablishFile;
    @MatchField("项目过程文档")
    private String proProcessDocument;
    @MatchField("项目是否结束")
    private String proEndFlag;


    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @MatchField("结束时间")
    private Date proEndTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private String createdUser;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;
    private String modifiedUser;


    @MatchField("建档日期")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    private String delFlag;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>delFlag -> del_flag</li>
     * <li>discription -> discription</li>
     * <li>belongingPlatform -> belonging_platform</li>
     * <li>belongingPlate -> belonging_plate</li>
     * <li>belongingCentralDept -> belonging_central_dept</li>
     * <li>belongingDept -> belonging_dept</li>
     * <li>mobile -> mobile</li>
     * <li>phone -> phone</li>
     * <li>email -> email</li>
     * <li>proPeriod -> pro_period</li>
     * <li>proEstablishTime -> pro_establish_time</li>
     * <li>proApprovalId -> pro_approval_id</li>
     * <li>proEstablishFile -> pro_establish_file</li>
     * <li>proProcessDocument -> pro_process_document</li>
     * <li>proEndFlag -> pro_end_flag</li>
     * <li>proEndTime -> pro_end_time</li>
     * <li>createdTime -> created_time</li>
     * <li>createdUser -> created_user</li>
     * <li>modifiedTime -> modified_time</li>
     * <li>modifiedUser -> modified_user</li>
     * <li>id -> id</li>
     * <li>projecrManagerId -> projecr_manager_id</li>
     * <li>deptId -> dept_id</li>
     * <li>createdDate -> created_date</li>
     * <li>projectNumber -> project_number</li>
     * <li>projectName -> project_name</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "delFlag": return "del_flag";
            case "discription": return "discription";
            case "belongingPlatform": return "belonging_platform";
            case "belongingPlate": return "belonging_plate";
            case "belongingCentralDept": return "belonging_central_dept";
            case "belongingDept": return "belonging_dept";
            case "mobile": return "mobile";
            case "phone": return "phone";
            case "email": return "email";
            case "proPeriod": return "pro_period";
            case "proEstablishTime": return "pro_establish_time";
            case "proApprovalId": return "pro_approval_id";
            case "proEstablishFile": return "pro_establish_file";
            case "proProcessDocument": return "pro_process_document";
            case "proEndFlag": return "pro_end_flag";
            case "proEndTime": return "pro_end_time";
            case "createdTime": return "created_time";
            case "createdUser": return "created_user";
            case "modifiedTime": return "modified_time";
            case "modifiedUser": return "modified_user";
            case "id": return "id";
            case "projecrManagerId": return "projecr_manager_id";
            case "deptId": return "dept_id";
            case "createdDate": return "created_date";
            case "projectNumber": return "project_number";
            case "projectName": return "project_name";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>del_flag -> delFlag</li>
     * <li>discription -> discription</li>
     * <li>belonging_platform -> belongingPlatform</li>
     * <li>belonging_plate -> belongingPlate</li>
     * <li>belonging_central_dept -> belongingCentralDept</li>
     * <li>belonging_dept -> belongingDept</li>
     * <li>mobile -> mobile</li>
     * <li>phone -> phone</li>
     * <li>email -> email</li>
     * <li>pro_period -> proPeriod</li>
     * <li>pro_establish_time -> proEstablishTime</li>
     * <li>pro_approval_id -> proApprovalId</li>
     * <li>pro_establish_file -> proEstablishFile</li>
     * <li>pro_process_document -> proProcessDocument</li>
     * <li>pro_end_flag -> proEndFlag</li>
     * <li>pro_end_time -> proEndTime</li>
     * <li>created_time -> createdTime</li>
     * <li>created_user -> createdUser</li>
     * <li>modified_time -> modifiedTime</li>
     * <li>modified_user -> modifiedUser</li>
     * <li>id -> id</li>
     * <li>projecr_manager_id -> projecrManagerId</li>
     * <li>dept_id -> deptId</li>
     * <li>created_date -> createdDate</li>
     * <li>project_number -> projectNumber</li>
     * <li>project_name -> projectName</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "del_flag": return "delFlag";
            case "discription": return "discription";
            case "belonging_platform": return "belongingPlatform";
            case "belonging_plate": return "belongingPlate";
            case "belonging_central_dept": return "belongingCentralDept";
            case "belonging_dept": return "belongingDept";
            case "mobile": return "mobile";
            case "phone": return "phone";
            case "email": return "email";
            case "pro_period": return "proPeriod";
            case "pro_establish_time": return "proEstablishTime";
            case "pro_approval_id": return "proApprovalId";
            case "pro_establish_file": return "proEstablishFile";
            case "pro_process_document": return "proProcessDocument";
            case "pro_end_flag": return "proEndFlag";
            case "pro_end_time": return "proEndTime";
            case "created_time": return "createdTime";
            case "created_user": return "createdUser";
            case "modified_time": return "modifiedTime";
            case "modified_user": return "modifiedUser";
            case "id": return "id";
            case "projecr_manager_id": return "projecrManagerId";
            case "dept_id": return "deptId";
            case "created_date": return "createdDate";
            case "project_number": return "projectNumber";
            case "project_name": return "projectName";
            default: return null;
        }
    }
    /**  **/
    public String getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    /**  **/
    public String getDiscription() {
        return this.discription;
    }

    /**  **/
    public void setDiscription(String discription) {
        this.discription = discription;
    }

    /**  **/
    public String getBelongingPlatform() {
        return this.belongingPlatform;
    }

    /**  **/
    public void setBelongingPlatform(String belongingPlatform) {
        this.belongingPlatform = belongingPlatform;
    }

    /**  **/
    public String getBelongingPlate() {
        return this.belongingPlate;
    }

    /**  **/
    public void setBelongingPlate(String belongingPlate) {
        this.belongingPlate = belongingPlate;
    }

    /**  **/
    public String getBelongingCentralDept() {
        return this.belongingCentralDept;
    }

    /**  **/
    public void setBelongingCentralDept(String belongingCentralDept) {
        this.belongingCentralDept = belongingCentralDept;
    }

    /**  **/
    public String getBelongingDept() {
        return this.belongingDept;
    }

    /**  **/
    public void setBelongingDept(String belongingDept) {
        this.belongingDept = belongingDept;
    }

    /**  **/
    public String getMobile() {
        return this.mobile;
    }

    /**  **/
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**  **/
    public String getPhone() {
        return this.phone;
    }

    /**  **/
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**  **/
    public String getEmail() {
        return this.email;
    }

    /**  **/
    public void setEmail(String email) {
        this.email = email;
    }

    /**  **/
    public String getProPeriod() {
        return this.proPeriod;
    }

    /**  **/
    public void setProPeriod(String proPeriod) {
        this.proPeriod = proPeriod;
    }

    /**  **/
    public Date getProEstablishTime() {
        return this.proEstablishTime;
    }

    /**  **/
    public void setProEstablishTime(Date proEstablishTime) {
        this.proEstablishTime = proEstablishTime;
    }

    /**  **/
    public String getProApprovalId() {
        return this.proApprovalId;
    }

    /**  **/
    public void setProApprovalId(String proApprovalId) {
        this.proApprovalId = proApprovalId;
    }

    /**  **/
    public String getProEstablishFile() {
        return this.proEstablishFile;
    }

    /**  **/
    public void setProEstablishFile(String proEstablishFile) {
        this.proEstablishFile = proEstablishFile;
    }

    /**  **/
    public String getProProcessDocument() {
        return this.proProcessDocument;
    }

    /**  **/
    public void setProProcessDocument(String proProcessDocument) {
        this.proProcessDocument = proProcessDocument;
    }

    /**  **/
    public String getProEndFlag() {
        return this.proEndFlag;
    }

    /**  **/
    public void setProEndFlag(String proEndFlag) {
        this.proEndFlag = proEndFlag;
    }

    /**  **/
    public Date getProEndTime() {
        return this.proEndTime;
    }

    /**  **/
    public void setProEndTime(Date proEndTime) {
        this.proEndTime = proEndTime;
    }

    /**  **/
    public Date getCreatedTime() {
        return this.createdTime;
    }

    /**  **/
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**  **/
    public String getCreatedUser() {
        return this.createdUser;
    }

    /**  **/
    public void setCreatedUser(String createdUser) {
        this.createdUser = createdUser;
    }

    /**  **/
    public Date getModifiedTime() {
        return this.modifiedTime;
    }

    /**  **/
    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    /**  **/
    public String getModifiedUser() {
        return this.modifiedUser;
    }

    /**  **/
    public void setModifiedUser(String modifiedUser) {
        this.modifiedUser = modifiedUser;
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
    public String getProjecrManagerId() {
        return this.projecrManagerId;
    }

    /**  **/
    public void setProjecrManagerId(String projecrManagerId) {
        this.projecrManagerId = projecrManagerId;
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
    public Date getCreatedDate() {
        return this.createdDate;
    }

    /**  **/
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**  **/
    public String getProjectNumber() {
        return this.projectNumber;
    }

    /**  **/
    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    /**  **/
    public String getProjectName() {
        return this.projectName;
    }

    /**  **/
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}
