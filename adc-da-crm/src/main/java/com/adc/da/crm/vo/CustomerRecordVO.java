package com.adc.da.crm.vo;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.crm.annotation.MatchField;
import com.adc.da.crm.annotation.MatchFieldCollection;
import com.adc.da.crm.entity.CustomerContactEO;
import com.adc.da.crm.entity.CustomerVisitRecordEO;
import com.adc.da.excel.annotation.Excel;
import com.adc.da.excel.annotation.ExcelCollection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <b>功能：</b>CUSTOMER_RECORD CustomerRecordEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class CustomerRecordVO extends BaseEntity {

    private String id;
    @Excel(name = "区域经理", orderNum = "1")
    @MatchField("区域经理")
    private String areaManagerId;
    @Excel(name = "所属部门", orderNum = "2")
    @MatchField("所属部门")
    private String deptId;
    @Excel(name = "建档日期", orderNum = "3", exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy/MM/dd HH:mm:ss")
    @MatchField("建档日期")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdDate;
    @Excel(name = "客户编号", orderNum = "4")
    @MatchField("客户编号")
    private String customerNumber;
    @Excel(name = "客户名称", orderNum = "5")
    @MatchField("客户名称")
    private String customerName;
    @Excel(name = "企业类型", orderNum = "6")
    @MatchField("企业类型")
    private String companyType;
    @Excel(name = "客户来源", orderNum = "7")
    @MatchField("客户来源")
    private String customerSource;
    @Excel(name = "省", orderNum = "8")
    @MatchField("省")
    private String provinceCode;
    @Excel(name = "市", orderNum = "9")
    @MatchField("市")
    private String cityCode;
    @Excel(name = "地址", orderNum = "10")
    @MatchField("地址")
    private String address;
    @Excel(name = "邮编", orderNum = "11")
    @MatchField("邮编")
    private String zip;
    private String delFlag;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    private String createdUser;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;
    private String modifiedUser;
    @ExcelCollection(name="客户联系信息", orderNum = "12")
    @MatchFieldCollection
    private List<CustomerContactEO> customerContactEOList = new ArrayList<>();
    @ExcelCollection(name="客户拜访记录", orderNum = "13")
    @MatchFieldCollection
    private List<CustomerVisitRecordEO> customerVisitRecordEOList = new ArrayList<>();

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>areaManagerId -> area_manager_id</li>
     * <li>deptId -> dept_id</li>
     * <li>createdDate -> created_date</li>
     * <li>customerNumber -> customer_number</li>
     * <li>customerName -> customer_name</li>
     * <li>companyType -> company_type</li>
     * <li>customerSource -> customer_source</li>
     * <li>provinceCode -> province_code</li>
     * <li>cityCode -> city_code</li>
     * <li>address -> address</li>
     * <li>zip -> zip</li>
     * <li>delFlag -> del_flag</li>
     * <li>createdTime -> created_time</li>
     * <li>createdUser -> created_user</li>
     * <li>modifiedTime -> modified_time</li>
     * <li>modifiedUser -> modified_user</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "areaManagerId": return "area_manager_id";
            case "deptId": return "dept_id";
            case "createdDate": return "created_date";
            case "customerNumber": return "customer_number";
            case "customerName": return "customer_name";
            case "companyType": return "company_type";
            case "customerSource": return "customer_source";
            case "provinceCode": return "province_code";
            case "cityCode": return "city_code";
            case "address": return "address";
            case "zip": return "zip";
            case "delFlag": return "del_flag";
            case "createdTime": return "created_time";
            case "createdUser": return "created_user";
            case "modifiedTime": return "modified_time";
            case "modifiedUser": return "modified_user";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>area_manager_id -> areaManagerId</li>
     * <li>dept_id -> deptId</li>
     * <li>created_date -> createdDate</li>
     * <li>customer_number -> customerNumber</li>
     * <li>customer_name -> customerName</li>
     * <li>company_type -> companyType</li>
     * <li>customer_source -> customerSource</li>
     * <li>province_code -> provinceCode</li>
     * <li>city_code -> cityCode</li>
     * <li>address -> address</li>
     * <li>zip -> zip</li>
     * <li>del_flag -> delFlag</li>
     * <li>created_time -> createdTime</li>
     * <li>created_user -> createdUser</li>
     * <li>modified_time -> modifiedTime</li>
     * <li>modified_user -> modifiedUser</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "area_manager_id": return "areaManagerId";
            case "dept_id": return "deptId";
            case "created_date": return "createdDate";
            case "customer_number": return "customerNumber";
            case "customer_name": return "customerName";
            case "company_type": return "companyType";
            case "customer_source": return "customerSource";
            case "province_code": return "provinceCode";
            case "city_code": return "cityCode";
            case "address": return "address";
            case "zip": return "zip";
            case "del_flag": return "delFlag";
            case "created_time": return "createdTime";
            case "created_user": return "createdUser";
            case "modified_time": return "modifiedTime";
            case "modified_user": return "modifiedUser";
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
    public String getAreaManagerId() {
        return this.areaManagerId;
    }

    /**  **/
    public void setAreaManagerId(String areaManagerId) {
        this.areaManagerId = areaManagerId;
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
    public String getCustomerNumber() {
        return this.customerNumber;
    }

    /**  **/
    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    /**  **/
    public String getCustomerName() {
        return this.customerName;
    }

    /**  **/
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**  **/
    public String getCompanyType() {
        return this.companyType;
    }

    /**  **/
    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    /**  **/
    public String getCustomerSource() {
        return this.customerSource;
    }

    /**  **/
    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }

    /**  **/
    public String getProvinceCode() {
        return this.provinceCode;
    }

    /**  **/
    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    /**  **/
    public String getCityCode() {
        return this.cityCode;
    }

    /**  **/
    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    /**  **/
    public String getAddress() {
        return this.address;
    }

    /**  **/
    public void setAddress(String address) {
        this.address = address;
    }

    /**  **/
    public String getZip() {
        return this.zip;
    }

    /**  **/
    public void setZip(String zip) {
        this.zip = zip;
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

    public List<CustomerContactEO> getCustomerContactEOList() {
        return customerContactEOList;
    }

    public void setCustomerContactEOList(List<CustomerContactEO> customerContactEOList) {
        this.customerContactEOList = customerContactEOList;
    }

    public List<CustomerVisitRecordEO> getCustomerVisitRecordEOList() {
        return customerVisitRecordEOList;
    }

    public void setCustomerVisitRecordEOList(List<CustomerVisitRecordEO> customerVisitRecordEOList) {
        this.customerVisitRecordEOList = customerVisitRecordEOList;
    }
}
