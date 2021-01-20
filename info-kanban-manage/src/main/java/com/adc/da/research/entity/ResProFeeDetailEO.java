package com.adc.da.research.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.adc.da.base.entity.BaseEntity;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>DB_RES_PRO_FEE_DETAIL ResProFeeDetailEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ResProFeeDetailEO extends BaseEntity implements IExcelModel, IExcelDataModel {

    private String errorMsg;

    private int rowNum;
    @Override
    public int getRowNum() {
        return this.rowNum;
    }

    @Override
    public void setRowNum(int i) {
        this.rowNum = i;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    @Override
    public void setErrorMsg(String s) {
        this.errorMsg = s;
    }

    private String id;
    @Excel(name = "科研项目ID",orderNum = "1")
    @Length(min=1, max=20,message = "科研项目ID长度不能为空且小于20字符")
    @NotNull(message = "科研项目ID长度不能为空且小于20字符")
    private String researchProjectId;
    @Excel(name = "科研项目编号",orderNum = "2")
    @Length(min=1, max=20,message = "项目编号长度不能为空且小于20字符")
    @NotNull(message = "科研项目编号长度不能为空且小于20字符")
    private String projectNo;
    @Excel(name = "费用类型ID",orderNum = "3")
    @Length(min=1, max=20,message = "费用类型ID长度不能为空且小于20字符")
    @NotNull(message = "费用类型ID长度不能为空且小于20字符")
    private String fundsType;


    private String fundsTypeId;

    @Excel(name = "年份",orderNum = "4")
    private Integer fundsYear;
    @Excel(name = "月份",orderNum = "5")
    private Integer fundsMonth;

    @Excel(name = "购置设备费",orderNum = "6",groupName = "设备费" )
    @Digits(integer=12, fraction=6,message = "购置设备费整数位长度小于12位，且小数位小于6位")
    private BigDecimal equipPurchaseFee;
    @Excel(name = "试制设备费",orderNum = "7",groupName = "设备费" )
    @Digits(integer=12, fraction=6,message = "试制设备费整数位长度小于12位，且小数位小于6位")
    private BigDecimal equipTestCreateFee;
    @Excel(name = "设备租赁费",orderNum = "8",groupName = "设备费" )
    @Digits(integer=12, fraction=6,message = "设备租赁费整数位长度小于12位，且小数位小于6位")
    private BigDecimal equipRentFee;

    @Excel(name = "材料费",orderNum = "9")
    @Digits(integer=12, fraction=6,message = "材料费费整数位长度小于12位，且小数位小于6位")
    private BigDecimal materialFee;
    @Excel(name = "测试化验加工费",orderNum = "10")
    @Digits(integer=12, fraction=6,message = "测试化验加工费整数位长度小于12位，且小数位小于6位")
    private BigDecimal testProcessFee;
    @Excel(name = "燃料动力费",orderNum = "11")
    @Digits(integer=12, fraction=6,message = "燃料动力费整数位长度小于12位，且小数位小于6位")
    private BigDecimal fuelPowerFee;
    @Excel(name = "差旅费",orderNum = "12")
    @Digits(integer=12, fraction=6,message = "差旅费整数位长度小于12位，且小数位小于6位")
    private BigDecimal travelFee;
    @Excel(name = "会议费",orderNum = "13")
    @Digits(integer=12, fraction=6,message = "会议费整数位长度小于12位，且小数位小于6位")
    private BigDecimal meetingFee;
    @Excel(name = "国际合作交流费",orderNum = "14")
    @Digits(integer=12, fraction=6,message = "国际合作交流费整数位长度小于12位，且小数位小于6位")
    private BigDecimal internationExchangeFee;

    @Excel(name = "软件购置费",orderNum = "15",groupName="知识产权事务费")
    @Digits(integer=12, fraction=6,message = "软件购置费整数位长度小于12位，且小数位小于6位")
    private BigDecimal kSoftFee;
    @Excel(name = "除软件购置费之外的其他费用",orderNum = "16",groupName="知识产权事务费")
    @Digits(integer=12, fraction=6,message = "除软件购置费之外的其他费用整数位长度小于12位，且小数位小于6位")
    private BigDecimal kOtherFee;

    @Excel(name = "劳务费",orderNum = "17")
    @Digits(integer=12, fraction=6,message = "劳务费整数位长度小于12位，且小数位小于6位")
    private BigDecimal laborFee;
    @Excel(name = "专家咨询费",orderNum = "18")
    @Digits(integer=12, fraction=6,message = "专家咨询费整数位长度小于12位，且小数位小于6位")
    private BigDecimal expertConsultFee;
    @Excel(name = "外协费",orderNum = "19")
    @Digits(integer=12, fraction=6,message = "外协费整数位长度小于12位，且小数位小于6位")
    private BigDecimal externalAssistFee;
    @Excel(name = "人工费",orderNum = "20")
    @Digits(integer=12, fraction=6,message = "人工费整数位长度小于12位，且小数位小于6位")
    private BigDecimal personFee;

    @Excel(name = "管理费",orderNum = "21")
    @Digits(integer=12, fraction=6,message = "管理费整数位长度小于12位，且小数位小于6位")
    private BigDecimal manageFee;
    @Excel(name = "其他费用",orderNum = "22")
    @Digits(integer=12, fraction=6,message = "其他费用整数位长度小于12位，且小数位小于6位")
    private BigDecimal otherFee;
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String updateUserId;
    private String updateUserName;
    private String projectName ;
    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectNo -> project_no</li>
     * <li>fundsType -> funds_type</li>
     * <li>fundsTypeId -> funds_type_id</li>
     * <li>fundsYear -> funds_year</li>
     * <li>fundsMonth -> funds_month</li>
     * <li>equipPurchaseFee -> equip_purchase_fee</li>
     * <li>equipTestCreateFee -> equip_test_create_fee</li>
     * <li>equipRentFee -> equip_rent_fee</li>
     * <li>materialFee -> material_fee</li>
     * <li>testProcessFee -> test_process_fee</li>
     * <li>fuelPowerFee -> fuel_power_fee</li>
     * <li>travelFee -> travel_fee</li>
     * <li>meetingFee -> meeting_fee</li>
     * <li>internationExchangeFee -> internation_exchange_fee</li>
     * <li>kSoftFee -> k_soft_fee</li>
     * <li>kOtherFee -> k_other_fee</li>
     * <li>laborFee -> labor_fee</li>
     * <li>expertConsultFee -> expert_consult_fee</li>
     * <li>externalAssistFee -> external_assist_fee</li>
     * <li>manageFee -> manage_fee</li>
     * <li>otherFee -> other_fee</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>projectNoUnique -> project_no_unique</li>
     * <li>createTime -> create_time</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>updateTime -> update_time</li>
     * <li>updateUserId -> update_user_id</li>
     * <li>updateUserName -> update_user_name</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "projectNo": return "project_no";
            case "fundsType": return "funds_type";
            case "fundsTypeId": return "funds_type_id";
            case "fundsYear": return "funds_year";
            case "fundsMonth": return "funds_month";
            case "equipPurchaseFee": return "equip_purchase_fee";
            case "equipTestCreateFee": return "equip_test_create_fee";
            case "equipRentFee": return "equip_rent_fee";
            case "materialFee": return "material_fee";
            case "testProcessFee": return "test_process_fee";
            case "fuelPowerFee": return "fuel_power_fee";
            case "travelFee": return "travel_fee";
            case "meetingFee": return "meeting_fee";
            case "internationExchangeFee": return "internation_exchange_fee";
            case "kSoftFee": return "k_soft_fee";
            case "kOtherFee": return "k_other_fee";
            case "laborFee": return "labor_fee";
            case "expertConsultFee": return "expert_consult_fee";
            case "externalAssistFee": return "external_assist_fee";
            case "manageFee": return "manage_fee";
            case "otherFee": return "other_fee";
            case "extInfo1": return "ext_info1";
            case "extInfo2": return "ext_info2";
            case "extInfo3": return "ext_info3";
            case "extInfo4": return "ext_info4";
            case "researchProjectId": return "research_project_id";
            case "createTime": return "create_time";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "updateTime": return "update_time";
            case "updateUserId": return "update_user_id";
            case "updateUserName": return "update_user_name";
            case "personFee": return "person_fee";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_no -> projectNo</li>
     * <li>funds_type -> fundsType</li>
     * <li>funds_type_id -> fundsTypeId</li>
     * <li>funds_year -> fundsYear</li>
     * <li>funds_month -> fundsMonth</li>
     * <li>equip_purchase_fee -> equipPurchaseFee</li>
     * <li>equip_test_create_fee -> equipTestCreateFee</li>
     * <li>equip_rent_fee -> equipRentFee</li>
     * <li>material_fee -> materialFee</li>
     * <li>test_process_fee -> testProcessFee</li>
     * <li>fuel_power_fee -> fuelPowerFee</li>
     * <li>travel_fee -> travelFee</li>
     * <li>meeting_fee -> meetingFee</li>
     * <li>internation_exchange_fee -> internationExchangeFee</li>
     * <li>k_soft_fee -> kSoftFee</li>
     * <li>k_other_fee -> kOtherFee</li>
     * <li>labor_fee -> laborFee</li>
     * <li>expert_consult_fee -> expertConsultFee</li>
     * <li>external_assist_fee -> externalAssistFee</li>
     * <li>manage_fee -> manageFee</li>
     * <li>other_fee -> otherFee</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>project_no_unique -> projectNoUnique</li>
     * <li>create_time -> createTime</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>update_time -> updateTime</li>
     * <li>update_user_id -> updateUserId</li>
     * <li>update_user_name -> updateUserName</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "project_no": return "projectNo";
            case "funds_type": return "fundsType";
            case "funds_type_id": return "fundsTypeId";
            case "funds_year": return "fundsYear";
            case "funds_month": return "fundsMonth";
            case "equip_purchase_fee": return "equipPurchaseFee";
            case "equip_test_create_fee": return "equipTestCreateFee";
            case "equip_rent_fee": return "equipRentFee";
            case "material_fee": return "materialFee";
            case "test_process_fee": return "testProcessFee";
            case "fuel_power_fee": return "fuelPowerFee";
            case "travel_fee": return "travelFee";
            case "meeting_fee": return "meetingFee";
            case "internation_exchange_fee": return "internationExchangeFee";
            case "k_soft_fee": return "kSoftFee";
            case "k_other_fee": return "kOtherFee";
            case "labor_fee": return "laborFee";
            case "expert_consult_fee": return "expertConsultFee";
            case "external_assist_fee": return "externalAssistFee";
            case "manage_fee": return "manageFee";
            case "other_fee": return "otherFee";
            case "ext_info1": return "extInfo1";
            case "ext_info2": return "extInfo2";
            case "ext_info3": return "extInfo3";
            case "ext_info4": return "extInfo4";
            case "research_project_id": return "researchProjectId";
            case "create_time": return "createTime";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "update_time": return "updateTime";
            case "update_user_id": return "updateUserId";
            case "update_user_name": return "updateUserName";
            case "person_fee": return "personFee";
            default: return null;
        }
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResearchProjectId() {
        return researchProjectId;
    }

    public void setResearchProjectId(String researchProjectId) {
        this.researchProjectId = researchProjectId;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getFundsType() {
        return fundsType;
    }

    public void setFundsType(String fundsType) {
        this.fundsType = fundsType;
    }

    public String getFundsTypeId() {
        return fundsTypeId;
    }

    public void setFundsTypeId(String fundsTypeId) {
        this.fundsTypeId = fundsTypeId;
    }

    public Integer getFundsYear() {
        return fundsYear;
    }

    public void setFundsYear(Integer fundsYear) {
        this.fundsYear = fundsYear;
    }

    public Integer getFundsMonth() {
        return fundsMonth;
    }

    public void setFundsMonth(Integer fundsMonth) {
        this.fundsMonth = fundsMonth;
    }

    public BigDecimal getEquipPurchaseFee() {
        return equipPurchaseFee;
    }

    public void setEquipPurchaseFee(BigDecimal equipPurchaseFee) {
        this.equipPurchaseFee = equipPurchaseFee;
    }

    public BigDecimal getEquipTestCreateFee() {
        return equipTestCreateFee;
    }

    public void setEquipTestCreateFee(BigDecimal equipTestCreateFee) {
        this.equipTestCreateFee = equipTestCreateFee;
    }

    public BigDecimal getEquipRentFee() {
        return equipRentFee;
    }

    public void setEquipRentFee(BigDecimal equipRentFee) {
        this.equipRentFee = equipRentFee;
    }

    public BigDecimal getMaterialFee() {
        return materialFee;
    }

    public void setMaterialFee(BigDecimal materialFee) {
        this.materialFee = materialFee;
    }

    public BigDecimal getTestProcessFee() {
        return testProcessFee;
    }

    public void setTestProcessFee(BigDecimal testProcessFee) {
        this.testProcessFee = testProcessFee;
    }

    public BigDecimal getFuelPowerFee() {
        return fuelPowerFee;
    }

    public void setFuelPowerFee(BigDecimal fuelPowerFee) {
        this.fuelPowerFee = fuelPowerFee;
    }

    public BigDecimal getTravelFee() {
        return travelFee;
    }

    public void setTravelFee(BigDecimal travelFee) {
        this.travelFee = travelFee;
    }

    public BigDecimal getMeetingFee() {
        return meetingFee;
    }

    public void setMeetingFee(BigDecimal meetingFee) {
        this.meetingFee = meetingFee;
    }

    public BigDecimal getInternationExchangeFee() {
        return internationExchangeFee;
    }

    public void setInternationExchangeFee(BigDecimal internationExchangeFee) {
        this.internationExchangeFee = internationExchangeFee;
    }

    public BigDecimal getkSoftFee() {
        return kSoftFee;
    }

    public void setkSoftFee(BigDecimal kSoftFee) {
        this.kSoftFee = kSoftFee;
    }

    public BigDecimal getkOtherFee() {
        return kOtherFee;
    }

    public void setkOtherFee(BigDecimal kOtherFee) {
        this.kOtherFee = kOtherFee;
    }

    public BigDecimal getLaborFee() {
        return laborFee;
    }

    public void setLaborFee(BigDecimal laborFee) {
        this.laborFee = laborFee;
    }

    public BigDecimal getExpertConsultFee() {
        return expertConsultFee;
    }

    public void setExpertConsultFee(BigDecimal expertConsultFee) {
        this.expertConsultFee = expertConsultFee;
    }

    public BigDecimal getExternalAssistFee() {
        return externalAssistFee;
    }

    public void setExternalAssistFee(BigDecimal externalAssistFee) {
        this.externalAssistFee = externalAssistFee;
    }

    public BigDecimal getPersonFee() {
        return personFee;
    }

    public void setPersonFee(BigDecimal personFee) {
        this.personFee = personFee;
    }

    public BigDecimal getManageFee() {
        return manageFee;
    }

    public void setManageFee(BigDecimal manageFee) {
        this.manageFee = manageFee;
    }

    public BigDecimal getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(BigDecimal otherFee) {
        this.otherFee = otherFee;
    }

    public String getExtInfo1() {
        return extInfo1;
    }

    public void setExtInfo1(String extInfo1) {
        this.extInfo1 = extInfo1;
    }

    public String getExtInfo2() {
        return extInfo2;
    }

    public void setExtInfo2(String extInfo2) {
        this.extInfo2 = extInfo2;
    }

    public String getExtInfo3() {
        return extInfo3;
    }

    public void setExtInfo3(String extInfo3) {
        this.extInfo3 = extInfo3;
    }

    public String getExtInfo4() {
        return extInfo4;
    }

    public void setExtInfo4(String extInfo4) {
        this.extInfo4 = extInfo4;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }
}
