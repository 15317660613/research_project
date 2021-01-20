package com.adc.da.research.project.vo;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.research.project.entity.MemberInfoEO;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>RS_MEMBER_INFO MemberInfoEOEntity<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class MemberInfoVO extends MemberInfoEO {
    private String identityNumber;
    private String lastDegree;
    private String finalDegree;
    private String officePhone;
    private String cellPhoneNumber;
    private String email;
}
