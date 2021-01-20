package com.adc.da.research.project.entity;

import com.adc.da.base.entity.BaseEntity;

/**
 * <b>功能：</b>RS_JUDGE_INFO JudgeInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public class JudgeInfoEO extends BaseEntity {

    private String id;
    private String projectId;//项目id
    private String judgeMethodId;//线上线下
    private String expertUserId;//专家人员id
    private String expertUserName;//专家人员
    private String ratingRulesId;//模板id
    private Long approveResult;
    private String approveComment;
    private Long delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private Long state;
    private String reviewRemark;//评审备注

    /**
     * java字段名转换为原始数据库列名。<b>如果不存在则返回null</b><br>
     * <p>字段列表：</p>
     * <li>id -> id</li>
     * <li>projectId -> project_id</li>
     * <li>judgeMethodId -> judge_method_id</li>
     * <li>expertUserId -> expert_user_id</li>
     * <li>expertUserName -> expert_user_name</li>
     * <li>ratingRulesId -> rating_rules_id</li>
     * <li>approveResult -> approve_result</li>
     * <li>approveComment -> approve_comment</li>
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
            case "judgeMethodId": return "judge_method_id";
            case "expertUserId": return "expert_user_id";
            case "expertUserName": return "expert_user_name";
            case "ratingRulesId": return "rating_rules_id";
            case "approveResult": return "approve_result";
            case "approveComment": return "approve_comment";
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
     * <li>judge_method_id -> judgeMethodId</li>
     * <li>expert_user_id -> expertUserId</li>
     * <li>expert_user_name -> expertUserName</li>
     * <li>rating_rules_id -> ratingRulesId</li>
     * <li>approve_result -> approveResult</li>
     * <li>approve_comment -> approveComment</li>
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
            case "judge_method_id": return "judgeMethodId";
            case "expert_user_id": return "expertUserId";
            case "expert_user_name": return "expertUserName";
            case "rating_rules_id": return "ratingRulesId";
            case "approve_result": return "approveResult";
            case "approve_comment": return "approveComment";
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
    public String getJudgeMethodId() {
        return this.judgeMethodId;
    }

    /**  **/
    public void setJudgeMethodId(String judgeMethodId) {
        this.judgeMethodId = judgeMethodId;
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
    public String getRatingRulesId() {
        return this.ratingRulesId;
    }

    /**  **/
    public void setRatingRulesId(String ratingRulesId) {
        this.ratingRulesId = ratingRulesId;
    }

    /**  **/
    public Long getApproveResult() {
        return this.approveResult;
    }

    /**  **/
    public void setApproveResult(Long approveResult) {
        this.approveResult = approveResult;
    }

    /**  **/
    public String getApproveComment() {
        return this.approveComment;
    }

    /**  **/
    public void setApproveComment(String approveComment) {
        this.approveComment = approveComment;
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

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getReviewRemark() {
        return reviewRemark;
    }

    public void setReviewRemark(String reviewRemark) {
        this.reviewRemark = reviewRemark;
    }
}
