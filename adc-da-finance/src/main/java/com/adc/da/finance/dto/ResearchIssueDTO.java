package com.adc.da.finance.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import com.adc.da.finance.constant.StatusEnum;
import com.adc.da.util.exception.AdcDaBaseException;
import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class ResearchIssueDTO implements IExcelModel, IExcelDataModel {

    @Excel(name = "课题名称", orderNum = "1")
    private String issueName;

    @Excel(name = "课题编号", orderNum = "2")
    private String issueNo;

    @Excel(name = "负责部门", orderNum = "3")
    private String orgName;

    @Excel(name = "课题状态", orderNum = "4")
    @NotNull(message = "课题状态不能为空！")
    private String statusStr;

    private Integer status;

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
        if (StatusEnum.PROGRESS.getLabel().equals(statusStr)) {
            this.status = StatusEnum.PROGRESS.getValue();
        }
        else if (StatusEnum.FINISHED.getLabel().equals(statusStr)) {
            this.status = StatusEnum.FINISHED.getValue();
        }
    }

    private String errorMsg;

    private int rowNum;
}
