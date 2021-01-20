package com.adc.da.finicialProcess.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>FINANCIAL_PROCESS_TABLE FinancialProcessTableEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-09-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class FinancialProcessTableEO extends BaseEntity {

    private String id;
    private String financialTableName;
    private String createUserId;
    private String createUserName;
    private String receiveUserId;
    private String receiveUserName;
    private String deptId;
    private String deptName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;
    private Integer state;
    private String parentId;
    private String fileId;
    private Integer delFlag;
    private String financialTableType;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String extInfo5;
    private String extInfo6;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>financialTableName -> financial_table_name</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>receiveUserId -> receive_user_id</li>
     * <li>receiveUserName -> receive_user_name</li>
     * <li>deptId -> dept_id</li>
     * <li>deptName -> dept_name</li>
     * <li>createTime -> create_time</li>
     * <li>receiveTime -> receive_time</li>
     * <li>state -> state</li>
     * <li>parentId -> parent_id</li>
     * <li>fileId -> file_id</li>
     * <li>delFlag -> del_flag</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>extInfo5 -> ext_info5</li>
     * <li>extInfo6 -> ext_info6</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "financialTableName": return "financial_table_name";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "receiveUserId": return "receive_user_id";
            case "receiveUserName": return "receive_user_name";
            case "deptId": return "dept_id";
            case "deptName": return "dept_name";
            case "createTime": return "create_time";
            case "receiveTime": return "receive_time";
            case "state": return "state";
            case "parentId": return "parent_id";
            case "fileId": return "file_id";
            case "delFlag": return "del_flag";
            case "extInfo1": return "ext_info1";
            case "extInfo2": return "ext_info2";
            case "extInfo3": return "ext_info3";
            case "extInfo4": return "ext_info4";
            case "extInfo5": return "ext_info5";
            case "extInfo6": return "ext_info6";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>financial_table_name -> financialTableName</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>receive_user_id -> receiveUserId</li>
     * <li>receive_user_name -> receiveUserName</li>
     * <li>dept_id -> deptId</li>
     * <li>dept_name -> deptName</li>
     * <li>create_time -> createTime</li>
     * <li>receive_time -> receiveTime</li>
     * <li>state -> state</li>
     * <li>parent_id -> parentId</li>
     * <li>file_id -> fileId</li>
     * <li>del_flag -> delFlag</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>ext_info5 -> extInfo5</li>
     * <li>ext_info6 -> extInfo6</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "financial_table_name": return "financialTableName";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "receive_user_id": return "receiveUserId";
            case "receive_user_name": return "receiveUserName";
            case "dept_id": return "deptId";
            case "dept_name": return "deptName";
            case "create_time": return "createTime";
            case "receive_time": return "receiveTime";
            case "state": return "state";
            case "parent_id": return "parentId";
            case "file_id": return "fileId";
            case "del_flag": return "delFlag";
            case "ext_info1": return "extInfo1";
            case "ext_info2": return "extInfo2";
            case "ext_info3": return "extInfo3";
            case "ext_info4": return "extInfo4";
            case "ext_info5": return "extInfo5";
            case "ext_info6": return "extInfo6";
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
    public String getFinancialTableName() {
        return this.financialTableName;
    }

    /**  **/
    public void setFinancialTableName(String financialTableName) {
        this.financialTableName = financialTableName;
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
    public String getReceiveUserId() {
        return this.receiveUserId;
    }

    /**  **/
    public void setReceiveUserId(String receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    /**  **/
    public String getReceiveUserName() {
        return this.receiveUserName;
    }

    /**  **/
    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
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
    public String getDeptName() {
        return this.deptName;
    }

    /**  **/
    public void setDeptName(String deptName) {
        this.deptName = deptName;
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
    public Date getReceiveTime() {
        return this.receiveTime;
    }

    /**  **/
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
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
    public String getParentId() {
        return this.parentId;
    }

    /**  **/
    public void setParentId(String parentId) {
        this.parentId = parentId;
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
    public Integer getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
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

    public String getFinancialTableType() {
        return financialTableType;
    }

    public void setFinancialTableType(String financialTableType) {
        this.financialTableType = financialTableType;
    }
}
