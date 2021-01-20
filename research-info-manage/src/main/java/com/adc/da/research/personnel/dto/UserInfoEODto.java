package com.adc.da.research.personnel.dto;


import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.handler.inter.IExcelDataModel;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
public class UserInfoEODto implements IExcelDataModel, IExcelModel {
    @Excel(name = "*职工编号",orderNum = "1",width = 50)
    @NotNull(message = "职工编号不能为空！")
    private String id;
    @Excel(name = "*职工姓名",orderNum = "2",width = 50)
    @NotNull(message = "职工姓名不能为空！")
    private String userName;
    @Excel(name = "*性别",orderNum = "3",width = 50)
    private String sex;
    @Excel(name = "*出生年月",orderNum = "4",width = 50)
    private String ext1;
    @Excel(name = "*邮箱",orderNum = "5",width = 50)
    private String   email;
    private String educationExperience;
    private String overseaExperience;

    @Excel(name = "*科研详情",orderNum = "6",width = 50)
    @NotNull(message = "科研详情不能为空！")
    private String researchExperience;

    //  private String personalEvaluation;
    //  private String resume;
    //  private String researchDirection;
    //  private String createUserId;
    //  private String createUserName;


    //  private Date modifyTime;
    // private Integer delFlag;



    @Excel(name = "*职称",orderNum = "7",width = 50)
    @NotNull(message = "职称不能为空！")
    private String titleOf;

    @Excel(name = "*最后学历",orderNum = "8",width = 50)
    @NotNull(message = "最后学历不能为空！")
     private String lastDegree;
    @Excel(name = "创建日期", orderNum = "9",format="yyyy-MM-dd")
    private Date createTime;

    // private String ext4;
    //  private String ext5;
    private int rowNum;

    private String errorMsg;
}
