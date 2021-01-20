package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

/**
 * 无用
 */
//@Document(indexName = "financial", type = "ChildBusiness")
@Data
public class ChildBusiness extends BaseEntity {
    @Excel(name = "子业务ID", orderNum = "1")
    @Id
    private String id;
    @Excel(name = "所属业务", orderNum = "2")
    private String parentId;
    @Excel(name = "子业务名称", orderNum = "3")
    private String name;
    //业务 n:1
    private Business business;
    private List<List<String>> tableArrays;

}
