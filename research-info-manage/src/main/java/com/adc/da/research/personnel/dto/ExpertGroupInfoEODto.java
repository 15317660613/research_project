package com.adc.da.research.personnel.dto;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;


@Data
public class ExpertGroupInfoEODto implements IExcelDataModel, IExcelModel {

    @Excel(name = "*专家组名称",orderNum = "1",width = 50)
    private String expertGroupName;

    @Excel(name = "*专家人数",orderNum = "2",width = 50)
    private String expertUserNumber;

    @Excel(name = "*备注",orderNum = "3",width = 100)
    private String remark;

    private int rowNum;

    private String errorMsg;

    @Override
    public int getRowNum() {
        return 0;
    }

    @Override
    public void setRowNum(int i) {

    }

    @Override
    public String getErrorMsg() {
        return null;
    }

    @Override
    public void setErrorMsg(String s) {

    }
}
