package com.adc.da.budget.entity;

import com.adc.da.excel.annotation.Excel;
import lombok.Data;

import java.util.Comparator;

/**
 * created by chenhaidong 2019/1/15
 */
@Data
public class ImportRole implements Comparator<ImportRole> {

    @Excel(name = "角色", orderNum = "0")
    private String role;
    private String rank;


    @Override
    public int compare(ImportRole o1, ImportRole o2) {
        return 1;
    }
}
