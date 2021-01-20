package com.adc.da.processform.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;

/**
 * <b>功能：</b>PF_PROJECT_BUDGET ProjectBudgetEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ProjectBudgetEO extends BaseEntity {
    private String id;
    private String version;
    private Integer delflag;
    private String projectId;
    private String projectName;
    private String projectBaerdeptId;
    private String projectBaerdeptName;
    private String projectManagerId;
    private String projectManagerName;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date projectImplStartTime;
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date projectImplEndTime;

    /**
     *
     */
    private Float projectBudgetSum;

    /**
     * 人工成本
     */
    private Float personBudgetMoneySum;

    /**
     * 采购成本
     */
    private Float purchaseBudgetSum;

    /**
     * 跨部门协作成本总额
     */
    private Float deptCooperatBudgetSum;

    /**
     * 其他成本
     */
    private Float otherBudgetSum;
    private Float projectGrossProfitrate;
    private Integer fitStandard;
    private Float projectContractAmount;
    private String projectPriceDetail;
    private Float projectWorkTimeSum;
    private String projectBaerDeptSuggest;
    private String directLeaderSuggest;
    private String financeDeptSuggest;
    private String ext01;
    private String ext02;
    private String ext03;

    private Float personBudgetTimeSum;
    private String ext04;
    private String ext05;
    private String ext06;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>version -> version_</li>
     * <li>delflag -> delflag_</li>
     * <li>projectId -> project_id_</li>
     * <li>projectName -> project_name_</li>
     * <li>projectBaerdeptId -> project_baerdept_id_</li>
     * <li>projectBaerdeptName -> project_baerdept_name_</li>
     * <li>projectManagerId -> project_manager_id_</li>
     * <li>projectManagerName -> project_manager_name_</li>
     * <li>projectImplStartTime -> project_impl_start_time_</li>
     * <li>projectImplEndTime -> project_impl_end_time_</li>
     * <li>projectBudgetSum -> project_budget_sum_</li>
     * <li>personBudgetMoneySum -> person_budget_money_sum_</li>
     * <li>purchaseBudgetSum -> purchase_budget_sum_</li>
     * <li>deptCooperatBudgetSum -> dept_cooperat_budget_sum_</li>
     * <li>otherBudgetSum -> other_budget_sum_</li>
     * <li>projectGrossProfitrate -> project_gross_profitrate_</li>
     * <li>fitStandard -> fit_standard_</li>
     * <li>projectContractAmount -> project_contract_amount_</li>
     * <li>projectPriceDetail -> project_price_detail_</li>
     * <li>projectWorkTimeSum -> project_work_time_sum_</li>
     * <li>projectBaerDeptSuggest -> project_baer_dept_suggest_</li>
     * <li>directLeaderSuggest -> direct_leader_suggest_</li>
     * <li>financeDeptSuggest -> finance_dept_suggest_</li>
     * <li>ext01 -> ext_01</li>
     * <li>ext02 -> ext_02</li>
     * <li>ext03 -> ext_03</li>
     * <li>id -> id_</li>
     * <li>personBudgetTimeSum -> person_budget_time_sum_</li>
     * <li>ext04 -> ext_04</li>
     * <li>ext05 -> ext_05</li>
     * <li>ext06 -> ext_06</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "version": return "version_";
            case "delflag": return "delflag_";
            case "projectId": return "project_id_";
            case "projectName": return "project_name_";
            case "projectBaerdeptId": return "project_baerdept_id_";
            case "projectBaerdeptName": return "project_baerdept_name_";
            case "projectManagerId": return "project_manager_id_";
            case "projectManagerName": return "project_manager_name_";
            case "projectImplStartTime": return "project_impl_start_time_";
            case "projectImplEndTime": return "project_impl_end_time_";
            case "projectBudgetSum": return "project_budget_sum_";
            case "personBudgetMoneySum": return "person_budget_money_sum_";
            case "purchaseBudgetSum": return "purchase_budget_sum_";
            case "deptCooperatBudgetSum": return "dept_cooperat_budget_sum_";
            case "otherBudgetSum": return "other_budget_sum_";
            case "projectGrossProfitrate": return "project_gross_profitrate_";
            case "fitStandard": return "fit_standard_";
            case "projectContractAmount": return "project_contract_amount_";
            case "projectPriceDetail": return "project_price_detail_";
            case "projectWorkTimeSum": return "project_work_time_sum_";
            case "projectBaerDeptSuggest": return "project_baer_dept_suggest_";
            case "directLeaderSuggest": return "direct_leader_suggest_";
            case "financeDeptSuggest": return "finance_dept_suggest_";
            case "ext01": return "ext_01";
            case "ext02": return "ext_02";
            case "ext03": return "ext_03";
            case "id": return "id_";
            case "personBudgetTimeSum": return "person_budget_time_sum_";
            case "ext04": return "ext_04";
            case "ext05": return "ext_05";
            case "ext06": return "ext_06";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>version_ -> version</li>
     * <li>delflag_ -> delflag</li>
     * <li>project_id_ -> projectId</li>
     * <li>project_name_ -> projectName</li>
     * <li>project_baerdept_id_ -> projectBaerdeptId</li>
     * <li>project_baerdept_name_ -> projectBaerdeptName</li>
     * <li>project_manager_id_ -> projectManagerId</li>
     * <li>project_manager_name_ -> projectManagerName</li>
     * <li>project_impl_start_time_ -> projectImplStartTime</li>
     * <li>project_impl_end_time_ -> projectImplEndTime</li>
     * <li>project_budget_sum_ -> projectBudgetSum</li>
     * <li>person_budget_money_sum_ -> personBudgetMoneySum</li>
     * <li>purchase_budget_sum_ -> purchaseBudgetSum</li>
     * <li>dept_cooperat_budget_sum_ -> deptCooperatBudgetSum</li>
     * <li>other_budget_sum_ -> otherBudgetSum</li>
     * <li>project_gross_profitrate_ -> projectGrossProfitrate</li>
     * <li>fit_standard_ -> fitStandard</li>
     * <li>project_contract_amount_ -> projectContractAmount</li>
     * <li>project_price_detail_ -> projectPriceDetail</li>
     * <li>project_work_time_sum_ -> projectWorkTimeSum</li>
     * <li>project_baer_dept_suggest_ -> projectBaerDeptSuggest</li>
     * <li>direct_leader_suggest_ -> directLeaderSuggest</li>
     * <li>finance_dept_suggest_ -> financeDeptSuggest</li>
     * <li>ext_01 -> ext01</li>
     * <li>ext_02 -> ext02</li>
     * <li>ext_03 -> ext03</li>
     * <li>id_ -> id</li>
     * <li>person_budget_time_sum_ -> personBudgetTimeSum</li>
     * <li>ext_04 -> ext04</li>
     * <li>ext_05 -> ext05</li>
     * <li>ext_06 -> ext06</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "version_": return "version";
            case "delflag_": return "delflag";
            case "project_id_": return "projectId";
            case "project_name_": return "projectName";
            case "project_baerdept_id_": return "projectBaerdeptId";
            case "project_baerdept_name_": return "projectBaerdeptName";
            case "project_manager_id_": return "projectManagerId";
            case "project_manager_name_": return "projectManagerName";
            case "project_impl_start_time_": return "projectImplStartTime";
            case "project_impl_end_time_": return "projectImplEndTime";
            case "project_budget_sum_": return "projectBudgetSum";
            case "person_budget_money_sum_": return "personBudgetMoneySum";
            case "purchase_budget_sum_": return "purchaseBudgetSum";
            case "dept_cooperat_budget_sum_": return "deptCooperatBudgetSum";
            case "other_budget_sum_": return "otherBudgetSum";
            case "project_gross_profitrate_": return "projectGrossProfitrate";
            case "fit_standard_": return "fitStandard";
            case "project_contract_amount_": return "projectContractAmount";
            case "project_price_detail_": return "projectPriceDetail";
            case "project_work_time_sum_": return "projectWorkTimeSum";
            case "project_baer_dept_suggest_": return "projectBaerDeptSuggest";
            case "direct_leader_suggest_": return "directLeaderSuggest";
            case "finance_dept_suggest_": return "financeDeptSuggest";
            case "ext_01": return "ext01";
            case "ext_02": return "ext02";
            case "ext_03": return "ext03";
            case "id_": return "id";
            case "person_budget_time_sum_": return "personBudgetTimeSum";
            case "ext_04": return "ext04";
            case "ext_05": return "ext05";
            case "ext_06": return "ext06";
            default: return null;
        }
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
    public Integer getDelflag() {
        return this.delflag;
    }

    /**  **/
    public void setDelflag(Integer delflag) {
        this.delflag = delflag;
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
    public String getProjectName() {
        return this.projectName;
    }

    /**  **/
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**  **/
    public String getProjectBaerdeptId() {
        return this.projectBaerdeptId;
    }

    /**  **/
    public void setProjectBaerdeptId(String projectBaerdeptId) {
        this.projectBaerdeptId = projectBaerdeptId;
    }

    /**  **/
    public String getProjectBaerdeptName() {
        return this.projectBaerdeptName;
    }

    /**  **/
    public void setProjectBaerdeptName(String projectBaerdeptName) {
        this.projectBaerdeptName = projectBaerdeptName;
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
    public String getProjectManagerName() {
        return this.projectManagerName;
    }

    /**  **/
    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }

    /**  **/
    public Date getProjectImplStartTime() {
        return this.projectImplStartTime;
    }

    /**  **/
    public void setProjectImplStartTime(Date projectImplStartTime) {
        this.projectImplStartTime = projectImplStartTime;
    }

    /**  **/
    public Date getProjectImplEndTime() {
        return this.projectImplEndTime;
    }

    /**  **/
    public void setProjectImplEndTime(Date projectImplEndTime) {
        this.projectImplEndTime = projectImplEndTime;
    }

    /**  **/
    public Float getProjectBudgetSum() {
        return this.projectBudgetSum;
    }

    /**  **/
    public void setProjectBudgetSum(Float projectBudgetSum) {
        this.projectBudgetSum = projectBudgetSum;
    }

    /**  **/
    public Float getPersonBudgetMoneySum() {
        return this.personBudgetMoneySum;
    }

    /**  **/
    public void setPersonBudgetMoneySum(Float personBudgetMoneySum) {
        this.personBudgetMoneySum = personBudgetMoneySum;
    }

    /**  **/
    public Float getPurchaseBudgetSum() {
        return this.purchaseBudgetSum;
    }

    /**  **/
    public void setPurchaseBudgetSum(Float purchaseBudgetSum) {
        this.purchaseBudgetSum = purchaseBudgetSum;
    }

    /**  **/
    public Float getDeptCooperatBudgetSum() {
        return this.deptCooperatBudgetSum;
    }

    /**  **/
    public void setDeptCooperatBudgetSum(Float deptCooperatBudgetSum) {
        this.deptCooperatBudgetSum = deptCooperatBudgetSum;
    }

    /**  **/
    public Float getOtherBudgetSum() {
        return this.otherBudgetSum;
    }

    /**  **/
    public void setOtherBudgetSum(Float otherBudgetSum) {
        this.otherBudgetSum = otherBudgetSum;
    }

    /**  **/
    public Float getProjectGrossProfitrate() {
        return this.projectGrossProfitrate;
    }

    /**  **/
    public void setProjectGrossProfitrate(Float projectGrossProfitrate) {
        this.projectGrossProfitrate = projectGrossProfitrate;
    }

    /**  **/
    public Integer getFitStandard() {
        return this.fitStandard;
    }

    /**  **/
    public void setFitStandard(Integer fitStandard) {
        this.fitStandard = fitStandard;
    }

    /**  **/
    public Float getProjectContractAmount() {
        return this.projectContractAmount;
    }

    /**  **/
    public void setProjectContractAmount(Float projectContractAmount) {
        this.projectContractAmount = projectContractAmount;
    }

    /**  **/
    public String getProjectPriceDetail() {
        return this.projectPriceDetail;
    }

    /**  **/
    public void setProjectPriceDetail(String projectPriceDetail) {
        this.projectPriceDetail = projectPriceDetail;
    }

    /**  **/
    public Float getProjectWorkTimeSum() {
        return this.projectWorkTimeSum;
    }

    /**  **/
    public void setProjectWorkTimeSum(Float projectWorkTimeSum) {
        this.projectWorkTimeSum = projectWorkTimeSum;
    }

    /**  **/
    public String getProjectBaerDeptSuggest() {
        return this.projectBaerDeptSuggest;
    }

    /**  **/
    public void setProjectBaerDeptSuggest(String projectBaerDeptSuggest) {
        this.projectBaerDeptSuggest = projectBaerDeptSuggest;
    }

    /**  **/
    public String getDirectLeaderSuggest() {
        return this.directLeaderSuggest;
    }

    /**  **/
    public void setDirectLeaderSuggest(String directLeaderSuggest) {
        this.directLeaderSuggest = directLeaderSuggest;
    }

    /**  **/
    public String getFinanceDeptSuggest() {
        return this.financeDeptSuggest;
    }

    /**  **/
    public void setFinanceDeptSuggest(String financeDeptSuggest) {
        this.financeDeptSuggest = financeDeptSuggest;
    }

    /**  **/
    public String getExt01() {
        return this.ext01;
    }

    /**  **/
    public void setExt01(String ext01) {
        this.ext01 = ext01;
    }

    /**  **/
    public String getExt02() {
        return this.ext02;
    }

    /**  **/
    public void setExt02(String ext02) {
        this.ext02 = ext02;
    }

    /**  **/
    public String getExt03() {
        return this.ext03;
    }

    /**  **/
    public void setExt03(String ext03) {
        this.ext03 = ext03;
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
    public Float getPersonBudgetTimeSum() {
        return this.personBudgetTimeSum;
    }

    /**  **/
    public void setPersonBudgetTimeSum(Float personBudgetTimeSum) {
        this.personBudgetTimeSum = personBudgetTimeSum;
    }

    /**  **/
    public String getExt04() {
        return this.ext04;
    }

    /**  **/
    public void setExt04(String ext04) {
        this.ext04 = ext04;
    }

    /**  **/
    public String getExt05() {
        return this.ext05;
    }

    /**  **/
    public void setExt05(String ext05) {
        this.ext05 = ext05;
    }

    /**  **/
    public String getExt06() {
        return this.ext06;
    }

    /**  **/
    public void setExt06(String ext06) {
        this.ext06 = ext06;
    }

}
