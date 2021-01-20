package com.adc.da.budget.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;


/**
 * @description: 中心业务工时统计
 * @author: qichunxu
 * @create: 2019-03-22 13:12
 **/
@Data
@Document(indexName = "financial_prd", type = "Statistics")
public class Statistics implements Comparable<Statistics>{

    /**
     * id
     */
    @Id
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 工时
     */
    private Double workTime;

    public Statistics(String id, String name, Double workTime) {
        this.id = id;
        this.name = name;
        this.workTime = workTime;
    }

    public Statistics() {
    }

    @Override
    public int compareTo(Statistics o) {
        return o.getWorkTime().compareTo(this.getWorkTime());
    }
}
