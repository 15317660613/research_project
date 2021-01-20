package com.adc.da.research.config.entity;

import com.adc.da.base.entity.BaseEntity;

import java.util.Date;


/**
 * <b>功能：</b>评分细则<br>
 * <b>作者：</b>zhn<br>
 * <b>日期：</b> 2020-10-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class GradingRulesEO extends BaseEntity {

    private String id;
    private String gradingName;
    private String lowestScore;
    private String cappedPoints;
    private String ratingRulesId;
    private Integer delFlag;
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
    private Integer sort;
    private String ext4;
    private String ext5;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>gradingName -> grading_name</li>
     * <li>lowestScore -> lowest_score</li>
     * <li>cappedPoints -> capped_points</li>
     * <li>ratingRulesId -> rating_rules_id</li>
     * <li>delFlag -> del_flag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     */
    public static String fieldToColumn(String fieldName) {
        if (fieldName == null) { return null; }
        switch (fieldName) {
            case "id": return "id";
            case "gradingName": return "grading_name";
            case "lowestScore": return "lowest_score";
            case "cappedPoints": return "capped_points";
            case "ratingRulesId": return "rating_rules_id";
            case "delFlag": return "del_flag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
            default: return null;
        }
    }

    /**
     * 原始数据库列名转换为java字段名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>grading_name -> gradingName</li>
     * <li>lowest_score -> lowestScore</li>
     * <li>capped_points -> cappedPoints</li>
     * <li>rating_rules_id -> ratingRulesId</li>
     * <li>del_flag -> delFlag</li>
     * <li>ext1 -> ext1</li>
     * <li>ext2 -> ext2</li>
     * <li>ext3 -> ext3</li>
     * <li>ext4 -> ext4</li>
     * <li>ext5 -> ext5</li>
     */
    public static String columnToField(String columnName) {
        if (columnName == null) { return null; }
        switch (columnName) {
            case "id": return "id";
            case "grading_name": return "gradingName";
            case "lowest_score": return "lowestScore";
            case "capped_points": return "cappedPoints";
            case "rating_rules_id": return "ratingRulesId";
            case "del_flag": return "delFlag";
            case "ext1": return "ext1";
            case "ext2": return "ext2";
            case "ext3": return "ext3";
            case "ext4": return "ext4";
            case "ext5": return "ext5";
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
    public String getGradingName() {
        return this.gradingName;
    }

    /**  **/
    public void setGradingName(String gradingName) {
        this.gradingName = gradingName;
    }

    /**  **/
    public String getLowestScore() {
        return this.lowestScore;
    }

    /**  **/
    public void setLowestScore(String lowestScore) {
        this.lowestScore = lowestScore;
    }

    /**  **/
    public String getCappedPoints() {
        return this.cappedPoints;
    }

    /**  **/
    public void setCappedPoints(String cappedPoints) {
        this.cappedPoints = cappedPoints;
    }

    /**  **/
    public String getRatingRulesId() {
        return this.ratingRulesId;
    }

    /**  **/
    public void setRatingRulesId(String ratingRulesId) {
        this.ratingRulesId = ratingRulesId;
    }

    /**  **/
    public Integer getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

}
