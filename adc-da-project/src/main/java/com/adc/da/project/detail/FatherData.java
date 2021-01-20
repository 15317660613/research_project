package com.adc.da.project.detail;

import com.adc.da.project.poi.config.Name;

public class FatherData {
    @Name("PlanTradeTable")
    private PlanTradeDetailData  planTradeTable ;

    @Name("ScheduleTradeTable")
    private ScheduleTradeDetailData scheduleTradeTable ;

    @Name("ScheduleResearchTable")
    private ScheduleResearchDetailData scheduleResearchTable ;

    private String dept ;

    private String trade_remark ;

    private String research_remark ;

    private String trade_count ;

    private String research_count ;

    private String plan_count ;

    private String schedule_title ;

    private String plan_title ;




    public PlanTradeDetailData getPlanTradeTable() {
        return planTradeTable;
    }

    public void setPlanTradeTable(PlanTradeDetailData planTradeTable) {
        this.planTradeTable = planTradeTable;
    }

    public ScheduleTradeDetailData getScheduleTradeTable() {
        return scheduleTradeTable;
    }

    public void setScheduleTradeTable(ScheduleTradeDetailData scheduleTradeTable) {
        this.scheduleTradeTable = scheduleTradeTable;
    }

    public ScheduleResearchDetailData getScheduleResearchTable() {
        return scheduleResearchTable;
    }

    public void setScheduleResearchTable(ScheduleResearchDetailData scheduleResearchTable) {
        this.scheduleResearchTable = scheduleResearchTable;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getTrade_remark() {
        return trade_remark;
    }

    public void setTrade_remark(String trade_remark) {
        this.trade_remark = trade_remark;
    }

    public String getResearch_remark() {
        return research_remark;
    }

    public void setResearch_remark(String research_remark) {
        this.research_remark = research_remark;
    }

    public String getTrade_count() {
        return trade_count;
    }

    public void setTrade_count(String trade_count) {
        this.trade_count = trade_count;
    }

    public String getResearch_count() {
        return research_count;
    }

    public void setResearch_count(String research_count) {
        this.research_count = research_count;
    }

    public String getPlan_count() {
        return plan_count;
    }

    public void setPlan_count(String plan_count) {
        this.plan_count = plan_count;
    }

    public String getSchedule_title() {
        return schedule_title;
    }

    public void setSchedule_title(String schedule_title) {
        this.schedule_title = schedule_title;
    }

    public String getPlan_title() {
        return plan_title;
    }

    public void setPlan_title(String plan_title) {
        this.plan_title = plan_title;
    }
}
