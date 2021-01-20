package com.adc.da.industymeeting.dto;

import com.adc.da.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BudgetManagementInfoOutputDTO {

    @Excel(name = "本部", orderNum = "1")
    private String headquarters;

    @Excel(name = "部门", orderNum = "2")
    private String department;

    @Excel(name = "年份", orderNum = "3")
    private String year;

    private Double ouputTarget = 0.0;

    @Excel(name = "产值目标（元）", orderNum = "4")
    private String ouputTargetStr;

    public void setOuputTarget(Double ouputTarget) {
        this.ouputTarget = ouputTarget;
        BigDecimal bd = new BigDecimal(ouputTarget.toString());
        this.ouputTargetStr = bd.toPlainString();
    }

    //    public void setOuputTargetStr(String ouputTargetStr) {
//        this.ouputTargetStr = ouputTargetStr;
//        if (StringUtils.isEmpty(ouputTargetStr)) {
//            this.ouputTarget = 0.0;
//        } else {
//            BigDecimal bd = new BigDecimal(ouputTargetStr);
//            this.ouputTarget = bd.doubleValue();
//        }
//    }
}