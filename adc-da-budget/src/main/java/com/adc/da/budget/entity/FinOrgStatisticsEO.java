package com.adc.da.budget.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Data
@Document(indexName = "financial_prd", type = "FinOrgStatisticsEO")
@ToString
public class FinOrgStatisticsEO {
    @Id
    private String id;
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String superOrgId;
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String parentDeptId;
    private Double allWorkTime;
    private List<StatisticsEntity> orgList;
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String type;

    private Date endTime;
    //角色
    private String role;

    //部门名
    private String orgType;

    private Date deadline;
}
