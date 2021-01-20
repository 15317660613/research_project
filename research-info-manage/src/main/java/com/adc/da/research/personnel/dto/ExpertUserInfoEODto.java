package com.adc.da.research.personnel.dto;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class ExpertUserInfoEODto implements IExcelDataModel, IExcelModel {
    @Excel(name = "姓名",orderNum = "1",width = 25)
    @NotNull(message = "姓名不能为空！")
    private String userName;
    @Excel(name = "学历",orderNum = "2",width = 25)
    @NotNull(message = "学历不能为空！")
    private String lastDegree;
    @Excel(name = "单位", orderNum = "3",width = 25)
    @NotNull(message = "单位不能为空！")
    private String companyName;
    @Excel(name = "职称",orderNum = "4",width = 25)
    @NotNull(message = "职称不能为空！")
    private String jobTitle;
    private int rowNum;
    private String errorMsg;
}
