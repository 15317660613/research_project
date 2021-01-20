package com.adc.da.budget.vo;

import com.adc.da.excel.annotation.Excel;
import lombok.Data;

@Data
public class DailyExportStasticsVO {
    @Excel(name = "部门", orderNum = "0",width = 50)
    String bigDeptName;
    @Excel(name = "科室", orderNum = "1",width = 30)
    String deptName;
    @Excel(name = "创建人", orderNum = "2",width = 10)
    String userName;
    @Excel(name = "工时", orderNum = "3",width = 10)
    String workTime;
    String compareString;
}
