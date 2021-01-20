package com.adc.da.research.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.adc.da.base.entity.BaseEntity;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;


import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <b>功能：</b>DB_RESEARCH_PROJECT ResearchProjectEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ResearchProjectEO extends BaseEntity implements IExcelModel, IExcelDataModel {

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

    private String updateUserId;
    private String updateUserName;
    @Excel(name = "风险等级",orderNum = "22")
    @NotNull
    private String extInfo1;
    private String extInfo2;
    private String extInfo3;
    private String extInfo4;
    private String leaderOrgId;
    private String chargeOrgId;
    private String projectManagerId;
    private String projectResponsibleId;
    private String projectContactId;
    private String projectSpecialistId;
    private String projectMemberIds;
    private String id;
    @Excel(name = "科研项目ID",orderNum = "0")
    @Length(min=1, max=20,message = "科研项目ID长度不能为空且小于20字符")
    private String researchProjectId;

    @Excel(name = "年份",orderNum = "1")
    @Length(min=4, max=4,message = "年份长度不能为空且小于4字符")
    private String year;

    @Excel(name = "项目编号",orderNum = "2")
    @Length(min=1, max=20,message = "项目编号长度不能为空且小于20字符")
    private String projectNo;

    @Excel(name = "项目名称",orderNum = "3")
    @Length(min=1, max=80,message = "项目名称长度不能为空且小于80字符")
    private String projectName;

    @Excel(name = "申报年度",orderNum = "4")
    @Length(min=0, max=4,message = "申报年度需长度等于4字符")
    private String applyYear;

    @Excel(name = "财务编号",orderNum = "5")
    @Length(min=0, max=20,message = "财务编号长度不能为空且小于20字符")
    private String financeNo;

    @Excel(name = "项目级别",orderNum = "6")
    @Length(min=1, max=20,message = "项目级别长度不能为空且小于20字符")
    private String projectLevelDicName;

    @Excel(name = "项目来源",orderNum = "7")
    @Length(min=0, max=80,message = "项目级别长度不能为空且小于80字符")
    private String projectOrigin;

    @Excel(name = "牵头单位",orderNum = "8")
    @Length(min=0, max=80,message = "牵头单位长度需小于80字符")
    private String leaderOrgName;
    @Excel(name = "负责部门",orderNum = "9")

    private String chargeOrgName;
    @Excel(name = "项目状态",orderNum = "19")
    @Length(min=1, max=80,message = "项目状态长度不能为空")
    private String projectStatusDicName;
    @Excel(name = "立项日期",orderNum = "11", exportFormat = "yyyy/MM/dd", importFormat = "yyyy/MM/dd" )
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date projectStartTime;
    @Excel(name = "结项日期",orderNum = "12", exportFormat = "yyyy/MM/dd", importFormat = "yyyy/MM/dd" )
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date projectEndTime;


    private String projectLevelDicId;
    private String projectStatusDicId;

    @Excel(name = "项目总经费（万元）",orderNum = "13")
    @Digits(integer=12, fraction=6,message = "项目总经费整数位长度小于12位，且小数位小于6位")
    private BigDecimal allFunds;
    @Excel(name = "国拨/中心经费（万元）",orderNum = "14")
    @Digits(integer=12, fraction=6,message = "国拨/中心经费整数位长度小于12位，且小数位小于6位")
    private BigDecimal allocateFunds;
    @Excel(name = "自筹经费（万元）",orderNum = "15")
    @Digits(integer=12, fraction=6,message = "自筹经费整数位长度小于12位，且小数位小于6位")
    private BigDecimal raiseFunds;

    @Excel(name = "课题负责人",orderNum = "16")
    @Length(min=0, max=80,message = "课题负责人长度不能为空且小于80字符")
    private String projectManagerName;

    @Excel(name = "授权责任人",orderNum = "17")
    @Length(min=0, max=80,message = "授权责任人长度不能为空且小于80字符")
    private String projectResponsibleName;

    @Excel(name = "项目联系人",orderNum = "18")
    @Length(min=0, max=80,message = "项目联系人长度不能为空且小于80字符")
    private String projectContactName;

    @Excel(name = "联系方式",orderNum = "19")
    @Length(min=0, max=80,message = "联系方式长度不能为空且小于80字符")
    private String projectContactPhone;
    @Excel(name = "责任专家",orderNum = "20")
    @Length(min=0, max=80,message = "责任专家长度不能为空且小于80字符")
    private String projectSpecialistName;

    @Excel(name = "项目成员",orderNum = "21")
    @Length(min=0, max=200,message = "项目成员长度不能为空且小于200字符")
    private String projectMemberNames;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String createUserId;
    private String createUserName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>updateUserId -> update_user_id</li>
     * <li>updateUserName -> update_user_name</li>
     * <li>extInfo1 -> ext_info1</li>
     * <li>extInfo2 -> ext_info2</li>
     * <li>extInfo3 -> ext_info3</li>
     * <li>extInfo4 -> ext_info4</li>
     * <li>leaderOrgId -> leader_org_id</li>
     * <li>chargeOrgId -> charge_org_id</li>
     * <li>projectManagerId -> project_manager_id</li>
     * <li>projectResponsibleId -> project_responsible_id</li>
     * <li>projectContactId -> project_contact_id</li>
     * <li>projectSpecialistId -> project_specialist_id</li>
     * <li>projectMemberIds -> project_member_ids</li>
     * <li>id -> id</li>
     * <li>projectNoUnique -> project_no_unique</li>
     * <li>projectNo -> project_no</li>
     * <li>applyYear -> apply_year</li>
     * <li>year -> year</li>
     * <li>projectName -> project_name</li>
     * <li>financeNo -> finance_no</li>
     * <li>projectLevelDicId -> project_level_dic_id</li>
     * <li>projectOrigin -> project_origin</li>
     * <li>leaderOrgName -> leader_org_name</li>
     * <li>chargeOrgName -> charge_org_name</li>
     * <li>projectStatusDicId -> project_status_dic_id</li>
     * <li>projectStartTime -> project_start_time</li>
     * <li>projectEndTime -> project_end_time</li>
     * <li>allFunds -> all_funds</li>
     * <li>allocateFunds -> allocate_funds</li>
     * <li>raiseFunds -> raise_funds</li>
     * <li>projectManagerName -> project_manager_name</li>
     * <li>projectResponsibleName -> project_responsible_name</li>
     * <li>projectContactName -> project_contact_name</li>
     * <li>projectContactPhone -> project_contact_phone</li>
     * <li>projectSpecialistName -> project_specialist_name</li>
     * <li>projectMemberNames -> project_member_names</li>
     * <li>createTime -> create_time</li>
     * <li>createUserId -> create_user_id</li>
     * <li>createUserName -> create_user_name</li>
     * <li>updateTime -> update_time</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "updateUserId": return "update_user_id";
            case "updateUserName": return "update_user_name";
            case "extInfo1": return "ext_info1";
            case "extInfo2": return "ext_info2";
            case "extInfo3": return "ext_info3";
            case "extInfo4": return "ext_info4";
            case "leaderOrgId": return "leader_org_id";
            case "chargeOrgId": return "charge_org_id";
            case "projectManagerId": return "project_manager_id";
            case "projectResponsibleId": return "project_responsible_id";
            case "projectContactId": return "project_contact_id";
            case "projectSpecialistId": return "project_specialist_id";
            case "projectMemberIds": return "project_member_ids";
            case "id": return "id";
            case "researchProjectId": return "research_project_id";
            case "projectNo": return "project_no";
            case "applyYear": return "apply_year";
            case "year": return "year";
            case "projectName": return "project_name";
            case "financeNo": return "finance_no";
            case "projectLevelDicId": return "project_level_dic_id";
            case "projectOrigin": return "project_origin";
            case "leaderOrgName": return "leader_org_name";
            case "chargeOrgName": return "charge_org_name";
            case "projectStatusDicId": return "project_status_dic_id";
            case "projectStartTime": return "project_start_time";
            case "projectEndTime": return "project_end_time";
            case "allFunds": return "all_funds";
            case "allocateFunds": return "allocate_funds";
            case "raiseFunds": return "raise_funds";
            case "projectManagerName": return "project_manager_name";
            case "projectResponsibleName": return "project_responsible_name";
            case "projectContactName": return "project_contact_name";
            case "projectContactPhone": return "project_contact_phone";
            case "projectSpecialistName": return "project_specialist_name";
            case "projectMemberNames": return "project_member_names";
            case "createTime": return "create_time";
            case "createUserId": return "create_user_id";
            case "createUserName": return "create_user_name";
            case "updateTime": return "update_time";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>update_user_id -> updateUserId</li>
     * <li>update_user_name -> updateUserName</li>
     * <li>ext_info1 -> extInfo1</li>
     * <li>ext_info2 -> extInfo2</li>
     * <li>ext_info3 -> extInfo3</li>
     * <li>ext_info4 -> extInfo4</li>
     * <li>leader_org_id -> leaderOrgId</li>
     * <li>charge_org_id -> chargeOrgId</li>
     * <li>project_manager_id -> projectManagerId</li>
     * <li>project_responsible_id -> projectResponsibleId</li>
     * <li>project_contact_id -> projectContactId</li>
     * <li>project_specialist_id -> projectSpecialistId</li>
     * <li>project_member_ids -> projectMemberIds</li>
     * <li>id -> id</li>
     * <li>project_no_unique -> projectNoUnique</li>
     * <li>project_no -> projectNo</li>
     * <li>apply_year -> applyYear</li>
     * <li>year -> year</li>
     * <li>project_name -> projectName</li>
     * <li>finance_no -> financeNo</li>
     * <li>project_level_dic_id -> projectLevelDicId</li>
     * <li>project_origin -> projectOrigin</li>
     * <li>leader_org_name -> leaderOrgName</li>
     * <li>charge_org_name -> chargeOrgName</li>
     * <li>project_status_dic_id -> projectStatusDicId</li>
     * <li>project_start_time -> projectStartTime</li>
     * <li>project_end_time -> projectEndTime</li>
     * <li>all_funds -> allFunds</li>
     * <li>allocate_funds -> allocateFunds</li>
     * <li>raise_funds -> raiseFunds</li>
     * <li>project_manager_name -> projectManagerName</li>
     * <li>project_responsible_name -> projectResponsibleName</li>
     * <li>project_contact_name -> projectContactName</li>
     * <li>project_contact_phone -> projectContactPhone</li>
     * <li>project_specialist_name -> projectSpecialistName</li>
     * <li>project_member_names -> projectMemberNames</li>
     * <li>create_time -> createTime</li>
     * <li>create_user_id -> createUserId</li>
     * <li>create_user_name -> createUserName</li>
     * <li>update_time -> updateTime</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "update_user_id": return "updateUserId";
            case "update_user_name": return "updateUserName";
            case "ext_info1": return "extInfo1";
            case "ext_info2": return "extInfo2";
            case "ext_info3": return "extInfo3";
            case "ext_info4": return "extInfo4";
            case "leader_org_id": return "leaderOrgId";
            case "charge_org_id": return "chargeOrgId";
            case "project_manager_id": return "projectManagerId";
            case "project_responsible_id": return "projectResponsibleId";
            case "project_contact_id": return "projectContactId";
            case "project_specialist_id": return "projectSpecialistId";
            case "project_member_ids": return "projectMemberIds";
            case "id": return "id";
            case "research_project_id": return "researchProjectId";
            case "project_no": return "projectNo";
            case "apply_year": return "applyYear";
            case "year": return "year";
            case "project_name": return "projectName";
            case "finance_no": return "financeNo";
            case "project_level_dic_id": return "projectLevelDicId";
            case "project_origin": return "projectOrigin";
            case "leader_org_name": return "leaderOrgName";
            case "charge_org_name": return "chargeOrgName";
            case "project_status_dic_id": return "projectStatusDicId";
            case "project_start_time": return "projectStartTime";
            case "project_end_time": return "projectEndTime";
            case "all_funds": return "allFunds";
            case "allocate_funds": return "allocateFunds";
            case "raise_funds": return "raiseFunds";
            case "project_manager_name": return "projectManagerName";
            case "project_responsible_name": return "projectResponsibleName";
            case "project_contact_name": return "projectContactName";
            case "project_contact_phone": return "projectContactPhone";
            case "project_specialist_name": return "projectSpecialistName";
            case "project_member_names": return "projectMemberNames";
            case "create_time": return "createTime";
            case "create_user_id": return "createUserId";
            case "create_user_name": return "createUserName";
            case "update_time": return "updateTime";
            default: return null;
        }
    }

    public String getProjectLevelDicName() {
        return projectLevelDicName;
    }

    public void setProjectLevelDicName(String projectLevelDicName) {
        this.projectLevelDicName = projectLevelDicName;
    }

    public String getProjectStatusDicName() {
        return projectStatusDicName;
    }

    public void setProjectStatusDicName(String projectStatusDicName) {
        this.projectStatusDicName = projectStatusDicName;
    }

    /**  **/
    public String getUpdateUserId() {
        return this.updateUserId;
    }

    /**  **/
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**  **/
    public String getUpdateUserName() {
        return this.updateUserName;
    }

    /**  **/
    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
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
    public String getLeaderOrgId() {
        return this.leaderOrgId;
    }

    /**  **/
    public void setLeaderOrgId(String leaderOrgId) {
        this.leaderOrgId = leaderOrgId;
    }

    /**  **/
    public String getChargeOrgId() {
        return this.chargeOrgId;
    }

    /**  **/
    public void setChargeOrgId(String chargeOrgId) {
        this.chargeOrgId = chargeOrgId;
    }

    /**  **/
    public String getProjectManagerId() {
        return this.projectManagerId;
    }

    /**  **/
    public void setProjectManagerId(String projectManagerId) {
        this.projectManagerId = projectManagerId;
    }

    /**  **/
    public String getProjectResponsibleId() {
        return this.projectResponsibleId;
    }

    /**  **/
    public void setProjectResponsibleId(String projectResponsibleId) {
        this.projectResponsibleId = projectResponsibleId;
    }

    /**  **/
    public String getProjectContactId() {
        return this.projectContactId;
    }

    /**  **/
    public void setProjectContactId(String projectContactId) {
        this.projectContactId = projectContactId;
    }

    /**  **/
    public String getProjectSpecialistId() {
        return this.projectSpecialistId;
    }

    /**  **/
    public void setProjectSpecialistId(String projectSpecialistId) {
        this.projectSpecialistId = projectSpecialistId;
    }

    /**  **/
    public String getProjectMemberIds() {
        return this.projectMemberIds;
    }

    /**  **/
    public void setProjectMemberIds(String projectMemberIds) {
        this.projectMemberIds = projectMemberIds;
    }

    /**  **/
    public String getId() {
        return this.id;
    }

    /**  **/
    public void setId(String id) {
        this.id = id;
    }

    public String getResearchProjectId() {
        return researchProjectId;
    }

    public void setResearchProjectId(String researchProjectId) {
        this.researchProjectId = researchProjectId;
    }

    /**  **/
    public String getProjectNo() {
        return this.projectNo;
    }

    /**  **/
    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    /**  **/
    public String getApplyYear() {
        return this.applyYear;
    }

    /**  **/
    public void setApplyYear(String applyYear) {
        this.applyYear = applyYear;
    }

    /**  **/
    public String getYear() {
        return this.year;
    }

    /**  **/
    public void setYear(String year) {
        this.year = year;
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
    public String getFinanceNo() {
        return this.financeNo;
    }

    /**  **/
    public void setFinanceNo(String financeNo) {
        this.financeNo = financeNo;
    }

    /**  **/
    public String getProjectLevelDicId() {
        return this.projectLevelDicId;
    }

    /**  **/
    public void setProjectLevelDicId(String projectLevelDicId) {
        this.projectLevelDicId = projectLevelDicId;
    }

    /**  **/
    public String getProjectOrigin() {
        return this.projectOrigin;
    }

    /**  **/
    public void setProjectOrigin(String projectOrigin) {
        this.projectOrigin = projectOrigin;
    }

    /**  **/
    public String getLeaderOrgName() {
        return this.leaderOrgName;
    }

    /**  **/
    public void setLeaderOrgName(String leaderOrgName) {
        this.leaderOrgName = leaderOrgName;
    }

    /**  **/
    public String getChargeOrgName() {
        return this.chargeOrgName;
    }

    /**  **/
    public void setChargeOrgName(String chargeOrgName) {
        this.chargeOrgName = chargeOrgName;
    }

    /**  **/
    public String getProjectStatusDicId() {
        return this.projectStatusDicId;
    }

    /**  **/
    public void setProjectStatusDicId(String projectStatusDicId) {
        this.projectStatusDicId = projectStatusDicId;
    }

    /**  **/
    public Date getProjectStartTime() {
        return this.projectStartTime;
    }

    /**  **/
    public void setProjectStartTime(Date projectStartTime) {
        this.projectStartTime = projectStartTime;
    }

    /**  **/
    public Date getProjectEndTime() {
        return this.projectEndTime;
    }

    /**  **/
    public void setProjectEndTime(Date projectEndTime) {
        this.projectEndTime = projectEndTime;
    }

    /**  **/
    public BigDecimal getAllFunds() {
        return this.allFunds;
    }

    /**  **/
    public void setAllFunds(BigDecimal allFunds) {
        this.allFunds = allFunds;
    }

    /**  **/
    public BigDecimal getAllocateFunds() {
        return this.allocateFunds;
    }

    /**  **/
    public void setAllocateFunds(BigDecimal allocateFunds) {
        this.allocateFunds = allocateFunds;
    }

    /**  **/
    public BigDecimal getRaiseFunds() {
        return this.raiseFunds;
    }

    /**  **/
    public void setRaiseFunds(BigDecimal raiseFunds) {
        this.raiseFunds = raiseFunds;
    }

    /**  **/
    public String getProjectManagerName() {
        return this.projectManagerName;
    }

    /**  **/
    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }

    /**  **/
    public String getProjectResponsibleName() {
        return this.projectResponsibleName;
    }

    /**  **/
    public void setProjectResponsibleName(String projectResponsibleName) {
        this.projectResponsibleName = projectResponsibleName;
    }

    /**  **/
    public String getProjectContactName() {
        return this.projectContactName;
    }

    /**  **/
    public void setProjectContactName(String projectContactName) {
        this.projectContactName = projectContactName;
    }

    /**  **/
    public String getProjectContactPhone() {
        return this.projectContactPhone;
    }

    /**  **/
    public void setProjectContactPhone(String projectContactPhone) {
        this.projectContactPhone = projectContactPhone;
    }

    /**  **/
    public String getProjectSpecialistName() {
        return this.projectSpecialistName;
    }

    /**  **/
    public void setProjectSpecialistName(String projectSpecialistName) {
        this.projectSpecialistName = projectSpecialistName;
    }

    /**  **/
    public String getProjectMemberNames() {
        return this.projectMemberNames;
    }

    /**  **/
    public void setProjectMemberNames(String projectMemberNames) {
        this.projectMemberNames = projectMemberNames;
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
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**  **/
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
