package com.adc.da.budget.entity;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.budget.vo.TaskResultVO;
import com.adc.da.excel.annotation.Excel;
import com.adc.da.file.entity.FileEO;
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
public class TaskWithResult extends BaseEntity {

    private  Task task ;

    private  List<TaskResultVO> taskResultVOList;

}
