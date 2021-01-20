package com.adc.da.budget.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * @description: 中心业务工时统计
 * @author: qichunxu
 * @create: 2019-03-22 13:12
 **/
@Data
//@Document(indexName = "financial", type = "ProjectStatistics")
public class ProjectStatistics {

    /**
     * id
     */
    @Id
    private String id;
    /**
     * 项目Id
     */
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String projectId;
    /**
     * 任务总工时
     */
    private Double taskAllWorkTime;
    /**
     * 任务列表
     */
    private List<Statistics> taskStatistics;

    private String type;
}
