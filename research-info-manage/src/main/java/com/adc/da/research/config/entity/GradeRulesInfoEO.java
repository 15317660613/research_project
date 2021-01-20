package com.adc.da.research.config.entity;

import com.adc.da.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @description
 * @date 2020/10/28 15:54
 * @auth zhn
 */
public class GradeRulesInfoEO extends BaseEntity {
//    @ApiModelProperty("评分规则id")
//    private String ratingRulesId;

    @ApiModelProperty("评分细则id")
    private String gradingRulesId;

    @ApiModelProperty("评价内容")
    private String gradingName;

    @ApiModelProperty("最低分值")
    private String lowestScore;

    @ApiModelProperty("封顶分值")
    private String cappedPoints;

    @ApiModelProperty("规则模板ID")
    private String ratingRulesId;

    @ApiModelProperty("评分细则删除标记")
    private Integer gradingRulesFlag;

    @ApiModelProperty("规则模板名称")
    private String ratingRulesName;

    private String ratingRulesNameOperator = "=";

    @ApiModelProperty("创建者id")
    private String createUserId;

    @ApiModelProperty("创建者名称")
    private String createUserName;


    @ApiModelProperty("评分规则创建时间")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date ratingRulesCreateTime;

    @ApiModelProperty("评分规则修改时间")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date ratingRulesModifyTime;

    @ApiModelProperty("评分细则创建时间")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gradingRulesCreateTime;

    @ApiModelProperty("评分细则修改时间")
    @org.springframework.format.annotation.DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date gradingRulesModifyTime;

    @ApiModelProperty("评分规则删除标记")
    private Integer ratingRulesDelFlag;

    @ApiModelProperty("评分细则删除标记")
    private Integer gradingRulesDelFlag;

    public String getGradingRulesId() {
        return gradingRulesId;
    }

    public void setGradingRulesId(String gradingRulesId) {
        this.gradingRulesId = gradingRulesId;
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

    public String getRatingRulesId() {
        return ratingRulesId;
    }

    public void setRatingRulesId(String ratingRulesId) {
        this.ratingRulesId = ratingRulesId;
    }

    public Integer getGradingRulesFlag() {
        return gradingRulesFlag;
    }

    public void setGradingRulesFlag(Integer gradingRulesFlag) {
        this.gradingRulesFlag = gradingRulesFlag;
    }

    public String getRatingRulesName() {
        return ratingRulesName;
    }

    public void setRatingRulesName(String ratingRulesName) {
        this.ratingRulesName = ratingRulesName;
    }

    public String getRatingRulesNameOperator() {
        return ratingRulesNameOperator;
    }

    public void setRatingRulesNameOperator(String ratingRulesNameOperator) {
        this.ratingRulesNameOperator = ratingRulesNameOperator;
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

    public Date getRatingRulesCreateTime() {
        return ratingRulesCreateTime;
    }

    public void setRatingRulesCreateTime(Date ratingRulesCreateTime) {
        this.ratingRulesCreateTime = ratingRulesCreateTime;
    }

    public Date getRatingRulesModifyTime() {
        return ratingRulesModifyTime;
    }

    public void setRatingRulesModifyTime(Date ratingRulesModifyTime) {
        this.ratingRulesModifyTime = ratingRulesModifyTime;
    }

    public Date getGradingRulesCreateTime() {
        return gradingRulesCreateTime;
    }

    public void setGradingRulesCreateTime(Date gradingRulesCreateTime) {
        this.gradingRulesCreateTime = gradingRulesCreateTime;
    }

    public Date getGradingRulesModifyTime() {
        return gradingRulesModifyTime;
    }

    public void setGradingRulesModifyTime(Date gradingRulesModifyTime) {
        this.gradingRulesModifyTime = gradingRulesModifyTime;
    }

    public Integer getRatingRulesDelFlag() {
        return ratingRulesDelFlag;
    }

    public void setRatingRulesDelFlag(Integer ratingRulesDelFlag) {
        this.ratingRulesDelFlag = ratingRulesDelFlag;
    }

    public Integer getGradingRulesDelFlag() {
        return gradingRulesDelFlag;
    }

    public void setGradingRulesDelFlag(Integer gradingRulesDelFlag) {
        this.gradingRulesDelFlag = gradingRulesDelFlag;
    }
}
