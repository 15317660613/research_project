package com.adc.da.budget.entity;

import com.adc.da.adcpoi.excel.annotation.ExcelCollection;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;

import java.util.List;

/**
 * created by chenhaidong 2019/1/15
 */
@Data
public class ImportUser {

    @Excel(name = "部门")
    private String org;

    @Excel(name = "姓名")
    private String name;

    @Excel(name = "角色")
    private String role;

    @Excel(name = "职级")
    private String rank;

    @Excel(name = "权重")
    private String level;

    @Excel(name = "用户名")
    private String account;

    @Excel(name = "密码")
    private String password;



}
