package com.adc.da.business.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Document(indexName = "financial_prd", type = "Task")
@Data
@ToString
public class Task extends BaseEntity {


    //@Excel(name = "任务ID", orderNum = "1")
    @Id
    private String id;
    //@Excel(name = "项目ID", orderNum = "2")
    private String projectId;
    //@Field(type = FieldType.String, analyzer = "not_analyzed")
    private String projectLeaderId;

    //@Field(type = FieldType.String, analyzer = "not_analyzed")
    private String budgetId;
    @Excel(name="业务名称", orderNum = "1")
    private String budgetName;

    /**
     * 用于搜索
     */
    private String searchBudgetId;

    //@Field(type = FieldType.String, analyzer = "not_analyzed")
    //@Excel(name="部门ID")
    private String deptId;
    @Excel(name = "项目名称", orderNum = "2")
    private String projectName;
    @Excel(name = "任务名称", orderNum = "3")
    private String name;
    @Excel(name = "参与成员", orderNum = "4")
    private String memberNameString;
    private String[] memberNames;
    //参与人员ID列表
    private String[] memberIds;
    @Excel(name = "任务计划开始时间", orderNum = "5", exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy-MM-dd HH:mm:ss")
    private Date planStartTime;
    @Excel(name = "任务计划结束时间", orderNum = "6", exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy-MM-dd HH:mm:ss")
    private Date planEndTime;
    @Excel(name = "优先级", orderNum = "7")
    private String priority;
    @Excel(name = "是否完成", orderNum = "8")
    private String taskStatus;
    @Excel(name = "任务实际完成时间", orderNum = "9", exportFormat = "yyyy-MM-dd HH:mm:ss", importFormat = "yyyy-MM-dd HH:mm:ss")
    private Date finishedActualTime;

    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String createUserId;
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String pm;
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String projectTeam;


    private List<Map<String,String>> mapsList;
    private Date createTime;

    private Date modifyTime;
    /**
     * 业务创建人字段
     */
    @Field(type = FieldType.String, analyzer = "not_analyzed")
    private String businessCreateUserId;

    //删除标记
    private Boolean delFlag;
    @ApiModelProperty("是否具有修改权限")
    private Boolean manager;
    //前段判断是否改变状态按钮是否可用
    private String btnFlag;


}
