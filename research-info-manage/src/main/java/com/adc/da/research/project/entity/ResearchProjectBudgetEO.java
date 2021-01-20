package com.adc.da.research.project.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>RS_RESEARCH_PROJECT_BUDGET ResearchProjectBudgetEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-10 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ResearchProjectBudgetEO extends BaseEntity {

    private String id;
    private String projectId;
    private String contractId;
    private String budgetType;
    private String purchaseEquipment;
    private String trialEquipment;
    private String leaseEquipment;
    private String materials;
    private String testMachining;
    private String fuelPower;
    private String travel;
    private String meeting;
    private String international;
    private String intellectualProperty;
    private String software;
    private String labor;
    private String expertAdvice;
    private String outsourcing;
    private String management;
    private String budgetYear;
    private String budgetQuarterly;
    private String budgetMonth;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private String delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private String ext6;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>contractId -> contract_id</li>
     * <li>budgetType -> budget_type</li>
     * <li>purchaseEquipment -> purchase_equipment</li>
     * <li>trialEquipment -> trial_equipment</li>
     * <li>leaseEquipment -> lease_equipment</li>
     * <li>materials -> materials</li>
     * <li>testMachining -> test_machining</li>
     * <li>fuelPower -> fuel_power</li>
     * <li>travel -> travel</li>
     * <li>meeting -> meeting</li>
     * <li>international -> international</li>
     * <li>intellectualProperty -> intellectual_property</li>
     * <li>software -> software</li>
     * <li>labor -> labor</li>
     * <li>expertAdvice -> expert_advice</li>
     * <li>outsourcing -> outsourcing</li>
     * <li>management -> management</li>
     * <li>budgetYear -> budget_year</li>
     * <li>budgetQuarterly -> budget_quarterly</li>
     * <li>budgetMonth -> budget_month</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>createTime -> create_time</li>
     * <li>modifyTime -> modify_time</li>
     * <li>delFlag -> del_flag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     * <li>ext6 -> ext6</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "projectId": return "project_id";
            case "contractId": return "contract_id";
            case "budgetType": return "budget_type";
            case "purchaseEquipment": return "purchase_equipment";
            case "trialEquipment": return "trial_equipment";
            case "leaseEquipment": return "lease_equipment";
            case "materials": return "materials";
            case "testMachining": return "test_machining";
            case "fuelPower": return "fuel_power";
            case "travel": return "travel";
            case "meeting": return "meeting";
            case "international": return "international";
            case "intellectualProperty": return "intellectual_property";
            case "software": return "software";
            case "labor": return "labor";
            case "expertAdvice": return "expert_advice";
            case "outsourcing": return "outsourcing";
            case "management": return "management";
            case "budgetYear": return "budget_year";
            case "budgetQuarterly": return "budget_quarterly";
            case "budgetMonth": return "budget_month";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "createTime": return "create_time";
            case "modifyTime": return "modify_time";
            case "delFlag": return "del_flag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            case "ext6": return "ext6";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>project_id -> projectId</li>
     * <li>contract_id -> contractId</li>
     * <li>budget_type -> budgetType</li>
     * <li>purchase_equipment -> purchaseEquipment</li>
     * <li>trial_equipment -> trialEquipment</li>
     * <li>lease_equipment -> leaseEquipment</li>
     * <li>materials -> materials</li>
     * <li>test_machining -> testMachining</li>
     * <li>fuel_power -> fuelPower</li>
     * <li>travel -> travel</li>
     * <li>meeting -> meeting</li>
     * <li>international -> international</li>
     * <li>intellectual_property -> intellectualProperty</li>
     * <li>software -> software</li>
     * <li>labor -> labor</li>
     * <li>expert_advice -> expertAdvice</li>
     * <li>outsourcing -> outsourcing</li>
     * <li>management -> management</li>
     * <li>budget_year -> budgetYear</li>
     * <li>budget_quarterly -> budgetQuarterly</li>
     * <li>budget_month -> budgetMonth</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>create_time -> createTime</li>
     * <li>modify_time -> modifyTime</li>
     * <li>del_flag -> delFlag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     * <li>ext6 -> ext6</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "project_id": return "projectId";
            case "contract_id": return "contractId";
            case "budget_type": return "budgetType";
            case "purchase_equipment": return "purchaseEquipment";
            case "trial_equipment": return "trialEquipment";
            case "lease_equipment": return "leaseEquipment";
            case "materials": return "materials";
            case "test_machining": return "testMachining";
            case "fuel_power": return "fuelPower";
            case "travel": return "travel";
            case "meeting": return "meeting";
            case "international": return "international";
            case "intellectual_property": return "intellectualProperty";
            case "software": return "software";
            case "labor": return "labor";
            case "expert_advice": return "expertAdvice";
            case "outsourcing": return "outsourcing";
            case "management": return "management";
            case "budget_year": return "budgetYear";
            case "budget_quarterly": return "budgetQuarterly";
            case "budget_month": return "budgetMonth";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "create_time": return "createTime";
            case "modify_time": return "modifyTime";
            case "del_flag": return "delFlag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            case "ext6": return "ext6";
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
    public String getProjectId() {
        return this.projectId;
    }

    /**  **/
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**  **/
    public String getContractId() {
        return this.contractId;
    }

    /**  **/
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    /**  **/
    public String getBudgetType() {
        return this.budgetType;
    }

    /**  **/
    public void setBudgetType(String budgetType) {
        this.budgetType = budgetType;
    }

    /**  **/
    public String getPurchaseEquipment() {
        return this.purchaseEquipment;
    }

    /**  **/
    public void setPurchaseEquipment(String purchaseEquipment) {
        this.purchaseEquipment = purchaseEquipment;
    }

    /**  **/
    public String getTrialEquipment() {
        return this.trialEquipment;
    }

    /**  **/
    public void setTrialEquipment(String trialEquipment) {
        this.trialEquipment = trialEquipment;
    }

    /**  **/
    public String getLeaseEquipment() {
        return this.leaseEquipment;
    }

    /**  **/
    public void setLeaseEquipment(String leaseEquipment) {
        this.leaseEquipment = leaseEquipment;
    }

    /**  **/
    public String getMaterials() {
        return this.materials;
    }

    /**  **/
    public void setMaterials(String materials) {
        this.materials = materials;
    }

    /**  **/
    public String getTestMachining() {
        return this.testMachining;
    }

    /**  **/
    public void setTestMachining(String testMachining) {
        this.testMachining = testMachining;
    }

    /**  **/
    public String getFuelPower() {
        return this.fuelPower;
    }

    /**  **/
    public void setFuelPower(String fuelPower) {
        this.fuelPower = fuelPower;
    }

    /**  **/
    public String getTravel() {
        return this.travel;
    }

    /**  **/
    public void setTravel(String travel) {
        this.travel = travel;
    }

    /**  **/
    public String getMeeting() {
        return this.meeting;
    }

    /**  **/
    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }

    /**  **/
    public String getInternational() {
        return this.international;
    }

    /**  **/
    public void setInternational(String international) {
        this.international = international;
    }

    /**  **/
    public String getIntellectualProperty() {
        return this.intellectualProperty;
    }

    /**  **/
    public void setIntellectualProperty(String intellectualProperty) {
        this.intellectualProperty = intellectualProperty;
    }

    /**  **/
    public String getSoftware() {
        return this.software;
    }

    /**  **/
    public void setSoftware(String software) {
        this.software = software;
    }

    /**  **/
    public String getLabor() {
        return this.labor;
    }

    /**  **/
    public void setLabor(String labor) {
        this.labor = labor;
    }

    /**  **/
    public String getExpertAdvice() {
        return this.expertAdvice;
    }

    /**  **/
    public void setExpertAdvice(String expertAdvice) {
        this.expertAdvice = expertAdvice;
    }

    /**  **/
    public String getOutsourcing() {
        return this.outsourcing;
    }

    /**  **/
    public void setOutsourcing(String outsourcing) {
        this.outsourcing = outsourcing;
    }

    /**  **/
    public String getManagement() {
        return this.management;
    }

    /**  **/
    public void setManagement(String management) {
        this.management = management;
    }

    /**  **/
    public String getBudgetYear() {
        return this.budgetYear;
    }

    /**  **/
    public void setBudgetYear(String budgetYear) {
        this.budgetYear = budgetYear;
    }

    /**  **/
    public String getBudgetQuarterly() {
        return this.budgetQuarterly;
    }

    /**  **/
    public void setBudgetQuarterly(String budgetQuarterly) {
        this.budgetQuarterly = budgetQuarterly;
    }

    /**  **/
    public String getBudgetMonth() {
        return this.budgetMonth;
    }

    /**  **/
    public void setBudgetMonth(String budgetMonth) {
        this.budgetMonth = budgetMonth;
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
    public String getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(String delFlag) {
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

    /**  **/
    public String getExt6() {
        return this.ext6;
    }

    /**  **/
    public void setExt6(String ext6) {
        this.ext6 = ext6;
    }

}
