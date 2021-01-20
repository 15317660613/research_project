package com.adc.da.budget.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @description: 任务优先级Entity
 * @author: qichunxu
 * @create: 2019-03-18 10:38
 **/
@Data
@Document(indexName = "financial_prd", type = "TaskPriority")
public class TaskPriority {

    /**
     * 任务ID
     */
    @Id
    private String id;

    /**
     * 任务名称
     */
    private String name;

    /**
     * 任务优先级
     */
    private Integer priority;

    /**
     * 任务成员
     */
    private String[] memberIds;

    public TaskPriority() {
    }

    public TaskPriority(String id, String name, Integer priority,String[] memberIds) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.memberIds = memberIds;
    }
}
