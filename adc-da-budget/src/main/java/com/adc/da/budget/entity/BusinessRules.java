package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

//@Document(indexName = "financial", type = "BusinessRules")
@Data
public class BusinessRules extends BaseEntity {
    @Excel(name = "规则ID", orderNum = "1")
    @Id
    private String id;
    @Excel(name = "规则名称", orderNum = "2")
    private String name;
    @Excel(name = "规则内容", orderNum = "3")
    private String content;
    @Excel(name = "规则分值", orderNum = "4")
    private String integral;
    @Excel(name = "所属业务ID", orderNum = "5")
    private String parentProjectId;
    //业务 1:1
    private Business business;
    private List<List<String>> tableArrays;
}
