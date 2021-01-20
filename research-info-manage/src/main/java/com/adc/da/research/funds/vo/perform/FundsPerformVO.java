package com.adc.da.research.funds.vo.perform;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * 经费执行VO类
 * @Auther: yanyujie
 * @Date: 2020/11/22/10:07
 * @Description:
 */

@Data
public class FundsPerformVO {
    //项目编号
    @Excel(name = "项目编号",orderNum = "1",width = 10)
    private String projectCode;

    //项目名称
    @Excel(name = "项目名称",orderNum = "2",width = 20)
    private String projectName;

    //项目类别
    @Excel(name = "项目类别",orderNum = "3",width = 10)
    private String projectTypeName;

    //国拨经费
    @Excel(name = "国拨经费",orderNum = "4",width = 10)
    private Double stateFunding;

    //国拨本季度经费
    @Excel(name = "国拨本季度经费",orderNum = "5",width = 10)
    private Double stateFundQuarter;

    //国拨本年度经费
    @Excel(name = "国拨本年度经费",orderNum = "6",width = 10)
    private Double stateFundYeah;

    //到账率
    @Excel(name = "到账率",orderNum = "7",width = 10)
    private String arrivalRate;

    //国拨经费执行率
    @Excel(name = "国拨经费执行率",orderNum = "8",width = 10)
    private String stateExecutions;

    //本季度国拨经费执行率
    @Excel(name = "本季度国拨经费执行率",orderNum = "9",width = 10)
    private String stateQuarterExecutions;

    //本年度国拨经费执行率
    @Excel(name = "本年度国拨经费执行率",orderNum = "10",width = 10)
    private String stateYearExecutions;

    //自筹经费
    @Excel(name = "自筹经费",orderNum = "11",width = 8)
    private Double selfFunded;

    //本季度自筹经费
    @Excel(name = "本季度自筹经费",orderNum = "12",width = 8)
    private Double selfFundedQuarter;

    //本年度自筹经费
    @Excel(name = "本年度自筹经费",orderNum = "13",width = 8)
    private Double selfFundedYear;

    //自筹经费执行率
    @Excel(name = "自筹经费执行率",orderNum = "14",width = 8)
    private String selfExecutions;

    //本季度自筹经费执行率
    @Excel(name = "本季度自筹经费执行率",orderNum = "15",width = 8)
    private String selfExecutionsQuarter;

    //本年度自筹经费执行率
    @Excel(name = "本年度自筹经费执行率",orderNum = "16",width = 8)
    private String selfExecutionsYear;

    //总预算
    private Double totalFunding;

    //项目id
    private String id;

    //渲染色
    private String color;



    //
    private Double arrivalRateDouble;


    //国拨经费执行率
    private Double stateExecutionsDouble;

    //国拨本季度执行率
    private Double stateQuarterExecutionsDouble;

    //自筹经费执行率
    private Double selfExecutionsDouble;

    //本季度自筹经费执行率
    private Double selfExecutionsQuarterDouble;

    //本年度自筹经费执行率
    private Double selfExecutionsYearDouble;

    //本年度国拨经费执行率
    private Double stateYearExecutionsDouble;

    //项目类别id
    private String projectTypeId;

    public String getProjectTypeId() {
        return projectTypeId;
    }

    public void setProjectTypeId(String projectTypeId) {
        this.projectTypeId = projectTypeId;
    }

