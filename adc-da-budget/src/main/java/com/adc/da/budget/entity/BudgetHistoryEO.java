package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import java.util.Date;

/**
 * <b>功能：</b>TS_BUDGET_HISTORY TsBudgetHistoryEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class BudgetHistoryEO extends BaseEntity {

    /**
     * 
     */
    private String id;
    /**
     * 
     */
    private String budgetId;
    /**
     * 
     */
    private String budgetName;
    /**
     * 
     */
    private String deptId;
    /**
     * 
     */
    private String deptName;
    /**
     * 
     */
    private String teamName;
    /**
     * 
     */
    private String propertyId;
    /**
     * 
     */
    private String pm;
    /**
     * 
     */
    private String domainId;
    /**
     * 
     */
    private String currentYear;
    /**
     * 
     */
    private Double currentYearEstimate;
    /**
     * 
     */
    private Double nextYear1Deal;
    /**
     * 
     */
    private Double nextYear1Income;
    /**
     * 
     */
    private Double nextYear2Deal;
    /**
     * 
     */
    private Double nextYear2Income;
    /**
     * 
     */
    private Double nextYear3Deal;
    /**
     * 
     */
    private Double nextYear3Income;
    /**
     * 
     */
    private Double nextYear4Deal;
    /**
     * 
     */
    private Double nextYear4Income;
    /**
     * 
     */
    private Double nextYear5Deal;
    /**
     * 
     */
    private Double nextYear5Income;
    /**
     * 
     */
    private Double nextYear6Deal;
    /**
     * 
     */
    private Double nextYear6Income;
    /**
     * 
     */
    private Double nextYear7Deal;
    /**
     * 
     */
    private Double nextYear7Income;
    /**
     * 
     */
    private Double nextYear8Deal;
    /**
     * 
     */
    private Double nextYear8Income;
    /**
     * 
     */
    private Double nextYear9Deal;
    /**
     * 
     */
    private Double nextYear9Income;
    /**
     * 
     */
    private Double nextYear10Deal;
    /**
     * 
     */
    private Double nextYear10Income;
    /**
     * 
     */
    private Double nextYear11Deal;
    /**
     * 
     */
    private Double nextYear11Income;
    /**
     * 
     */
    private Double nextYear12Deal;
    /**
     * 
     */
    private Double nextYear12Income;
    /**
     * 
     */
    private String remark;
    /**
     * 
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date cycleBegin;
    /**
     * 
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date cycleEnd;
    /**
     * 
     */
    private String cycle;
    /**
     * 
     */
    private Double currentYearDeal;
    /**
     * 
     */
    private String property;
    /**
     * 
     */
    private String createUserId;
    /**
     * 
     */
    private String projectTeam;
    /**
     * 
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operateDate;
    /**
     * 
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date operateTime;
    /**
     * 
     */
    private String operateUser;
    /**
     * 
     */
    private String operateType;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>budgetId -> budget_id</li>
     * <li>budgetName -> budget_name</li>
     * <li>deptId -> dept_id</li>
     * <li>deptName -> dept_name</li>
     * <li>teamName -> team_name</li>
     * <li>propertyId -> property_id</li>
     * <li>pm -> pm</li>
     * <li>domainId -> domain_id</li>
     * <li>currentYear -> current_year</li>
     * <li>currentYearEstimate -> current_year_estimate</li>
     * <li>nextYear1Deal -> next_year_1_deal</li>
     * <li>nextYear1Income -> next_year_1_income</li>
     * <li>nextYear2Deal -> next_year_2_deal</li>
     * <li>nextYear2Income -> next_year_2_income</li>
     * <li>nextYear3Deal -> next_year_3_deal</li>
     * <li>nextYear3Income -> next_year_3_income</li>
     * <li>nextYear4Deal -> next_year_4_deal</li>
     * <li>nextYear4Income -> next_year_4_income</li>
     * <li>nextYear5Deal -> next_year_5_deal</li>
     * <li>nextYear5Income -> next_year_5_income</li>
     * <li>nextYear6Deal -> next_year_6_deal</li>
     * <li>nextYear6Income -> next_year_6_income</li>
     * <li>nextYear7Deal -> next_year_7_deal</li>
     * <li>nextYear7Income -> next_year_7_income</li>
     * <li>nextYear8Deal -> next_year_8_deal</li>
     * <li>nextYear8Income -> next_year_8_income</li>
     * <li>nextYear9Deal -> next_year_9_deal</li>
     * <li>nextYear9Income -> next_year_9_income</li>
     * <li>nextYear10Deal -> next_year_10_deal</li>
     * <li>nextYear10Income -> next_year_10_income</li>
     * <li>nextYear11Deal -> next_year_11_deal</li>
     * <li>nextYear11Income -> next_year_11_income</li>
     * <li>nextYear12Deal -> next_year_12_deal</li>
     * <li>nextYear12Income -> next_year_12_income</li>
     * <li>remark -> remark</li>
     * <li>cycleBegin -> cycle_begin</li>
     * <li>cycleEnd -> cycle_end</li>
     * <li>cycle -> cycle</li>
     * <li>currentYearDeal -> current_year_deal</li>
     * <li>property -> property</li>
     * <li>createUserId -> create_user_id</li>
     * <li>projectTeam -> project_team</li>
     * <li>operateDate -> operate_date</li>
     * <li>operateTime -> operate_time</li>
     * <li>operateUser -> operate_user</li>
     * <li>operateType -> operate_type</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) {return null;}
        switch (fieldName) {
            case "id": return "id";
            case "budgetId": return "budget_id";
            case "budgetName": return "budget_name";
            case "deptId": return "dept_id";
            case "deptName": return "dept_name";
            case "teamName": return "team_name";
            case "propertyId": return "property_id";
            case "pm": return "pm";
            case "domainId": return "domain_id";
            case "currentYear": return "current_year";
            case "currentYearEstimate": return "current_year_estimate";
            case "nextYear1Deal": return "next_year_1_deal";
            case "nextYear1Income": return "next_year_1_income";
            case "nextYear2Deal": return "next_year_2_deal";
            case "nextYear2Income": return "next_year_2_income";
            case "nextYear3Deal": return "next_year_3_deal";
            case "nextYear3Income": return "next_year_3_income";
            case "nextYear4Deal": return "next_year_4_deal";
            case "nextYear4Income": return "next_year_4_income";
            case "nextYear5Deal": return "next_year_5_deal";
            case "nextYear5Income": return "next_year_5_income";
            case "nextYear6Deal": return "next_year_6_deal";
            case "nextYear6Income": return "next_year_6_income";
            case "nextYear7Deal": return "next_year_7_deal";
            case "nextYear7Income": return "next_year_7_income";
            case "nextYear8Deal": return "next_year_8_deal";
            case "nextYear8Income": return "next_year_8_income";
            case "nextYear9Deal": return "next_year_9_deal";
            case "nextYear9Income": return "next_year_9_income";
            case "nextYear10Deal": return "next_year_10_deal";
            case "nextYear10Income": return "next_year_10_income";
            case "nextYear11Deal": return "next_year_11_deal";
            case "nextYear11Income": return "next_year_11_income";
            case "nextYear12Deal": return "next_year_12_deal";
            case "nextYear12Income": return "next_year_12_income";
            case "remark": return "remark";
            case "cycleBegin": return "cycle_begin";
            case "cycleEnd": return "cycle_end";
            case "cycle": return "cycle";
            case "currentYearDeal": return "current_year_deal";
            case "property": return "property";
            case "createUserId": return "create_user_id";
            case "projectTeam": return "project_team";
            case "operateDate": return "operate_date";
            case "operateTime": return "operate_time";
            case "operateUser": return "operate_user";
            case "operateType": return "operate_type";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>budget_id -> budgetId</li>
     * <li>budget_name -> budgetName</li>
     * <li>dept_id -> deptId</li>
     * <li>dept_name -> deptName</li>
     * <li>team_name -> teamName</li>
     * <li>property_id -> propertyId</li>
     * <li>pm -> pm</li>
     * <li>domain_id -> domainId</li>
     * <li>current_year -> currentYear</li>
     * <li>current_year_estimate -> currentYearEstimate</li>
     * <li>next_year_1_deal -> nextYear1Deal</li>
     * <li>next_year_1_income -> nextYear1Income</li>
     * <li>next_year_2_deal -> nextYear2Deal</li>
     * <li>next_year_2_income -> nextYear2Income</li>
     * <li>next_year_3_deal -> nextYear3Deal</li>
     * <li>next_year_3_income -> nextYear3Income</li>
     * <li>next_year_4_deal -> nextYear4Deal</li>
     * <li>next_year_4_income -> nextYear4Income</li>
     * <li>next_year_5_deal -> nextYear5Deal</li>
     * <li>next_year_5_income -> nextYear5Income</li>
     * <li>next_year_6_deal -> nextYear6Deal</li>
     * <li>next_year_6_income -> nextYear6Income</li>
     * <li>next_year_7_deal -> nextYear7Deal</li>
     * <li>next_year_7_income -> nextYear7Income</li>
     * <li>next_year_8_deal -> nextYear8Deal</li>
     * <li>next_year_8_income -> nextYear8Income</li>
     * <li>next_year_9_deal -> nextYear9Deal</li>
     * <li>next_year_9_income -> nextYear9Income</li>
     * <li>next_year_10_deal -> nextYear10Deal</li>
     * <li>next_year_10_income -> nextYear10Income</li>
     * <li>next_year_11_deal -> nextYear11Deal</li>
     * <li>next_year_11_income -> nextYear11Income</li>
     * <li>next_year_12_deal -> nextYear12Deal</li>
     * <li>next_year_12_income -> nextYear12Income</li>
     * <li>remark -> remark</li>
     * <li>cycle_begin -> cycleBegin</li>
     * <li>cycle_end -> cycleEnd</li>
     * <li>cycle -> cycle</li>
     * <li>current_year_deal -> currentYearDeal</li>
     * <li>property -> property</li>
     * <li>create_user_id -> createUserId</li>
     * <li>project_team -> projectTeam</li>
     * <li>operate_date -> operateDate</li>
     * <li>operate_time -> operateTime</li>
     * <li>operate_user -> operateUser</li>
     * <li>operate_type -> operateType</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) {return null;}
        switch (columnName) {
            case "id": return "id";
            case "budget_id": return "budgetId";
            case "budget_name": return "budgetName";
            case "dept_id": return "deptId";
            case "dept_name": return "deptName";
            case "team_name": return "teamName";
            case "property_id": return "propertyId";
            case "pm": return "pm";
            case "domain_id": return "domainId";
            case "current_year": return "currentYear";
            case "current_year_estimate": return "currentYearEstimate";
            case "next_year_1_deal": return "nextYear1Deal";
            case "next_year_1_income": return "nextYear1Income";
            case "next_year_2_deal": return "nextYear2Deal";
            case "next_year_2_income": return "nextYear2Income";
            case "next_year_3_deal": return "nextYear3Deal";
            case "next_year_3_income": return "nextYear3Income";
            case "next_year_4_deal": return "nextYear4Deal";
            case "next_year_4_income": return "nextYear4Income";
            case "next_year_5_deal": return "nextYear5Deal";
            case "next_year_5_income": return "nextYear5Income";
            case "next_year_6_deal": return "nextYear6Deal";
            case "next_year_6_income": return "nextYear6Income";
            case "next_year_7_deal": return "nextYear7Deal";
            case "next_year_7_income": return "nextYear7Income";
            case "next_year_8_deal": return "nextYear8Deal";
            case "next_year_8_income": return "nextYear8Income";
            case "next_year_9_deal": return "nextYear9Deal";
            case "next_year_9_income": return "nextYear9Income";
            case "next_year_10_deal": return "nextYear10Deal";
            case "next_year_10_income": return "nextYear10Income";
            case "next_year_11_deal": return "nextYear11Deal";
            case "next_year_11_income": return "nextYear11Income";
            case "next_year_12_deal": return "nextYear12Deal";
            case "next_year_12_income": return "nextYear12Income";
            case "remark": return "remark";
            case "cycle_begin": return "cycleBegin";
            case "cycle_end": return "cycleEnd";
            case "cycle": return "cycle";
            case "current_year_deal": return "currentYearDeal";
            case "property": return "property";
            case "create_user_id": return "createUserId";
            case "project_team": return "projectTeam";
            case "operate_date": return "operateDate";
            case "operate_time": return "operateTime";
            case "operate_user": return "operateUser";
            case "operate_type": return "operateType";
            default: return null;
        }
    }
    
    /**
     * get 
     */
    public String getId() {
        return id;
    }

    /**
     * set 
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * get 
     */
    public String getBudgetId() {
        return budgetId;
    }

    /**
     * set 
     */
    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }
    /**
     * get 
     */
    public String getBudgetName() {
        return budgetName;
    }

    /**
     * set 
     */
    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }
    /**
     * get 
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * set 
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    /**
     * get 
     */
    public String getDeptName() {
        return deptName;
    }

    /**
     * set 
     */
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    /**
     * get 
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * set 
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    /**
     * get 
     */
    public String getPropertyId() {
        return propertyId;
    }

    /**
     * set 
     */
    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }
    /**
     * get 
     */
    public String getPm() {
        return pm;
    }

    /**
     * set 
     */
    public void setPm(String pm) {
        this.pm = pm;
    }
    /**
     * get 
     */
    public String getDomainId() {
        return domainId;
    }

    /**
     * set 
     */
    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }
    /**
     * get 
     */
    public String getCurrentYear() {
        return currentYear;
    }

    /**
     * set 
     */
    public void setCurrentYear(String currentYear) {
        this.currentYear = currentYear;
    }
    /**
     * get 
     */
    public Double getCurrentYearEstimate() {
        return currentYearEstimate;
    }

    /**
     * set 
     */
    public void setCurrentYearEstimate(Double currentYearEstimate) {
        this.currentYearEstimate = currentYearEstimate;
    }
    /**
     * get 
     */
    public Double getNextYear1Deal() {
        return nextYear1Deal;
    }

    /**
     * set 
     */
    public void setNextYear1Deal(Double nextYear1Deal) {
        this.nextYear1Deal = nextYear1Deal;
    }
    /**
     * get 
     */
    public Double getNextYear1Income() {
        return nextYear1Income;
    }

    /**
     * set 
     */
    public void setNextYear1Income(Double nextYear1Income) {
        this.nextYear1Income = nextYear1Income;
    }
    /**
     * get 
     */
    public Double getNextYear2Deal() {
        return nextYear2Deal;
    }

    /**
     * set 
     */
    public void setNextYear2Deal(Double nextYear2Deal) {
        this.nextYear2Deal = nextYear2Deal;
    }
    /**
     * get 
     */
    public Double getNextYear2Income() {
        return nextYear2Income;
    }

    /**
     * set 
     */
    public void setNextYear2Income(Double nextYear2Income) {
        this.nextYear2Income = nextYear2Income;
    }
    /**
     * get 
     */
    public Double getNextYear3Deal() {
        return nextYear3Deal;
    }

    /**
     * set 
     */
    public void setNextYear3Deal(Double nextYear3Deal) {
        this.nextYear3Deal = nextYear3Deal;
    }
    /**
     * get 
     */
    public Double getNextYear3Income() {
        return nextYear3Income;
    }

    /**
     * set 
     */
    public void setNextYear3Income(Double nextYear3Income) {
        this.nextYear3Income = nextYear3Income;
    }
    /**
     * get 
     */
    public Double getNextYear4Deal() {
        return nextYear4Deal;
    }

    /**
     * set 
     */
    public void setNextYear4Deal(Double nextYear4Deal) {
        this.nextYear4Deal = nextYear4Deal;
    }
    /**
     * get 
     */
    public Double getNextYear4Income() {
        return nextYear4Income;
    }

    /**
     * set 
     */
    public void setNextYear4Income(Double nextYear4Income) {
        this.nextYear4Income = nextYear4Income;
    }
    /**
     * get 
     */
    public Double getNextYear5Deal() {
        return nextYear5Deal;
    }

    /**
     * set 
     */
    public void setNextYear5Deal(Double nextYear5Deal) {
        this.nextYear5Deal = nextYear5Deal;
    }
    /**
     * get 
     */
    public Double getNextYear5Income() {
        return nextYear5Income;
    }

    /**
     * set 
     */
    public void setNextYear5Income(Double nextYear5Income) {
        this.nextYear5Income = nextYear5Income;
    }
    /**
     * get 
     */
    public Double getNextYear6Deal() {
        return nextYear6Deal;
    }

    /**
     * set 
     */
    public void setNextYear6Deal(Double nextYear6Deal) {
        this.nextYear6Deal = nextYear6Deal;
    }
    /**
     * get 
     */
    public Double getNextYear6Income() {
        return nextYear6Income;
    }

    /**
     * set 
     */
    public void setNextYear6Income(Double nextYear6Income) {
        this.nextYear6Income = nextYear6Income;
    }
    /**
     * get 
     */
    public Double getNextYear7Deal() {
        return nextYear7Deal;
    }

    /**
     * set 
     */
    public void setNextYear7Deal(Double nextYear7Deal) {
        this.nextYear7Deal = nextYear7Deal;
    }
    /**
     * get 
     */
    public Double getNextYear7Income() {
        return nextYear7Income;
    }

    /**
     * set 
     */
    public void setNextYear7Income(Double nextYear7Income) {
        this.nextYear7Income = nextYear7Income;
    }
    /**
     * get 
     */
    public Double getNextYear8Deal() {
        return nextYear8Deal;
    }

    /**
     * set 
     */
    public void setNextYear8Deal(Double nextYear8Deal) {
        this.nextYear8Deal = nextYear8Deal;
    }
    /**
     * get 
     */
    public Double getNextYear8Income() {
        return nextYear8Income;
    }

    /**
     * set 
     */
    public void setNextYear8Income(Double nextYear8Income) {
        this.nextYear8Income = nextYear8Income;
    }
    /**
     * get 
     */
    public Double getNextYear9Deal() {
        return nextYear9Deal;
    }

    /**
     * set 
     */
    public void setNextYear9Deal(Double nextYear9Deal) {
        this.nextYear9Deal = nextYear9Deal;
    }
    /**
     * get 
     */
    public Double getNextYear9Income() {
        return nextYear9Income;
    }

    /**
     * set 
     */
    public void setNextYear9Income(Double nextYear9Income) {
        this.nextYear9Income = nextYear9Income;
    }
    /**
     * get 
     */
    public Double getNextYear10Deal() {
        return nextYear10Deal;
    }

    /**
     * set 
     */
    public void setNextYear10Deal(Double nextYear10Deal) {
        this.nextYear10Deal = nextYear10Deal;
    }
    /**
     * get 
     */
    public Double getNextYear10Income() {
        return nextYear10Income;
    }

    /**
     * set 
     */
    public void setNextYear10Income(Double nextYear10Income) {
        this.nextYear10Income = nextYear10Income;
    }
    /**
     * get 
     */
    public Double getNextYear11Deal() {
        return nextYear11Deal;
    }

    /**
     * set 
     */
    public void setNextYear11Deal(Double nextYear11Deal) {
        this.nextYear11Deal = nextYear11Deal;
    }
    /**
     * get 
     */
    public Double getNextYear11Income() {
        return nextYear11Income;
    }

    /**
     * set 
     */
    public void setNextYear11Income(Double nextYear11Income) {
        this.nextYear11Income = nextYear11Income;
    }
    /**
     * get 
     */
    public Double getNextYear12Deal() {
        return nextYear12Deal;
    }

    /**
     * set 
     */
    public void setNextYear12Deal(Double nextYear12Deal) {
        this.nextYear12Deal = nextYear12Deal;
    }
    /**
     * get 
     */
    public Double getNextYear12Income() {
        return nextYear12Income;
    }

    /**
     * set 
     */
    public void setNextYear12Income(Double nextYear12Income) {
        this.nextYear12Income = nextYear12Income;
    }
    /**
     * get 
     */
    public String getRemark() {
        return remark;
    }

    /**
     * set 
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     * get 
     */
    public Date getCycleBegin() {
        return cycleBegin;
    }

    /**
     * set 
     */
    public void setCycleBegin(Date cycleBegin) {
        this.cycleBegin = cycleBegin;
    }
    /**
     * get 
     */
    public Date getCycleEnd() {
        return cycleEnd;
    }

    /**
     * set 
     */
    public void setCycleEnd(Date cycleEnd) {
        this.cycleEnd = cycleEnd;
    }
    /**
     * get 
     */
    public String getCycle() {
        return cycle;
    }

    /**
     * set 
     */
    public void setCycle(String cycle) {
        this.cycle = cycle;
    }
    /**
     * get 
     */
    public Double getCurrentYearDeal() {
        return currentYearDeal;
    }

    /**
     * set 
     */
    public void setCurrentYearDeal(Double currentYearDeal) {
        this.currentYearDeal = currentYearDeal;
    }
    /**
     * get 
     */
    public String getProperty() {
        return property;
    }

    /**
     * set 
     */
    public void setProperty(String property) {
        this.property = property;
    }
    /**
     * get 
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * set 
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
    /**
     * get 
     */
    public String getProjectTeam() {
        return projectTeam;
    }

    /**
     * set 
     */
    public void setProjectTeam(String projectTeam) {
        this.projectTeam = projectTeam;
    }
    /**
     * get 
     */
    public Date getOperateDate() {
        return operateDate;
    }

    /**
     * set 
     */
    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }
    /**
     * get 
     */
    public Date getOperateTime() {
        return operateTime;
    }

    /**
     * set 
     */
    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }
    /**
     * get 
     */
    public String getOperateUser() {
        return operateUser;
    }

    /**
     * set 
     */
    public void setOperateUser(String operateUser) {
        this.operateUser = operateUser;
    }
    /**
     * get 
     */
    public String getOperateType() {
        return operateType;
    }

    /**
     * set 
     */
    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }
}
