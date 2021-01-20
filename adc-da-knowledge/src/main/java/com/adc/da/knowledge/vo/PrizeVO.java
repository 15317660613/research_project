package com.adc.da.knowledge.vo;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>K_PRIZE PrizeEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class PrizeVO extends BaseEntity {

    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;
    private String extInfo6;
    private String id;
    private String num;
    private String prizeName;
    private String projectName;
    private String level;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date prizeTime;
    private String issuedDept;
    private String belongedUserName;
    private String belongedUserId;
    private String deptName;
    private String deptId;

    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String uploadUserName;
    private String uploadUserId;
    private String fileId;
    private String fileName;
    private String autoNumber;
    private String prizeAbstract;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>extInfo1 -> ext_info1_</li>
     * <li>extInfo2 -> ext_info2_</li>
     * <li>extInfo3 -> ext_info3_</li>
     * <li>extInfo4 -> ext_info4_</li>
     * <li>extInfo5 -> ext_info5_</li>
     * <li>extInfo6 -> ext_info6_</li>
     * <li>id -> id_</li>
     * <li>num -> num_</li>
     * <li>prizeName -> prize_name_</li>
     * <li>projectName -> project_name_</li>
     * <li>level -> level_</li>
     * <li>prizeTime -> prize_time_</li>
     * <li>issuedDept -> issued_dept_</li>
     * <li>belongedUserName -> belonged_user_name_</li>
     * <li>belongedUserId -> belonged_user_id_</li>
     * <li>deptName -> dept_name_</li>
     * <li>deptId -> dept_id_</li>
     * <li>updateTime -> update_time_</li>
     * <li>uploadUserName -> upload_user_name_</li>
     * <li>uploadUserId -> upload_user_id_</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "extInfo1": return "ext_info1_";
            case "extInfo2": return "ext_info2_";
            case "extInfo3": return "ext_info3_";
            case "extInfo4": return "ext_info4_";
            case "extInfo5": return "ext_info5_";
            case "extInfo6": return "ext_info6_";
            case "id": return "id_";
            case "num": return "num_";
            case "prizeName": return "prize_name_";
            case "projectName": return "project_name_";
            case "level": return "level_";
            case "prizeTime": return "prize_time_";
            case "issuedDept": return "issued_dept_";
            case "belongedUserName": return "belonged_user_name_";
            case "belongedUserId": return "belonged_user_id_";
            case "deptName": return "dept_name_";
            case "deptId": return "dept_id_";
            case "updateTime": return "update_time_";
            case "uploadUserName": return "upload_user_name_";
            case "uploadUserId": return "upload_user_id_";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>ext_info1_ -> extInfo1</li>
     * <li>ext_info2_ -> extInfo2</li>
     * <li>ext_info3_ -> extInfo3</li>
     * <li>ext_info4_ -> extInfo4</li>
     * <li>ext_info5_ -> extInfo5</li>
     * <li>ext_info6_ -> extInfo6</li>
     * <li>id_ -> id</li>
     * <li>num_ -> num</li>
     * <li>prize_name_ -> prizeName</li>
     * <li>project_name_ -> projectName</li>
     * <li>level_ -> level</li>
     * <li>prize_time_ -> prizeTime</li>
     * <li>issued_dept_ -> issuedDept</li>
     * <li>belonged_user_name_ -> belongedUserName</li>
     * <li>belonged_user_id_ -> belongedUserId</li>
     * <li>dept_name_ -> deptName</li>
     * <li>dept_id_ -> deptId</li>
     * <li>update_time_ -> updateTime</li>
     * <li>upload_user_name_ -> uploadUserName</li>
     * <li>upload_user_id_ -> uploadUserId</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "ext_info1_": return "extInfo1";
            case "ext_info2_": return "extInfo2";
            case "ext_info3_": return "extInfo3";
            case "ext_info4_": return "extInfo4";
            case "ext_info5_": return "extInfo5";
            case "ext_info6_": return "extInfo6";
            case "id_": return "id";
            case "num_": return "num";
            case "prize_name_": return "prizeName";
            case "project_name_": return "projectName";
            case "level_": return "level";
            case "prize_time_": return "prizeTime";
            case "issued_dept_": return "issuedDept";
            case "belonged_user_name_": return "belongedUserName";
            case "belonged_user_id_": return "belongedUserId";
            case "dept_name_": return "deptName";
            case "dept_id_": return "deptId";
            case "update_time_": return "updateTime";
            case "upload_user_name_": return "uploadUserName";
            case "upload_user_id_": return "uploadUserId";
            default: return null;
        }
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
    public String getNum() {
        return this.num;
    }

    /**  **/
    public void setNum(String num) {
        this.num = num;
    }

    /**  **/
    public String getPrizeName() {
        return this.prizeName;
    }

    /**  **/
    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    /**  **/
    public String getProjectName() {
        return this.projectName;
    }

    /**  **/
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**  **/
    public String getLevel() {
        return this.level;
    }

    /**  **/
    public void setLevel(String level) {
        this.level = level;
    }

    /**  **/
    public Date getPrizeTime() {
        return this.prizeTime;
    }

    /**  **/
    public void setPrizeTime(Date prizeTime) {
        this.prizeTime = prizeTime;
    }

    /**  **/
    public String getIssuedDept() {
        return this.issuedDept;
    }

    /**  **/
    public void setIssuedDept(String issuedDept) {
        this.issuedDept = issuedDept;
    }

    /**  **/
    public String getBelongedUserName() {
        return this.belongedUserName;
    }

    /**  **/
    public void setBelongedUserName(String belongedUserName) {
        this.belongedUserName = belongedUserName;
    }

    /**  **/
    public String getBelongedUserId() {
        return this.belongedUserId;
    }

    /**  **/
    public void setBelongedUserId(String belongedUserId) {
        this.belongedUserId = belongedUserId;
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
    public String getDeptId() {
        return this.deptId;
    }

    /**  **/
    public void setDeptId(String deptId) {
        this.deptId = deptId;
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
    public String getUploadUserName() {
        return this.uploadUserName;
    }

    /**  **/
    public void setUploadUserName(String uploadUserName) {
        this.uploadUserName = uploadUserName;
    }

    /**  **/
    public String getUploadUserId() {
        return this.uploadUserId;
    }

    /**  **/
    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId;
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

    public String getPrizeAbstract() {
        return prizeAbstract;
    }

    public void setPrizeAbstract(String prizeAbstract) {
        this.prizeAbstract = prizeAbstract;
    }
}
