package com.adc.da.research.project.vo;

import lombok.Data;

@Data
public class JudgeInfoVO {
    private String id;
    private String projectId;//项目id
    private String judgeMethodId;//线上线下
    private String expertUserId;//专家人员id字符串，逗号分割!!!
    private String expertUserName;//专家人员姓名，逗号分割!!!
    private String ratingRulesId;//模板id
    private Long approveResult;
    private String approveComment;
    private Long delFlag;
    private String ext1;
    private String ext2;
    private String ext3;
    private String ext4;
    private String ext5;
    private String reviewRemark;
    private Long state;
}
