package com.adc.da.budget.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * @description: 中心业务工时统计
 * @author: qichunxu
 * @create: 2019-03-22 13:12
 **/
@Data
//@Document(indexName = "financial", type = "AllBusinessStatistics")
public class AllBusinessStatistics {

    /**
     * id
     */
    @Id
    private String id;
    /**
     * 业务总工时
     */
    private Double businessAllWorkTime;
    /**
     * 业务列表
     */
    private List<Statistics> businessStatistics;

    private String type;

    /**
     * 部门Id
     */
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String deptId;

    /**
     * 本部Id
     */
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String parentDeptId;

    private Date deadline;
}
