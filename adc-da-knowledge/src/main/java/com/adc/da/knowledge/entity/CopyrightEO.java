package com.adc.da.knowledge.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;

import java.util.Date;

/**
 * <b>功能：</b>K_COPYRIGHT CopyrightEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class CopyrightEO extends BaseEntity {

    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;
    private String extInfo6;

    private String id;
    @Excel(name = "软件名称",orderNum = "2")
    private String softwareName;
    @Excel(name = "类型",orderNum = "3")
    private String copyrightType;
    @Excel(name = "申请日期",orderNum = "4",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date applyDate;
    @Excel(name = "登记日期",orderNum = "5",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date registerDate;
    @Excel(name = "登记号",orderNum = "6")
    private String registerNum;
    @Excel(name = "状态",orderNum = "7")
    private String state;
    private String belongUserId;
    @Excel(name = "部门",orderNum = "9")
    private String deptName;
    private String deptId;
    @Excel(name = "上传人",orderNum = "10")
    private String uploadUserName;
    private String uploadUserId;
    @Excel(name = "更新时间",orderNum = "11",exportFormat = "yyyy-MM-dd HH:mm:ss")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String num;
    @Excel(name = "著作权人",orderNum = "8")
    private String belongUserName;
    @Excel(name = "编号",orderNum = "1")
    private String autoNumber;

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
     * <li>softwareName -> software_name_</li>
     * <li>copyrightType -> copyright_type_</li>
     * <li>applyDate -> apply_date_</li>
     * <li>registerDate -> register_date_</li>
     * <li>registerNum -> register_num_</li>
     * <li>state -> state_</li>
     * <li>belongUserId -> belong_user_id_</li>
     * <li>deptName -> dept_name_</li>
     * <li>deptId -> dept_id_</li>
     * <li>uploadUserName -> upload_user_name_</li>
     * <li>uploadUserId -> upload_user_id_</li>
     * <li>updateTime -> update_time_</li>
     * <li>num -> num_</li>
     * <li>belongUserName -> belong_user_name_</li>
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
            case "softwareName": return "software_name_";
            case "copyrightType": return "copyright_type_";
            case "applyDate": return "apply_date_";
            case "registerDate": return "register_date_";
            case "registerNum": return "register_num_";
            case "state": return "state_";
            case "belongUserId": return "belong_user_id_";
            case "deptName": return "dept_name_";
            case "deptId": return "dept_id_";
            case "uploadUserName": return "upload_user_name_";
            case "uploadUserId": return "upload_user_id_";
            case "updateTime": return "update_time_";
            case "num": return "num_";
            case "belongUserName": return "belong_user_name_";
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
     * <li>software_name_ -> softwareName</li>
     * <li>copyright_type_ -> copyrightType</li>
     * <li>apply_date_ -> applyDate</li>
     * <li>register_date_ -> registerDate</li>
     * <li>register_num_ -> registerNum</li>
     * <li>state_ -> state</li>
     * <li>belong_user_id_ -> belongUserId</li>
     * <li>dept_name_ -> deptName</li>
     * <li>dept_id_ -> deptId</li>
     * <li>upload_user_name_ -> uploadUserName</li>
     * <li>upload_user_id_ -> uploadUserId</li>
     * <li>update_time_ -> updateTime</li>
     * <li>num_ -> num</li>
     * <li>belong_user_name_ -> belongUserName</li>
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
            case "software_name_": return "softwareName";
            case "copyright_type_": return "copyrightType";
            case "apply_date_": return "applyDate";
            case "register_date_": return "registerDate";
            case "register_num_": return "registerNum";
            case "state_": return "state";
            case "belong_user_id_": return "belongUserId";
            case "dept_name_": return "deptName";
            case "dept_id_": return "deptId";
            case "upload_user_name_": return "uploadUserName";
            case "upload_user_id_": return "uploadUserId";
            case "update_time_": return "updateTime";
            case "num_": return "num";
            case "belong_user_name_": return "belongUserName";
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
    public String getSoftwareName() {
        return this.softwareName;
    }

    /**  **/
    public void setSoftwareName(String softwareName) {
        this.softwareName = softwareName;
    }

    /**  **/
    public String getCopyrightType() {
        return this.copyrightType;
    }

    /**  **/
    public void setCopyrightType(String copyrightType) {
        this.copyrightType = copyrightType;
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
    public Date getRegisterDate() {
        return this.registerDate;
    }

    /**  **/
    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    /**  **/
    public String getRegisterNum() {
        return this.registerNum;
    }

    /**  **/
    public void setRegisterNum(String registerNum) {
        this.registerNum = registerNum;
    }

    /**  **/
    public String getState() {
        return this.state;
    }

    /**  **/
    public void setState(String state) {
        this.state = state;
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

    /**  **/
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**  **/
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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
    public String getBelongUserName() {
        return this.belongUserName;
    }

    /**  **/
    public void setBelongUserName(String belongUserName) {
        this.belongUserName = belongUserName;
    }

    public String getAutoNumber() {
        return autoNumber;
    }

    public void setAutoNumber(String autoNumber) {
        this.autoNumber = autoNumber;
    }
}
