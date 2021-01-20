package com.adc.da.export.dto;

import com.adc.da.excel.annotation.Excel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * describe:
 * 用于EXCEL批量导入项目的实体类
 *
 * @author 李坤澔
 *     date 2019-05-27
 */
@Getter
@Setter
@Builder
public class ProjectExportDTO {
    /**
     * 编号
     */
    @Excel(name = "合同编号")
    private String contractNo;

    /**
     * 项目名称
     */
    @Excel(name = "项目名称", orderNum = "2")
    private String name;

    /**
     * 所属业务
     */
    @Excel(name = "所属业务", orderNum = "3")
    private String budget;

    /**
     *
     */
    @Excel(name = "业务类型", orderNum = "4")
    private String type;

    /**
     * 项目负责人-名称
     */
    @Excel(name = "项目负责人", orderNum = "5")
    private String projectLeader;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间", orderNum = "5", type = 1)
    private String projectStartTimeStr;

    @Excel(name = "完成状态", orderNum = "6", type = 1)
    private String finishedStatus;

    /**
     * 项目组成员
     */
    @Excel(name = "项目组成员", orderNum = "7")
    private String projectMemberNames;

    /**
     * 人力投入，记得转换成Integer
     */
    @Excel(name = "人力投入（人/天）", orderNum = "9")
    private String personInput;

    /**
     * 项目描述
     */
    @Excel(name = "创建人", orderNum = "10")
    private String createUserName;

    /**
     * 开始时间
     */
    @Excel(name = "创建时间", orderNum = "14")
    private String startTime;

    /**
     * 名称
     */
    @Excel(name = "所属部门", orderNum = "15")
    private String depName;

    /**
     * 百分比
     */
    @Excel(name = "项目进度", orderNum = "16")
    private String projectProgress;

    /**
     * 金额
     */
    @Excel(name = "合同金额", orderNum = "17")
    private String amount;
}
