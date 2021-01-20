package com.adc.da.research.project.entity;

import com.adc.da.base.entity.BaseEntity;


/**
 * <b>功能：</b>RS_SCORE_INFO ScoreInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class ScoreInfoEO extends BaseEntity {

    private String id;
    private String projectId;
    private String expertUserId;
    private String expertUserName;
    private String gradingRulesId;
    private String gradingRulesName;
    private Double gradingRulesScore;
    private Long state;
    private Long delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private Long result;
    private String auditReason;
    private String uploadFile;

    private String lowestScore;
    private String cappedPoints;
    private String ratingRulesId;

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>expertUserId -> expert_user_id</li>
     * <li>expertUserName -> expert_user_name</li>
     * <li>gradingRulesId -> grading_rules_id</li>
     * <li>gradingRulesScore -> grading_rules_score</li>
     * <li>state -> state</li>
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
            case "projectId": return "project_id";
            case "expertUserId": return "expert_user_id";
            case "expertUserName": return "expert_user_name";
            case "gradingRulesId": return "grading_rules_id";
            case "gradingRulesScore": return "grading_rules_score";
            case "state": return "state";
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
     * <li>project_id -> projectId</li>
     * <li>expert_user_id -> expertUserId</li>
     * <li>expert_user_name -> expertUserName</li>
     * <li>grading_rules_id -> gradingRulesId</li>
     * <li>grading_rules_score -> gradingRulesScore</li>
     * <li>state -> state</li>
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
            case "project_id": return "projectId";
            case "expert_user_id": return "expertUserId";
            case "expert_user_name": return "expertUserName";
            case "grading_rules_id": return "gradingRulesId";
            case "grading_rules_score": return "gradingRulesScore";
            case "state": return "state";
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
    public String getProjectId() {
        return this.projectId;
    }

    /**  **/
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**  **/
    public String getExpertUserId() {
        return this.expertUserId;
    }

    /**  **/
    public void setExpertUserId(String expertUserId) {
        this.expertUserId = expertUserId;
    }

    /**  **/
    public String getExpertUserName() {
        return this.expertUserName;
    }

    /**  **/
    public void setExpertUserName(String expertUserName) {
        this.expertUserName = expertUserName;
    }

    /**  **/
    public String getGradingRulesId() {
        return this.gradingRulesId;
    }

    /**  **/
    public void setGradingRulesId(String gradingRulesId) {
        this.gradingRulesId = gradingRulesId;
    }

    /**  **/
    public Double getGradingRulesScore() {
        return this.gradingRulesScore;
    }

    /**  **/
    public void setGradingRulesScore(Double gradingRulesScore) {
        this.gradingRulesScore = gradingRulesScore;
    }

    /**  **/
    public Long getState() {
        return this.state;
    }

    /**  **/
    public void setState(Long state) {
        this.state = state;
    }

    /**  **/
    public Long getDelFlag() {
        return this.delFlag;
    }

    /**  **/
    public void setDelFlag(Long delFlag) {
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

    public String getGradingRulesName() {
        return gradingRulesName;
    }

    public void setGradingRulesName(String gradingRulesName) {
        this.gradingRulesName = gradingRulesName;
    }

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    public String getAuditReason() {
        return auditReason;
    }

    public void setAuditReason(String auditReason) {
        this.auditReason = auditReason;
    }

    public String getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(String uploadFile) {
        this.uploadFile = uploadFile;
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
}
