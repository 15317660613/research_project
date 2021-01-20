package com.adc.da.batchSyncData.entity;

import com.adc.da.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.Collator;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Document(indexName = "financial_prd", type = "project")
public class ProjectEO extends BaseEntity {


    //    matchField
//     项目名称,业务方,人力投入,项目描述,所属业务,创建时间,项目负责人,人员
    //@Excel(name = "项目ID", orderNum = "1")
    @Id
    private String id;

       //@Field
    private String name;


    private String projectLeaderId;


    private String projectLeader;

    private String deptId;


    private String projectMemberNames;

    private String[] memberNames;


    private String[] projectMemberIds;


    private String budgetId;


    private String budget;


    private String businessId;


    private String business;


    private String projectOwner;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;


    private Date projectStartTime;


    private String finishedStatus;


    private Integer personInput;

    private List<Map<String, String>> mapList;

    private Date createTime;

    private Date modifyTime;




    private String projectDescription;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String createUserId;

    private String createUserName;

    //删除标记
    private Boolean delFlag;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String pm;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    /*
     * 项目所属业务所在部门
     */
    private String projectTeam;


    private String contractNo;

    //业务创建人字段
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String businessCreateUserId;

    //提供给前段判断是否能点状态按钮 0表示进行中
    private String btnFlag;

    private String approveUserId;

    @ApiModelProperty("是否具有修改权限")
    private Boolean manager;

    /**
     * 合同合计，转化为float型，存在失真，所以原始数据用 ，该字段仅应用于范围筛选
     *
     * @see #contractAmountStr
     */
    @Field(type = FieldType.Float)
    private float contractAmount;

    /**
     * 保证 表单中的数据精度
     * 项目搜索中 这个字段表示累计投入工时
     */
    private String contractAmountStr;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectBeginTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectEndTime;

    /** 0. 经营类项目 ， 1.日常类事务项目 ， 2. 科研类项目,*/
    /** -1. 经营类旧项目 ， -2.日常事务类旧项目 ， -3.科研旧项目 ,*/
    private int projectType;

}
