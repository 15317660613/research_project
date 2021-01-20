package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 无用
 */
@Document(indexName = "financial_prd", type = "ExpensesIncurred")
@Data
@ToString
public class ExpensesIncurred extends BaseEntity {
    @Id
    private String id;//主键
    @Excel(name = "项目ID", orderNum = "1")
    private String parentProjectId;
    private String projectName;
    @Excel(name = "类型", orderNum = "2")
    private String feeType;
    @Excel(name = "内容", orderNum = "3")
    private String feeContent;
    @Excel(name = "参与者", orderNum = "4")
    private String participateMember;
    private String[] participateMembers;
    @Excel(name = "金额", orderNum = "5")
    private Double expensesAmount;
    @Excel(name = "时间", orderNum = "6", exportFormat = "yyyy-MM-dd HH:mm:ss",importFormat = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    //    @Excel(name = "费用备注", orderNum = "5")
//    private String feeNote;
    //@Excel(name = "修改时间", orderNum = "7", exportFormat = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    //项目 n:1
    private Project project;

    private List<Map<String,String>> mapList;
}
