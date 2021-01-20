package com.adc.da.industymeeting.dto;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>INDUSTY_MEETING MeetingEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class MeetingDto extends BaseEntity {

    @Excel(name = "日期",orderNum = "1")
    private Date imDate;
    @Excel(name = "地点",orderNum = "2")
    private String imPlace;
    @Excel(name = "企业",orderNum = "3")
    private String imEnterprise;
    @Excel(name = "会议",orderNum = "4")
    private String imName;
    @Excel(name = "负责部门",orderNum = "5")
    private String departName;
}
