package com.adc.da.customerresourcemanage.dto;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>DB_ENTERPRISE EnterpriseEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class EnterpriseDto extends BaseEntity {

    /** 企业类型 **/
    @Excel(name = "类型",orderNum = "1")
    private String enterpriseType;
    /** 企业领域 **/
    @Excel(name = "企业领域",orderNum = "2")
    private String enterpriseDomain;
    /**企业省份  **/
    @Excel(name = "省份",orderNum = "3")
    private String enterpriseProvince;
    /** 企业名称 **/
    @Excel(name = "企业名称",orderNum = "4",width = 50)
    private String enterpriseName;
    /** 企业地址 **/
    @Excel(name = "单位地址",orderNum = "5",width = 80)
    private String enterpriseAddress;
    /**创建时间  **/
    @Excel(name = "创建日期",orderNum = "6",width = 15)
    private Date createTime;

}
