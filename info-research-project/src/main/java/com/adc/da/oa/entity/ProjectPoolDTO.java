package com.adc.da.oa.entity;

import com.adc.da.excel.annotation.Excel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-11-22
 */
@Builder
@Setter
@Getter
public class ProjectPoolDTO {

    @Excel(name = "ID")
    private String id;
    /*
     * 合同编号，
     * 合同名称，
     * 客户名称，
     *
     * 合同金额，
     * 生效日期，
     * 归属部门
     */
    @Excel(name = "合同编号")
    String contractNo;

    @Excel(name = "合同名称")
    String contractName;

    @Excel(name = "客户名称")
    String contractCompany;

    @Excel(name = "合同金额")
    String amount;

    @Excel(name = "生效日期",   exportFormat = "yyyy-MM-dd", importFormat = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date projectBeginDate; //哪天的日报

    @Excel(name = "归属部门")
    String deptName;
}
