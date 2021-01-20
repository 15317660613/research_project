package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;


//@Document(indexName = "financial", type = "gualityinspection")
@Data
public class GualityInspection extends BaseEntity {
    //主键
    @Id
    private String id;
    @Excel(name = "所属项目ID", orderNum = "1")
    private String parentProjectId;
    @Excel(name = "规则ID", orderNum = "2")
    private String businessRulesId;
    @Excel(name = "规则内容", orderNum = "3")
    private String content;
    @Excel(name = "分值", orderNum = "4")
    private String score;
    //项目 1:1
    private Project project;

    private Date createTime;

    private Date modifyTime;

}
