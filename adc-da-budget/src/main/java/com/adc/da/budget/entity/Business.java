package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 业务类型 7种
 * @return
 * @author Lee Kwanho 李坤澔
 * date 2019-06-03
 **/
@Document(indexName = "financial_prd", type = "business")
@Data
public class Business extends BaseEntity {
    @Id
    @Excel(name = "业务ID", orderNum = "1")
    private String id;
    @Excel(name = "业务名称", orderNum = "2")
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss ")
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss ")
    private Date modifyTime;
    //项目 1：n
    private List<Project> projectList;
    //子业务 1:n
    private List<ChildBusiness> childBusinessList;
    //规则 1:1
    private BusinessRules businessRules;
    //父级业务
    private String parentId;
    //子业务 1:n
    private List<Business> businessList;

}
