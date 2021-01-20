package com.adc.da.research.config.vo;

import java.util.Date;

/**
 *
 *
 *
 * [{}{}]
 * @description
 * @date 2020/10/23 9:44
 * @auth zhn
 */
public class GradeRulesInfoVO {
    /**
     * 规则模板id
     */
    private String ratingRulesId;

    /**
     *评分细则id
     */
    private String gradingRulesId;

    /**
     * 规则模板名称
     */
    private String ratingRulesName;

    /**
     * 评分标准名称
     */
    private String gradingName;

    /**
     * 最低分值
     */
    private String lowestScore;

    /**
     * 封顶分值
     */
    private String cappedPoints;

    /**
     * 创建者id
     */
    private String createUserId;

    /**
     * 创建者名称
     */
    private String createUserName;

    /**
     * 创建时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    public String getRatingRulesName() {
        return ratingRulesName;
    }

    public void setRatingRulesName(String ratingRulesName) {
        this.ratingRulesName = ratingRulesName;
    }

    public String getGradingName() {
        return gradingName;
    }

    public void setGradingName(String gradingName) {
        this.gradingName = gradingName;
    }

    public String getLowestScore() {
        return lowestScore;
    }

    public void setLowestScore(String lowestScore) {
        this.lowestScore = lowestScore;
    }

    public String getCappedPoints() {
        return cappedPoints;
    }

    public void setCappedPoints(String cappedPoints) {
        this.cappedPoints = cappedPoints;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRatingRulesId() {
        return ratingRulesId;
    }

    public void setRatingRulesId(String ratingRulesId) {
        this.ratingRulesId = ratingRulesId;
    }

    public String getGradingRulesId() {
        return gradingRulesId;
    }

    public void setGradingRulesId(String gradingRulesId) {
        this.gradingRulesId = gradingRulesId;
    }
}