    public String getSelfExecutions() {
        return selfExecutions;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectTypeName() {
        return projectTypeName;
    }

    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    public Double getStateFunding() {
        return stateFunding;
    }

    public void setStateFunding(Double stateFunding) {
        this.stateFunding = stateFunding;
    }

    public Double getStateFundQuarter() {
        return stateFundQuarter;
    }

    public void setStateFundQuarter(Double stateFundQuarter) {
        this.stateFundQuarter = stateFundQuarter;
    }

    public String getArrivalRate() {
        return arrivalRate;
    }

    public void setArrivalRate(String arrivalRate) {
        this.arrivalRate = arrivalRate;
    }

    public String getStateExecutions() {
        return stateExecutions;
    }

    public void setStateExecutions(String stateExecutions) {
        this.stateExecutions = stateExecutions;
    }

    public String getStateQuarterExecutions() {
        return stateQuarterExecutions;
    }

    public void setStateQuarterExecutions(String stateQuarterExecutions) {
        this.stateQuarterExecutions = stateQuarterExecutions;
    }

    public Double getSelfFunded() {
        return selfFunded;
    }

    public void setSelfFunded(Double selfFunded) {
        this.selfFunded = selfFunded;
    }

    public Double getSelfFundedQuarter() {
        return selfFundedQuarter;
    }

    public void setSelfFundedQuarter(Double selfFundedQuarter) {
        this.selfFundedQuarter = selfFundedQuarter;
    }

    public String getS() {
        return selfExecutions;
    }

    public void setSelfExecutions(String selfExecutions) {
        this.selfExecutions = selfExecutions;
    }

    public String getSelfExecutionsQuarter() {
        return selfExecutionsQuarter;
    }

    public void setSelfExecutionsQuarter(String selfExecutionsQuarter) {
        this.selfExecutionsQuarter = selfExecutionsQuarter;
    }

    public Double getTotalFunding() {
        return totalFunding;
    }

    public void setTotalFunding(Double totalFunding) {
        this.totalFunding = totalFunding;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getArrivalRateDouble() {
        double doubleValue=0d;
        NumberFormat format= NumberFormat.getPercentInstance();
        try {
            doubleValue = format.parse(getArrivalRate() + "%").doubleValue();

        } catch (ParseException e) {
            doubleValue =0d;
        }
        return doubleValue;
    }

    public void setArrivalRateDouble(Double arrivalRateDouble) {
        this.arrivalRateDouble = arrivalRateDouble;
    }

    public Double getStateExecutionsDouble() {
        double doubleValue=0d;
        NumberFormat format= NumberFormat.getPercentInstance();
        try {
            doubleValue = format.parse(getStateExecutions() + "%").doubleValue();

        } catch (ParseException e) {
            doubleValue =0d;
        }
        return doubleValue;
    }

    public void setStateExecutionsDouble(Double stateExecutionsDouble) {
        this.stateExecutionsDouble = stateExecutionsDouble;
    }

    public Double getStateQuarterExecutionsDouble() {
        double doubleValue=0d;
        NumberFormat format= NumberFormat.getPercentInstance();
        try {
            doubleValue = format.parse(getStateQuarterExecutions() + "%").doubleValue();

        } catch (ParseException e) {
            doubleValue =0d;
        }
        return doubleValue;
    }

    public void setStateQuarterExecutionsDouble(Double stateQuarterExecutionsDouble) {
        this.stateQuarterExecutionsDouble = stateQuarterExecutionsDouble;
    }

    public Double getSelfExecutionsDouble() {
        double doubleValue=0d;
        NumberFormat format= NumberFormat.getPercentInstance();
        try {
            doubleValue = format.parse(getS() + "%").doubleValue();

        } catch (ParseException e) {
            doubleValue =0d;
        }
        return doubleValue;
    }


    public void setSelfExecutionsDouble(Double selfExecutionsDouble) {
        this.selfExecutionsDouble = selfExecutionsDouble;
    }

    public Double getSelfExecutionsQuarterDouble() {
        double doubleValue=0d;
        NumberFormat format= NumberFormat.getPercentInstance();
        try {
            doubleValue = format.parse(getSelfExecutionsQuarter() + "%").doubleValue();

        } catch (ParseException e) {
            doubleValue =0d;
        }
        return doubleValue;     }

    public void setSelfExecutionsQuarterDouble(Double selfExecutionsQuarterDouble) {
        this.selfExecutionsQuarterDouble = selfExecutionsQuarterDouble;
    }

    public Double getStateFundYeah() {
        return stateFundYeah;
    }

    public void setStateFundYeah(Double stateFundYeah) {
        this.stateFundYeah = stateFundYeah;
    }

    public String getStateYearExecutions() {
        return stateYearExecutions;
    }

    public void setStateYearExecutions(String stateYearExecutions) {
        this.stateYearExecutions = stateYearExecutions;
    }

    public Double getSelfFundedYear() {
        return selfFundedYear;
    }

    public void setSelfFundedYear(Double selfFundedYear) {
        this.selfFundedYear = selfFundedYear;
    }

    public String getSelfExecutionsYear() {
        return selfExecutionsYear;
    }

    public void setSelfExecutionsYear(String selfExecutionsYear) {
        this.selfExecutionsYear = selfExecutionsYear;
    }

    public Double getSelfExecutionsYearDouble() {
        double doubleValue=0d;
        NumberFormat format= NumberFormat.getPercentInstance();
        try {
            doubleValue = format.parse(getSelfExecutionsYear() + "%").doubleValue();

        } catch (ParseException e) {
            doubleValue =0d;
        }
        return doubleValue;
    }

    public void setSelfExecutionsYearDouble(Double selfExecutionsYearDouble) {
        this.selfExecutionsYearDouble = selfExecutionsYearDouble;
    }

    public Double getStateYearExecutionsDouble() {
        double doubleValue=0d;
        NumberFormat format= NumberFormat.getPercentInstance();
        try {
            doubleValue = format.parse(getSelfExecutionsYear() + "%").doubleValue();

        } catch (ParseException e) {
            doubleValue =0d;
        }
        return doubleValue;
    }

    public void setStateYearExecutionsDouble(Double stateYearExecutionsDouble) {
        this.stateYearExecutionsDouble = stateYearExecutionsDouble;
    }
}
