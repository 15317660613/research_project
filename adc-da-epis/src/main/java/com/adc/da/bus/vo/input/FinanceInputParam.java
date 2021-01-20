package com.adc.da.bus.vo.input;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/9 14:30
 * @Description: 条件查询实体
 */
public class FinanceInputParam {

    /**
     *
     * @Description: 日期格式，季度 yyyy-Q 月 yyyy-MM 年 yyyy
     * @auther: ZHAIKAIXUAN
     */
    String dateFormat;


    /**
     *
     * @Description: 环比的单位 月 -1 季度 -3 年 -12
     * @auther: ZHAIKAIXUAN
     */
    Integer unitRate;


    /**
     *
     * @Description: 合同类型 支出 和收入 开口 和非开口
     * @auther: ZHAIKAIXUAN
     */
    Integer typeOne ;
    Integer typeTwo;

    /**
     *
     * @Description:  开始时间
     * @auther: ZHAIKAIXUAN
     * @date: 2018/11/9 14:41
     */
    String startTime;

    /**
     *
     * @Description:  结束时间
     * @auther: ZHAIKAIXUAN
     * @date: 2018/11/9 14:41
     */
    String endTime;

    /**
     *
     * @Description:  部门
     * @auther: ZHAIKAIXUAN
     * @date: 2018/11/9 14:41
     */
    String departMent;

    /**
     *
     * @Description:  产品
     * @auther: ZHAIKAIXUAN
     * @date: 2018/11/9 14:41
     */
    String producation;

    /**
     *
     * @Description: 排名
     * @auther: ZHAIKAIXUAN
     * @date: 2018/11/9 15:11
     */
    Integer ranking;



    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Integer getUnitRate() {
        return unitRate;
    }

    public void setUnitRate(Integer unitRate) {
        this.unitRate = unitRate;
    }

    public Integer getTypeOne() {
        return typeOne;
    }

    public void setTypeOne(Integer typeOne) {
        this.typeOne = typeOne;
    }

    public Integer getTypeTwo() {
        return typeTwo;
    }

    public void setTypeTwo(Integer typeTwo) {
        this.typeTwo = typeTwo;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDepartMent() {
        return departMent;
    }

    public void setDepartMent(String departMent) {
        this.departMent = departMent;
    }

    public String getProducation() {
        return producation;
    }

    public void setProducation(String producation) {
        this.producation = producation;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

}
