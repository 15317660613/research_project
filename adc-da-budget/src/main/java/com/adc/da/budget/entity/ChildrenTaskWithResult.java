package com.adc.da.budget.entity;

import com.adc.da.budget.vo.TaskResultVO;
import com.adc.da.excel.annotation.Excel;
import com.adc.da.file.entity.FileEO;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/22 09:50
 * @Description: 子任务
 */
//@Document(indexName = "financial", type = "childtask")
@Data
@ToString
public class ChildrenTaskWithResult {

    private ChildrenTask childrenTask ;

    private List<TaskResultVO> taskResultVOList ;
}
